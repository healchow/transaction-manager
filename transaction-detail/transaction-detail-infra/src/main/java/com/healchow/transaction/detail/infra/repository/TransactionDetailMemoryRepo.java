package com.healchow.transaction.detail.infra.repository;

import com.healchow.transaction.detail.domain.TransactionDetail;
import com.healchow.transaction.detail.domain.page.PageInfo;
import com.healchow.transaction.detail.domain.repository.TransactionDetailRepo;
import com.healchow.transaction.detail.infra.cache.MemoryCache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Memory repository for Transaction Detail
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = "service.storage.type", havingValue = "memory", matchIfMissing = true)
public class TransactionDetailMemoryRepo implements TransactionDetailRepo {

    @Value("${service.storage.memory.max-size:1024}")
    private int maxSize;

    private MemoryCache<String, TransactionDetail> memoryCache;

    @PostConstruct
    public void init() {
        memoryCache = new MemoryCache<>(maxSize);
        log.info("Success to init a memory cache with maxSize={}", maxSize);
    }

    @Override
    public TransactionDetail save(TransactionDetail detail) {
        // set some extra info
        LocalDateTime now = LocalDateTime.now();
        detail.setCreateTime(now);
        detail.setUpdateTime(now);

        // save to memory
        memoryCache.put(detail.getTid(), detail);
        return detail;
    }

    @Override
    public TransactionDetail get(String tid) {
        return memoryCache.get(tid);
    }

    @Override
    public PageInfo<TransactionDetail> list(int pageNum, int pageSize) {
        int total = memoryCache.size();

        // calculate the start and end index
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        List<TransactionDetail> list = memoryCache.list(startIndex, endIndex);
        return new PageInfo<>(list, (long) total);
    }

    @Override
    public TransactionDetail update(TransactionDetail entity) {
        memoryCache.update(entity.getTid(), entity);
        return entity;
    }

    @Override
    public TransactionDetail delete(TransactionDetail detail) {
        return memoryCache.remove(detail.getTid());
    }

}
