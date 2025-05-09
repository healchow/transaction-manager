package com.healchow.transaction.detail.infra.cache;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Memory-based, thread-safe cache.
 */
public class MemoryCache<K, V> {

    private final ConcurrentSkipListMap<K, V> cache;
    private final int maxSize;
    private final AtomicInteger size = new AtomicInteger(0);

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    public MemoryCache(int maxSize) {
        this.maxSize = maxSize;
        cache = new ConcurrentSkipListMap<>(new ToStringComparator<>());
    }

    /**
     * A comparator that compares objects by their toString() method.
     *
     * @param <K> The type of keys in the cache.
     */
    static class ToStringComparator<K> implements Comparator<K> {
        @Override
        public int compare(K o1, K o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.toString().compareTo(o2.toString());
        }
    }

    /**
     * Put a key-value pair into the cache.
     *
     * @param key key need to be cache
     * @param value value need to be cache
     */
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new RuntimeException("key or value cannot be null");
        }
        writeLock.lock();
        try {
            if (cache.containsKey(key)) {
                throw new RuntimeException(String.format("[%s] already exists", key));
            }

            if (size.get() == maxSize) {
                throw new RuntimeException(String.format("Cache size has already reached the max size [%s]", maxSize));
            }

            size.incrementAndGet();
            cache.put(key, value);
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
        if (key == null) {
            throw new RuntimeException("key cannot be null");
        }

        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * List values by the given start and end index
     *
     * @return list of values
     */
    public List<V> list(int startIndex, int endIndex) {
        if (startIndex < 0 || startIndex > maxSize || endIndex < 0 || endIndex > maxSize || startIndex > endIndex) {
            return new ArrayList<>();
        }

        readLock.lock();
        try {
            List<V> allValues = new ArrayList<>(cache.values());
            this.size();
            endIndex = Math.min(endIndex, size.get());
            if (cache.size() <= startIndex) {
                return new ArrayList<>();
            }

            return allValues.subList(startIndex, endIndex);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Update value by the key.
     *
     * @param key cached key
     * @param value value need to be updated
     */
    public void update(K key, V value) {
        if (key == null || value == null) {
            throw new RuntimeException("key or value cannot be null");
        }

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

    /**
     * Get the size of current cache.
     *
     * @return size of cached records
     */
    public int size() {
        return size.get();
    }

}