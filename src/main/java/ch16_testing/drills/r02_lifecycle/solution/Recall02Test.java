package ch16_testing.drills.r02_lifecycle.solution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/** Le corrige du drill 2 : le cycle de vie des tests. */
class Recall02Test {

    static int answer;
    List<String> items;

    @BeforeAll
    static void once() {
        answer = 41;
    }

    @BeforeEach
    void fresh() {
        items = new ArrayList<>();
        items.add("outer");
    }

    @AfterEach
    void clean() {
        items.clear();
    }

    // d01 et d02 passent dans n'importe quel ordre : chaque test a SA liste neuve.
    @Test
    void d01() {
        items.add("a");
        assertEquals(List.of("outer", "a"), items);
    }

    @Test
    void d02() {
        items.add("b");
        items.add("c");
        assertEquals(3, items.size());
    }

    @Test
    void d03() {
        assertEquals(42, answer + 1);
    }

    @RepeatedTest(3)
    void d04(RepetitionInfo info) {
        assertEquals(3, info.getTotalRepetitions());
        assertTrue(info.getCurrentRepetition() >= 1 && info.getCurrentRepetition() <= 3);
    }

    @Test
    @Disabled("pas encore")
    void d05() {
        fail("ne doit jamais s'executer");
    }

    @Nested
    class Inner {

        @BeforeEach
        void more() {
            items.add("inner");
        }

        @Test
        void d06() {
            assertEquals(List.of("outer", "inner"), items);
        }
    }

    @Test
    @DisplayName("un nom en francais")
    @Tag("rapide")
    void d07() {
        assertTrue(items.contains("outer"));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Shared {

        int counter;

        @BeforeAll
        void start() {
            counter = 0;
        }

        @Test
        void d08a() {
            counter++;
            assertTrue(counter >= 1 && counter <= 2);
        }

        @Test
        void d08b() {
            counter++;
            assertTrue(counter >= 1 && counter <= 2);
        }
    }
}
