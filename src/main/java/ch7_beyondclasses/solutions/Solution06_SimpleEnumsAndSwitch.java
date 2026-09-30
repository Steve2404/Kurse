package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise06_SimpleEnumsAndSwitch.
 */
public class Solution06_SimpleEnumsAndSwitch {

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public static boolean isWeekend(Day day) {
        // switch expression sur un enum : case SATURDAY, SUNDAY (jamais Day.SATURDAY), exhaustif avec default.
        return switch (day) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
    }

    public static String describeDay(Day day) {
        // switch instruction classique : break pour ne pas couler dans le case suivant.
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
