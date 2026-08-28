package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 11 - Boucles imbriquees avec ETIQUETTE : le piege PREFERE de l'examen (niveau : difficile)
 * =============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise10_BreakAndContinue.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans 2 boucles imbriquees (une boucle DANS une autre boucle), un
 * break (ou continue) SANS etiquette ne concerne QUE la boucle la
 * PLUS PROCHE (la boucle INTERNE) - la boucle EXTERNE, elle,
 * continue normalement comme si de rien n'etait. Pour arreter (ou
 * sauter un tour de) la boucle EXTERNE directement DEPUIS
 * l'INTERIEUR de la boucle interne, il faut "etiqueter" la boucle
 * externe avec un nom suivi de ":" (comme "search:"), et ecrire
 * "break search;" (ou "continue search;") - un peu comme appeler
 * quelqu'un PAR SON NOM dans une foule, plutot que de s'adresser
 * juste "a la personne la plus proche".
 *
 *
 * ==================================================================
 * TODO 1 : rowsContainingValue(grid, target)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * grid = {{1,2,3}, {4,5,3}, {3,7,8}}, target = 3 : les 3 LIGNES
 * contiennent chacune un 3 (a des positions differentes) - resultat
 * attendu : 3.
 *
 * -- Le plan --
 *
 *   1. Declarer int rowCount = 0.
 *   2. Pour chaque ligne (for-each sur grid) : pour chaque valeur de
 *      CETTE ligne (for-each sur la ligne) : si value == target :
 *      rowCount++, PUIS break (SANS etiquette - arrete UNIQUEMENT la
 *      boucle interne, la boucle EXTERNE continue vers la ligne
 *      suivante).
 *   3. Renvoyer rowCount.
 *
 *
 * ==================================================================
 * TODO 2 : findFirstOccurrenceLabeled(grid, target)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Meme grid, meme target : la 1ere occurrence de 3 est en ligne 0,
 * colonne 2 - {0, 2}. On veut s'arreter LA, DEFINITIVEMENT (les
 * autres 3, plus loin, ne nous interessent plus).
 *
 * -- Le plan --
 *
 *   1. Declarer int[] found = null.
 *   2. Etiqueter la boucle EXTERNE "search:".
 *   3. for (int i = 0; i < grid.length; i++) : for (int j = 0; j <
 *      grid[i].length; j++) : si grid[i][j] == target : found = new
 *      int[]{i, j}, PUIS "break search;" (AVEC etiquette cette
 *      fois - arrete LES 2 boucles d'un coup).
 *   4. Renvoyer found.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans 2 boucles imbriquees.
 *
 * Exemple a verifier : rowsContainingValue({{1,2,3},{4,5,3},{3,7,8}}, 3)
 * == 3 (le break SANS etiquette n'empeche PAS les autres lignes
 * d'etre examinees). findFirstOccurrenceLabeled(meme grid, 3) ==
 * {0, 2} (le break AVEC etiquette arrete tout des la 1ere trouvaille,
 * jamais les suivantes).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "search:" (2 points, PAS de point-virgule) se place
 *     IMMEDIATEMENT avant le mot-cle "for" de la boucle qu'on
 *     etiquette, sans rien entre les 2.
 */
public class Exercise11_LabeledLoops {

    public static int rowsContainingValue(int[][] grid, int target) {
        throw new UnsupportedOperationException("TODO 1 : implementer rowsContainingValue()");
    }

    public static int[] findFirstOccurrenceLabeled(int[][] grid, int target) {
        throw new UnsupportedOperationException("TODO 2 : implementer findFirstOccurrenceLabeled()");
    }

    public static void main(String[] args) {
        int[][] grid = {{1, 2, 3}, {4, 5, 3}, {3, 7, 8}};

        ExerciseChecker.check("rowsContainingValue() : break SANS etiquette n'arrete QUE la boucle interne",
                rowsContainingValue(grid, 3) == 3);

        ExerciseChecker.check("findFirstOccurrenceLabeled() : break AVEC etiquette arrete LES 2 boucles",
                Arrays.equals(findFirstOccurrenceLabeled(grid, 3), new int[] {0, 2}));

        ExerciseChecker.summary();
    }
}
