package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise04_SimpleEnumsAndSwitch.
 */
public class Solution04_SimpleEnumsAndSwitch {

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public static boolean isWeekend(Day day) {
        return switch (day) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
    }

    public static String describeDay(Day day) {
        String result;
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                result = "Jour ouvre";
                break;
            case SATURDAY:
            case SUNDAY:
                result = "Week-end";
                break;
            default:
                result = "Inconnu";
        }
        return result;
    }
}
