package ch2_operators.drills.solutions;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.drills.exercises.Drill03_LogicAndBits.
 */
public class SolutionDrill03_LogicAndBits {

    public static boolean both(boolean a, boolean b) {
        // && : faux des que a est faux (b n'est alors pas evalue).
        return a && b;
    }

    public static boolean either(boolean a, boolean b) {
        // || : vrai des que a est vrai (b n'est alors pas evalue).
        return a || b;
    }

    public static boolean exactlyOne(boolean a, boolean b) {
        // ^ : vrai si les deux valeurs different.
        return a ^ b;
    }

    public static boolean opposite(boolean a) {
        // ! n'accepte qu'un boolean.
        return !a;
    }

    public static boolean safeRatioAbove(int a, int b, int n) {
        // Si b vaut 0, && s'arrete avant la division : pas d'ArithmeticException.
        return b != 0 && a / b > n;
    }

    public static boolean hasOption(int options, int option) {
        // Parentheses obligatoires : != passe avant &.
        return (options & option) != 0;
    }

    public static int addOption(int options, int option) {
        // | allume le bit de l'option.
        return options | option;
    }

    public static int removeOption(int options, int option) {
        // ~option : tous les bits sauf celui-la ; & eteint seulement ce bit.
        return options & ~option;
    }

    public static int flipOption(int options, int option) {
        // ^ inverse le bit : 101 ^ 100 == 001.
        return options ^ option;
    }

    public static int shiftLeft(int x, int n) {
        // Multiplie par 2^n : 3 * 4 == 12.
        return x << n;
    }

    public static int shiftRight(int x, int n) {
        // Recopie le bit de signe : -16 / 4 == -4.
        return x >> n;
    }

    public static int shiftRightUnsigned(int x, int n) {
        // Fait entrer des 0 : les 4 bits du haut de -16 (1111) deviennent 15.
        return x >>> n;
    }

    public static boolean isOdd(int n) {
        // Le dernier bit vaut 1 pour tout impair, meme negatif (contrairement a n % 2 == 1).
        return (n & 1) == 1;
    }

    public static int lowestBit(int n) {
        // -n == ~n + 1 : seul le bit a 1 le plus a droite survit au &.
        return n & -n;
    }

    public static int complement(int x) {
        // ~x == -(x + 1).
        return ~x;
    }
}
