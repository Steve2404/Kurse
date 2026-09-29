package ch3_makingdecisions.drills.exercises;

import ch3_makingdecisions.ExerciseChecker;
import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * DRILL 05 - Kata melange : tout le chapitre 3 sans indice de forme
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_IfAndPatterns. Ici, PAS de crochet : c'est
 * a toi de choisir if, switch (quelle forme ?), quelle boucle, break ou
 * continue. Fais ce drill seulement quand les drills 01 a 04 passent.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : weekReport()          classe chaque temperature : < 0 gel, < 10 froid, < 20 doux, sinon chaud.
 *                                  -> "gel=1 froid=2 doux=3 chaud=1".
 * TODO 2  : describeItems()       pour chaque element de Week.ITEMS : Integer -> "int:" + valeur,
 *                                  String -> "str:" + longueur, null -> "null", autre -> "autre" ;
 *                                  joints par ",". -> "str:5,int:42,null,autre,str:5,int:-7,autre".
 * TODO 3  : finalState()          machine a etats, depart "OFF". start -> "ON" ; pause -> "PAUSED"
 *                                  seulement si "ON" ; stop -> "OFF". Etat apres Week.COMMANDS -> "ON".
 * TODO 4  : weekendAverage()      moyenne entiere des temperatures de SAT et SUN -> 8.
 * TODO 5  : longestWarmStreak()   plus longue suite de jours consecutifs > 10 -> 2.
 * TODO 6  : rowMaxes()            maximum de chaque ligne de Week.GRID -> "3,6,9".
 * TODO 7  : columnSums()          somme de chaque colonne de Week.GRID -> "12,5,18".
 * TODO 8  : firstRepeatIndex()    premier index de Week.COMMANDS dont la commande est deja apparue
 *                                  avant -> 2 (sinon -1).
 * TODO 9  : daysWithinBudget(max) additionne les temperatures jour apres jour ; renvoie le nombre de
 *                                  jours additionnes avant que la somme depasse max. 30 -> 3.
 * TODO 10 : hottestDay()          le Day de la temperature maximale -> FRI.
 * TODO 11 : dayCodes()            'S' pour un jour de semaine, 'W' pour SAT et SUN -> "SSSSSWW".
 * TODO 12 : weekSummary()         "<jour chaud> <temp> / <jour froid> <temp> / moyenne <moyenne entiere>"
 *                                  -> "FRI 21 / WED -2 / moyenne 10".
 */
public class Drill05_MixedKata {

    public static String weekReport() {
        throw new UnsupportedOperationException("TODO 1 : implementer weekReport()");
    }

    public static String describeItems() {
        throw new UnsupportedOperationException("TODO 2 : implementer describeItems()");
    }

    public static String finalState() {
        throw new UnsupportedOperationException("TODO 3 : implementer finalState()");
    }

    public static int weekendAverage() {
        throw new UnsupportedOperationException("TODO 4 : implementer weekendAverage()");
    }

    public static int longestWarmStreak() {
        throw new UnsupportedOperationException("TODO 5 : implementer longestWarmStreak()");
    }

    public static String rowMaxes() {
        throw new UnsupportedOperationException("TODO 6 : implementer rowMaxes()");
    }

    public static String columnSums() {
        throw new UnsupportedOperationException("TODO 7 : implementer columnSums()");
    }

    public static int firstRepeatIndex() {
        throw new UnsupportedOperationException("TODO 8 : implementer firstRepeatIndex()");
    }

    public static int daysWithinBudget(int max) {
        throw new UnsupportedOperationException("TODO 9 : implementer daysWithinBudget()");
    }

    public static Day hottestDay() {
        throw new UnsupportedOperationException("TODO 10 : implementer hottestDay()");
    }

    public static String dayCodes() {
        throw new UnsupportedOperationException("TODO 11 : implementer dayCodes()");
    }

    public static String weekSummary() {
        throw new UnsupportedOperationException("TODO 12 : implementer weekSummary()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  weekReport() == \"gel=1 froid=2 doux=3 chaud=1\"", weekReport().equals("gel=1 froid=2 doux=3 chaud=1"));
        ExerciseChecker.check("2  describeItems()",
                describeItems().equals("str:5,int:42,null,autre,str:5,int:-7,autre"));
        ExerciseChecker.check("3  finalState() == \"ON\"", finalState().equals("ON"));
        ExerciseChecker.check("4  weekendAverage() == 8", weekendAverage() == 8);
        ExerciseChecker.check("5  longestWarmStreak() == 2", longestWarmStreak() == 2);
        ExerciseChecker.check("6  rowMaxes() == \"3,6,9\"", rowMaxes().equals("3,6,9"));
        ExerciseChecker.check("7  columnSums() == \"12,5,18\"", columnSums().equals("12,5,18"));
        ExerciseChecker.check("8  firstRepeatIndex() == 2", firstRepeatIndex() == 2);
        ExerciseChecker.check("9  daysWithinBudget : 30 -> 3, 100 -> 7", daysWithinBudget(30) == 3 && daysWithinBudget(100) == 7);
        ExerciseChecker.check("10 hottestDay() == FRI", hottestDay() == Day.FRI);
        ExerciseChecker.check("11 dayCodes() == \"SSSSSWW\"", dayCodes().equals("SSSSSWW"));
        ExerciseChecker.check("12 weekSummary() == \"FRI 21 / WED -2 / moyenne 10\"", weekSummary().equals("FRI 21 / WED -2 / moyenne 10"));

        ExerciseChecker.summary();
    }
}
