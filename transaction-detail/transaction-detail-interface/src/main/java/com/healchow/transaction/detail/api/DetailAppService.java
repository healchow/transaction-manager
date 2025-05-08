package com.healchow.transaction.detail.api;

import com.healchow.transaction.detail.request.CreateDetailRequest;

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
}