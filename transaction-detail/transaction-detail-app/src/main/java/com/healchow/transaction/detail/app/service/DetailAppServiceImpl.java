package com.healchow.transaction.detail.app.service;

import com.healchow.transaction.detail.api.DetailAppService;
import com.healchow.transaction.detail.app.assembler.TransactionDetailAssembler;
import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.service.DetailService;
import com.healchow.transaction.detail.domain.valueobj.TransactionStatus;
import com.healchow.transaction.detail.request.CreateDetailRequest;
import com.healchow.transaction.detail.response.DetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Transaction Detail App Service
 */
@Service
public class DetailAppServiceImpl implements DetailAppService {

    @Autowired
    private DetailService detailService;

    @Override
    public String create(String userId, CreateDetailRequest request) {
        TransactionDetail detail = TransactionDetailAssembler.createDetail(request);

        // set user id
        detail.setUserId(userId);

        // generate transaction id
        detail.setTid(genTransactionId());

        // set status to processing, after processed, status will be updated
        detail.setStatus(TransactionStatus.PROCESSING.getCode());

        TransactionDetail result = detailService.create(detail);
        return result.getTid();
    }

    @Override
    public DetailResponse get(String userId, String tid) {
        TransactionDetail detail = detailService.get(tid);
        if (detail == null) {
            throw new RuntimeException("Transaction detail not found by tid: " + tid);
        }
        return TransactionDetailAssembler.createDetailResponse(detail);
    }

    /**
     * Generate transaction id, currently just generate a random UUID.
     *
     * @return transaction id
     */
    private static String genTransactionId() {
        return UUID.randomUUID().toString();
    }
}
