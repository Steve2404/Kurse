package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 9 - Varargs = un tableau deguise, et les tableaux "en escalier" (jagged) (niveau : moyen/difficile)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise06_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un parametre varargs (int... nums) est, A L'INTERIEUR de la
 * methode, un VRAI tableau (int[]) tout ce qu'il y a de plus normal -
 * nums.length, nums[0]... fonctionnent EXACTEMENT pareil. La SEULE
 * difference est cote APPELANT : on peut soit donner les valeurs UNE
 * PAR UNE (sumVarargs(1, 2, 3)), soit donner directement un tableau
 * DEJA construit (sumVarargs(monTableau)) - les 2 marchent, au choix.
 *
 * Un tableau a 2 dimensions (int[][]) n'est, EN REALITE, qu'un
 * tableau DONT CHAQUE CASE contient... un AUTRE tableau. Rien
 * n'oblige ces sous-tableaux a avoir la MEME taille : on parle alors
 * de tableau "en escalier" (jagged) - chaque ligne peut avoir SA
 * PROPRE longueur, comme un escalier dont les marches ne font pas
 * toutes la meme largeur.
 *
 *
 * ==================================================================
 * TODO 1 : sumVarargs(nums)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. Parcourir CHAQUE case de nums (exactement comme un int[]
 *      normal) et l'ajouter a total.
 *   3. Renvoyer total.
 *
 *
 * ==================================================================
 * TODO 2 : buildJaggedArray()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer { {1}, {2, 3}, {4, 5, 6} } - 3 lignes, de longueurs
 *      1, 2 puis 3 : un VRAI escalier.
 *
 *
 * ==================================================================
 * TODO 3 : sumJagged(grid)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Impossible de supposer que TOUTES les lignes ont la meme longueur
 * (justement, ici, elles ne l'ont PAS) : il faut redemander
 * grid[i].length a CHAQUE ligne i, jamais une seule fois pour toutes.
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. Pour chaque ligne (for-each sur grid, une variable int[] row) :
 *      pour chaque valeur de CETTE ligne (for-each sur row) : ajouter
 *      a total.
 *   3. Renvoyer total.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : sumVarargs(1, 2, 3) == 6 (valeurs individuelles).
 * int[] arr = {4, 5, 6}; sumVarargs(arr) == 15 (un VRAI tableau donne
 * directement, ca marche AUSSI). buildJaggedArray() a 3 lignes de
 * longueurs 1, 2, 3. sumJagged(buildJaggedArray()) == 21 (1+2+3+4+5+6).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "for (int[] row : grid) { for (int value : row) { ... } }" :
 *     un for-each imbrique dans un autre, chacun sur son propre
 *     niveau de tableau.
 */
public class Exercise09_VarargsAndJaggedArrays {

    public static int sumVarargs(int... nums) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumVarargs()");
    }

    public static int[][] buildJaggedArray() {
        throw new UnsupportedOperationException("TODO 2 : implementer buildJaggedArray()");
    }

    public static int sumJagged(int[][] grid) {
        throw new UnsupportedOperationException("TODO 3 : implementer sumJagged()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("sumVarargs(1, 2, 3) == 6 (valeurs individuelles)", sumVarargs(1, 2, 3) == 6);

        int[] arr = {4, 5, 6};
        ExerciseChecker.check("sumVarargs(arr) == 15 (un VRAI tableau donne directement)", sumVarargs(arr) == 15);

        int[][] jagged = buildJaggedArray();
        ExerciseChecker.check("buildJaggedArray() a 3 lignes de longueurs 1, 2, 3",
                jagged.length == 3 && jagged[0].length == 1 && jagged[1].length == 2 && jagged[2].length == 3);

        ExerciseChecker.check("sumJagged() additionne TOUTES les valeurs, lignes de tailles differentes",
                sumJagged(jagged) == 21);

        ExerciseChecker.summary();
    }
}
