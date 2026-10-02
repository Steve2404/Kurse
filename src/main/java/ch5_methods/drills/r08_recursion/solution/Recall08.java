package ch5_methods.drills.r08_recursion.solution;

/**
 * SOLUTION du drill de rappel 8 - la recursivite.
 */
public class Recall08 {

    static int sumTo(int n) {
        return n == 0 ? 0 : n + sumTo(n - 1);
    }

    static String reverse(String s) {
        return s.isEmpty() ? "" : reverse(s.substring(1)) + s.charAt(0);
    }

    static int countChar(String s, char c) {
        if (s.isEmpty()) {
            return 0;
        }
        return (s.charAt(0) == c ? 1 : 0) + countChar(s.substring(1), c);
    }

    static int max(int[] a, int i) {
        return i == a.length - 1 ? a[i] : Math.max(a[i], max(a, i + 1));
    }

    static long pow(long x, int n) {
        if (n == 0) {
            return 1;
        }
        long h = pow(x, n / 2);
        return n % 2 == 0 ? h * h : h * h * x;
    }

    static int depth;

    static int ackermannLike(int n) {
        depth++;
        return n <= 1 ? 1 : ackermannLike(n / 2) + ackermannLike(n - 1);
    }

    static String toBase(int n, int base) {
        String digits = "0123456789ABCDEF";
        return n < base ? "" + digits.charAt(n) : toBase(n / base, base) + digits.charAt(n % base);
    }

    static int paths(int r, int c, int[][] memo) {
        if (r == 0 || c == 0) {
            return 1;
        }
        if (memo[r][c] != 0) {
            return memo[r][c];
        }
        memo[r][c] = paths(r - 1, c, memo) + paths(r, c - 1, memo);
        return memo[r][c];
    }

    static void countdown(int n, StringBuilder out) {
        if (n < 0) {
            return;
        }
        out.append(n).append(' ');
        countdown(n - 1, out);
        out.append(n).append(' ');   // apres l'appel : s'execute en REMONTANT
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + sumTo(100) + " " + reverse("recursion") + " " + countChar("banana", 'a'));
        System.out.println("D02 : " + max(new int[] {3, 9, 2, 7}, 0) + " " + pow(2, 30) + " " + pow(7, 0));
        System.out.println("D03 : " + ackermannLike(6) + " " + depth);
        System.out.println("D04 : " + toBase(255, 16) + " " + toBase(10, 2) + " " + toBase(0, 8));
        System.out.println("D05 : " + paths(2, 2, new int[3][3]) + " " + paths(10, 10, new int[11][11]));
        StringBuilder out = new StringBuilder();
        countdown(3, out);
        System.out.println("D06 : " + out.toString().strip());
    }
}
