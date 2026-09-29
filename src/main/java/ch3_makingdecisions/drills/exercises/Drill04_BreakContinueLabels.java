package ch3_makingdecisions.drills.exercises;

import ch3_makingdecisions.ExerciseChecker;
import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * DRILL 04 - break, continue, etiquettes et return dans les boucles
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_IfAndPatterns.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : firstNegativeTemp()          [break] premiere temperature < 0 -> -2 (0 si aucune).
 * TODO 2  : sumPositiveTemps()           [continue] somme des temperatures > 0 -> 73.
 * TODO 3  : daysBeforeFreeze()           [break] nombre de jours avant la premiere temperature <= 0 -> 2.
 * TODO 4  : rowsWithoutNegatives()       [continue etiquete] lignes de Week.GRID sans negatif -> 2.
 * TODO 5  : findInGrid(target)           [break etiquete] "ligne,colonne" ou "absent". 6 -> "1,2".
 * TODO 6  : commandsBeforeStop()         [switch DANS une boucle + break etiquete] commandes lues
 *                                         avant "stop" dans Week.COMMANDS -> 3.
 * TODO 7  : firstIndexOver(limit)        [while + break] premier index ou TEMPS > limit, sinon -1.
 * TODO 8  : cellsBeforeNegativeInner()   [break SANS etiquette dans une boucle imbriquee]
 *                                         chaque ligne s'arrete au premier negatif, puis on
 *                                         passe a la ligne suivante : cases lues -> 7.
 * TODO 9  : cellsBeforeNegativeOuter()   [break etiquete] tout s'arrete au premier negatif -> 4.
 * TODO 10 : firstDayAtLeast(t)           [return dans une boucle] premier Day avec TEMPS >= t, sinon null.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   break    : sort de la boucle (ou du switch) la plus proche
 *   continue : passe au tour suivant de la boucle la plus proche (interdit dans un switch seul)
 *   outer: for (...) { for (...) { break outer; / continue outer; } }
 *   Un break dans un switch DANS une boucle ne sort QUE du switch
 *   continue sur une etiquette qui n'est pas une boucle -> not a loop label
 *   Une instruction juste apres break/continue/return dans le meme bloc -> unreachable statement
 * ---------------------------------------------------------------------
 */
public class Drill04_BreakContinueLabels {

    public static int firstNegativeTemp() {
        throw new UnsupportedOperationException("TODO 1 : implementer firstNegativeTemp()");
    }

    public static int sumPositiveTemps() {
        throw new UnsupportedOperationException("TODO 2 : implementer sumPositiveTemps()");
    }

    public static int daysBeforeFreeze() {
        throw new UnsupportedOperationException("TODO 3 : implementer daysBeforeFreeze()");
    }

    public static int rowsWithoutNegatives() {
        throw new UnsupportedOperationException("TODO 4 : implementer rowsWithoutNegatives()");
    }

    public static String findInGrid(int target) {
        throw new UnsupportedOperationException("TODO 5 : implementer findInGrid()");
    }

    public static int commandsBeforeStop() {
        throw new UnsupportedOperationException("TODO 6 : implementer commandsBeforeStop()");
    }

    public static int firstIndexOver(int limit) {
        throw new UnsupportedOperationException("TODO 7 : implementer firstIndexOver()");
    }

    public static int cellsBeforeNegativeInner() {
        throw new UnsupportedOperationException("TODO 8 : implementer cellsBeforeNegativeInner()");
    }

    public static int cellsBeforeNegativeOuter() {
        throw new UnsupportedOperationException("TODO 9 : implementer cellsBeforeNegativeOuter()");
    }

    public static Day firstDayAtLeast(int t) {
        throw new UnsupportedOperationException("TODO 10 : implementer firstDayAtLeast()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  firstNegativeTemp() == -2", firstNegativeTemp() == -2);
        ExerciseChecker.check("2  sumPositiveTemps() == 73", sumPositiveTemps() == 73);
        ExerciseChecker.check("3  daysBeforeFreeze() == 2", daysBeforeFreeze() == 2);
        ExerciseChecker.check("4  rowsWithoutNegatives() == 2", rowsWithoutNegatives() == 2);
        ExerciseChecker.check("5  findInGrid : 6 -> 1,2 ; 10 -> absent", findInGrid(6).equals("1,2") && findInGrid(10).equals("absent"));
        ExerciseChecker.check("6  commandsBeforeStop() == 3", commandsBeforeStop() == 3);
        ExerciseChecker.check("7  firstIndexOver : 20 -> 4, 30 -> -1", firstIndexOver(20) == 4 && firstIndexOver(30) == -1);
        ExerciseChecker.check("8  cellsBeforeNegativeInner() == 7", cellsBeforeNegativeInner() == 7);
        ExerciseChecker.check("9  cellsBeforeNegativeOuter() == 4", cellsBeforeNegativeOuter() == 4);
        ExerciseChecker.check("10 firstDayAtLeast : 20 -> FRI, 30 -> null", firstDayAtLeast(20) == Day.FRI && firstDayAtLeast(30) == null);

        ExerciseChecker.summary();
    }
}
