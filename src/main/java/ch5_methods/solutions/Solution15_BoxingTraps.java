package ch5_methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise15_BoxingTraps.
 */
public class Solution15_BoxingTraps {

    public static boolean sameValue(Integer a, Integer b) {
        // equals compare les valeurs (== compare les adresses, faux au-dela du cache -128..127).
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static void removeValue(List<Integer> list, int value) {
        // Un Integer force remove(Object) : on supprime la VALEUR.
        list.remove(Integer.valueOf(value));
    }

    public static void removeAt(List<Integer> list, int index) {
        // Un int choisit remove(int index) : pas besoin de boxing, donc cette surcharge gagne.
        list.remove(index);
    }

    public static int sumIgnoringNulls(List<Integer> values) {
        // Deballer null lance NullPointerException : on filtre avant.
        int total = 0;
        for (Integer v : values) {
            if (v != null) {
                total += v;
            }
        }
        return total;
    }

    public static int orDefault(Integer value, int fallback) {
        // value n'est deballe que si la branche est choisie, donc jamais quand il est null.
        return value == null ? fallback : value;
    }

    public static int countEqual(List<Long> values, long target) {
        // v == target deballe v en long ; v.equals(1) comparerait un Long a un Integer (toujours faux).
        int count = 0;
        for (Long v : values) {
            if (v != null && v == target) {
                count++;
            }
        }
        return count;
    }

    public static Integer parseOrNull(String text) {
        // valueOf rend un Integer ; texte invalide ou null -> NumberFormatException.
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
