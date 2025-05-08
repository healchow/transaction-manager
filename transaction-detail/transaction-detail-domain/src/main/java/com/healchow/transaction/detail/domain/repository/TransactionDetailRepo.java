package com.healchow.transaction.detail.domain.repository;

import com.healchow.transaction.detail.domain.TransactionDetail;

/**
 * Transaction Detail Repository
 */
public interface TransactionDetailRepo {

    /**
     * Save the transaction detail
     *
     * @param entity entity to be saving
     * @return entity after saving
     */
    default TransactionDetail save(TransactionDetail entity) {
        return null;
    }

    /**
     * Get transaction detail
     *
     * @param tid transaction id
     * @return transaction detail
     */
    default TransactionDetail get(String tid) {
        return null;
    }
}
