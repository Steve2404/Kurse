package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise06_SwitchExpressionBasics.
 */
public class Solution06_SwitchExpressionBasics {

    public static String seasonForMonthExpr(int monthNumber) {
        return switch (monthNumber) {
            case 12, 1, 2 -> "Hiver";
            case 3, 4, 5 -> "Printemps";
            case 6, 7, 8 -> "Ete";
            case 9, 10, 11 -> "Automne";
            default -> "Inconnu";
        };
    }

    public static String letterGrade(int score) {
        return switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            default -> {
                String grade = "F";
                yield grade;
            }
        };
    }
}
