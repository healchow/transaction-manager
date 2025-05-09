package com.healchow.transaction.detail.app.event;

import com.healchow.transaction.detail.api.DetailAppService;
import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.service.DetailService;
import com.healchow.transaction.detail.domain.valueobj.TransactionStatus;
import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import com.healchow.transaction.detail.request.CreateDetailRequest;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Listener for transaction process event
 */
@Slf4j
@Component
public class TransactionProcessEventListener implements ApplicationListener<TransactionProcessEvent> {

    @Autowired
    private DetailAppService detailAppService;

    @Autowired
    private DetailService detailService;

    @Override
    public void onApplicationEvent(@Nonnull TransactionProcessEvent event) {
        TransactionDetail detail = event.getTransactionDetail();
        String tid = detail.getTid();
        if (TransactionStatus.PROCESSING.getCode() != detail.getStatus()) {
            log.info("Transaction status with tid [{}] was not [{}], skip to process", tid, TransactionStatus.PROCESSING);
            return;
        }

        TransactionType transactionType = TransactionType.ofCode(detail.getType());
        try {
            switch (transactionType) {
                case DEPOSIT:
                    doDeposit(detail);
                    break;
                case WITHDRAWAL:
                    doWithdrawal(detail);
                    break;
                case TRANSFER_IN:
                    doTransferIn(detail);
                    break;
                case TRANSFER_OUT:
                    doTransferOut(detail);
                    break;
                default:
                    log.error("Unsupported transaction type [{}]", transactionType);
            }
        } catch (Exception e) {
            log.error(String.format("Failed to process the transaction detail with tid [%s], error: %s", tid, e.getMessage()), e);
            // TODO Send monitoring alert messages.
        }
    }

    /**
     * Process the deposit operation
     *
     * @param detail transaction detail
     */
    protected void doDeposit(TransactionDetail detail) {
        try {
            BigDecimal accountBalance = detail.getAccountBalance();
            if (accountBalance == null) {
                accountBalance = BigDecimal.ZERO;
            }
            detail.setAccountBalance(accountBalance.add(detail.getAmount()));

            detail.setStatus(TransactionStatus.SUCCESS.getCode());
        } catch (Exception e) {
            // if failed, set status to failed
            log.error(String.format("Failed to deposit for tid [%s], error: %s", detail.getTid(), e.getMessage()), e);
            detail.setStatus(TransactionStatus.FAILED.getCode());
            detail.setRemarks(appendRemarks(detail.getRemarks(), ". Process info: " + e.getMessage()));
        }

        // update transaction detail
        detailService.update(detail);
    }

    /**
     * Process the withdrawal operation
     *
     * @param detail transaction detail
     */
    protected void doWithdrawal(TransactionDetail detail) {
        try {
            BigDecimal accountBalance = detail.getAccountBalance();
            if (accountBalance == null) {
                accountBalance = BigDecimal.ZERO;
            }
            if (accountBalance.compareTo(detail.getAmount()) < 0) {
                detail.setStatus(TransactionStatus.FAILED.getCode());
                String msg = "Insufficient balance, unable to withdraw funds";
                detail.setRemarks(appendRemarks(detail.getRemarks(), msg));
            } else {
                detail.setStatus(TransactionStatus.SUCCESS.getCode());
                detail.setAccountBalance(accountBalance.subtract(detail.getAmount()));
            }
        } catch (Exception e) {
            // if failed, set status to failed
            String msg = String.format("Failed to withdrawal for tid [%s], error: %s", detail.getTid(), e.getMessage());
            log.error(msg, e);
            detail.setStatus(TransactionStatus.FAILED.getCode());
            detail.setRemarks(appendRemarks(detail.getRemarks(), msg));
        }

        // save transaction detail
        detailService.update(detail);
    }

    /**
     * Process the transfer in operation
     *
     * @param detail transaction detail
     * @apiNote Initiated incoming transactions are not supported, change the status to rejected directly.
     */
    protected void doTransferIn(TransactionDetail detail) {
        detail.setStatus(TransactionStatus.REJECTED.getCode());
        String msg = "Initiated incoming transactions are not supported";
        log.error("{} for tid [{}]", msg, detail.getTid());

        detail.setRemarks(appendRemarks(detail.getRemarks(), msg));
        detailService.update(detail);
    }

    /**
     * Process the transfer out operation
     *
     * @param detail transaction detail
     * @apiNote The transfer-out and transfer-in operations should be performed within a single transaction.
     */
    protected void doTransferOut(TransactionDetail detail) {
        String counterparty = detail.getCounterpartyAccount();
        try {
            // 1. check if the balance is sufficient
            BigDecimal accountBalance = detail.getAccountBalance();
            if (accountBalance == null) {
                accountBalance = BigDecimal.ZERO;
            }
            BigDecimal amount = detail.getAmount();
            if (accountBalance.compareTo(amount) < 0) {
                String msg = "Insufficient balance, unable to make payment";
                log.error("{} for tid [{}]", msg, detail.getTid());
                detail.setStatus(TransactionStatus.FAILED.getCode());
                detail.setRemarks(appendRemarks(detail.getRemarks(), msg));
                detailService.update(detail);
                return;
            }

            // 2. check if the counterparty exists.
            if (!isCounterpartyExist(counterparty)) {
                String msg = "Counterparty account does not exist, unable to make payment";
                log.error("{} for tid [{}]", msg, detail.getTid());
                detail.setStatus(TransactionStatus.FAILED.getCode());
                detail.setRemarks(appendRemarks(detail.getRemarks(), msg));
                detailService.update(detail);
                return;
            }

            // 3. deduct the amount from own account
            detail.setAccountBalance(accountBalance.subtract(amount));

            // 4. transfer the funds to the counterparty
            transferToCounterparty(detail);

            // 5. update the transaction detail
            detail.setStatus(TransactionStatus.SUCCESS.getCode());
            detailService.update(detail);
        } catch (Exception e) {
            // if failed, set status to failed
            log.error(String.format("Failed to make payment for tid [%s], error: %s", detail.getTid(), e.getMessage()), e);
            detail.setStatus(TransactionStatus.FAILED.getCode());
            detail.setRemarks(detail.getRemarks() != null ? detail.getRemarks() + ". Process info: " + e.getMessage() : e.getMessage());
        }
    }

    /**
     * Check if the counterparty account exists
     *
     * @param counterparty counterparty account identifier
     * @return true if exists, false otherwise
     */
    private boolean isCounterpartyExist(String counterparty) {
        // TODO After supporting user center, add the real logic here.
        return true;
    }

    /**
     * Transfer funds to the counterparty account
     *
     * @param detail transaction detail
     */
    private void transferToCounterparty(TransactionDetail detail) {
        CreateDetailRequest transferInRequest = new CreateDetailRequest();
        transferInRequest.setType(TransactionType.TRANSFER_IN.toString());
        transferInRequest.setAmount(detail.getAmount());

        // swap the settings of the user ID and account
        transferInRequest.setOwnUserId(detail.getCounterpartyUserId());
        transferInRequest.setOwnAccount(detail.getCounterpartyAccount());
        transferInRequest.setCounterpartyUserId(detail.getOwnUserId());
        transferInRequest.setCounterpartyAccount(detail.getOwnAccount());

        // create an incoming transaction for the counterparty, so the user should fill in the counterparty.
        detailAppService.create(detail.getCounterpartyUserId(), transferInRequest);
        log.info("Success to transfer_in the transaction for tid [{}]", detail.getTid());
    }

    private static String appendRemarks(String oldRemarks, String newRemarks) {
        if (StringUtils.isBlank(oldRemarks)) {
            return newRemarks;
        }
        return oldRemarks + System.lineSeparator() + newRemarks;
    }

}
