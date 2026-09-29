package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 11 - Bits : masques de droits d'acces, et les 3 decalages << >> >>> (niveau : difficile)
 * ================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le contexte --
 *
 * Un fichier a 3 droits : lire (r), ecrire (w), executer (x). On les
 * range dans UN SEUL int, un bit par droit :
 *
 *   READ = 1 (binaire 001)   WRITE = 2 (010)   EXECUTE = 4 (100)
 *   "lire + executer" = 1 | 4 = 5 (binaire 101)
 *
 * Sur des ENTIERS, & | ^ ~ travaillent bit par bit (l'Exercise10 les
 * a vus sur des boolean). Les decalages deplacent les bits :
 *
 *   x << n  : vers la gauche, on ajoute des 0 a droite : x * 2^n
 *   x >> n  : vers la droite, en RECOPIANT le bit de signe : garde le signe
 *   x >>> n : vers la droite, en ajoutant des 0 : un negatif devient positif
 *
 * Valeurs REELLES (verifiees avec Java 17) :
 *
 *   -8 >> 1 == -4      -8 >>> 1 == 2147483644
 *   -7 >> 1 == -4      mais -7 / 2 == -3  (>> arrondit vers le bas, / vers zero)
 *   1 << 31 == -2147483648 (le bit de signe)   1 << 32 == 1 (!)   1L << 32 == 4294967296
 *   Pour un int, la distance de decalage est prise modulo 32 (modulo 64 pour un long).
 *   Le type du resultat est celui de l'operande de GAUCHE (promu) : int << long -> int.
 *
 *
 * ==================================================================
 * TODO 1 a 4 : grant, revoke, has, toggle
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque droit est un interrupteur dans une rangee.
 *   grant  (donner)  : allumer l'interrupteur        -> perms | flag
 *   revoke (retirer) : eteindre l'interrupteur        -> perms & ~flag
 *                      (~flag = tous les interrupteurs SAUF celui-la)
 *   has    (tester)  : l'interrupteur est-il allume ? -> (perms & flag) != 0
 *   toggle (basculer): inverser l'interrupteur        -> perms ^ flag
 *
 * -- Essayons a la main --
 *
 *   grant(READ, EXECUTE)  = 001 | 100 = 101 = 5
 *   revoke(7, WRITE)      = 111 & 101 = 101 = 5
 *   has(5, WRITE)         = 101 & 010 = 000 -> false
 *   toggle(5, READ)       = 101 ^ 001 = 100 = 4
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une ligne chacune. has sert au TODO 5.
 *
 *
 * ==================================================================
 * TODO 5 : describe(perms)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. 'r' si READ, sinon '-' ; 'w' si WRITE, sinon '-' ; 'x' si EXECUTE, sinon '-'.
 *
 * -- Essayons a la main --
 *
 *   7 -> "rwx" ; 5 -> "r-x" ; 0 -> "---"
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : has (TODO 3).
 *
 *
 * ==================================================================
 * TODO 6 a 8 : timesPowerOfTwo(x, n), halveSigned(x), halveUnsigned(x)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   timesPowerOfTwo : x << n.   halveSigned : x >> 1.   halveUnsigned : x >>> 1.
 *
 * -- Essayons a la main --
 *
 *   timesPowerOfTwo(3, 4) = 48 ; halveSigned(-8) = -4 ; halveUnsigned(-8) = 2147483644
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 9 : countOnes(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On compte les bits a 1 en regardant le dernier bit (x & 1), puis en
 * poussant tout d'un cran vers la droite, jusqu'a ce qu'il ne reste
 * que des 0. PIEGE : avec >>, un negatif garde son bit de signe a 1
 * pour toujours -> boucle infinie ! Il faut >>> (qui fait entrer des 0).
 *
 * -- Essayons a la main --
 *
 *   11 (1011) -> 3 ; 0 -> 0 ; -1 (32 bits a 1) -> 32
 *
 * -- Le plan --
 *
 *   1. count = 0 ; tant que x != 0 : count += x & 1 ; x = x >>> 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 10 : isPowerOfTwo(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une puissance de 2 n'a qu'UN bit a 1 (8 = 1000). Retirer 1 allume
 * tous les bits en dessous et eteint celui-la (7 = 0111). Donc
 * x & (x - 1) vaut 0 exactement pour les puissances de 2 (et pour 0,
 * qu'il faut exclure).
 *
 * -- Essayons a la main --
 *
 *   8 -> true ; 1 -> true ; 6 -> false ; 0 -> false ; -8 -> false
 *
 * -- Le plan --
 *
 *   1. x > 0 && (x & (x - 1)) == 0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - (perms & flag) != 0 : les parentheses sont OBLIGATOIRES, car != passe avant &
 *   - "" + (has(perms, READ) ? 'r' : '-') + ...
 *   - Integer.bitCount(x) donne la bonne reponse du TODO 9 (utilisee par main pour verifier)
 */
public class Exercise11_BitShiftsAndMasks {

    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int EXECUTE = 4;

    public static int grant(int perms, int flag) {
        throw new UnsupportedOperationException("TODO 1 : implementer grant()");
    }

    public static int revoke(int perms, int flag) {
        throw new UnsupportedOperationException("TODO 2 : implementer revoke()");
    }

    public static boolean has(int perms, int flag) {
        throw new UnsupportedOperationException("TODO 3 : implementer has()");
    }

    public static int toggle(int perms, int flag) {
        throw new UnsupportedOperationException("TODO 4 : implementer toggle()");
    }

    public static String describe(int perms) {
        throw new UnsupportedOperationException("TODO 5 : implementer describe()");
    }

    public static int timesPowerOfTwo(int x, int n) {
        throw new UnsupportedOperationException("TODO 6 : implementer timesPowerOfTwo()");
    }

    public static int halveSigned(int x) {
        throw new UnsupportedOperationException("TODO 7 : implementer halveSigned()");
    }

    public static int halveUnsigned(int x) {
        throw new UnsupportedOperationException("TODO 8 : implementer halveUnsigned()");
    }

    public static int countOnes(int x) {
        throw new UnsupportedOperationException("TODO 9 : implementer countOnes()");
    }

    public static boolean isPowerOfTwo(int x) {
        throw new UnsupportedOperationException("TODO 10 : implementer isPowerOfTwo()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 grant(READ, EXECUTE) == 5", grant(READ, EXECUTE) == 5);
        ExerciseChecker.check("2 revoke(7, WRITE) == 5, revoke(5, WRITE) == 5 (deja absent)",
                revoke(7, WRITE) == 5 && revoke(5, WRITE) == 5);
        ExerciseChecker.check("3 has(5, WRITE) == false, has(5, EXECUTE) == true", !has(5, WRITE) && has(5, EXECUTE));
        ExerciseChecker.check("4 toggle(5, READ) == 4, toggle(4, READ) == 5", toggle(5, READ) == 4 && toggle(4, READ) == 5);
        ExerciseChecker.check("5 describe : 7 -> rwx, 5 -> r-x, 0 -> ---",
                describe(7).equals("rwx") && describe(5).equals("r-x") && describe(0).equals("---"));
        ExerciseChecker.check("6 timesPowerOfTwo(3, 4) == 48", timesPowerOfTwo(3, 4) == 48);
        ExerciseChecker.check("7 halveSigned(-8) == -4, halveSigned(-7) == -4 (et -7 / 2 == -3)",
                halveSigned(-8) == -4 && halveSigned(-7) == -4 && -7 / 2 == -3);
        ExerciseChecker.check("8 halveUnsigned(-8) == 2147483644", halveUnsigned(-8) == 2147483644);
        boolean countOk = true;
        for (int x : new int[]{11, 0, -1, 255, Integer.MIN_VALUE, 123456789}) {
            countOk &= countOnes(x) == Integer.bitCount(x);
        }
        ExerciseChecker.check("9 countOnes == Integer.bitCount (dont -1 -> 32, sans boucle infinie)", countOk && countOnes(-1) == 32);
        ExerciseChecker.check("10 isPowerOfTwo : 8 et 1 oui ; 6, 0 et -8 non",
                isPowerOfTwo(8) && isPowerOfTwo(1) && !isPowerOfTwo(6) && !isPowerOfTwo(0) && !isPowerOfTwo(-8));

        ExerciseChecker.summary();
    }
}
