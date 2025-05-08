package com.healchow.transaction.detail.infra.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Memory-based, thread-safe cache.
 */
public class MemoryCache<K, V> {

    private final ConcurrentHashMap<K, V> cache;
    private final int maxSize;
    private final AtomicInteger size = new AtomicInteger(0);

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    public MemoryCache(int maxSize) {
        this.maxSize = maxSize;
        cache = new ConcurrentHashMap<>(maxSize + 1, 1.0f);
    }

    /**
     * Put a key-value pair into the cache.
     *
     * @param key   key need to be cache
     * @param value value need to be cache
     */
    public void put(K key, V value) {
        writeLock.lock();
        try {
            if (size.get() < maxSize) {
                if (cache.containsKey(key)) {
                    throw new RuntimeException(String.format("[%s] already exists", key));
                }

                size.incrementAndGet();
                cache.put(key, value);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Get value by the key.
     *
     * @param key cached key
     * @return cached value
     */
    public V get(K key) {
        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Update value by the key.
     *
     * @param key   cached key
     * @param value value need to be updated
     */
    public void update(K key, V value) {
        writeLock.lock();
        try {
            if (!cache.containsKey(key)) {
                throw new RuntimeException(String.format("[%s] not exists", key));
            }
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove cache by the key.
     *
     * @param key cached key
     */
    public synchronized V remove(K key) {
        writeLock.lock();
        try {
            V value = cache.remove(key);
            if (value != null) {
                size.decrementAndGet();
            }

            return value;
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        return size.get();
    }

}