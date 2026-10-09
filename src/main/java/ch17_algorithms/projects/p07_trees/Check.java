package ch17_algorithms.projects.p07_trees;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON SearchTree et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("SearchTree.java", "            if (cmp == 0) {\n                return false;\n            }", "            if (cmp == 0) {\n                size++;\n                return false;\n            }"),
            new Mutant("SearchTree.java", "return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));", "return n == null ? 0 : 1 + height(n.left);"),
            new Mutant("SearchTree.java", "            } else {\n                best = n.key;\n                n = n.right;", "            } else {\n                n = n.right;"),
            new Mutant("SearchTree.java", "            if (cmp > 0) {\n                n = n.right;\n            } else {\n                best = n.key;\n                n = n.left;", "            if (cmp > 0) {\n                best = n.key;\n                n = n.right;\n            } else {\n                n = n.left;"),
            new Mutant("SearchTree.java", "            out.add(n.key);\n            preOrder(n.left, out);", "            preOrder(n.left, out);\n            out.add(n.key);"),
            new Mutant("SearchTree.java", "Node<K> n = queue.poll();", "Node<K> n = queue.pollLast();"),
            new Mutant("SearchTree.java", "        if (n.left == null) {\n            size--;\n            return n.right;", "        if (n.left == null) {\n            size--;\n            return null;"),
            new Mutant("SearchTree.java", "        while (successor.left != null) {\n            successor = successor.left;\n        }", "        while (successor.right != null) {\n            successor = successor.right;\n        }"),
            new Mutant("SearchTree.java", "replacement.left = n.left;", "replacement.left = null;"),
            new Mutant("SearchTree.java", "        if (n.key.compareTo(lo) < 0) {\n            return rangeCount(n.right, lo, hi);", "        if (n.key.compareTo(lo) <= 0) {\n            return rangeCount(n.right, lo, hi);"),
            new Mutant("SearchTree.java", "} else if (a.compareTo(n.key) > 0 && b.compareTo(n.key) > 0) {", "} else if (a.compareTo(n.key) > 0 || b.compareTo(n.key) > 0) {"),
            new Mutant("SearchTree.java", "if (!contains(a) || !contains(b)) {", "if (!contains(a)) {"),
            new Mutant("SearchTree.java", "            throw new NoSuchElementException(\"arbre vide\");", "            return null;"));

    static final List<String> API_CODE = List.of(
            "final class SearchTree<K extends Comparable<K>>", "boolean add(K key)", "boolean contains(K key)",
            "boolean remove(K key)", "int size()", "int height()", "K min()", "K max()", "K floor(K key)", "K ceiling(K key)",
            "List<K> inOrder()", "List<K> preOrder()", "List<K> levelOrder()", "int rangeCount(K lo, K hi)",
            "K lowestCommonAncestor(K a, K b)", ".compareTo(",
            "!TreeMap", "!TreeSet", "!Collections.sort", "!.sort(");

    static final List<String> API_TESTS = List.of(
            "@BeforeEach", "@Test", "@ParameterizedTest", "assertEquals(", "assertThrows(", "assertTimeoutPreemptively(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 25, MUTANTS, API_CODE, API_TESTS);
    }
}
