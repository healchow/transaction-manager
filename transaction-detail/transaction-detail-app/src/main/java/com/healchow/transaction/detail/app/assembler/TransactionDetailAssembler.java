package com.healchow.transaction.detail.app.assembler;

import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.valueobj.TransactionStatus;
import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import com.healchow.transaction.detail.request.CreateDetailRequest;
import com.healchow.transaction.detail.response.DetailResponse;

/**
 * Assembler for transaction detail
 */
public class TransactionDetailAssembler {

    public static TransactionDetail createDetail(CreateDetailRequest request) {
        if (request == null) {
            return null;
        }

        TransactionDetail detail = new TransactionDetail();
        detail.setType(TransactionType.ofName(request.getType()).getCode());
        detail.setAmount(request.getAmount());
        detail.setOwnUserId(request.getOwnUserId());
        detail.setCounterpartyUserId(request.getCounterpartyUserId());
        detail.setOwnAccount(request.getOwnAccount());
        detail.setCounterpartyAccount(request.getCounterpartyAccount());
        detail.setRemarks(request.getRemarks());
        detail.setDeviceInfo(request.getDeviceInfo());
        detail.setTimestamp(request.getTimestamp());

        return detail;
    }

    public static TransactionDetail copyDetail(TransactionDetail sourceDetail) {
        if (sourceDetail == null) {
            return null;
        }

        TransactionDetail detail = new TransactionDetail();
        detail.setTid(sourceDetail.getTid());
        detail.setType(sourceDetail.getType());
        detail.setAmount(sourceDetail.getAmount());
        detail.setStatus(sourceDetail.getStatus());
        detail.setOwnUserId(sourceDetail.getOwnUserId());
        detail.setOwnAccount(sourceDetail.getOwnAccount());
        detail.setCounterpartyUserId(sourceDetail.getCounterpartyUserId());
        detail.setCounterpartyAccount(sourceDetail.getCounterpartyAccount());
        detail.setAccountBalance(sourceDetail.getAccountBalance());
        detail.setRemarks(sourceDetail.getRemarks());
        detail.setDeviceInfo(sourceDetail.getDeviceInfo());
        detail.setTimestamp(sourceDetail.getTimestamp());
        detail.setCreateTime(sourceDetail.getCreateTime());
        detail.setUpdateTime(sourceDetail.getUpdateTime());

        return detail;
    }

    public static DetailResponse createDetailResponse(TransactionDetail transactionDetail) {
        if (transactionDetail == null) {
            return null;
        }

        DetailResponse response = new DetailResponse();
        response.setTid(transactionDetail.getTid());
        response.setType(TransactionType.ofCode(transactionDetail.getType()).toString());
        response.setStatus(TransactionStatus.ofCode(transactionDetail.getStatus()).toString());
        response.setAmount(transactionDetail.getAmount());
        response.setOwnUserId(transactionDetail.getOwnUserId());
        response.setOwnAccount(transactionDetail.getOwnAccount());
        response.setCounterpartyUserId(transactionDetail.getCounterpartyUserId());
        response.setCounterpartyAccount(transactionDetail.getCounterpartyAccount());
        response.setAccountBalance(transactionDetail.getAccountBalance());
        response.setRemarks(transactionDetail.getRemarks());
        response.setDeviceInfo(transactionDetail.getDeviceInfo());
        response.setTimestamp(transactionDetail.getTimestamp());
        return response;
    }
}
