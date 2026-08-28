package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 6 - switch expression : RENVOIE une valeur directement, plus besoin de break (niveau : moyen/difficile)
 * ==========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise04_SwitchStatementBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un switch expression (avec des fleches ->, PAS des deux-points :)
 * est different d'un switch statement sur 3 points cles :
 *   - il RENVOIE une VALEUR directement (comme un ternaire geant) :
 *     on peut ecrire "return switch (x) { ... };" - remarquez le
 *     point-virgule final, APRES l'accolade fermante.
 *   - PAS de fall-through : chaque "case ... ->" s'arrete tout seul,
 *     aucun break necessaire (et aucun "case 1: case 2:" empile :
 *     on regroupe plusieurs valeurs avec une simple virgule, "case
 *     1, 2 ->").
 *   - le compilateur EXIGE que TOUTES les valeurs possibles soient
 *     couvertes (par les case, ou par un default) - sinon, ca ne
 *     compile MEME PAS (voir Exercise07).
 *
 * Pour une seule expression apres la fleche, sa valeur est
 * AUTOMATIQUEMENT le resultat (pas besoin d'ecrire "yield"). Pour un
 * BLOC {} de plusieurs instructions apres la fleche, en revanche,
 * "yield valeur;" DOIT etre ecrit explicitement pour dire "voici la
 * valeur a renvoyer".
 *
 *
 * ==================================================================
 * TODO 1 : seasonForMonthExpr(monthNumber)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer switch (monthNumber) { case 12, 1, 2 -> "Hiver"; case
 *      3, 4, 5 -> "Printemps"; case 6, 7, 8 -> "Ete"; case 9, 10, 11
 *      -> "Automne"; default -> "Inconnu"; }.
 *
 *
 * ==================================================================
 * TODO 2 : letterGrade(score)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * score / 10 (division ENTIERE) : 95 -> 9, 82 -> 8, 61 -> 6.
 *
 * -- Le plan --
 *
 *   1. Renvoyer switch (score / 10) { case 10, 9 -> "A"; case 8 ->
 *      "B"; case 7 -> "C"; default -> { ... un bloc qui declare
 *      String grade = "F"; PUIS yield grade; } }.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul switch expression.
 *
 * Exemple a verifier : seasonForMonthExpr(12) == "Hiver".
 * letterGrade(95) == "A". letterGrade(82) == "B". letterGrade(61) ==
 * "F" (via le bloc default avec yield).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "case 12, 1, 2 -> ..." (virgule, PAS "case 12: case 1: case 2:")
 *     - la syntaxe de regroupement change ENTIEREMENT entre statement
 *     et expression.
 */
public class Exercise06_SwitchExpressionBasics {

    public static String seasonForMonthExpr(int monthNumber) {
        throw new UnsupportedOperationException("TODO 1 : implementer seasonForMonthExpr()");
    }

    public static String letterGrade(int score) {
        throw new UnsupportedOperationException("TODO 2 : implementer letterGrade()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("seasonForMonthExpr(12) == \"Hiver\" (regroupement par virgule)",
                seasonForMonthExpr(12).equals("Hiver"));
        ExerciseChecker.check("seasonForMonthExpr(99) == \"Inconnu\" (default)",
                seasonForMonthExpr(99).equals("Inconnu"));

        ExerciseChecker.check("letterGrade(95) == \"A\"", letterGrade(95).equals("A"));
        ExerciseChecker.check("letterGrade(82) == \"B\"", letterGrade(82).equals("B"));
        ExerciseChecker.check("letterGrade(61) == \"F\" (bloc default avec yield)", letterGrade(61).equals("F"));

        ExerciseChecker.summary();
    }
}
