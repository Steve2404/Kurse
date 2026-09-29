package ch1_buildingblocks.drills.solutions;

import ch1_buildingblocks.drills.Shop;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.drills.exercises.Drill01_WrapperApi.
 */
public class SolutionDrill01_WrapperApi {

    public static int parse(String text) {
        // parseInt rend un int (primitif) ; valueOf rendrait un Integer (boite).
        return Integer.parseInt(text);
    }

    public static int parseBinary() {
        // Le 2e argument est la base : "1010" en base 2 vaut 10.
        return Integer.parseInt(Shop.BINARY_FLAGS, 2);
    }

    public static int parseHex() {
        // En base 16, les lettres A-F (majuscules ou minuscules) sont des chiffres.
        return Integer.parseInt(Shop.HEX_COLOR, 16);
    }

    public static Integer box(int value) {
        // valueOf reutilise les boites du cache pour -128..127.
        return Integer.valueOf(value);
    }

    public static int unbox(Integer box) {
        // intValue ouvre la boite ; sur un null, ce serait une NullPointerException.
        return box.intValue();
    }

    public static int maxInt() {
        // 2^31 - 1 : au-dela, un int deborde sans prevenir.
        return Integer.MAX_VALUE;
    }

    public static long minLong() {
        // Chaque wrapper numerique a ses constantes MIN_VALUE / MAX_VALUE.
        return Long.MIN_VALUE;
    }

    public static int compare(int a, int b) {
        // Plus sur que a - b, qui peut deborder pour des valeurs extremes.
        return Integer.compare(a, b);
    }

    public static int bigger(int a, int b) {
        // Integer.max existe (depuis Java 8), comme Math.max.
        return Integer.max(a, b);
    }

    public static int sum(int a, int b) {
        // Integer.sum sert surtout comme reference de methode (Integer::sum) aux chapitres 8 et 10.
        return Integer.sum(a, b);
    }

    public static String toBinary(int value) {
        // Pas de zeros inutiles devant : 10 -> "1010".
        return Integer.toBinaryString(value);
    }

    public static String toHex(int value) {
        // Les lettres sortent en minuscules.
        return Integer.toHexString(value);
    }

    public static double parsePrice(String text) {
        // Le point est le separateur decimal, quelle que soit la langue de la machine.
        return Double.parseDouble(text);
    }

    public static boolean parseFlag(String text) {
        // true seulement pour "true" (casse ignoree) ; jamais d'exception.
        return Boolean.parseBoolean(text);
    }

    public static boolean isDigit(char c) {
        // Methode static de Character : un char primitif n'a aucune methode.
        return Character.isDigit(c);
    }

    public static boolean isLetter(char c) {
        // Accepte aussi les lettres accentuees et des autres alphabets.
        return Character.isLetter(c);
    }

    public static char upper(char c) {
        // Surcharge char -> char (il en existe aussi une int -> int).
        return Character.toUpperCase(c);
    }

    public static int digitValue(char c) {
        // '7' a le code 55 ; getNumericValue donne la valeur 7.
        return Character.getNumericValue(c);
    }

    public static long parseBig(String text) {
        // 9 milliards depassent Integer.MAX_VALUE : parseInt lancerait NumberFormatException.
        return Long.parseLong(text);
    }

    public static String toText(int value) {
        // String.valueOf fonctionne pour tous les types (Integer.toString aussi pour un int).
        return String.valueOf(value);
    }

    public static boolean sameValue(Integer a, Integer b) {
        // equals compare les valeurs ; == comparerait les boites (faux pour 1000 hors cache).
        return a.equals(b);
    }
}
