package ch17_algorithms.projects.p05_stacks.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference des piles monotones. */
class MonotonicTest {

    @Test
    void daysUntilAWarmerDay() {
        assertAll(
                () -> assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, Monotonic.daysUntilWarmer(new int[]{73, 74, 75, 71, 69, 72, 76, 73})),
                () -> assertArrayEquals(new int[]{0, 0, 0}, Monotonic.daysUntilWarmer(new int[]{30, 30, 30})),
                () -> assertArrayEquals(new int[]{1, 1, 0}, Monotonic.daysUntilWarmer(new int[]{10, 20, 30})),
                () -> assertArrayEquals(new int[0], Monotonic.daysUntilWarmer(new int[0])));
    }

    static int[] parse(String s) {
        return s == null || s.isEmpty() ? new int[0] : Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    @ParameterizedTest(name = "[{0}] : {1}")
    @CsvSource(delimiter = '|', value = {"2,1,5,6,2,3|10", "2,4|4", "6,2,5,4,5,1,6|12", "5|5", "3,3,3|9", "1,2,3,4,5|9", "|0"})
    void largestRectangleInAHistogram(String heights, long expected) {
        assertEquals(expected, Monotonic.largestRectangle(parse(heights)));
    }

    @Test
    void hugeRectangleDoesNotOverflow() {
        int[] h = new int[100_000];
        Arrays.fill(h, 100_000);
        assertEquals(10_000_000_000L, Monotonic.largestRectangle(h));
    }

    @Test
    void monotonicStacksAreLinear() {
        int n = 1_000_000;
        int[] down = new int[n];
        int[] up = new int[n];
        for (int i = 0; i < n; i++) {
            down[i] = n - i;
            up[i] = i;
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(0, Monotonic.daysUntilWarmer(down)[0]);
            assertEquals(1, Monotonic.daysUntilWarmer(up)[0]);
            Monotonic.largestRectangle(down);
            Monotonic.largestRectangle(up);
        });
    }
}
