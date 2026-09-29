package ch4_coreapis.drills.solutions;

import ch4_coreapis.drills.Journal;

import java.util.Arrays;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill03_ArraysApi.
 */
public class SolutionDrill03_ArraysApi {

    public static String scoresText() {
        // Un tableau affiche avec println donne "[I@..." : Arrays.toString montre le contenu.
        return Arrays.toString(Journal.SCORES);
    }

    public static int[] sortedCopy() {
        // sort trie EN PLACE : on trie une copie pour ne pas abimer les donnees partagees.
        int[] copy = Arrays.copyOf(Journal.SCORES, Journal.SCORES.length);
        Arrays.sort(copy);
        return copy;
    }

    public static int findSorted(int value) {
        // Tableau deja trie ; absent -> -(point d'insertion) - 1 (10 irait en 2 -> -3).
        return Arrays.binarySearch(Journal.SORTED, value);
    }

    public static int[] firstThree() {
        // copyOfRange : fin EXCLUE.
        return Arrays.copyOfRange(Journal.SCORES, 0, 3);
    }

    public static int[] filled(int size, int value) {
        // fill remplace une boucle ; il rend void.
        int[] a = new int[size];
        Arrays.fill(a, value);
        return a;
    }

    public static boolean sameAsScores(int[] other) {
        // Arrays.equals compare le contenu ; other.equals(...) comparerait les adresses.
        return Arrays.equals(Journal.SCORES, other);
    }

    public static int compareWithSorted() {
        // Premier element different decide : 42 contre 3.
        return Arrays.compare(Journal.SCORES, Journal.SORTED);
    }

    public static int firstDifference() {
        // mismatch rend le premier index different (ou -1 si identiques).
        return Arrays.mismatch(Journal.SCORES, new int[] {42, 7, 20});
    }

    public static String gridText() {
        // deepToString descend dans les lignes ; toString afficherait des adresses.
        return Arrays.deepToString(new int[][] {{1, 2}, {3}});
    }

    public static String[] sortedWords() {
        // Ordre naturel des String : les MAJUSCULES passent avant les minuscules.
        String[] copy = Arrays.copyOf(Journal.WORDS, Journal.WORDS.length);
        Arrays.sort(copy);
        return copy;
    }

    public static String renameThroughList() {
        // asList est une fenetre sur le tableau : set ecrit DANS le tableau.
        String[] copy = Journal.WORDS.clone();
        Arrays.asList(copy).set(0, "zulu");
        return copy[0];
    }

    public static int[][] staircase(int n) {
        // new int[n][] : les lignes sont null jusqu'a ce qu'on les cree.
        int[][] stairs = new int[n][];
        for (int i = 0; i < n; i++) {
            stairs[i] = new int[i + 1];
        }
        return stairs;
    }

    public static String defaultsText() {
        // Un tableau neuf est rempli de valeurs par defaut : false, 0, 0.0, null.
        return Arrays.toString(new boolean[2]);
    }

    public static int[] sortFirstThree() {
        // sort(a, debut, fin) ne trie que la tranche [0, 3).
        int[] copy = Journal.SCORES.clone();
        Arrays.sort(copy, 0, 3);
        return copy;
    }
}
