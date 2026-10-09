package ch16_testing.drills.r01_assertions.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Le corrige du drill 1 : une assertion de JUnit par defi. */
class Recall01Test {

    @Test
    void d01() {
        assertEquals(7, Math.max(3, 7));
    }

    @Test
    void d02() {
        assertNotEquals("java", "JAVA");
    }

    @Test
    void d03() {
        assertTrue(new StringBuilder("kayak").reverse().toString().equals("kayak"));
        assertFalse("java".isBlank());
    }

    @Test
    void d04() {
        assertNull(Map.of("a", 1).get("b"));
        assertNotNull(Map.of("a", 1).get("a"));
    }

    // Piege : le cache des Integer va de -128 a 127.
    @Test
    void d05() {
        assertSame(Integer.valueOf(127), Integer.valueOf(127));
        assertNotSame(Integer.valueOf(128), Integer.valueOf(128));
    }

    @Test
    void d06() {
        int[] numbers = {5, 3, 9, 1};
        Arrays.sort(numbers);
        assertArrayEquals(new int[]{1, 3, 5, 9}, numbers);
    }

    // Piege : assertIterableEquals compare les elements, pas le type de la liste.
    @Test
    void d07() {
        assertIterableEquals(List.of(1, 2, 3), new ArrayList<>(List.of(1, 2, 3)));
    }

    @Test
    void d08() {
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> Integer.parseInt("x"));
        assertEquals("For input string: \"x\"", e.getMessage());
    }

    @Test
    void d09() {
        int n = assertDoesNotThrow(() -> Integer.parseInt("42"));
        assertEquals(42, n);
    }

    @Test
    void d10() {
        String s = "Bonjour";
        assertAll(
                () -> assertEquals(7, s.length()),
                () -> assertTrue(s.startsWith("Bon")),
                () -> assertEquals('j', s.charAt(3)));
    }

    @Test
    void d11() {
        assertEquals(0.3, 0.1 + 0.2, 1e-9);
    }

    @Test
    void d12() {
        assertTimeout(Duration.ofSeconds(1), () -> assertEquals(5050, java.util.stream.IntStream.rangeClosed(1, 100).sum()));
    }
}
