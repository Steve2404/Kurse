package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

import java.util.Arrays;

/**
 * DRILL 07 - Les schemas d'algorithme a connaitre par coeur (a ecrire en moins d'une minute chacun)
 * ================================================================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Ici, chaque TODO est un SCHEMA
 * (5 a 10 lignes) qui revient dans presque tous les problemes. Le but :
 * l'ecrire sans reflechir, comme on tape son code de carte bleue.
 * Objectif de temps : tout le drill en moins de 20 minutes.
 *
 *
 * -- Les TODO (schema vise entre crochets) --
 *
 * TODO 1  : swap(arr, i, j)             [echange avec variable temporaire]
 * TODO 2  : reverseInPlace(arr)         [deux pointeurs qui se rapprochent] {1, 2, 3, 4} -> {4, 3, 2, 1}.
 * TODO 3  : indexOfMax(arr)             [garder le meilleur] sur Journal.SCORES -> 3 (88).
 * TODO 4  : letterCounts(s)             [compter avec un int[26]] "banana" -> a 3, b 1, n 2.
 * TODO 5  : isPalindrome(s)             [deux pointeurs] "kayak" -> true.
 * TODO 6  : binarySearch(sorted, key)   [low / high / mid] dans Journal.SORTED : 42 -> 4 ; 10 -> -1.
 * TODO 7  : maxWindowSum(arr, k)        [fenetre glissante] {2, 1, 5, 1, 3, 2}, 3 -> 9.
 * TODO 8  : prefix(arr)                 [sommes prefixes, une case de plus] {3, 1, 4} -> {0, 3, 4, 8}.
 * TODO 9  : gcd(a, b)                   [Euclide] (84, 36) -> 12.
 * TODO 10 : isPrime(n)                  [i * i <= n] 97 -> true, 91 -> false.
 * TODO 11 : reverseDigits(n)            [% 10 et / 10] 1234 -> 4321.
 * TODO 12 : fizzBuzz(n)                 [StringBuilder + %] 15 -> "1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz".
 * TODO 13 : insertSorted(sorted, v)     [binarySearch + copie] {1, 3, 5}, 4 -> {1, 3, 4, 5}.
 * TODO 14 : countDistinctSorted(arr)    [compter les changements] {1, 1, 2, 3, 3} -> 3.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   swap           : int t = a[i]; a[i] = a[j]; a[j] = t;
 *   2 pointeurs    : for (int i = 0, j = n - 1; i < j; i++, j--)
 *   meilleur       : int best = 0; for (i = 1..) if (a[i] > a[best]) best = i;
 *   int[26]        : counts[c - 'a']++
 *   dichotomie     : while (low <= high) { mid = (low + high) >>> 1; ... low = mid + 1 / high = mid - 1 }
 *   fenetre        : sum += a[i] - a[i - k]
 *   prefixes       : p[i + 1] = p[i] + a[i]
 *   Euclide        : while (b != 0) { r = a % b; a = b; b = r; }
 *   chiffres       : while (n > 0) { d = n % 10; n /= 10; }
 *   insertion      : pos = binarySearch ; si < 0 : pos = -(pos) - 1 ; copier avant, v, apres
 * ---------------------------------------------------------------------
 */
public class Drill07_AlgorithmPatterns {

    public static void swap(int[] arr, int i, int j) {
        throw new UnsupportedOperationException("TODO 1 : implementer swap()");
    }

    public static void reverseInPlace(int[] arr) {
        throw new UnsupportedOperationException("TODO 2 : implementer reverseInPlace()");
    }

    public static int indexOfMax(int[] arr) {
        throw new UnsupportedOperationException("TODO 3 : implementer indexOfMax()");
    }

    public static int[] letterCounts(String s) {
        throw new UnsupportedOperationException("TODO 4 : implementer letterCounts()");
    }

    public static boolean isPalindrome(String s) {
        throw new UnsupportedOperationException("TODO 5 : implementer isPalindrome()");
    }

    public static int binarySearch(int[] sorted, int key) {
        throw new UnsupportedOperationException("TODO 6 : implementer binarySearch()");
    }

    public static int maxWindowSum(int[] arr, int k) {
        throw new UnsupportedOperationException("TODO 7 : implementer maxWindowSum()");
    }

    public static int[] prefix(int[] arr) {
        throw new UnsupportedOperationException("TODO 8 : implementer prefix()");
    }

    public static int gcd(int a, int b) {
        throw new UnsupportedOperationException("TODO 9 : implementer gcd()");
    }

    public static boolean isPrime(int n) {
        throw new UnsupportedOperationException("TODO 10 : implementer isPrime()");
    }

    public static int reverseDigits(int n) {
        throw new UnsupportedOperationException("TODO 11 : implementer reverseDigits()");
    }

    public static String fizzBuzz(int n) {
        throw new UnsupportedOperationException("TODO 12 : implementer fizzBuzz()");
    }

    public static int[] insertSorted(int[] sorted, int v) {
        throw new UnsupportedOperationException("TODO 13 : implementer insertSorted()");
    }

    public static int countDistinctSorted(int[] arr) {
        throw new UnsupportedOperationException("TODO 14 : implementer countDistinctSorted()");
    }

    public static void main(String[] args) {
        int[] pair = {1, 2};
        swap(pair, 0, 1);
        ExerciseChecker.check("1  swap -> {2, 1}", Arrays.equals(pair, new int[] {2, 1}));
        int[] four = {1, 2, 3, 4};
        reverseInPlace(four);
        int[] three = {1, 2, 3};
        reverseInPlace(three);
        ExerciseChecker.check("2  reverseInPlace (pair et impair)",
                Arrays.equals(four, new int[] {4, 3, 2, 1}) && Arrays.equals(three, new int[] {3, 2, 1}));
        ExerciseChecker.check("3  indexOfMax(SCORES) == 3", indexOfMax(Journal.SCORES) == 3);
        int[] counts = letterCounts("banana");
        ExerciseChecker.check("4  letterCounts(\"banana\")", counts.length == 26 && counts[0] == 3 && counts[1] == 1 && counts['n' - 'a'] == 2);
        ExerciseChecker.check("5  isPalindrome : kayak oui, java non", isPalindrome("kayak") && !isPalindrome("java"));
        ExerciseChecker.check("6  binarySearch : 42 -> 4, 10 -> -1", binarySearch(Journal.SORTED, 42) == 4 && binarySearch(Journal.SORTED, 10) == -1);
        ExerciseChecker.check("7  maxWindowSum == 9", maxWindowSum(new int[] {2, 1, 5, 1, 3, 2}, 3) == 9);
        ExerciseChecker.check("8  prefix", Arrays.equals(prefix(new int[] {3, 1, 4}), new int[] {0, 3, 4, 8}));
        ExerciseChecker.check("9  gcd(84, 36) == 12", gcd(84, 36) == 12);
        ExerciseChecker.check("10 isPrime : 97 oui, 91 non, 1 non", isPrime(97) && !isPrime(91) && !isPrime(1));
        ExerciseChecker.check("11 reverseDigits(1234) == 4321", reverseDigits(1234) == 4321);
        ExerciseChecker.check("12 fizzBuzz(15)", fizzBuzz(15).equals("1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz"));
        ExerciseChecker.check("13 insertSorted : milieu, debut, fin",
                Arrays.equals(insertSorted(new int[] {1, 3, 5}, 4), new int[] {1, 3, 4, 5})
                        && Arrays.equals(insertSorted(new int[] {1, 3, 5}, 0), new int[] {0, 1, 3, 5})
                        && Arrays.equals(insertSorted(new int[] {1, 3, 5}, 9), new int[] {1, 3, 5, 9}));
        ExerciseChecker.check("14 countDistinctSorted : 3 et 0",
                countDistinctSorted(new int[] {1, 1, 2, 3, 3}) == 3 && countDistinctSorted(new int[0]) == 0);

        ExerciseChecker.summary();
    }
}
