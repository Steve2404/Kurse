package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise07_SwitchExpressionBasics.
 */
public class Solution07_SwitchExpressionBasics {

    public static String seasonForMonthExpr(int monthNumber) {
        // switch expression : fleches, plusieurs valeurs par case, default pour couvrir tous les int, ; final.
        return switch (monthNumber) {
            case 12, 1, 2 -> "Hiver";
            case 3, 4, 5 -> "Printemps";
            case 6, 7, 8 -> "Ete";
            case 9, 10, 11 -> "Automne";
            default -> "Inconnu";
        };
    }

    public static String letterGrade(int score) {
        // score / 10 (division entiere) regroupe les notes par dizaine ; un bloc rend sa valeur avec yield.
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
