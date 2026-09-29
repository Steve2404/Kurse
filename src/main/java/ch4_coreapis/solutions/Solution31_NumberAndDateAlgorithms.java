package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 31. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise31_NumberAndDateAlgorithms.
 */
public class Solution31_NumberAndDateAlgorithms {

    public static boolean isPrime(int n) {
        // i * i <= n : un diviseur plus grand que la racine aurait un partenaire plus petit, deja teste.
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

    public static int[] primesUpTo(int n) {
        // Crible : un boolean[] (false par defaut) ; on raye a partir de i * i (les plus petits multiples sont deja rayes).
        boolean[] crossed = new boolean[n + 1];
        for (int i = 2; i * i <= n; i++) {
            if (!crossed[i]) {
                for (int m = i * i; m <= n; m += i) {
                    crossed[m] = true;
                }
            }
        }
        int[] primes = new int[n + 1];
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (!crossed[i]) {
                primes[count++] = i;
            }
        }
        return Arrays.copyOf(primes, count);
    }

    public static int gcd(int a, int b) {
        // Euclide : pgcd(a, b) = pgcd(b, a % b), jusqu'a un reste nul.
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static int lcm(int a, int b) {
        // Diviser avant de multiplier limite le risque de debordement.
        return a / gcd(a, b) * b;
    }

    public static int digitSum(int n) {
        // % 10 lit le dernier chiffre, / 10 l'enleve ; abs gere les negatifs.
        n = Math.abs(n);
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    public static int fibonacciOrMinusOne(int n) {
        // addExact lance ArithmeticException au premier debordement ; on ne calcule JAMAIS un terme de trop.
        if (n == 0) {
            return 0;
        }
        try {
            int a = 0;
            int b = 1;
            for (int i = 1; i < n; i++) {
                int next = Math.addExact(a, b);
                a = b;
                b = next;
            }
            return b;
        } catch (ArithmeticException e) {
            return -1;
        }
    }

    public static int dayOfWeek(int year, int month, int day) {
        // Sakamoto : janvier et fevrier comptent dans l'annee precedente (le 29 fevrier est "a la fin").
        int[] t = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        if (month < 3) {
            year--;
        }
        int w = (year + year / 4 - year / 100 + year / 400 + t[month - 1] + day) % 7;
        return w == 0 ? 7 : w;
    }
}
