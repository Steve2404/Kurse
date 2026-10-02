package ch5_methods.projects.p06_recursion.solution;

import ch5_methods.projects.p06_recursion.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 6 - le laboratoire de recursivite.
 * Toute methode recursive a : un CAS DE BASE (qui s'arrete) et un appel sur un probleme PLUS PETIT.
 */
public class RecursionLab {

    // Compteurs static : partages par tous les appels recursifs (une variable locale repartirait de 0).
    private static int calls;
    private static int comparisons;
    private static int multiplications;

    // ------------------------------------------------------------ 1. bases
    static long factorial(int n) {
        return n <= 1 ? 1 : n * factorial(n - 1);
    }

    static int digitSum(int n) {
        return n < 10 ? n : n % 10 + digitSum(n / 10);
    }

    // Exponentiation rapide : x^n = (x^(n/2))^2, d'ou O(log n) multiplications au lieu de n.
    static long power(long x, int n) {
        if (n == 0) {
            return 1;
        }
        long half = power(x, n / 2);
        multiplications++;
        long result = half * half;
        if (n % 2 == 1) {
            multiplications++;
            result *= x;
        }
        return result;
    }

    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static String binary(int n) {
        return n < 2 ? String.valueOf(n) : binary(n / 2) + n % 2;
    }

    static boolean palindrome(String s) {
        if (s.length() < 2) {
            return true;
        }
        return s.charAt(0) == s.charAt(s.length() - 1) && palindrome(s.substring(1, s.length() - 1));
    }

    // ------------------------------------------------------------ 2. Fibonacci
    static long fibNaive(int n) {
        calls++;
        return n < 2 ? n : fibNaive(n - 1) + fibNaive(n - 2);
    }

    // Memoisation : chaque valeur n'est calculee qu'une fois, puis lue dans le tableau.
    static long fibMemo(int n, long[] memo) {
        calls++;
        if (n < 2) {
            return n;
        }
        if (memo[n] != 0) {
            return memo[n];
        }
        memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        return memo[n];
    }

    // ------------------------------------------------------------ 3. diviser pour regner
    static int search(int[] a, int key, int low, int high) {
        if (low > high) {
            return -(low + 1);
        }
        int mid = (low + high) >>> 1;
        if (a[mid] == key) {
            return mid;
        }
        return a[mid] < key ? search(a, key, mid + 1, high) : search(a, key, low, mid - 1);
    }

    static void mergeSort(int[] a, int low, int high) {
        if (high - low < 1) {
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(a, low, mid);
        mergeSort(a, mid + 1, high);
        int[] merged = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= high) {
            comparisons++;
            merged[k++] = a[i] <= a[j] ? a[i++] : a[j++];   // <= : tri stable
        }
        while (i <= mid) {
            merged[k++] = a[i++];
        }
        while (j <= high) {
            merged[k++] = a[j++];
        }
        System.arraycopy(merged, 0, a, low, merged.length);
    }

    static void quickSort(int[] a, int low, int high) {
        if (low >= high) {
            return;
        }
        int pivot = a[high];
        int i = low;
        for (int j = low; j < high; j++) {
            comparisons++;
            if (a[j] < pivot) {
                int t = a[i];
                a[i++] = a[j];
                a[j] = t;
            }
        }
        int t = a[i];
        a[i] = a[high];
        a[high] = t;
        quickSort(a, low, i - 1);
        quickSort(a, i + 1, high);
    }

    // ------------------------------------------------------------ 4. Hanoi
    private static int moves;

    static void hanoi(int disks, char from, char to, char via, StringBuilder first) {
        if (disks == 0) {
            return;
        }
        hanoi(disks - 1, from, via, to, first);
        moves++;
        if (moves <= 5) {
            first.append(' ').append(disks).append(':').append(from).append("->").append(to);
        }
        hanoi(disks - 1, via, to, from, first);
    }

    // ------------------------------------------------------------ 5. enumerations
    // Chaque element est soit PRIS, soit LAISSE : 2^n sous-ensembles.
    static void subsets(int[] items, int index, String current, StringBuilder out) {
        if (index == items.length) {
            out.append(" {").append(current).append('}');
            return;
        }
        subsets(items, index + 1, current, out);
        subsets(items, index + 1, current.isEmpty() ? "" + items[index] : current + "," + items[index], out);
    }

    static int combinations(int start, int n, int k, String current, StringBuilder out) {
        if (k == 0) {
            out.append(' ').append(current);
            return 1;
        }
        int count = 0;
        for (int i = start; i <= n - k + 1; i++) {   // elagage : il doit rester assez d'elements
            count += combinations(i + 1, n, k - 1, current + i, out);
        }
        return count;
    }

    // ------------------------------------------------------------ 6. N reines (retour arriere)
    static int queens(int row, int[] cols, int[] firstSolution) {
        int n = cols.length;
        if (row == n) {
            if (firstSolution[0] == -1) {
                System.arraycopy(cols, 0, firstSolution, 0, n);
            }
            return 1;
        }
        int count = 0;
        for (int c = 0; c < n; c++) {
            if (safe(cols, row, c)) {
                cols[row] = c;
                count += queens(row + 1, cols, firstSolution);
            }
        }
        return count;
    }

    static boolean safe(int[] cols, int row, int c) {
        for (int r = 0; r < row; r++) {
            if (cols[r] == c || Math.abs(cols[r] - c) == row - r) {   // meme colonne ou meme diagonale
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------ 7. remplissage (flood fill)
    static int fill(char[][] g, int r, int c) {
        if (r < 0 || r >= g.length || c < 0 || c >= g[0].length || g[r][c] != '#') {
            return 0;
        }
        g[r][c] = '~';   // marquer AVANT de recurser, sinon on boucle a l'infini
        return 1 + fill(g, r + 1, c) + fill(g, r - 1, c) + fill(g, r, c + 1) + fill(g, r, c - 1);
    }

    // ------------------------------------------------------------ 8. rendu de monnaie
    // Nombre de facons : avec la piece i (et on peut la reprendre) + sans la piece i. memo[i][montant].
    static long ways(int[] coins, int i, int amount, long[][] memo) {
        if (amount == 0) {
            return 1;
        }
        if (amount < 0 || i == coins.length) {
            return 0;
        }
        if (memo[i][amount] != -1) {
            return memo[i][amount];
        }
        memo[i][amount] = ways(coins, i, amount - coins[i], memo) + ways(coins, i + 1, amount, memo);
        return memo[i][amount];
    }

    static int fewest(int[] coins, int amount, int[] memo) {
        if (amount == 0) {
            return 0;
        }
        if (memo[amount] != 0) {
            return memo[amount];
        }
        int best = Integer.MAX_VALUE;
        for (int coin : coins) {
            if (coin <= amount) {
                best = Math.min(best, 1 + fewest(coins, amount - coin, memo));
            }
        }
        memo[amount] = best;
        return best;
    }

    public static void main(String[] args) {
        System.out.println("bases : 20! = " + factorial(20) + ", somme des chiffres de 98765 = " + digitSum(98765) + ", pgcd(1071, 462) = " + gcd(1071, 462)
                + ", 37 en binaire = " + binary(37) + ", kayak " + palindrome("kayak") + ", kayaks " + palindrome("kayaks"));
        System.out.println("puissance : 3^20 = " + power(3, 20) + " en " + multiplications + " multiplications (au lieu de 19)");
        long naive = fibNaive(25);
        int naiveCalls = calls;
        calls = 0;
        long memo = fibMemo(25, new long[26]);
        System.out.println("fibonacci(25) = " + naive + " en " + naiveCalls + " appels ; memoise " + memo + " en " + calls + " appels ; fibonacci(90) = "
                + fibMemo(90, new long[91]));

        int[] merge = Data.UNSORTED.clone();
        comparisons = 0;
        mergeSort(merge, 0, merge.length - 1);
        int mergeComparisons = comparisons;
        int[] quick = Data.UNSORTED.clone();
        comparisons = 0;
        quickSort(quick, 0, quick.length - 1);
        System.out.println("tri fusion " + Arrays.toString(merge) + " (" + mergeComparisons + " comparaisons), tri rapide " + Arrays.equals(merge, quick) + " ("
                + comparisons + " comparaisons), original " + Arrays.toString(Data.UNSORTED));
        System.out.println("recherche recursive : 43 -> " + search(merge, 43, 0, merge.length - 1) + ", 11 -> " + search(merge, 11, 0, merge.length - 1));

        StringBuilder first = new StringBuilder();
        hanoi(4, 'A', 'C', 'B', first);
        System.out.println("hanoi 4 disques : " + moves + " deplacements (2^4 - 1), debut :" + first);

        StringBuilder subs = new StringBuilder();
        subsets(new int[] {1, 2, 3}, 0, "", subs);
        StringBuilder comb = new StringBuilder();
        int c = combinations(1, 5, 3, "", comb);
        System.out.println("sous-ensembles de {1,2,3} :" + subs);
        System.out.println("combinaisons 3 parmi 5 (" + c + ") :" + comb);

        int[] solution = new int[6];
        Arrays.fill(solution, -1);
        int six = queens(0, new int[6], solution);
        int[] ignored = {-1, 0, 0, 0, 0, 0, 0, 0};
        System.out.println("reines : 6x6 -> " + six + " solutions, 8x8 -> " + queens(0, new int[8], ignored) + " solutions ; premiere 6x6 :");
        for (int col : solution) {
            System.out.println("  " + ".".repeat(col) + "Q" + ".".repeat(5 - col));
        }

        char[][] grid = new char[Data.MAP.length][];
        for (int r = 0; r < grid.length; r++) {
            grid[r] = Data.MAP[r].toCharArray();
        }
        int islands = 0;
        int biggest = 0;
        StringBuilder sizes = new StringBuilder();
        for (int r = 0; r < grid.length; r++) {
            for (int col = 0; col < grid[r].length; col++) {
                int size = fill(grid, r, col);
                if (size > 0) {
                    islands++;
                    biggest = Math.max(biggest, size);
                    sizes.append(sizes.length() == 0 ? "" : ",").append(size);
                }
            }
        }
        System.out.println("iles : " + islands + " (tailles " + sizes + "), la plus grande " + biggest);

        long[][] table = new long[Data.COINS.length][Data.AMOUNT + 1];
        for (long[] row : table) {
            Arrays.fill(row, -1);
        }
        System.out.println("monnaie : " + ways(Data.COINS, 0, Data.AMOUNT, table) + " facons de faire " + Data.AMOUNT + ", minimum " + fewest(Data.COINS, Data.AMOUNT,
                new int[Data.AMOUNT + 1]) + " pieces, pour 63 : " + fewest(Data.COINS, 63, new int[64]));
    }
}
