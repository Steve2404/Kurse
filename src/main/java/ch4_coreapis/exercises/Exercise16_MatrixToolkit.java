package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 16 - Boite a outils de matrices : transposer, tourner, triangle de Pascal, spirale (niveau : avance)
 * =============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un tableau 2D int[][] est un tableau de lignes. m.length = nombre de
 * lignes, m[0].length = nombre de colonnes (si toutes les lignes ont la
 * meme taille). m[r][c] = la case ligne r, colonne c. Un tableau "en
 * escalier" (jagged) a des lignes de longueurs differentes.
 *
 *
 * ==================================================================
 * TODO 1 : transpose(m)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {{1, 2, 3},        {{1, 4},
 *    {4, 5, 6}}   ->    {2, 5},
 *                       {3, 6}}      les lignes deviennent des colonnes : t[c][r] = m[r][c]
 *
 * -- Le plan --
 *
 *   1. t = new int[colonnes][lignes].
 *   2. Deux boucles : t[c][r] = m[r][c].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : rotateClockwise(m)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {{1, 2},       {{3, 1},
 *    {3, 4}}  ->    {4, 2}}      la ligne r devient la colonne (lignes - 1 - r)
 *
 *   {{1, 2, 3},    {{4, 1},
 *    {4, 5, 6}} ->  {5, 2},
 *                   {6, 3}}
 *
 * -- Le plan --
 *
 *   1. rows = m.length ; cols = m[0].length ; out = new int[cols][rows].
 *   2. out[c][rows - 1 - r] = m[r][c].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : pascal(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un triangle : la ligne i a i + 1 cases. Les bords valent 1, chaque
 * case interieure est la somme des deux cases au-dessus d'elle.
 *
 * -- Essayons a la main --
 *
 *   pascal(5) -> [1] [1, 1] [1, 2, 1] [1, 3, 3, 1] [1, 4, 6, 4, 1]
 *
 * -- Le plan --
 *
 *   1. t = new int[n][] (lignes a creer une par une).
 *   2. t[i] = new int[i + 1] ; t[i][0] = t[i][i] = 1 ; interieur : t[i - 1][j - 1] + t[i - 1][j].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : spiral(m)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On lit la grille en tournant comme un escargot : la ligne du haut
 * vers la droite, la colonne de droite vers le bas, la ligne du bas
 * vers la gauche, la colonne de gauche vers le haut, puis on recommence
 * a l'interieur. 4 bornes (top, bottom, left, right) se resserrent.
 *
 * -- Essayons a la main --
 *
 *   {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}          -> "1 2 3 6 9 8 7 4 5"
 *   {{1, 2, 3, 4}, {5, 6, 7, 8}}               -> "1 2 3 4 8 7 6 5"
 *
 * -- Le plan --
 *
 *   1. top = 0, bottom = lignes - 1, left = 0, right = colonnes - 1.
 *   2. Tant que top <= bottom et left <= right :
 *      haut (left -> right) puis top++ ; droite (top -> bottom) puis right-- ;
 *      si top <= bottom : bas (right -> left) puis bottom-- ;
 *      si left <= right : gauche (bottom -> top) puis left++.
 *   3. Les nombres sont separes par un espace.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais les deux "si" de l'etape 2 evitent de relire une ligne ou
 * une colonne deja lue.
 *
 *
 * ==================================================================
 * TODO 5 : isSymmetric(m)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {{1, 7}, {7, 2}} -> true (m[r][c] == m[c][r] partout)     {{1, 2}, {3, 4}} -> false
 *
 * -- Le plan --
 *
 *   1. Carree obligatoire ; puis comparer m[r][c] et m[c][r]. Ou : Arrays.deepEquals(m, transpose(m)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui si tu reutilises transpose (TODO 1) : c'est le plus court.
 *
 *
 * ==================================================================
 * TODO 6 : sumAll(rows...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un varargs de tableaux : int[]... rows EST un int[][]. On peut
 * appeler sumAll(), sumAll(a) ou sumAll(a, b, c).
 *
 * -- Essayons a la main --
 *
 *   sumAll(new int[] {1, 2}, new int[] {3}) -> 6     sumAll() -> 0
 *
 * -- Le plan --
 *
 *   1. Deux for-each imbriques.
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
 *   - new int[3][2] cree toutes les lignes ; new int[3][] les laisse a null.
 *   - Arrays.deepToString(m) affiche une grille pour deboguer.
 */
public class Exercise16_MatrixToolkit {

    public static int[][] transpose(int[][] m) {
        throw new UnsupportedOperationException("TODO 1 : implementer transpose()");
    }

    public static int[][] rotateClockwise(int[][] m) {
        throw new UnsupportedOperationException("TODO 2 : implementer rotateClockwise()");
    }

    public static int[][] pascal(int n) {
        throw new UnsupportedOperationException("TODO 3 : implementer pascal()");
    }

    public static String spiral(int[][] m) {
        throw new UnsupportedOperationException("TODO 4 : implementer spiral()");
    }

    public static boolean isSymmetric(int[][] m) {
        throw new UnsupportedOperationException("TODO 5 : implementer isSymmetric()");
    }

    public static int sumAll(int[]... rows) {
        throw new UnsupportedOperationException("TODO 6 : implementer sumAll()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("transpose 2x3 -> 3x2",
                Arrays.deepEquals(transpose(new int[][] {{1, 2, 3}, {4, 5, 6}}), new int[][] {{1, 4}, {2, 5}, {3, 6}}));
        ExerciseChecker.check("rotateClockwise 2x2 et 2x3",
                Arrays.deepEquals(rotateClockwise(new int[][] {{1, 2}, {3, 4}}), new int[][] {{3, 1}, {4, 2}})
                        && Arrays.deepEquals(rotateClockwise(new int[][] {{1, 2, 3}, {4, 5, 6}}), new int[][] {{4, 1}, {5, 2}, {6, 3}}));
        int[][] p = pascal(5);
        ExerciseChecker.check("pascal(5) : 5 lignes en escalier, derniere [1, 4, 6, 4, 1]",
                p.length == 5 && p[0].length == 1 && Arrays.equals(p[4], new int[] {1, 4, 6, 4, 1}));
        ExerciseChecker.check("spiral 3x3 et 2x4",
                spiral(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}).equals("1 2 3 6 9 8 7 4 5")
                        && spiral(new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8}}).equals("1 2 3 4 8 7 6 5"));
        ExerciseChecker.check("isSymmetric : oui puis non",
                isSymmetric(new int[][] {{1, 7}, {7, 2}}) && !isSymmetric(new int[][] {{1, 2}, {3, 4}}));
        ExerciseChecker.check("sumAll : 6 et 0", sumAll(new int[] {1, 2}, new int[] {3}) == 6 && sumAll() == 0);

        ExerciseChecker.summary();
    }
}
