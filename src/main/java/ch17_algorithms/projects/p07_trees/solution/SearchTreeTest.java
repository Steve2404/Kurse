package ch17_algorithms.projects.p07_trees.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Les tests de reference du projet 7, sur cet arbre :
 * <pre>
 *          50
 *        /    \
 *      30      70
 *     /  \    /  \
 *   20   40  60   80
 *       /  \   \
 *      35  45   65
 * </pre>
 */
class SearchTreeTest {

    private SearchTree<Integer> tree;

    @BeforeEach
    void build() {
        tree = new SearchTree<>();
        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80, 35, 45, 65}) {
            tree.add(k);
        }
    }

    @Test
    void addContainsSize() {
        assertAll(
                () -> assertEquals(10, tree.size()),
                () -> assertFalse(tree.add(40)),
                () -> assertEquals(10, tree.size()),
                () -> assertTrue(tree.contains(65)),
                () -> assertFalse(tree.contains(66)),
                () -> assertTrue(tree.add(66)),
                () -> assertTrue(tree.contains(66)));
    }

    @Test
    void heightCountsNodesOnTheLongestPath() {
        assertAll(
                () -> assertEquals(4, tree.height()),
                () -> assertEquals(0, new SearchTree<Integer>().height()));
        SearchTree<Integer> one = new SearchTree<>();
        one.add(7);
        assertEquals(1, one.height());
    }

    @Test
    void minAndMax() {
        assertAll(
                () -> assertEquals(20, tree.min()),
                () -> assertEquals(80, tree.max()));
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> new SearchTree<Integer>().min());
        assertEquals("arbre vide", e.getMessage());
        assertThrows(NoSuchElementException.class, () -> new SearchTree<Integer>().max());
    }

    @ParameterizedTest(name = "plancher et plafond de {0} : {1} et {2}")
    @CsvSource({"42, 40, 45", "65, 65, 65", "10, , 20", "81, 80, ", "55, 50, 60", "36, 35, 40"})
    void floorAndCeiling(int key, Integer floor, Integer ceiling) {
        assertAll(
                () -> assertEquals(floor, tree.floor(key)),
                () -> assertEquals(ceiling, tree.ceiling(key)));
    }

    @Test
    void theThreeTraversals() {
        assertAll(
                () -> assertEquals(List.of(20, 30, 35, 40, 45, 50, 60, 65, 70, 80), tree.inOrder()),
                () -> assertEquals(List.of(50, 30, 20, 40, 35, 45, 70, 60, 65, 80), tree.preOrder()),
                () -> assertEquals(List.of(50, 30, 70, 20, 40, 60, 80, 35, 45, 65), tree.levelOrder()),
                () -> assertEquals(List.of(), new SearchTree<Integer>().levelOrder()));
    }

    @Test
    void removeALeafAndANodeWithOneChild() {
        assertTrue(tree.remove(20));
        assertTrue(tree.remove(60));
        assertAll(
                () -> assertEquals(List.of(50, 30, 40, 35, 45, 70, 65, 80), tree.preOrder()),
                () -> assertEquals(8, tree.size()),
                () -> assertFalse(tree.remove(99)),
                () -> assertEquals(8, tree.size()));
    }

    // Deux enfants : 30 est remplace par son successeur, 35 (le plus petit a sa droite).
    @Test
    void removeANodeWithTwoChildren() {
        assertTrue(tree.remove(30));
        assertAll(
                () -> assertEquals(List.of(50, 35, 20, 40, 45, 70, 60, 65, 80), tree.preOrder()),
                () -> assertEquals(9, tree.size()),
                () -> assertFalse(tree.contains(30)));
    }

    @Test
    void removeTheRoot() {
        assertTrue(tree.remove(50));
        assertAll(
                () -> assertEquals(List.of(60, 30, 20, 40, 35, 45, 70, 65, 80), tree.preOrder()),
                () -> assertEquals(List.of(20, 30, 35, 40, 45, 60, 65, 70, 80), tree.inOrder()));
    }

    @ParameterizedTest(name = "cles dans [{0}, {1}] : {2}")
    @CsvSource({"33, 66, 6", "0, 100, 10", "41, 44, 0", "50, 50, 1", "20, 20, 1"})
    void countKeysInARange(int lo, int hi, int expected) {
        assertEquals(expected, tree.rangeCount(lo, hi));
    }

    @ParameterizedTest(name = "ancetre commun de {0} et {1} : {2}")
    @CsvSource({"35, 45, 40", "20, 45, 30", "35, 65, 50", "60, 65, 60", "80, 80, 80"})
    void lowestCommonAncestor(int a, int b, int expected) {
        assertEquals(expected, tree.lowestCommonAncestor(a, b));
    }

    @Test
    void ancestorOfAnAbsentKeyIsRefused() {
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> tree.lowestCommonAncestor(35, 99));
        assertEquals("cle absente", e.getMessage());
    }

    @Test
    void worksWithStringsToo() {
        SearchTree<String> names = new SearchTree<>();
        for (String s : List.of("Marc", "Ana", "Zoe", "Bob")) {
            names.add(s);
        }
        assertAll(
                () -> assertEquals(List.of("Ana", "Bob", "Marc", "Zoe"), names.inOrder()),
                () -> assertEquals("Bob", names.floor("Carl")),
                () -> assertNull(names.ceiling("Zz")));
    }

    @Test
    void aMillionRandomKeysStayLogarithmic() {
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            keys.add(i);
        }
        Collections.shuffle(keys, new Random(7));
        SearchTree<Integer> big = new SearchTree<>();
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            for (int k : keys) {
                big.add(k);
            }
            for (int k = 0; k < 1_000_000; k += 3) {
                assertTrue(big.contains(k));
            }
            assertEquals(1_000_000, big.size());
            assertEquals(1001, big.rangeCount(1000, 2000));
        });
        assertTrue(big.height() < 60, "hauteur " + big.height());
    }
}
