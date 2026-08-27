package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

/**
 * EXERCICE 4 - Enums simples, et les 2 syntaxes de switch (niveau : moyen)
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un enum, c'est une LISTE FERMEE de valeurs possibles, connue A
 * L'AVANCE, comme les 7 jours de la semaine : il n'existe PAS de 8e
 * jour qu'on pourrait inventer au dernier moment. Contrairement a une
 * String ("lundi" mal orthographiee compile quand meme !), le
 * compilateur VERIFIE qu'on n'utilise QUE des valeurs qui existent
 * vraiment dans la liste - une String mal ecrite plante a
 * l'EXECUTION, une valeur d'enum qui n'existe pas ne compile MEME PAS.
 *
 * Java propose 2 facons d'ecrire un aiguillage sur un enum :
 *   - le switch STATEMENT "classique" (case ... : ... break;) - existe
 *     depuis toujours, RISQUE d'oublier un break (fall-through).
 *   - le switch EXPRESSION moderne (case ... -> ...) - RENVOIE une
 *     valeur directement, PAS de fall-through possible, et le
 *     compilateur VERIFIE que tous les cas de l'enum sont couverts.
 *
 *
 * ==================================================================
 * TODO 1 : isWeekend(day) - switch EXPRESSION
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ecrire "return switch (day) { case SATURDAY, SUNDAY -> true;
 *      default -> false; };" - remarque : PAS de break, PAS de
 *      point-virgule apres chaque case, mais UN point-virgule final
 *      apres l'accolade fermante (c'est une EXPRESSION, comme un
 *      ternaire).
 *
 *
 * ==================================================================
 * TODO 2 : describeDay(day) - switch STATEMENT classique
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY doivent tous rendre
 * "Jour ouvre". SATURDAY et SUNDAY doivent rendre "Week-end".
 *
 * -- Le plan --
 *
 *   1. Declarer une variable String result.
 *   2. switch (day) { sur plusieurs "case XXX:" qui s'enchainent SANS
 *      break entre eux (le "fall-through" volontaire : MONDAY tombe
 *      dans TUESDAY qui tombe dans WEDNESDAY... jusqu'au dernier qui
 *      fait le VRAI travail), puis un break ; PUIS un second groupe
 *      "case SATURDAY: case SUNDAY:" avec un autre break.
 *   3. Renvoyer result.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : isWeekend(Day.SATURDAY) == true,
 * isWeekend(Day.MONDAY) == false. describeDay(Day.WEDNESDAY) ==
 * "Jour ouvre", describeDay(Day.SUNDAY) == "Week-end".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Dans un switch sur un enum, on n'ecrit JAMAIS le nom de
 *     l'enum devant chaque case (jamais "Day.SATURDAY", juste
 *     "SATURDAY") : le compilateur sait deja de quel enum il s'agit
 *     grace au type de la variable testee.
 *   - "case SATURDAY, SUNDAY ->" (switch expression) regroupe 2
 *     valeurs qui menent au MEME resultat, en un seul case - pas
 *     besoin de les repeter sur 2 lignes separees.
 */
public class Exercise04_SimpleEnumsAndSwitch {

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public static boolean isWeekend(Day day) {
        throw new UnsupportedOperationException("TODO 1 : implementer isWeekend()");
    }

    public static String describeDay(Day day) {
        throw new UnsupportedOperationException("TODO 2 : implementer describeDay()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("isWeekend(SATURDAY) est vrai", isWeekend(Day.SATURDAY));
        ExerciseChecker.check("isWeekend(SUNDAY) est vrai", isWeekend(Day.SUNDAY));
        ExerciseChecker.check("isWeekend(MONDAY) est faux", !isWeekend(Day.MONDAY));

        ExerciseChecker.check("describeDay(WEDNESDAY) == 'Jour ouvre'",
                describeDay(Day.WEDNESDAY).equals("Jour ouvre"));
        ExerciseChecker.check("describeDay(FRIDAY) == 'Jour ouvre'",
                describeDay(Day.FRIDAY).equals("Jour ouvre"));
        ExerciseChecker.check("describeDay(SUNDAY) == 'Week-end'",
                describeDay(Day.SUNDAY).equals("Week-end"));

        ExerciseChecker.summary();
    }
}
