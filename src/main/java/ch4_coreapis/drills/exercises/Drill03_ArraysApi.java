package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

import java.util.Arrays;

/**
 * DRILL 03 - L'API Arrays et les tableaux : une methode par TODO
 * ==============================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Ne modifie JAMAIS les tableaux
 * de Journal (ils sont partages) : travaille sur une copie quand il le faut.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : scoresText()          [Arrays.toString] -> "[42, 7, 19, 88, 7, 63]".
 * TODO 2  : sortedCopy()          [Arrays.copyOf puis Arrays.sort] -> [7, 7, 19, 42, 63, 88] ; SCORES intact.
 * TODO 3  : findSorted(value)     [Arrays.binarySearch] dans SORTED : 23 -> 3 ; 10 -> -3.
 * TODO 4  : firstThree()          [Arrays.copyOfRange] -> [42, 7, 19].
 * TODO 5  : filled(size, value)   [Arrays.fill] (4, 9) -> [9, 9, 9, 9].
 * TODO 6  : sameAsScores(other)   [Arrays.equals] (et PAS other.equals(...)).
 * TODO 7  : compareWithSorted()   [Arrays.compare] SCORES contre SORTED -> positif (42 > 3).
 * TODO 8  : firstDifference()     [Arrays.mismatch] SCORES contre {42, 7, 20} -> 2.
 * TODO 9  : gridText()            [Arrays.deepToString] {{1, 2}, {3}} -> "[[1, 2], [3]]".
 * TODO 10 : sortedWords()         [Arrays.sort sur des String] copie de WORDS -> [Alpha, Echo, bravo, charlie, delta].
 * TODO 11 : renameThroughList()   [Arrays.asList(...).set] sur une copie de WORDS : set(0, "zulu") ; rendre copie[0] -> "zulu".
 * TODO 12 : staircase(n)          [new int[n][] puis une ligne par tour] n = 3 -> longueurs 1, 2, 3.
 * TODO 13 : defaultsText()        [valeurs par defaut] Arrays.toString(new boolean[2]) -> "[false, false]".
 * TODO 14 : sortFirstThree()      [Arrays.sort(a, debut, fin)] copie de SCORES -> [7, 19, 42, 88, 7, 63].
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Arrays.toString(a) deepToString(m) sort(a) sort(a, d, f) binarySearch(a, v)
 *   copyOf(a, n) copyOfRange(a, d, f) fill(a, v) fill(a, d, f, v)
 *   equals(a, b) deepEquals(m1, m2) compare(a, b) mismatch(a, b) asList(...)
 *   binarySearch absent -> -(point d'insertion) - 1 ; tableau NON trie -> resultat indefini
 *   compare : signe seulement ; mismatch : -1 si identiques
 *   Ordre des String : chiffres < MAJUSCULES < minuscules
 *   a.length (champ) ; s.length() (methode) ; list.size()
 * ---------------------------------------------------------------------
 */
public class Drill03_ArraysApi {

    public static String scoresText() {
        throw new UnsupportedOperationException("TODO 1 : implementer scoresText()");
    }

    public static int[] sortedCopy() {
        throw new UnsupportedOperationException("TODO 2 : implementer sortedCopy()");
    }

    public static int findSorted(int value) {
        throw new UnsupportedOperationException("TODO 3 : implementer findSorted()");
    }

    public static int[] firstThree() {
        throw new UnsupportedOperationException("TODO 4 : implementer firstThree()");
    }

    public static int[] filled(int size, int value) {
        throw new UnsupportedOperationException("TODO 5 : implementer filled()");
    }

    public static boolean sameAsScores(int[] other) {
        throw new UnsupportedOperationException("TODO 6 : implementer sameAsScores()");
    }

    public static int compareWithSorted() {
        throw new UnsupportedOperationException("TODO 7 : implementer compareWithSorted()");
    }

    public static int firstDifference() {
        throw new UnsupportedOperationException("TODO 8 : implementer firstDifference()");
    }

    public static String gridText() {
        throw new UnsupportedOperationException("TODO 9 : implementer gridText()");
    }

    public static String[] sortedWords() {
        throw new UnsupportedOperationException("TODO 10 : implementer sortedWords()");
    }

    public static String renameThroughList() {
        throw new UnsupportedOperationException("TODO 11 : implementer renameThroughList()");
    }

    public static int[][] staircase(int n) {
        throw new UnsupportedOperationException("TODO 12 : implementer staircase()");
    }

    public static String defaultsText() {
        throw new UnsupportedOperationException("TODO 13 : implementer defaultsText()");
    }

    public static int[] sortFirstThree() {
        throw new UnsupportedOperationException("TODO 14 : implementer sortFirstThree()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  scoresText()", scoresText().equals("[42, 7, 19, 88, 7, 63]"));
        ExerciseChecker.check("2  sortedCopy() et SCORES intact",
                Arrays.equals(sortedCopy(), new int[] {7, 7, 19, 42, 63, 88}) && Journal.SCORES[0] == 42);
        ExerciseChecker.check("3  findSorted : 23 -> 3, 10 -> -3", findSorted(23) == 3 && findSorted(10) == -3);
        ExerciseChecker.check("4  firstThree()", Arrays.equals(firstThree(), new int[] {42, 7, 19}));
        ExerciseChecker.check("5  filled(4, 9)", Arrays.equals(filled(4, 9), new int[] {9, 9, 9, 9}));
        ExerciseChecker.check("6  sameAsScores", sameAsScores(new int[] {42, 7, 19, 88, 7, 63}) && !sameAsScores(new int[] {42}));
        ExerciseChecker.check("7  compareWithSorted() > 0", compareWithSorted() > 0);
        ExerciseChecker.check("8  firstDifference() == 2", firstDifference() == 2);
        ExerciseChecker.check("9  gridText()", gridText().equals("[[1, 2], [3]]"));
        ExerciseChecker.check("10 sortedWords() et WORDS intact",
                Arrays.equals(sortedWords(), new String[] {"Alpha", "Echo", "bravo", "charlie", "delta"}) && Journal.WORDS[0].equals("delta"));
        ExerciseChecker.check("11 renameThroughList() == \"zulu\" et WORDS intact", renameThroughList().equals("zulu") && Journal.WORDS[0].equals("delta"));
        int[][] stairs = staircase(3);
        ExerciseChecker.check("12 staircase(3) : longueurs 1, 2, 3",
                stairs.length == 3 && stairs[0].length == 1 && stairs[1].length == 2 && stairs[2].length == 3);
        ExerciseChecker.check("13 defaultsText()", defaultsText().equals("[false, false]"));
        ExerciseChecker.check("14 sortFirstThree() et SCORES intact",
                Arrays.equals(sortFirstThree(), new int[] {7, 19, 42, 88, 7, 63}) && Journal.SCORES[0] == 42);

        ExerciseChecker.summary();
    }
}
