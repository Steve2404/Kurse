package ch2_operators.solutions;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise17_GradeReportCapstone.
 */
public class Solution17_GradeReportCapstone {

    public static final int ABSENT = 1;
    public static final int LATE = 2;
    public static final int EXEMPT = 4;

    public static double average(int[] scores) {
        // Le cast (double) porte sur le DIVISEUR avant la division : sinon division entiere (31 / 2 == 15).
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return scores.length == 0 ? 0.0 : sum / (double) scores.length;
    }

    public static String mention(double average) {
        // Ternaires enchaines du seuil le plus haut au plus bas : le premier vrai gagne.
        return average >= 16 ? "TB" : average >= 14 ? "B" : average >= 12 ? "AB" : average >= 10 ? "P" : "AJ";
    }

    public static byte addBonus(byte score, int bonus) {
        // score + bonus est deja un int (promotion) : on plafonne AVANT de revenir en byte,
        // sinon 120 + 10 deborderait en -126.
        int total = score + bonus;
        return (byte) (total > 20 ? 20 : total);
    }

    public static String optionsLabel(int options) {
        // (options & X) != 0 : parentheses obligatoires, != passe avant &.
        String label = "";
        if ((options & ABSENT) != 0) {
            label += "absent";
        }
        if ((options & LATE) != 0) {
            label += (label.isEmpty() ? "" : ",") + "retard";
        }
        if ((options & EXEMPT) != 0) {
            label += (label.isEmpty() ? "" : ",") + "dispense";
        }
        return label.isEmpty() ? "-" : label;
    }

    public static boolean doubledProgress(int before, int after) {
        // && court-circuite : si before vaut 0, la division (qui lancerait ArithmeticException) n'est jamais faite.
        return before != 0 && after / before >= 2;
    }

    public static String reportLine(String name, int[] scores, int options) {
        // Concatenation : le double s'affiche tel quel (15.5, 9.0, 0.0).
        double avg = average(scores);
        return name + " : " + avg + " (" + mention(avg) + ") [" + optionsLabel(options) + "]";
    }
}
