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
        detail.setTimestamp(request.getTimestamp());
        detail.setCounterparty(request.getCounterparty());
        detail.setRemarks(request.getRemarks());
        detail.setDeviceInfo(request.getDeviceInfo());

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
        response.setTimestamp(transactionDetail.getTimestamp());
        response.setCounterparty(transactionDetail.getCounterparty());
        response.setAccountBalance(transactionDetail.getAccountBalance());
        response.setRemarks(transactionDetail.getRemarks());
        response.setDeviceInfo(transactionDetail.getDeviceInfo());
        return response;
    }
}
