package ch3_makingdecisions.drills.r03_switchexpr.solution;

/**
 * SOLUTION du drill de rappel 3 - le switch expression.
 */
public class Recall03 {

    static int days(int month) {
        return switch (month) {
            case 2 -> 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    static String season(String month) {
        return switch (month) {
            case "dec", "jan", "fev" -> "hiver";
            case "mar", "avr", "mai" -> "printemps";
            case "jun", "jul", "aou" -> "ete";
            default -> {
                String result = "automne";
                yield result;
            }
        };
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + days(2) + " " + days(4) + " " + days(12));
        System.out.println("D02 : " + season("jan") + " " + season("mai") + " " + season("oct"));
        int score = 85;
        String level = switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> {
                String base = "B";
                yield score % 10 >= 5 ? base + "+" : base;
            }
            case 7 -> "C";
            default -> "D";
        };
        System.out.println("D03 : " + level);
        // Les branches sont promues vers un type commun : int et double -> double.
        var result = switch (1) {
            case 1 -> 10;
            default -> 2.5;
        };
        System.out.println("D04 : " + result);
        char op = '*';
        int a = 6;
        int b = 3;
        System.out.println("D05 : " + switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            default -> a / b;
        });
        // Forme "ancienne" avec : et yield dans un switch expression.
        int code = 2;
        String word = switch (code) {
            case 1:
                yield "un";
            case 2:
                yield "deux";
            default:
                yield "beaucoup";
        };
        System.out.println("D06 : " + word);
        int counter = 0;
        int got = switch (counter++) {
            case 0 -> counter * 100;
            default -> -1;
        };
        System.out.println("D07 : " + got + " " + counter);
        String bits = "";
        for (int i = 0; i < 4; i++) {
            bits = bits + switch (i % 3) {
                case 0 -> "z";
                case 1 -> "u";
                default -> "d";
            };
        }
        System.out.println("D08 : " + bits);
    }
}
