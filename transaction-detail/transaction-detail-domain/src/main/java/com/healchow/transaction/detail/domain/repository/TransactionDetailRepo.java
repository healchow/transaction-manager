package com.healchow.transaction.detail.domain.repository;

import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.page.PageInfo;

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

    /**
     * List transaction detail by pagination
     *
     * @param pageNum  page number
     * @param pageSize page size
     * @return transaction detail list
     */
    default PageInfo<TransactionDetail> list(int pageNum, int pageSize) {
        return null;
    }

    /**
     * Update the transaction detail
     *
     * @param entity entity to be updating
     * @return entity after updating
     */
    default TransactionDetail update(TransactionDetail entity) {
        return null;
    }

    /**
     * Delete the transaction detail
     *
     * @param detail entity to be deleting
     * @return entity after deleting
     */
    default TransactionDetail delete(TransactionDetail detail) {
        return null;
    }
}
