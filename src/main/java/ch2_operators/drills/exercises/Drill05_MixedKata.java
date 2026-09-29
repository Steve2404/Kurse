package ch2_operators.drills.exercises;

import ch2_operators.ExerciseChecker;
import ch2_operators.drills.Grades;

/**
 * DRILL 05 - KATA MELANGE : 13 questions du professeur principal (tout le chapitre 2, sans indice)
 * ===============================================================================================
 *
 * Mode d'emploi : voir Drill01_ArithmeticAndPromotion, AVEC UNE
 * DIFFERENCE : ici, AUCUN operateur n'est indique. C'est a toi de
 * choisir (division entiere ou non, cast, ternaire, bits, &&...).
 * Fais-le en dernier, puis refais-le regulierement.
 *
 * Donnees : ch2_operators.drills.Grades.
 *
 *
 * -- Les 13 questions --
 *
 * TODO 1  : total()               somme des notes -> 70.
 * TODO 2  : average()             moyenne exacte des notes -> 70 / 6.0.
 * TODO 3  : passingCount()        nombre de notes >= 10 -> 4.
 * TODO 4  : best()                meilleure note, avec un ternaire dans la boucle -> 16.
 * TODO 5  : mention(avg)          >= 16 TB, >= 14 B, >= 12 AB, >= 10 P, sinon AJ.
 * TODO 6  : cappedBonus(score, b) note (byte) + bonus, plafonnee a 20, sans debordement :
 *                                 (120, 10) -> 20.
 * TODO 7  : optionsText()         les options de Grades.OPTIONS -> "absent,dispense".
 * TODO 8  : evenCount()           le nombre de notes est-il pair ? -> true.
 * TODO 9  : gradeCode()           le code de Grades.GRADE -> 66.
 * TODO 10 : nextGrade()           la lettre apres Grades.GRADE -> 'C'.
 * TODO 11 : roundedAverage()      moyenne arrondie a l'entier le plus proche -> 12.
 * TODO 12 : passingPercent()      pourcentage (entier) de notes >= 10 -> 66.
 * TODO 13 : safeProgress(b, a)    a / b >= 2 sans jamais diviser par 0 : (0, 10) -> false.
 *
 * Pas de carte memoire ici : si tu bloques, retourne voir celle du
 * drill concerne (01 arithmetique, 02 casts, 03 logique et bits,
 * 04 ternaire et precedence).
 */
public class Drill05_MixedKata {

    public static int total() {
        throw new UnsupportedOperationException("TODO 1 : implementer total()");
    }

    public static double average() {
        throw new UnsupportedOperationException("TODO 2 : implementer average()");
    }

    public static int passingCount() {
        throw new UnsupportedOperationException("TODO 3 : implementer passingCount()");
    }

    public static int best() {
        throw new UnsupportedOperationException("TODO 4 : implementer best()");
    }

    public static String mention(double avg) {
        throw new UnsupportedOperationException("TODO 5 : implementer mention()");
    }

    public static byte cappedBonus(byte score, int bonus) {
        throw new UnsupportedOperationException("TODO 6 : implementer cappedBonus()");
    }

    public static String optionsText() {
        throw new UnsupportedOperationException("TODO 7 : implementer optionsText()");
    }

    public static boolean evenCount() {
        throw new UnsupportedOperationException("TODO 8 : implementer evenCount()");
    }

    public static int gradeCode() {
        throw new UnsupportedOperationException("TODO 9 : implementer gradeCode()");
    }

    public static char nextGrade() {
        throw new UnsupportedOperationException("TODO 10 : implementer nextGrade()");
    }

    public static int roundedAverage() {
        throw new UnsupportedOperationException("TODO 11 : implementer roundedAverage()");
    }

    public static int passingPercent() {
        throw new UnsupportedOperationException("TODO 12 : implementer passingPercent()");
    }

    public static boolean safeProgress(int before, int after) {
        throw new UnsupportedOperationException("TODO 13 : implementer safeProgress()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  total() == 70", total() == 70);
        ExerciseChecker.check("2  average() == 70 / 6.0", average() == 70 / 6.0);
        ExerciseChecker.check("3  passingCount() == 4", passingCount() == 4);
        ExerciseChecker.check("4  best() == 16", best() == 16);
        ExerciseChecker.check("5  mention : 16 TB, 11.7 P, 9 AJ", mention(16).equals("TB") && mention(11.7).equals("P") && mention(9).equals("AJ"));
        ExerciseChecker.check("6  cappedBonus(120, 10) == 20, (12, 3) == 15",
                cappedBonus(Grades.SMALL, 10) == 20 && cappedBonus((byte) 12, 3) == 15);
        ExerciseChecker.check("7  optionsText() == absent,dispense", optionsText().equals("absent,dispense"));
        ExerciseChecker.check("8  evenCount() == true", evenCount());
        ExerciseChecker.check("9  gradeCode() == 66", gradeCode() == 66);
        ExerciseChecker.check("10 nextGrade() == 'C'", nextGrade() == 'C');
        ExerciseChecker.check("11 roundedAverage() == 12", roundedAverage() == 12);
        ExerciseChecker.check("12 passingPercent() == 66", passingPercent() == 66);
        ExerciseChecker.check("13 safeProgress : (5, 10) oui, (0, 10) non sans exception", safeProgress(5, 10) && !safeProgress(0, 10));

        ExerciseChecker.summary();
    }
}
