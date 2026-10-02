package ch4_coreapis.drills.r12_algos.solution;

import java.util.Arrays;

/**
 * SOLUTION du drill de rappel 12 - les algorithmes classiques, ecrits de memoire.
 */
public class Recall12 {

    public static void main(String[] args) {
        // D01 : inversion en place, deux pointeurs qui se rapprochent.
        int[] a = {1, 2, 3, 4, 5, 6};
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        System.out.println("D01 : " + Arrays.toString(a));

        int[] sorted = {3, 8, 15, 17, 23, 29, 42};
        System.out.println("D02 : " + search(sorted, 23) + " " + search(sorted, 10) + " " + search(sorted, 50) + " "
                + (search(sorted, 10) == Arrays.binarySearch(sorted, 10)));

        // D03 : tri par insertion.
        int[] b = {5, 2, 9, 1, 5, 6};
        for (int i = 1; i < b.length; i++) {
            int key = b[i];
            int j = i - 1;
            while (j >= 0 && b[j] > key) {
                b[j + 1] = b[j--];
            }
            b[j + 1] = key;
        }
        System.out.println("D03 : " + Arrays.toString(b));

        // D04 : Kadane.
        int[] p = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int current = p[0];
        int best = p[0];
        for (int i = 1; i < p.length; i++) {
            current = Math.max(p[i], current + p[i]);
            best = Math.max(best, current);
        }
        System.out.println("D04 : " + best);

        // D05 : crible d'Eratosthene.
        boolean[] composite = new boolean[101];
        int count = 0;
        for (int i = 2; i <= 100; i++) {
            if (!composite[i]) {
                count++;
                for (int m = i * i; m <= 100; m += i) {
                    composite[m] = true;
                }
            }
        }
        System.out.println("D05 : " + count);

        // D06 : palindrome en ignorant tout sauf les lettres, puis mots inverses.
        String s = "A man, a plan, a canal: Panama";
        StringBuilder letters = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = Character.toLowerCase(s.charAt(i));
            if (c >= 'a' && c <= 'z') {
                letters.append(c);
            }
        }
        String clean = letters.toString();
        String[] words = "le chat noir".split(" ");
        StringBuilder reversed = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            reversed.append(words[i]).append(i > 0 ? " " : "");
        }
        System.out.println("D06 : " + clean.equals(letters.reverse().toString()) + " " + reversed);

        // D07 : la lettre la plus frequente (a egalite, la premiere dans l'alphabet).
        int[] freq = new int[26];
        for (char c : "mississippi".toCharArray()) {
            freq[c - 'a']++;
        }
        int top = 0;
        for (int i = 1; i < 26; i++) {
            if (freq[i] > freq[top]) {
                top = i;
            }
        }
        System.out.println("D07 : " + (char) ('a' + top) + freq[top]);

        // D08 : produit de matrices 2x2, puis rotation a droite de 2 crans.
        int[][] m = {{1, 2}, {3, 4}};
        int[][] square = new int[2][2];
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                for (int k = 0; k < 2; k++) {
                    square[r][c] += m[r][k] * m[k][c];
                }
            }
        }
        int[] rot = {1, 2, 3, 4, 5};
        int[] rotated = new int[rot.length];
        for (int i = 0; i < rot.length; i++) {
            rotated[(i + 2) % rot.length] = rot[i];
        }
        System.out.println("D08 : " + Arrays.deepToString(square) + " " + Arrays.toString(rotated));
    }

    // D02 : recherche dichotomique avec la convention de Arrays.binarySearch.
    static int search(int[] a, int key) {
        int low = 0;
        int high = a.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (a[mid] < key) {
                low = mid + 1;
            } else if (a[mid] > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
}
