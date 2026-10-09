package ch17_algorithms.projects.p04_hashing.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference de la table de hachage ecrite a la main. */
class SimpleHashMapTest {

    private SimpleHashMap<String, Integer> map;

    @BeforeEach
    void newMap() {
        map = new SimpleHashMap<>();
    }

    @Test
    void putGetReplaceRemove() {
        assertAll(
                () -> assertNull(map.put("a", 1)),
                () -> assertNull(map.put("b", 2)),
                () -> assertEquals(1, map.put("a", 10)),
                () -> assertEquals(10, map.get("a")),
                () -> assertEquals(2, map.size()),
                () -> assertEquals(2, map.remove("b")),
                () -> assertNull(map.remove("b")),
                () -> assertNull(map.get("b")),
                () -> assertEquals(1, map.size()));
    }

    // Pourquoi new String : la table doit comparer avec equals, pas avec ==.
    @Test
    void keysAreComparedWithEquals() {
        map.put(new String("cle"), 1);
        assertEquals(1, map.put(new String("cle"), 2));
        assertAll(
                () -> assertEquals(2, map.get(new String("cle"))),
                () -> assertEquals(1, map.size()));
    }

    // Trois cles du meme seau : retirer celle du MILIEU de la chaine ne doit pas perdre la suivante.
    @Test
    void removingInTheMiddleOfAChainKeepsTheRest() {
        map.put("AaAa", 1);
        map.put("BBBB", 2);
        map.put("AaBB", 3);
        assertEquals(2, map.remove("BBBB"));
        assertAll(
                () -> assertEquals(1, map.get("AaAa")),
                () -> assertEquals(3, map.get("AaBB")),
                () -> assertEquals(2, map.size()));
    }

    // "Aa" et "BB" ont le MEME hashCode (2112) : ils tombent dans le meme seau, sans s'ecraser.
    @Test
    void collisionsShareABucket() {
        map.put("Aa", 1);
        map.put("BB", 2);
        assertAll(
                () -> assertEquals("Aa".hashCode(), "BB".hashCode()),
                () -> assertEquals(1, map.get("Aa")),
                () -> assertEquals(2, map.get("BB")),
                () -> assertEquals(2, map.remove("BB")),
                () -> assertEquals(1, map.get("Aa")));
    }

    @Test
    void negativeHashCodesWork() {
        SimpleHashMap<Integer, String> m = new SimpleHashMap<>();
        m.put(-7, "moins sept");
        m.put(Integer.MIN_VALUE, "min");
        assertAll(
                () -> assertEquals("moins sept", m.get(-7)),
                () -> assertEquals("min", m.get(Integer.MIN_VALUE)));
    }

    @Test
    void theTableGrowsAtThreeQuarters() {
        assertEquals(8, map.capacity());
        for (int i = 0; i < 6; i++) {
            map.put("k" + i, i);
        }
        assertEquals(8, map.capacity());
        map.put("k6", 6);
        assertAll(
                () -> assertEquals(16, map.capacity()),
                () -> assertEquals(7, map.size()),
                () -> assertEquals(0, map.get("k0")),
                () -> assertEquals(6, map.get("k6")));
    }

    @Test
    void nullKeysAreRejected() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> map.put(null, 1));
        assertEquals("cle absente", e.getMessage());
    }

    @Test
    void aMillionEntriesStayFast() {
        SimpleHashMap<Integer, Integer> m = new SimpleHashMap<>();
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            for (int i = 0; i < 1_000_000; i++) {
                m.put(i, i);
            }
            for (int i = 0; i < 1_000_000; i++) {
                assertEquals(i, m.get(i));
            }
        });
    }
}
