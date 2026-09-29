package ch1_buildingblocks.drills.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * DRILL 02 - Litteraux et types primitifs : bases, suffixes, char, limites, valeurs par defaut
 * ============================================================================================
 *
 * Mode d'emploi : voir Drill01_WrapperApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Particularite : pour les TODO 1 a 8, le test ne peut verifier que la
 * VALEUR, pas la facon de l'ecrire. Joue le jeu : ecris le litteral
 * EXACTEMENT sous la forme demandee (c'est ca que l'examen teste).
 *
 *
 * -- Les TODO (forme demandee entre crochets) --
 *
 * TODO 1  : binaryTen()        [litteral binaire 0b...] -> 10.
 * TODO 2  : octalTen()         [litteral octal 0...] -> 10.
 * TODO 3  : hexTen()           [litteral hexadecimal 0x...] -> 10.
 * TODO 4  : oneMillion()       [litteral avec des _] -> 1000000.
 * TODO 5  : nineBillion()      [litteral long avec suffixe L] -> 9000000000.
 * TODO 6  : half()             [litteral float avec suffixe f] -> 0.5f.
 * TODO 7  : letterA()          [litteral char 'A'].
 * TODO 8  : unicodeA()         [echappement unicode 'A'] -> 'A'.
 * TODO 9  : nextLetter(c)      [arithmetique sur char + cast] 'a' -> 'b'.
 * TODO 10 : codeOf(c)          [conversion char -> int] 'a' -> 97.
 * TODO 11 : byteMax()          [Byte.MAX_VALUE] 127.
 * TODO 12 : shortMin()         [Short.MIN_VALUE] -32768.
 * TODO 13 : overflow()         [Integer.MAX_VALUE + 1] -> -2147483648 (debordement silencieux).
 * TODO 14 : defaults()         [valeurs par defaut des champs] "0/0.0/false/0/null"
 *                              (byte, double, boolean, code du char, String de la classe Defaults).
 * TODO 15 : widen(value)       [elargissement automatique int -> long] rendre value en long.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Bases     : 0b1010 (binaire) 012 (octal : commence par 0) 0xA (hexa) 10 (decimal)
 *   _         : seulement ENTRE deux chiffres : 1_000_000, jamais au bord ni pres du point
 *   Suffixes  : L (long), f/F (float), d/D (double, facultatif) ; 3.14 seul = double
 *   char      : 'A', 'A', 65 ; 'a' + 1 est un int (98) ; (char) ('a' + 1) = 'b'
 *   Tailles   : byte 8 bits (-128..127), short 16, int 32, long 64, float 32, double 64, char 16 (0..65535)
 *   Defauts (champs seulement) : 0, 0L, 0.0, false, '\u0000' (code 0), null
 *   Debordement : Integer.MAX_VALUE + 1 == Integer.MIN_VALUE, sans aucune erreur
 * ---------------------------------------------------------------------
 */
public class Drill02_LiteralsAndPrimitives {

    static class Defaults {
        byte b;
        double d;
        boolean z;
        char c;
        String s;
    }

    public static int binaryTen() {
        throw new UnsupportedOperationException("TODO 1 : implementer binaryTen()");
    }

    public static int octalTen() {
        throw new UnsupportedOperationException("TODO 2 : implementer octalTen()");
    }

    public static int hexTen() {
        throw new UnsupportedOperationException("TODO 3 : implementer hexTen()");
    }

    public static int oneMillion() {
        throw new UnsupportedOperationException("TODO 4 : implementer oneMillion()");
    }

    public static long nineBillion() {
        throw new UnsupportedOperationException("TODO 5 : implementer nineBillion()");
    }

    public static float half() {
        throw new UnsupportedOperationException("TODO 6 : implementer half()");
    }

    public static char letterA() {
        throw new UnsupportedOperationException("TODO 7 : implementer letterA()");
    }

    public static char unicodeA() {
        throw new UnsupportedOperationException("TODO 8 : implementer unicodeA()");
    }

    public static char nextLetter(char c) {
        throw new UnsupportedOperationException("TODO 9 : implementer nextLetter()");
    }

    public static int codeOf(char c) {
        throw new UnsupportedOperationException("TODO 10 : implementer codeOf()");
    }

    public static byte byteMax() {
        throw new UnsupportedOperationException("TODO 11 : implementer byteMax()");
    }

    public static short shortMin() {
        throw new UnsupportedOperationException("TODO 12 : implementer shortMin()");
    }

    public static int overflow() {
        throw new UnsupportedOperationException("TODO 13 : implementer overflow()");
    }

    public static String defaults() {
        throw new UnsupportedOperationException("TODO 14 : implementer defaults()");
    }

    public static long widen(int value) {
        throw new UnsupportedOperationException("TODO 15 : implementer widen()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  binaryTen() == 10", binaryTen() == 10);
        ExerciseChecker.check("2  octalTen() == 10", octalTen() == 10);
        ExerciseChecker.check("3  hexTen() == 10", hexTen() == 10);
        ExerciseChecker.check("4  oneMillion() == 1000000", oneMillion() == 1000000);
        ExerciseChecker.check("5  nineBillion() == 9000000000", nineBillion() == 9000000000L);
        ExerciseChecker.check("6  half() == 0.5f", half() == 0.5f);
        ExerciseChecker.check("7  letterA() == 'A'", letterA() == 'A');
        ExerciseChecker.check("8  unicodeA() == 'A'", unicodeA() == 'A');
        ExerciseChecker.check("9  nextLetter('a') == 'b'", nextLetter('a') == 'b');
        ExerciseChecker.check("10 codeOf('a') == 97", codeOf('a') == 97);
        ExerciseChecker.check("11 byteMax() == 127", byteMax() == 127);
        ExerciseChecker.check("12 shortMin() == -32768", shortMin() == -32768);
        ExerciseChecker.check("13 overflow() == -2147483648", overflow() == -2147483648);
        ExerciseChecker.check("14 defaults() == \"0/0.0/false/0/null\"", defaults().equals("0/0.0/false/0/null"));
        ExerciseChecker.check("15 widen(5) == 5L", widen(5) == 5L);

        ExerciseChecker.summary();
    }
}
