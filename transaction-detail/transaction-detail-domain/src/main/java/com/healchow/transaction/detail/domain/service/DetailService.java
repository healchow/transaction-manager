package com.healchow.transaction.detail.domain.service;

import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.repository.TransactionDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Transaction Detail Service
 */
@Service
public class DetailService {

    @Autowired
    private TransactionDetailRepo transactionDetailRepo;

    /**
     * Create transaction detail
     *
     * @param detail transaction detail entity
     * @return transaction detail after creation
     */
    public TransactionDetail create(TransactionDetail detail) {
        return transactionDetailRepo.save(detail);
    }

    /**
     * Get transaction detail
     *
     * @param tid transaction id
     * @return transaction detail
     */
    public TransactionDetail get(String tid) {
        return transactionDetailRepo.get(tid);
    }

    /**
     * Update transaction detail
     *
     * @param detail transaction detail entity
     * @return transaction detail after updating
     */
    public TransactionDetail update(TransactionDetail detail) {
        return transactionDetailRepo.update(detail);
    }

    /**
     * Delete transaction detail
     *
     * @param detail transaction detail entity
     * @return transaction detail after deleting
     */
    public TransactionDetail delete(TransactionDetail detail) {
        return transactionDetailRepo.delete(detail);
    }
}
