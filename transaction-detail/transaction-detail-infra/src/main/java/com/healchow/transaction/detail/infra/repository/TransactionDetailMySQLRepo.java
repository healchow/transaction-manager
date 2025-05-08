package com.healchow.transaction.detail.infra.repository;

import com.healchow.transaction.detail.domain.repository.TransactionDetailRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

/**
 * MySQL repository for Transaction Detail
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = "service.storage.type", havingValue = "mysql", matchIfMissing = false)
public class TransactionDetailMySQLRepo implements TransactionDetailRepo {

}
