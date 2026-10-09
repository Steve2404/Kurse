package ch17_algorithms.projects.p04_hashing.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference des classiques du hachage. */
class HashingTest {

    @Test
    void twoSumFindsTheFirstCompletedPair() {
        assertAll(
                () -> assertArrayEquals(new int[]{0, 1}, Hashing.twoSum(new int[]{2, 7, 11, 15}, 9)),
                () -> assertArrayEquals(new int[]{1, 2}, Hashing.twoSum(new int[]{3, 2, 4}, 6)),
                () -> assertArrayEquals(new int[]{0, 1}, Hashing.twoSum(new int[]{3, 3}, 6)),
                () -> assertArrayEquals(new int[]{0, 3}, Hashing.twoSum(new int[]{4, 1, 2, 6}, 10)),
                () -> assertArrayEquals(new int[]{0, 2}, Hashing.twoSum(new int[]{5, 5, 1}, 6)),
                () -> assertArrayEquals(new int[0], Hashing.twoSum(new int[]{1, 2, 3}, 100)),
                () -> assertArrayEquals(new int[0], Hashing.twoSum(new int[]{5}, 10)));
    }

    @Test
    void anagramsShareTheirSortedLetters() {
        Map<String, List<String>> groups = Hashing.groupAnagrams(List.of("chien", "niche", "chine", "rage", "gare", "loup"));
        assertEquals(Map.of("cehin", List.of("chien", "niche", "chine"), "aegr", List.of("rage", "gare"), "lopu", List.of("loup")), groups);
        assertEquals(Map.of(), Hashing.groupAnagrams(List.of()));
    }

    @ParameterizedTest(name = "\"{0}\" : {1}")
    @CsvSource({"leetcode, l", "loveleetcode, v", "aabbc, c", "abcabc, ''", "z, z"})
    void firstUniqueCharacter(String s, String expected) {
        Character got = Hashing.firstUnique(s);
        if (expected.isEmpty()) {
            assertNull(got);
        } else {
            assertEquals(expected.charAt(0), got);
        }
    }

    @Test
    void firstUniqueOfAnEmptyString() {
        assertNull(Hashing.firstUnique(""));
    }

    @Test
    void topWordsByFrequencyThenAlphabet() {
        String text = "Le chat et le chien. Le chat dort, le chien joue ; ET le loup ?";
        assertAll(
                () -> assertEquals(List.of("le", "chat", "chien"), Hashing.topWords(text, 3)),
                () -> assertEquals(List.of("le", "chat", "chien", "et", "dort"), Hashing.topWords(text, 5)),
                () -> assertEquals(List.of("a"), Hashing.topWords("a", 10)),
                () -> assertEquals(List.of("chat", "ours"), Hashing.topWords("ours chat", 2)),
                () -> assertEquals(List.of(), Hashing.topWords("... !", 2)));
    }

    @ParameterizedTest(name = "suite la plus longue : {1}")
    @CsvSource(delimiter = '|', value = {"100,4,200,1,3,2|4", "0,3,7,2,5,8,4,6,0,1|9", "5|1", "1,1,1|1", "-2,-1,0,9|3"})
    void longestConsecutiveRun(String values, int expected) {
        int[] a = java.util.Arrays.stream(values.split(",")).mapToInt(Integer::parseInt).toArray();
        assertEquals(expected, Hashing.longestConsecutive(a));
    }

    @Test
    void longestConsecutiveOfNothing() {
        assertEquals(0, Hashing.longestConsecutive(new int[0]));
    }

    @Test
    void hashingIsLinear() {
        int n = 1_000_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = 2 * i;
        }
        int[] shuffled = new Random(3).ints(n, 0, Integer.MAX_VALUE).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            assertArrayEquals(new int[0], Hashing.twoSum(a, 1));
            assertEquals(n, Hashing.longestConsecutive(java.util.stream.IntStream.range(0, n).map(i -> n - i).toArray()));
            Hashing.longestConsecutive(shuffled);
        });
    }
}
