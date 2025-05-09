package com.healchow.transaction.detail.app.event;

import com.healchow.transaction.detail.api.DetailAppService;
import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.service.DetailService;
import com.healchow.transaction.detail.domain.valueobj.TransactionStatus;
import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Test for {@link TransactionProcessEventListener}
 */
@ExtendWith(MockitoExtension.class)
public class TransactionProcessEventListenerTest {

    private static final String USER_ID_TOM = "Tom";
    private static final String USER_ID_JACK = "Jack";

    private static final String ACCOUNT_ID_TOM = "Tom-1001";
    private static final String ACCOUNT_ID_JACK = "Jack-2001";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Mock
    private DetailAppService detailAppService;

    @Mock
    private DetailService detailService;

    @InjectMocks
    private TransactionProcessEventListener eventListener;

    @Test
    public void testDoDeposit_Success() {
        TransactionDetail detail = mockTransactionDetail();
        detail.setType(TransactionType.DEPOSIT.getCode());

        BigDecimal total = detail.getAmount().add(detail.getAccountBalance());
        eventListener.doDeposit(detail);

        Assertions.assertEquals(detail.getAccountBalance(), total);
        Assertions.assertEquals(TransactionStatus.SUCCESS.getCode(), detail.getStatus());
    }

    @Test
    public void testDoWithdrawal_Failed() {
        TransactionDetail detail = mockTransactionDetail();
        detail.setType(TransactionType.WITHDRAWAL.getCode());

        detail.setAmount(BigDecimal.TEN.add(detail.getAccountBalance()));
        eventListener.doWithdrawal(detail);

        Assertions.assertEquals(TransactionStatus.FAILED.getCode(), detail.getStatus());
    }

    @Test
    public void testDoWithdrawal_Success() {
        TransactionDetail detail = mockTransactionDetail();
        detail.setType(TransactionType.WITHDRAWAL.getCode());

        BigDecimal total = detail.getAccountBalance().subtract(detail.getAmount());
        eventListener.doWithdrawal(detail);

        Assertions.assertEquals(detail.getAccountBalance(), total);
        Assertions.assertEquals(TransactionStatus.SUCCESS.getCode(), detail.getStatus());
    }

    @Test
    public void testDoTransferIn() {
        TransactionDetail detail = mockTransactionDetail();
        detail.setType(TransactionType.TRANSFER_IN.getCode());

        eventListener.doTransferIn(detail);
        Assertions.assertEquals(TransactionStatus.REJECTED.getCode(), detail.getStatus());
    }

    @Test
    public void testDoTransferOut_Failed() throws Exception {
        TransactionDetail detail = mockTransactionDetail();
        detail.setType(TransactionType.TRANSFER_OUT.getCode());
        detail.setAmount(BigDecimal.valueOf(1000.0));
        detail.setAccountBalance(BigDecimal.valueOf(100.0));

        eventListener.doTransferOut(detail);
        Assertions.assertEquals(TransactionStatus.FAILED.getCode(), detail.getStatus());
    }

    @Test
    public void testDoTransferOut_Success() throws Exception {
        TransactionDetail detail = mockTransactionDetail();
        detail.setType(TransactionType.TRANSFER_OUT.getCode());

        BigDecimal total = detail.getAccountBalance().subtract(detail.getAmount());
        eventListener.doTransferOut(detail);

        Assertions.assertEquals(detail.getAccountBalance(), total);
        Assertions.assertEquals(TransactionStatus.SUCCESS.getCode(), detail.getStatus());
    }

    private static TransactionDetail mockTransactionDetail() {
        TransactionDetail detail = new TransactionDetail();
        detail.setTid(LocalDateTime.now().format(FORMATTER));
        detail.setAmount(BigDecimal.valueOf(100.0));
        detail.setStatus(TransactionStatus.PROCESSING.getCode());

        detail.setOwnUserId(USER_ID_TOM);
        detail.setOwnAccount(ACCOUNT_ID_TOM);
        detail.setCounterpartyUserId(USER_ID_JACK);
        detail.setCounterpartyAccount(ACCOUNT_ID_JACK);
        detail.setAccountBalance(BigDecimal.valueOf(1000.0));

        return detail;
    }

}
