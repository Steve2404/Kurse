package ch18_design.drills.r01_refactor.solution;

import java.util.Set;

/** Le prix du cinema, refactore : des constantes nommees, une petite methode par regle, aucun "else if". */
public final class Recall01 {

    static final int CHILD = 600;
    static final int SENIOR = 750;
    static final int STUDENT = 800;
    static final int FULL = 1100;
    static final int WEDNESDAY_KIDS = 100;
    static final int MORNING = 200;
    static final int WEEKEND = 150;
    static final Set<String> WEEKEND_DAYS = Set.of("SAM", "DIM");

    private Recall01() {
    }

    // Les moins de 4 ans entrent gratuitement : aucune autre regle ne s'applique (pas de matinee negative).
    public static int ticket(int age, boolean student, String day, int hour) {
        if (age < 0 || hour < 0 || hour > 23) {
            throw new IllegalArgumentException("entree invalide");
        }
        return age < 4 ? 0 : base(age, student) - reductions(age, day, hour) + surcharge(day);
    }

    private static int base(int age, boolean student) {
        if (age < 14) {
            return CHILD;
        }
        return age >= 65 ? SENIOR : (student ? STUDENT : FULL);
    }

    private static int reductions(int age, String day, int hour) {
        int wednesday = day.equals("MER") && age < 14 ? WEDNESDAY_KIDS : 0;
        return wednesday + (hour < 12 ? MORNING : 0);
    }

    private static int surcharge(String day) {
        return WEEKEND_DAYS.contains(day) ? WEEKEND : 0;
    }
}
