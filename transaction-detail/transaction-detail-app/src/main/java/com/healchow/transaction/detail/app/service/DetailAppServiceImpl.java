package com.healchow.transaction.detail.app.service;

import com.healchow.transaction.detail.api.DetailAppService;
import com.healchow.transaction.detail.app.assembler.TransactionDetailAssembler;
import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.service.DetailService;
import com.healchow.transaction.detail.domain.valueobj.TransactionStatus;
import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import com.healchow.transaction.detail.request.CreateDetailRequest;
import com.healchow.transaction.detail.request.UpdateDetailRequest;
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

    @Override
    public String update(String userId, UpdateDetailRequest request) {
        // 1. get transaction detail
        TransactionDetail existDetail = detailService.get(request.getTid());
        if (existDetail == null) {
            throw new RuntimeException("Transaction detail not found by tid: " + request.getTid());
        }

        // 2. check update operation
        checkUpdate(userId, existDetail);

        // 3. update transaction detail
        TransactionDetail targetDetail = TransactionDetailAssembler.copyDetail(existDetail);
        targetDetail.setType(TransactionType.ofName(request.getType()).getCode());
        targetDetail.setAmount(request.getAmount());
        targetDetail.setRemarks(request.getRemarks() != null ? request.getRemarks() : targetDetail.getRemarks());
        targetDetail.setDeviceInfo(request.getDeviceInfo() != null ? request.getDeviceInfo() : targetDetail.getDeviceInfo());

        // 4. save transaction detail
        TransactionDetail result = detailService.update(targetDetail);
        return result.getTid();
    }

    @Override
    public DetailResponse delete(String userId, String tid) {
        // 1. get transaction detail
        TransactionDetail existDetail = detailService.get(tid);
        if (existDetail == null) {
            throw new RuntimeException("Transaction detail not found by tid: " + tid);
        }

        // 2. check delete operation
        checkDelete(userId, existDetail);

        // 3. delete transaction detail
        TransactionDetail deletedDetail = detailService.delete(existDetail);
        return TransactionDetailAssembler.createDetailResponse(deletedDetail);
    }

    /**
     * Generate transaction id, currently just generate a random UUID.
     *
     * @return transaction id
     */
    private static String genTransactionId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Check update request is valid
     *
     * @param userId      user id
     * @param existDetail exist detail
     */
    private void checkUpdate(String userId, TransactionDetail existDetail) {
        if (!userId.equals(existDetail.getUserId())) {
            throw new RuntimeException("User " + userId + " has no permission to update transaction detail");
        }

        if (existDetail.getStatus() == TransactionStatus.PENDING.getCode()
                || existDetail.getStatus() == TransactionStatus.PROCESSING.getCode()) {
            throw new RuntimeException(String.format("Transaction [%s] was in [%s], cannot be updated",
                    existDetail.getTid(), TransactionStatus.ofCode(existDetail.getStatus())));
        }
    }

    /**
     * Check delete operation is valid
     *
     * @param userId      user id
     * @param existDetail exist detail
     */
    private void checkDelete(String userId, TransactionDetail existDetail) {
        if (!userId.equals(existDetail.getUserId())) {
            throw new RuntimeException("User " + userId + " has no permission to delete transaction detail");
        }

        if (existDetail.getStatus() == TransactionStatus.PENDING.getCode()
                || existDetail.getStatus() == TransactionStatus.PROCESSING.getCode()) {
            throw new RuntimeException(String.format("Transaction [%s] was in [%s], cannot be deleted",
                    existDetail.getTid(), TransactionStatus.ofCode(existDetail.getStatus())));
        }
    }

}
