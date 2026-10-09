package ch16_testing.projects.p04_roman.solution;

import java.util.Objects;

/** Les chiffres romains de 1 a 3999, dans les deux sens. Construit par TDD : voir CORRIGE.md, cycle par cycle. */
public final class RomanNumerals {

    // Pourquoi les paires soustractives (900, 400...) DANS la table : l'algorithme glouton n'a plus de cas particulier.
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    private RomanNumerals() {
    }

    // Glouton : a chaque fois, le plus grand symbole qui rentre encore.
    public static String toRoman(int n) {
        if (n < 1 || n > 3999) {
            throw new IllegalArgumentException("hors limites : " + n);
        }
        StringBuilder sb = new StringBuilder();
        int rest = n;
        for (int i = 0; i < VALUES.length; i++) {
            while (rest >= VALUES[i]) {
                sb.append(SYMBOLS[i]);
                rest -= VALUES[i];
            }
        }
        return sb.toString();
    }

    // Un symbole plus petit que son voisin de droite se SOUSTRAIT (IV = 5 - 1).
    // Piege : "IIII", "IC" ou "VV" donnent un nombre, mais ne sont pas des ecritures correctes.
    // Pourquoi la verification par toRoman : il n'y a qu'UNE ecriture correcte par nombre ; on la compare a l'entree.
    public static int fromRoman(String s) {
        Objects.requireNonNull(s, "chiffre absent");
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int value = value(s.charAt(i), s);
            int next = i + 1 < s.length() ? value(s.charAt(i + 1), s) : 0;
            total += value < next ? -value : value;
        }
        if (total < 1 || total > 3999 || !toRoman(total).equals(s)) {
            throw new IllegalArgumentException("chiffre romain invalide : " + s);
        }
        return total;
    }

    private static int value(char c, String s) {
        return switch (c) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            case 'L' -> 50;
            case 'C' -> 100;
            case 'D' -> 500;
            case 'M' -> 1000;
            default -> throw new IllegalArgumentException("chiffre romain invalide : " + s);
        };
    }
}
