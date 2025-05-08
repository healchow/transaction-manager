package com.healchow.transaction.detail.app.assembler;

import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import com.healchow.transaction.detail.request.CreateDetailRequest;

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
}
