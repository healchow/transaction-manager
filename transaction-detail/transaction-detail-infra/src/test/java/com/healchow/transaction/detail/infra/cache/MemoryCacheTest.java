package com.healchow.transaction.detail.infra.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link MemoryCache}
 */
@ExtendWith(MockitoExtension.class)
public class MemoryCacheTest {

    private Map<String, Integer> backingMap;

    private MemoryCache<String, Integer> cache;

    @BeforeEach
    public void setUp() {
        backingMap = new HashMap<>();
        cache = new MemoryCache<>(8);
    }

    @Test
    public void testPut_addsNewKey() {
        String key = "testKey";
        Integer value = 123;
        cache.put(key, value);
        assertEquals(value, cache.get(key));
    }

    @Test
    public void testPut_withExistKey_shouldThrowException() {
        String key = "testKey";
        Integer initialValue = 123;
        cache.put(key, initialValue);

        Integer updatedValue = 456;
        assertThrows(RuntimeException.class, () -> cache.put(key, updatedValue));
    }

    @Test
    public void testPut_withNullKeyAndValue_shouldThrowException() {
        String keyNull = null;
        Integer value = 123;

        assertThrows(RuntimeException.class, () -> cache.put(keyNull, value));

        String key = "testKey";
        Integer valueNull = null;
        assertThrows(RuntimeException.class, () -> cache.put(key, valueNull));
    }

    @Test
    public void testPut_whenExceedsMaxSize_shouldThrowException() {
        MemoryCache<String, Integer> limitedCache = new MemoryCache<>(2);
        limitedCache.put("key1", 1);
        limitedCache.put("key2", 2);
        assertEquals(2, limitedCache.size());

        assertThrows(RuntimeException.class, () -> limitedCache.put("key3", 3));
    }

    @Test
    public void testGet_existsKey() {
        cache.put("key1", 100);
        Integer result = cache.get("key1");

        assertEquals(Integer.valueOf(100), result);
    }

    @Test
    public void testGet_notExistsKey() {
        Integer result = cache.get("nonExistingKey");
        assertNull(result);
    }

    @Test
    public void testGet_nullKey() {
        // throw RuntimeException
        assertThrowsExactly(RuntimeException.class, () -> cache.get(null));
    }

    @Test
    @DisplayName("Should return correct sublist when start and end are within bounds")
    public void testList_withValidRange_returnsCorrectSublist() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(2, 5);
        assertEquals(3, result.size());
        assertIterableEquals(List.of(2, 3, 4), result);
    }

    @Test
    @DisplayName("Should return empty list when start > end")
    public void testList_withStartGreaterThanEnd_returnsEmptyList() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(5, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when cache is empty")
    public void testList_withEmptyCache_returnsEmptyList() {
        MemoryCache<String, Integer> emptyCache = new MemoryCache<>(4);
        List<Integer> result = emptyCache.list(0, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return elements from start to end when end exceeds cache size")
    public void testList_withEndExceedingSize_returnsElementsToEnd() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(7, 15);
        assertEquals(3, result.size());
        assertIterableEquals(List.of(7, 8, 9), result);
    }

    @Test
    @DisplayName("Should return empty list when start exceeds cache size")
    public void testList_withStartExceedingSize_returnsEmpty() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(15, 20);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when start equals end")
    public void testList_withStartEqualsEnd_returnsEmpty() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(3, 3);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should handle negative start index")
    public void testList_withNegativeStart_returnsEmpty() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(-2, 3);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should handle negative end index by returning empty list")
    public void testList_withNegativeEnd_ReturnsEmptyList() {
        MemoryCache<String, Integer> memoryCache = mockMemoryCache();
        List<Integer> result = memoryCache.list(2, -3);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Update existing key should replace value")
    public void testUpdate_ExistingKey() {
        cache.put("key1", 100);
        cache.update("key1", 200);
        assertEquals(200, cache.get("key1"));
    }

    @Test
    @DisplayName("Update non-existent key should throws exception")
    public void testUpdate_notExistsKey_shouldThrowsException() {
        assertThrowsExactly(RuntimeException.class, () -> cache.update("newKey", 300));
    }

    @Test
    @DisplayName("Update with null key or null value should throw exception")
    public void testUpdate_withNullKeyAndValue_shouldThrowException() {
        assertThrows(RuntimeException.class, () -> cache.update(null, 400));
        assertThrows(RuntimeException.class, () -> cache.update("key1", null));
    }

    @Test
    @DisplayName("Update should maintain other entries")
    public void testUpdate_notAffectOthers() {
        cache.put("keyA", 1);
        cache.put("keyB", 2);

        cache.update("keyA", 10);

        assertEquals(10, cache.get("keyA"));
        assertEquals(2, cache.get("keyB"));
    }

    @Test
    public void testRemove_existsKey_returnsValueAndRemovesEntry() {
        cache.put("key1", 100);
        Integer result = cache.remove("key1");

        assertEquals(100, result);
        assertNull(cache.get("key1"));
    }

    @Test
    public void testRemove_notExistsKey_returnsNull() {
        Integer result = cache.remove("nonExistingKey");
        assertNull(result);
        assertTrue(backingMap.isEmpty());
    }

    @Test
    public void testRemove_nullKey_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            cache.remove(null);
        });
    }

    @Test
    public void testRemove_emptyCache_returnsNull() {
        Integer result = cache.remove("anyKey");
        assertNull(result);
    }

    private static MemoryCache<String, Integer> mockMemoryCache() {
        MemoryCache<String, Integer> memoryCache = new MemoryCache<>(16);
        for (int i = 0; i < 10; i++) {
            memoryCache.put("key" + i, i);
        }

        return memoryCache;
    }

}
