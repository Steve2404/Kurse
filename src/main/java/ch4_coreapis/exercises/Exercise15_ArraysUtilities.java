package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 15 - La boite a outils Arrays : copyOf, copyOfRange, fill, sort(debut, fin), deepEquals, asList (niveau : difficile)
 * =============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_ArraysBasics.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   Arrays.copyOf({5, 2, 8, 1}, 6)          -> [5, 2, 8, 1, 0, 0]   (complete avec la valeur par defaut)
 *   Arrays.copyOf({5, 2, 8, 1}, 2)          -> [5, 2]               (coupe)
 *   Arrays.copyOfRange({5, 2, 8, 1}, 1, 3)  -> [2, 8]               (fin EXCLUE)
 *   Arrays.copyOfRange({5, 2, 8, 1}, 2, 6)  -> [8, 1, 0, 0]         (fin au-dela : complete)
 *   Arrays.fill(a, 1, 3, 0) sur [7, 7, 7, 7, 7] -> [7, 0, 0, 7, 7]
 *   Arrays.sort(a, 1, 4) sur [9, 4, 7, 1, 3]    -> [9, 1, 4, 7, 3]
 *   Arrays.sort({"banana", "Apple", "10", "9", "apple", "_x", " z", "Zebra"})
 *        -> [ z, 10, 9, Apple, Zebra, _x, apple, banana]  (espace < chiffres < MAJUSCULES < '_' < minuscules)
 *   new int[] {1}.equals(new int[] {1})               -> false (compare les ADRESSES)
 *   Arrays.equals(new int[][] {{1}}, new int[][] {{1}}) -> false ; Arrays.deepEquals(...) -> true
 *   Arrays.toString(grille 2D) -> "[[I@..." ; Arrays.deepToString -> "[[1, 2], [3]]"
 *   m.clone() sur un tableau 2D : copie SUPERFICIELLE (les lignes sont partagees)
 *   Arrays.asList(tableau) : set() ecrit DANS le tableau ; add() -> UnsupportedOperationException
 *
 *
 * ==================================================================
 * TODO 1 : grow(arr)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un tableau ne grandit jamais. Pour "l'agrandir", on en fabrique un
 * deux fois plus grand et on recopie (c'est ce que fait ArrayList en
 * cachette). Un tableau vide passe a 1 case.
 *
 * -- Essayons a la main --
 *
 *   [1, 2, 3] -> [1, 2, 3, 0, 0, 0]     [] -> [0]
 *
 * -- Le plan --
 *
 *   1. Rendre Arrays.copyOf(arr, Math.max(1, arr.length * 2)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : page(arr, pageNumber, pageSize)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Comme un livre : la page 0 contient les pageSize premiers elements,
 * la page 1 les suivants... La derniere page peut etre plus courte :
 * copyOfRange completerait avec des 0, donc on borne la fin.
 *
 * -- Essayons a la main --
 *
 *   [1..7], page 0, taille 3 -> [1, 2, 3] ; page 2 -> [7] ; page 3 -> []
 *
 * -- Le plan --
 *
 *   1. from = Math.min(pageNumber * pageSize, arr.length) ; to = Math.min(from + pageSize, arr.length).
 *   2. Rendre Arrays.copyOfRange(arr, from, to).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : sortMiddle(arr)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [9, 4, 7, 1, 3] -> on trie SEULEMENT les index 1 a 3 -> [9, 1, 4, 7, 3]
 *
 * -- Le plan --
 *
 *   1. Arrays.sort(arr, 1, arr.length - 1) (fin EXCLUE : le dernier ne bouge pas).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : deepCopy(grid)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une grille 2D est un tableau de LIGNES. clone() recopie la liste des
 * lignes, mais les lignes elles-memes restent partagees : modifier la
 * copie abime l'original. Il faut cloner CHAQUE ligne.
 *
 * -- Le plan --
 *
 *   1. copy = new int[grid.length][] (les lignes viendront apres).
 *   2. Pour chaque i : copy[i] = grid[i].clone() (ou Arrays.copyOf).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : sameGrid(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Rendre Arrays.deepEquals(a, b) (Arrays.equals comparerait les ADRESSES des lignes).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : resetFrom(scores, from)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [5, 8, 3, 9], from 2 -> [5, 8, 0, 0] (le MEME tableau est modifie)
 *
 * -- Le plan --
 *
 *   1. Arrays.fill(scores, from, scores.length, 0).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : canAddThroughAsList(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Arrays.asList met une "fenetre" List sur le tableau : on peut
 * changer une case (set), mais pas agrandir (add), car un tableau ne
 * grandit jamais. On essaie add et on rend true si ca a marche.
 *
 * -- Le plan --
 *
 *   1. try { Arrays.asList(values).add(0); return true; }
 *      catch (UnsupportedOperationException e) { return false; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new int[3][] cree 3 lignes a null : il faut les remplir.
 *   - Arrays.asList attend des objets : Integer[], pas int[].
 */
public class Exercise15_ArraysUtilities {

    public static int[] grow(int[] arr) {
        throw new UnsupportedOperationException("TODO 1 : implementer grow()");
    }

    public static int[] page(int[] arr, int pageNumber, int pageSize) {
        throw new UnsupportedOperationException("TODO 2 : implementer page()");
    }

    public static void sortMiddle(int[] arr) {
        throw new UnsupportedOperationException("TODO 3 : implementer sortMiddle()");
    }

    public static int[][] deepCopy(int[][] grid) {
        throw new UnsupportedOperationException("TODO 4 : implementer deepCopy()");
    }

    public static boolean sameGrid(int[][] a, int[][] b) {
        throw new UnsupportedOperationException("TODO 5 : implementer sameGrid()");
    }

    public static void resetFrom(int[] scores, int from) {
        throw new UnsupportedOperationException("TODO 6 : implementer resetFrom()");
    }

    public static boolean canAddThroughAsList(Integer[] values) {
        throw new UnsupportedOperationException("TODO 7 : implementer canAddThroughAsList()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("grow : [1, 2, 3] -> [1, 2, 3, 0, 0, 0], [] -> [0]",
                Arrays.equals(grow(new int[] {1, 2, 3}), new int[] {1, 2, 3, 0, 0, 0}) && Arrays.equals(grow(new int[0]), new int[] {0}));

        int[] seven = {1, 2, 3, 4, 5, 6, 7};
        ExerciseChecker.check("page : [1, 2, 3], [7], []",
                Arrays.equals(page(seven, 0, 3), new int[] {1, 2, 3}) && Arrays.equals(page(seven, 2, 3), new int[] {7})
                        && page(seven, 3, 3).length == 0);

        int[] middle = {9, 4, 7, 1, 3};
        sortMiddle(middle);
        ExerciseChecker.check("sortMiddle : [9, 1, 4, 7, 3]", Arrays.equals(middle, new int[] {9, 1, 4, 7, 3}));

        int[][] grid = {{1, 2}, {3, 4}};
        int[][] copy = deepCopy(grid);
        copy[0][0] = 99;
        ExerciseChecker.check("deepCopy : modifier la copie ne touche PAS l'original", grid[0][0] == 1 && copy[0][0] == 99);
        int[][] shallow = grid.clone();
        shallow[0][0] = 42;
        ExerciseChecker.check("(rappel) clone() est superficiel : l'original a change", grid[0][0] == 42);

        ExerciseChecker.check("sameGrid : true alors que Arrays.equals dit false",
                sameGrid(new int[][] {{1}, {2, 3}}, new int[][] {{1}, {2, 3}}) && !Arrays.equals(new int[][] {{1}}, new int[][] {{1}})
                        && !sameGrid(new int[][] {{1}}, new int[][] {{2}}));

        int[] scores = {5, 8, 3, 9};
        resetFrom(scores, 2);
        ExerciseChecker.check("resetFrom : [5, 8, 0, 0]", Arrays.equals(scores, new int[] {5, 8, 0, 0}));

        ExerciseChecker.check("canAddThroughAsList -> false (taille fixe)", !canAddThroughAsList(new Integer[] {1, 2}));

        ExerciseChecker.summary();
    }
}
