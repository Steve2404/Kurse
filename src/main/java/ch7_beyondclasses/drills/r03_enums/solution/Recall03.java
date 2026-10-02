package ch7_beyondclasses.drills.r03_enums.solution;

/**
 * SOLUTION du drill de rappel 3 - les enums simples.
 */
public class Recall03 {

    // Un switch instruction : les case portent le NOM de la constante, sans le prefixe Day.
    static boolean isWeekend(Day d) {
        switch (d) {
            case SAT:
            case SUN:
                return true;
            default:
                return false;
        }
    }

    static int hours(Day d) {
        return switch (d) {
            case SAT, SUN -> 0;
            case FRI -> 4;
            default -> 8;
        };
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + Day.values().length + " " + Day.MON.ordinal() + " " + Day.SUN.name() + " " + Day.valueOf("FRI") + " " + Day.WED);
        Day d = Day.TUE;
        System.out.println("D02 : " + Day.MON.compareTo(Day.FRI) + " " + Day.SUN.compareTo(Day.MON) + " " + (d == Day.TUE) + " " + d.equals(Day.TUE));
        int weekend = 0;
        int total = 0;
        for (Day day : Day.values()) {
            if (isWeekend(day)) {
                weekend++;
            }
            total += hours(day);
        }
        System.out.println("D03 : " + weekend + " " + total);
        Day next = Day.values()[(Day.SUN.ordinal() + 1) % Day.values().length];
        System.out.println("D04 : " + next + " " + Day.values()[Day.FRI.ordinal() + 1]);
        StringBuilder letters = new StringBuilder();
        for (Day day : Day.values()) {
            letters.append(day.name().charAt(0));
        }
        System.out.println("D05 : " + letters);
    }
}

// Un enum simple : la liste des constantes ; le ; final est facultatif quand il n'y a rien d'autre.
enum Day {
    MON, TUE, WED, THU, FRI, SAT, SUN
}
