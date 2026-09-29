package ch4_coreapis.drills.solutions;

import java.util.Arrays;

/**
 * Corrige du drill 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill07_AlgorithmPatterns.
 */
public class SolutionDrill07_AlgorithmPatterns {

    public static void swap(int[] arr, int i, int j) {
        // La variable temporaire garde la premiere valeur avant qu'elle soit ecrasee.
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void reverseInPlace(int[] arr) {
        // i < j : au milieu d'un tableau impair, la case centrale ne bouge pas.
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            swap(arr, i, j);
        }
    }

    public static int indexOfMax(int[] arr) {
        // On garde l'INDEX du meilleur (la valeur se retrouve avec arr[best]).
        int best = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[best]) {
                best = i;
            }
        }
        return best;
    }

    public static int[] letterCounts(String s) {
        // c - 'a' transforme une lettre en index 0 a 25.
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        return counts;
    }

    public static boolean isPalindrome(String s) {
        // Deux pointeurs : a la premiere difference, ce n'est pas un palindrome.
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    public static int binarySearch(int[] sorted, int key) {
        // low <= high (avec le =) sinon on rate la derniere case ; >>> 1 evite le debordement.
        int low = 0;
        int high = sorted.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (sorted[mid] == key) {
                return mid;
            }
            if (sorted[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static int maxWindowSum(int[] arr, int k) {
        // Premiere fenetre, puis on la fait glisser : + entrant - sortant.
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        int best = sum;
        for (int i = k; i < arr.length; i++) {
            sum += arr[i] - arr[i - k];
            best = Math.max(best, sum);
        }
        return best;
    }

    public static int[] prefix(int[] arr) {
        // Une case de plus : p[0] = 0.
        int[] p = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            p[i + 1] = p[i] + arr[i];
        }
        return p;
    }

    public static int gcd(int a, int b) {
        // Euclide : on remplace (a, b) par (b, a % b) jusqu'a b == 0.
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static boolean isPrime(int n) {
        // Tester jusqu'a la racine suffit.
        if (n < 2) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int reverseDigits(int n) {
        // On retire le dernier chiffre de n et on l'ajoute a droite du resultat.
        int result = 0;
        while (n > 0) {
            result = result * 10 + n % 10;
            n /= 10;
        }
        return result;
    }

    public static String fizzBuzz(int n) {
        // Tester 15 (les deux) AVANT 3 et 5, sinon on n'y arrive jamais.
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if (i > 1) {
                sb.append(' ');
            }
            if (i % 15 == 0) {
                sb.append("FizzBuzz");
            } else if (i % 3 == 0) {
                sb.append("Fizz");
            } else if (i % 5 == 0) {
                sb.append("Buzz");
            } else {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public static int[] insertSorted(int[] sorted, int v) {
        // binarySearch donne le point d'insertion ; copyOf + decalage font la place.
        int pos = Arrays.binarySearch(sorted, v);
        if (pos < 0) {
            pos = -(pos) - 1;
        }
        int[] out = Arrays.copyOf(sorted, sorted.length + 1);
        for (int i = sorted.length; i > pos; i--) {
            out[i] = out[i - 1];
        }
        out[pos] = v;
        return out;
    }

    public static int countDistinctSorted(int[] arr) {
        // Trie : une nouvelle valeur = une valeur differente de la precedente.
        if (arr.length == 0) {
            return 0;
        }
        int count = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1]) {
                count++;
            }
        }
        return count;
    }
}
