package ch17_algorithms.projects.p04_hashing.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du cache LRU. */
class LruCacheTest {

    @Test
    void theLeastRecentlyUsedEntryIsEvicted() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        assertAll(
                () -> assertNull(cache.get("a")),
                () -> assertEquals(List.of("b", "c"), cache.keysFromOldest()),
                () -> assertEquals(2, cache.size()));
    }

    @Test
    void aGetMakesAnEntryRecentAgain() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        assertEquals(1, cache.get("a"));
        cache.put("c", 3);
        assertAll(
                () -> assertNull(cache.get("b")),
                () -> assertEquals(List.of("a", "c"), cache.keysFromOldest()));
    }

    @Test
    void putOnAnExistingKeyUpdatesAndRefreshes() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("a", 10);
        cache.put("c", 3);
        assertAll(
                () -> assertEquals(10, cache.get("a")),
                () -> assertNull(cache.get("b")),
                () -> assertEquals(2, cache.size()));
    }

    @Test
    void aMissDoesNotChangeTheOrder() {
        LruCache<String, Integer> cache = new LruCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        assertNull(cache.get("z"));
        assertEquals(List.of("a", "b"), cache.keysFromOldest());
    }

    @Test
    void capacityMustBePositive() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new LruCache<String, Integer>(0));
        assertEquals("capacite invalide : 0", e.getMessage());
    }

    @Test
    void aMillionOperationsInConstantTime() {
        LruCache<Integer, Integer> cache = new LruCache<>(10_000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 1_000_000; i++) {
                cache.put(i, i);
                cache.get(i - 5_000);
            }
            assertEquals(10_000, cache.size());
        });
    }
}
