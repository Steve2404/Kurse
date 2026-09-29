package ch3_makingdecisions.drills.exercises;

import ch3_makingdecisions.ExerciseChecker;
import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * DRILL 02 - Toutes les formes de switch : statement, expression, fleches, yield, fall-through, throw
 * ==================================================================================================
 *
 * Mode d'emploi : voir Drill01_IfAndPatterns.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : dayNameFr(day)          [switch expression sur un enum, SANS default] MON -> "lundi"...
 * TODO 2  : isWeekendSwitch(day)    [case SAT, SUN -> true ; default -> false]
 * TODO 3  : monthDays(month)        [switch statement, case empiles + break] 28 / 30 / 31, sinon -1.
 * TODO 4  : commandCode(command)    [switch statement sur un String] start 1, stop 2, pause 3, sinon 0.
 * TODO 5  : isVowel(c)              [switch expression sur un char, plusieurs valeurs] a e i o u y.
 * TODO 6  : tempLabel(t)            [bloc + yield] 0 -> "zero pile", sinon "negatif" ou "positif".
 * TODO 7  : fallThroughChar(c)      [fall-through voulu] n = 0 ; case 'a': n++ ; case 'b': n++ ; break ;
 *                                    default: n = -1. 'a' -> 2, 'b' -> 1, 'z' -> -1.
 * TODO 8  : nullSafeCommand(c)      [null avant un switch sur String] null -> "aucune", sinon la commande en majuscules.
 * TODO 9  : colonYield(x)           [switch expression ecrit avec ":" et yield] 1 "un", 2 "deux", sinon "beaucoup".
 * TODO 10 : levelName(level)        [default -> throw] 1 "bas", 2 "moyen", 3 "haut",
 *                                    sinon IllegalArgumentException("niveau : " + level).
 * TODO 11 : byteLabel(b)            [switch sur un byte] 0 "zero", 127 "max", sinon "autre".
 * TODO 12 : startCount()            [switch statement dans une boucle] nombre de "start" dans Week.COMMANDS -> 3.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Types acceptes : byte, short, char, int (et leurs boites), String, enum. Jamais long, float,
 *                    double, boolean.
 *   case : une constante de compilation ; plusieurs valeurs : case 1, 2 ; dans un enum : case SAT
 *   Statement ":" : coule vers le bas jusqu'au break ; aucun case + pas de default -> rien
 *   Expression : exhaustive (default, sauf enum complet) ; ; final ; bloc -> yield ; throw permis
 *   Jamais melanger "->" et ":" dans le meme switch ; switch sur String null -> NullPointerException
 * ---------------------------------------------------------------------
 */
public class Drill02_SwitchForms {

    public static String dayNameFr(Day day) {
        throw new UnsupportedOperationException("TODO 1 : implementer dayNameFr()");
    }

    public static boolean isWeekendSwitch(Day day) {
        throw new UnsupportedOperationException("TODO 2 : implementer isWeekendSwitch()");
    }

    public static int monthDays(int month) {
        throw new UnsupportedOperationException("TODO 3 : implementer monthDays()");
    }

    public static int commandCode(String command) {
        throw new UnsupportedOperationException("TODO 4 : implementer commandCode()");
    }

    public static boolean isVowel(char c) {
        throw new UnsupportedOperationException("TODO 5 : implementer isVowel()");
    }

    public static String tempLabel(int t) {
        throw new UnsupportedOperationException("TODO 6 : implementer tempLabel()");
    }

    public static int fallThroughChar(char c) {
        throw new UnsupportedOperationException("TODO 7 : implementer fallThroughChar()");
    }

    public static String nullSafeCommand(String command) {
        throw new UnsupportedOperationException("TODO 8 : implementer nullSafeCommand()");
    }

    public static String colonYield(int x) {
        throw new UnsupportedOperationException("TODO 9 : implementer colonYield()");
    }

    public static String levelName(int level) {
        throw new UnsupportedOperationException("TODO 10 : implementer levelName()");
    }

    public static String byteLabel(byte b) {
        throw new UnsupportedOperationException("TODO 11 : implementer byteLabel()");
    }

    public static int startCount() {
        throw new UnsupportedOperationException("TODO 12 : implementer startCount()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  dayNameFr : MON lundi, SUN dimanche", dayNameFr(Day.MON).equals("lundi") && dayNameFr(Day.SUN).equals("dimanche"));
        ExerciseChecker.check("2  isWeekendSwitch : SUN oui, FRI non", isWeekendSwitch(Day.SUN) && !isWeekendSwitch(Day.FRI));
        ExerciseChecker.check("3  monthDays : 2 -> 28, 11 -> 30, 7 -> 31, 13 -> -1",
                monthDays(2) == 28 && monthDays(11) == 30 && monthDays(7) == 31 && monthDays(13) == -1);
        ExerciseChecker.check("4  commandCode : start 1, stop 2, pause 3, abc 0",
                commandCode("start") == 1 && commandCode("stop") == 2 && commandCode("pause") == 3 && commandCode("abc") == 0);
        ExerciseChecker.check("5  isVowel : y oui, k non", isVowel('y') && !isVowel('k'));
        ExerciseChecker.check("6  tempLabel : 0 zero pile, -2 negatif, 17 positif",
                tempLabel(0).equals("zero pile") && tempLabel(-2).equals("negatif") && tempLabel(17).equals("positif"));
        ExerciseChecker.check("7  fallThroughChar : a 2, b 1, z -1", fallThroughChar('a') == 2 && fallThroughChar('b') == 1 && fallThroughChar('z') == -1);
        ExerciseChecker.check("8  nullSafeCommand : null aucune, pause PAUSE",
                nullSafeCommand(null).equals("aucune") && nullSafeCommand("pause").equals("PAUSE"));
        ExerciseChecker.check("9  colonYield : 1 un, 2 deux, 9 beaucoup",
                colonYield(1).equals("un") && colonYield(2).equals("deux") && colonYield(9).equals("beaucoup"));
        String message = null;
        try {
            levelName(4);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("10 levelName : 2 moyen, 4 -> exception \"niveau : 4\"", levelName(2).equals("moyen") && "niveau : 4".equals(message));
        ExerciseChecker.check("11 byteLabel : 0 zero, 127 max, 5 autre",
                byteLabel((byte) 0).equals("zero") && byteLabel((byte) 127).equals("max") && byteLabel((byte) 5).equals("autre"));
        ExerciseChecker.check("12 startCount() == 3", startCount() == 3);

        ExerciseChecker.summary();
    }
}
