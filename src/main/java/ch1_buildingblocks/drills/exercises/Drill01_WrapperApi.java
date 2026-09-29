package ch1_buildingblocks.drills.exercises;

import ch1_buildingblocks.ExerciseChecker;
import ch1_buildingblocks.drills.Shop;

/**
 * DRILL 01 - Les methodes des classes wrapper : Integer, Long, Double, Boolean, Character (projet caisse)
 * ======================================================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en 1 ligne et vise
 * UNE methode precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (calendrier et
 *      remise a zero : drills/REVISION.md).
 *
 * Donnees : ch1_buildingblocks.drills.Shop.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : parse(text)          [Integer.parseInt] "42" -> 42.
 * TODO 2  : parseBinary()        [Integer.parseInt(texte, base)] Shop.BINARY_FLAGS "1010" -> 10.
 * TODO 3  : parseHex()           [Integer.parseInt(texte, 16)] Shop.HEX_COLOR "FF" -> 255.
 * TODO 4  : box(value)           [Integer.valueOf] int -> Integer.
 * TODO 5  : unbox(box)           [intValue] Integer -> int.
 * TODO 6  : maxInt()             [Integer.MAX_VALUE] 2147483647.
 * TODO 7  : minLong()            [Long.MIN_VALUE]
 * TODO 8  : compare(a, b)        [Integer.compare] negatif / 0 / positif.
 * TODO 9  : bigger(a, b)         [Integer.max]
 * TODO 10 : sum(a, b)            [Integer.sum]
 * TODO 11 : toBinary(value)      [Integer.toBinaryString] 10 -> "1010".
 * TODO 12 : toHex(value)         [Integer.toHexString] 255 -> "ff" (minuscules).
 * TODO 13 : parsePrice(text)     [Double.parseDouble] "0.50" -> 0.5.
 * TODO 14 : parseFlag(text)      [Boolean.parseBoolean] "TRUE" -> true, "yes" -> false.
 * TODO 15 : isDigit(c)           [Character.isDigit]
 * TODO 16 : isLetter(c)          [Character.isLetter]
 * TODO 17 : upper(c)             [Character.toUpperCase] 'x' -> 'X'.
 * TODO 18 : digitValue(c)        [Character.getNumericValue] '7' -> 7.
 * TODO 19 : parseBig(text)       [Long.parseLong] "9000000000" (trop grand pour un int).
 * TODO 20 : toText(value)        [String.valueOf ou Integer.toString] 42 -> "42".
 * TODO 21 : sameValue(a, b)      [Integer.equals] compare les VALEURS de 2 Integer (jamais ==).
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Texte -> nombre : Integer.parseInt(s) / parseInt(s, base)   -> int
 *                     Integer.valueOf(s)                        -> Integer
 *                     Long.parseLong, Double.parseDouble, Boolean.parseBoolean
 *                     (parseInt lance NumberFormatException ; parseBoolean jamais)
 *   Nombre -> texte : String.valueOf(n), Integer.toString(n),
 *                     Integer.toBinaryString(n), Integer.toHexString(n)
 *   Boite           : Integer.valueOf(int) (cache -128..127), box.intValue()
 *   Constantes      : Integer.MAX_VALUE / MIN_VALUE, Long.MAX_VALUE / MIN_VALUE, Byte.MAX_VALUE...
 *   Outils          : Integer.compare(a, b), Integer.max, Integer.min, Integer.sum
 *   Character       : isDigit, isLetter, isWhitespace, isUpperCase, toUpperCase,
 *                     toLowerCase, getNumericValue
 *   Comparer 2 Integer : a.equals(b), JAMAIS a == b (compare les boites)
 * ---------------------------------------------------------------------
 */
public class Drill01_WrapperApi {

    public static int parse(String text) {
        throw new UnsupportedOperationException("TODO 1 : implementer parse()");
    }

    public static int parseBinary() {
        throw new UnsupportedOperationException("TODO 2 : implementer parseBinary()");
    }

    public static int parseHex() {
        throw new UnsupportedOperationException("TODO 3 : implementer parseHex()");
    }

    public static Integer box(int value) {
        throw new UnsupportedOperationException("TODO 4 : implementer box()");
    }

    public static int unbox(Integer box) {
        throw new UnsupportedOperationException("TODO 5 : implementer unbox()");
    }

    public static int maxInt() {
        throw new UnsupportedOperationException("TODO 6 : implementer maxInt()");
    }

    public static long minLong() {
        throw new UnsupportedOperationException("TODO 7 : implementer minLong()");
    }

    public static int compare(int a, int b) {
        throw new UnsupportedOperationException("TODO 8 : implementer compare()");
    }

    public static int bigger(int a, int b) {
        throw new UnsupportedOperationException("TODO 9 : implementer bigger()");
    }

    public static int sum(int a, int b) {
        throw new UnsupportedOperationException("TODO 10 : implementer sum()");
    }

    public static String toBinary(int value) {
        throw new UnsupportedOperationException("TODO 11 : implementer toBinary()");
    }

    public static String toHex(int value) {
        throw new UnsupportedOperationException("TODO 12 : implementer toHex()");
    }

    public static double parsePrice(String text) {
        throw new UnsupportedOperationException("TODO 13 : implementer parsePrice()");
    }

    public static boolean parseFlag(String text) {
        throw new UnsupportedOperationException("TODO 14 : implementer parseFlag()");
    }

    public static boolean isDigit(char c) {
        throw new UnsupportedOperationException("TODO 15 : implementer isDigit()");
    }

    public static boolean isLetter(char c) {
        throw new UnsupportedOperationException("TODO 16 : implementer isLetter()");
    }

    public static char upper(char c) {
        throw new UnsupportedOperationException("TODO 17 : implementer upper()");
    }

    public static int digitValue(char c) {
        throw new UnsupportedOperationException("TODO 18 : implementer digitValue()");
    }

    public static long parseBig(String text) {
        throw new UnsupportedOperationException("TODO 19 : implementer parseBig()");
    }

    public static String toText(int value) {
        throw new UnsupportedOperationException("TODO 20 : implementer toText()");
    }

    public static boolean sameValue(Integer a, Integer b) {
        throw new UnsupportedOperationException("TODO 21 : implementer sameValue()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  parse(\"42\") == 42", parse("42") == 42);
        ExerciseChecker.check("2  parseBinary() == 10", parseBinary() == 10);
        ExerciseChecker.check("3  parseHex() == 255", parseHex() == 255);
        ExerciseChecker.check("4  box(7) est un Integer egal a 7", box(7).equals(7));
        ExerciseChecker.check("5  unbox(Integer.valueOf(9)) == 9", unbox(Integer.valueOf(9)) == 9);
        ExerciseChecker.check("6  maxInt() == 2147483647", maxInt() == 2147483647);
        ExerciseChecker.check("7  minLong() == -9223372036854775808", minLong() == -9223372036854775808L);
        ExerciseChecker.check("8  compare(3, 9) < 0, compare(9, 3) > 0, compare(4, 4) == 0",
                compare(3, 9) < 0 && compare(9, 3) > 0 && compare(4, 4) == 0);
        ExerciseChecker.check("9  bigger(3, 9) == 9", bigger(3, 9) == 9);
        ExerciseChecker.check("10 sum(3, 9) == 12", sum(3, 9) == 12);
        ExerciseChecker.check("11 toBinary(10) == \"1010\"", toBinary(10).equals("1010"));
        ExerciseChecker.check("12 toHex(255) == \"ff\"", toHex(255).equals("ff"));
        ExerciseChecker.check("13 parsePrice(\"0.50\") == 0.5", parsePrice("0.50") == 0.5);
        ExerciseChecker.check("14 parseFlag : TRUE -> true, yes -> false", parseFlag("TRUE") && !parseFlag("yes"));
        ExerciseChecker.check("15 isDigit('7') && !isDigit('A')", isDigit('7') && !isDigit('A'));
        ExerciseChecker.check("16 isLetter('A') && !isLetter('-')", isLetter('A') && !isLetter('-'));
        ExerciseChecker.check("17 upper('x') == 'X'", upper('x') == 'X');
        ExerciseChecker.check("18 digitValue('7') == 7", digitValue('7') == 7);
        ExerciseChecker.check("19 parseBig(\"9000000000\") == 9000000000", parseBig("9000000000") == 9_000_000_000L);
        ExerciseChecker.check("20 toText(42) == \"42\"", toText(42).equals("42"));
        ExerciseChecker.check("21 sameValue(1000, 1000) == true (equals, pas ==)",
                sameValue(Integer.valueOf(1000), Integer.valueOf(1000)));

        ExerciseChecker.summary();
    }
}
