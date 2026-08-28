package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 4 - switch statement : branche au 1er match, PUIS continue jusqu'a un break (niveau : moyen)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_IfElseBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * A l'execution, un switch statement saute DIRECTEMENT au premier
 * "case" qui correspond (ou a "default" si aucun ne correspond, ou
 * quitte completement le switch si NI un case NI default ne
 * correspond) - PUIS continue d'executer TOUT ce qui suit, ligne par
 * ligne, EN TRAVERSANT les autres "case" suivants SANS LES
 * RE-TESTER, jusqu'a rencontrer un break (ou un return) qui arrete
 * tout. C'est ce "traversage" volontaire (le "fall-through") qui
 * permet de regrouper plusieurs valeurs menant au MEME resultat, en
 * empilant simplement plusieurs "case" d'affilee SANS break entre
 * eux.
 *
 *
 * ==================================================================
 * TODO 1 : monthName(monthNumber)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. switch (monthNumber) avec un case pour chaque mois de 1 a 12,
 *      chacun renvoyant son nom (via return, qui quitte
 *      IMMEDIATEMENT la methode - pas besoin de break si on utilise
 *      return).
 *   2. default : renvoyer "Inconnu".
 *
 *
 * ==================================================================
 * TODO 2 : seasonForMonth(monthNumber)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Decembre(12), Janvier(1), Fevrier(2) -> "Hiver". Mars(3), Avril(4),
 * Mai(5) -> "Printemps". Juin(6), Juillet(7), Aout(8) -> "Ete".
 * Septembre(9), Octobre(10), Novembre(11) -> "Automne".
 *
 * -- Le plan --
 *
 *   1. Declarer String result.
 *   2. switch (monthNumber) : "case 12: case 1: case 2:" (3 case
 *      EMPILES, SANS rien entre eux) PUIS "result = \"Hiver\"; break;"
 *      - et ainsi de suite pour chaque saison.
 *   3. default : result = "Inconnu".
 *   4. Renvoyer result.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul switch.
 *
 * Exemple a verifier : monthName(3) == "Mars". seasonForMonth(12) ==
 * "Hiver". seasonForMonth(6) == "Ete".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "case 12: case 1: case 2: result = \"Hiver\"; break;" - les 3
 *     "case" empiles n'executent RIEN par eux-memes : ils "tombent"
 *     TOUS jusqu'a la premiere instruction reelle rencontree
 *     (result = "Hiver"), quel que soit CELUI des 3 qui a
 *     initialement matche.
 */
public class Exercise04_SwitchStatementBasics {

    public static String monthName(int monthNumber) {
        throw new UnsupportedOperationException("TODO 1 : implementer monthName()");
    }

    public static String seasonForMonth(int monthNumber) {
        throw new UnsupportedOperationException("TODO 2 : implementer seasonForMonth()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("monthName(3) == \"Mars\"", monthName(3).equals("Mars"));
        ExerciseChecker.check("monthName(99) == \"Inconnu\" (default)", monthName(99).equals("Inconnu"));

        ExerciseChecker.check("seasonForMonth(12) == \"Hiver\" (fall-through jusqu'a Hiver)",
                seasonForMonth(12).equals("Hiver"));
        ExerciseChecker.check("seasonForMonth(1) == \"Hiver\" (meme groupe que 12)",
                seasonForMonth(1).equals("Hiver"));
        ExerciseChecker.check("seasonForMonth(6) == \"Ete\"", seasonForMonth(6).equals("Ete"));
        ExerciseChecker.check("seasonForMonth(9) == \"Automne\"", seasonForMonth(9).equals("Automne"));

        ExerciseChecker.summary();
    }
}
