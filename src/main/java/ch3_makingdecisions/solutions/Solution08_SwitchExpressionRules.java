package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise08_SwitchExpressionRules.
 */
public class Solution08_SwitchExpressionRules {

    public enum Size { S, M, L }

    public static String sizeLabel(Size size) {
        // Toutes les constantes de l'enum sont listees : l'exhaustivite est prouvee, pas de default.
        return switch (size) {
            case S -> "petit";
            case M -> "moyen";
            case L -> "grand";
        };
    }

    public static String dayKind(int day) {
        // Un int a trop de valeurs possibles : default obligatoire pour un switch EXPRESSION.
        return switch (day) {
            case 1, 2, 3, 4, 5 -> "semaine";
            case 6, 7 -> "week-end";
            default -> "invalide";
        };
    }

    public static int shippingCost(Size size, boolean express) {
        // Un bloc apres une fleche doit finir par yield (ou throw).
        return switch (size) {
            case S -> 3;
            case M -> 5;
            case L -> {
                int base = 10;
                yield express ? base * 2 : base;
            }
        };
    }

    public static int colonStyle(int x) {
        // Forme "ancienne" pour une expression : chaque case rend sa valeur avec yield.
        return switch (x) {
            case 1:
                yield 10;
            case 2:
                yield 20;
            default:
                yield 0;
        };
    }

    public static int daysInMonth(int month) {
        // throw compte comme une issue valide : l'expression reste exhaustive.
        return switch (month) {
            case 2 -> 28;
            case 4, 6, 9, 11 -> 30;
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            default -> throw new IllegalArgumentException("mois invalide : " + month);
        };
    }
}
