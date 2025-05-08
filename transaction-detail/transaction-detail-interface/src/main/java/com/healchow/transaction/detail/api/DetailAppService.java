package com.healchow.transaction.detail.api;

import com.healchow.transaction.detail.request.CreateDetailRequest;
import com.healchow.transaction.detail.request.UpdateDetailRequest;
import com.healchow.transaction.detail.response.DetailResponse;

/**
 * Transaction Detail App Service
 */
public interface DetailAppService {

    /**
     * Create transaction detail
     *
     * @param userId  user id
     * @param request create request
     * @return transaction id after saving
     */
    String create(String userId, CreateDetailRequest request);

    /**
     * Get transaction detail
     *
     * @param userId user id
     * @param tid    transaction id
     * @return transaction detail
     */
    DetailResponse get(String userId, String tid);

    /**
     * Update transaction detail
     *
     * @param userId  user id
     * @param request update request
     * @return transaction id of updated detail
     */
    String update(String userId, UpdateDetailRequest request);
}