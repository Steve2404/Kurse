package ch17_algorithms.projects.p02_sorting;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Sorting et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE : un tri trop lent echoue.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Sorting.java", "for (int i = 1; i < a.length; i++) {\n            int key = a[i];", "for (int i = 2; i < a.length; i++) {\n            int key = a[i];"),
            new Mutant("Sorting.java", "        while (i < mid) {\n            tmp[k++] = a[i++];\n        }\n", ""),
            new Mutant("Sorting.java", "mergeSort(a, tmp, mid, hi);", "mergeSort(a, tmp, mid + 1, hi);"),
            new Mutant("Sorting.java", "} else {\n                i++;\n            }\n        }\n        quickSort(a, lo, lt - 1);",
                    "} else {\n                swap(a, i, gt--);\n            }\n        }\n        quickSort(a, lo, lt - 1);"),
            new Mutant("Sorting.java", "quickSort(a, gt + 1, hi);", "quickSort(a, gt + 2, hi);"),
            new Mutant("Sorting.java", "int[] counts = new int[max + 1];", "int[] counts = new int[max + 2];\n        max = max - 1;"),
            new Mutant("Sorting.java", "if (v < 0 || v > max) {", "if (v > max) {"),
            new Mutant("Sorting.java", "cmp.compare(left.get(i), right.get(j)) <= 0", "cmp.compare(left.get(i), right.get(j)) < 0"),
            new Mutant("Sorting.java", "result.addAll(right.subList(j, right.size()));", ""),
            new Mutant("Sorting.java", "mergeSort(list.subList(0, mid), cmp)", "mergeSort(list.subList(0, Math.max(0, mid - 1)), cmp)"));

    static final List<String> API_CODE = List.of(
            "final class Sorting", "static void insertionSort(int[] a)", "static void mergeSort(int[] a)",
            "static void quickSort(int[] a)", "static void countingSort(int[] a, int max)",
            "static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp)", "record Runner(String name, int seconds)",
            // Tu ECRIS les tris : aucun tri tout fait.
            "!Arrays.sort", "!Collections.sort", "!.sort(", "!.sorted(", "!TreeMap", "!TreeSet", "!PriorityQueue");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertArrayEquals(", "assertTimeoutPreemptively(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 10, MUTANTS, API_CODE, API_TESTS);
    }
}
