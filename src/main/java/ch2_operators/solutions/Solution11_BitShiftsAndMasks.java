package ch2_operators.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise11_BitShiftsAndMasks.
 */
public class Solution11_BitShiftsAndMasks {

    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int EXECUTE = 4;

    public static int grant(int perms, int flag) {
        // | allume le bit du droit sans toucher aux autres (deja allume : aucun effet).
        return perms | flag;
    }

    public static int revoke(int perms, int flag) {
        // ~flag = tous les bits sauf celui du droit ; & eteint donc seulement ce bit-la.
        return perms & ~flag;
    }

    public static boolean has(int perms, int flag) {
        // Parentheses obligatoires : sans elles, flag != 0 serait calcule d'abord (precedence).
        return (perms & flag) != 0;
    }

    public static int toggle(int perms, int flag) {
        // ^ inverse un bit : 1 ^ 1 == 0, 0 ^ 1 == 1.
        return perms ^ flag;
    }

    public static String describe(int perms) {
        // "" en tete pour que + soit une concatenation (sinon char + char serait une addition).
        return "" + (has(perms, READ) ? 'r' : '-') + (has(perms, WRITE) ? 'w' : '-') + (has(perms, EXECUTE) ? 'x' : '-');
    }

    public static int timesPowerOfTwo(int x, int n) {
        // Decaler de n vers la gauche multiplie par 2^n (tant que ca ne deborde pas).
        return x << n;
    }

    public static int halveSigned(int x) {
        // >> recopie le bit de signe : garde le signe mais arrondit vers le BAS (-7 -> -4).
        return x >> 1;
    }

    public static int halveUnsigned(int x) {
        // >>> fait entrer un 0 a gauche : un negatif devient un tres grand positif.
        return x >>> 1;
    }

    public static int countOnes(int x) {
        // >>> et pas >> : avec >>, un negatif garderait un 1 a gauche et la boucle ne finirait jamais.
        int count = 0;
        while (x != 0) {
            count += x & 1;
            x = x >>> 1;
        }
        return count;
    }

    public static boolean isPowerOfTwo(int x) {
        // Une puissance de 2 a un seul bit a 1 : x - 1 eteint ce bit et allume ceux du dessous.
        return x > 0 && (x & (x - 1)) == 0;
    }
}
