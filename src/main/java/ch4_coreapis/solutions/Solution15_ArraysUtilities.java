package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise15_ArraysUtilities.
 */
public class Solution15_ArraysUtilities {

    public static int[] grow(int[] arr) {
        // copyOf fabrique un NOUVEAU tableau et complete avec 0 ; Math.max gere le tableau vide.
        return Arrays.copyOf(arr, Math.max(1, arr.length * 2));
    }

    public static int[] page(int[] arr, int pageNumber, int pageSize) {
        // Bornes calculees avec Math.min : copyOfRange au-dela de la fin ajouterait des 0.
        int from = Math.min(pageNumber * pageSize, arr.length);
        int to = Math.min(from + pageSize, arr.length);
        return Arrays.copyOfRange(arr, from, to);
    }

    public static void sortMiddle(int[] arr) {
        // sort(debut, fin) trie seulement la tranche ; fin EXCLUE.
        Arrays.sort(arr, 1, arr.length - 1);
    }

    public static int[][] deepCopy(int[][] grid) {
        // Chaque ligne est clonee : clone() seul partagerait les lignes.
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }

    public static boolean sameGrid(int[][] a, int[][] b) {
        // deepEquals descend dans les lignes ; equals comparerait les adresses des lignes.
        return Arrays.deepEquals(a, b);
    }

    public static void resetFrom(int[] scores, int from) {
        // fill(debut, fin, valeur) modifie le tableau recu.
        Arrays.fill(scores, from, scores.length, 0);
    }

    public static boolean canAddThroughAsList(Integer[] values) {
        // asList a une taille fixe : add lance UnsupportedOperationException.
        try {
            Arrays.asList(values).add(0);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
