package ch2_operators.drills.solutions;

import ch2_operators.drills.Grades;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    public static int total() {
        // += dans une boucle : l'accumulateur classique.
        int sum = 0;
        for (int s : Grades.SCORES) {
            sum += s;
        }
        return sum;
    }

    public static double average() {
        // Division en double grace au cast du diviseur (70 / 6 donnerait 11).
        return total() / (double) Grades.SCORES.length;
    }

    public static int passingCount() {
        // Comparaison relationnelle, bornes incluses.
        int count = 0;
        for (int s : Grades.SCORES) {
            if (s >= 10) {
                count++;
            }
        }
        return count;
    }

    public static int best() {
        // Le ternaire garde le plus grand a chaque tour.
        int best = Grades.SCORES[0];
        for (int s : Grades.SCORES) {
            best = s > best ? s : best;
        }
        return best;
    }

    public static String mention(double avg) {
        // Ternaires enchaines du seuil le plus haut au plus bas.
        return avg >= 16 ? "TB" : avg >= 14 ? "B" : avg >= 12 ? "AB" : avg >= 10 ? "P" : "AJ";
    }

    public static byte cappedBonus(byte score, int bonus) {
        // Calcul en int (promotion) et plafond AVANT le cast : pas de debordement du byte.
        int sum = score + bonus;
        return (byte) (sum > 20 ? 20 : sum);
    }

    public static String optionsText() {
        // Tester chaque bit avec (x & option) != 0, parentheses obligatoires.
        String text = "";
        if ((Grades.OPTIONS & Grades.ABSENT) != 0) {
            text += "absent";
        }
        if ((Grades.OPTIONS & Grades.LATE) != 0) {
            text += (text.isEmpty() ? "" : ",") + "retard";
        }
        if ((Grades.OPTIONS & Grades.EXEMPT) != 0) {
            text += (text.isEmpty() ? "" : ",") + "dispense";
        }
        return text;
    }

    public static boolean evenCount() {
        // Reste de la division par 2.
        return Grades.SCORES.length % 2 == 0;
    }

    public static int gradeCode() {
        // char -> int : elargissement automatique.
        return Grades.GRADE;
    }

    public static char nextGrade() {
        // char + 1 est un int : le cast est obligatoire pour revenir a une lettre.
        return (char) (Grades.GRADE + 1);
    }

    public static int roundedAverage() {
        // Arrondi d'un positif : + 0.5 puis couper (11.67 + 0.5 = 12.17 -> 12).
        return (int) (average() + 0.5);
    }

    public static int passingPercent() {
        // Multiplier AVANT de diviser en entiers : 400 / 6 == 66 (4 / 6 * 100 donnerait 0).
        return passingCount() * 100 / Grades.SCORES.length;
    }

    public static boolean safeProgress(int before, int after) {
        // && court-circuit : la division n'est faite que si before != 0.
        return before != 0 && after / before >= 2;
    }
}
