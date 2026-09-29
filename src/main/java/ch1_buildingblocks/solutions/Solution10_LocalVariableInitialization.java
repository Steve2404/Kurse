package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise10_LocalVariableInitialization.
 */
public class Solution10_LocalVariableInitialization {

    public static String grade(int score) {
        // Pas de valeur de depart : c'est le "else" final qui prouve au compilateur
        // que result est initialisee sur TOUS les chemins.
        String result;
        if (score >= 90) {
            result = "A";
        } else if (score >= 70) {
            result = "B";
        } else if (score >= 50) {
            result = "C";
        } else {
            result = "D";
        }
        return result;
    }

    public static int sumOfPair() {
        // Declaration groupee : permise avec un type explicite (pas avec var).
        int a = 3, b = 4;
        return a + b;
    }

    public static int discountedPriceCents(int priceCents, boolean vip) {
        // if ET else donnent chacun une valeur : sans le else, "might not have been initialized".
        int discount;
        if (vip) {
            discount = priceCents / 10;
        } else {
            discount = 0;
        }
        return priceCents - discount;
    }

    public static int firstIndexOf(int[] values, int target) {
        // Valeur "pas trouve" donnee des le depart : la variable est initialisee meme
        // si la boucle ne tourne jamais (tableau vide).
        int found = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == target) {
                found = i;
                break;
            }
        }
        return found;
    }
}
