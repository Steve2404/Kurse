package ch3_makingdecisions.drills.r07_kata.solution;

/**
 * SOLUTION du drill de rappel 7 - kata mixte chronometre.
 */
public class Recall07 {

    static String classify(Object o) {
        if (o instanceof Integer i && i % 2 == 0) {
            return "pair";
        } else if (o instanceof Integer) {
            return "impair";
        }
        return "texte";
    }

    public static void main(String[] args) {
        int limit = Integer.parseInt(args[0]);
        int sum = 0;
        for (int i = 1; i <= limit; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                sum += i;
            }
        }
        System.out.println("D01 : " + sum);
        int a = 48;
        int b = 180;
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        System.out.println("D02 : " + a);
        String kinds = "";
        for (String arg : args) {
            Object parsed = switch (arg) {
                case "un", "deux" -> arg;
                default -> Integer.parseInt(arg);
            };
            kinds = kinds + classify(parsed) + " ";
        }
        System.out.println("D03 : " + kinds);
        int count = 0;
        outer:
        for (int x = 2; x <= limit; x++) {
            for (int d = 2; d * d <= x; d++) {
                if (x % d == 0) {
                    continue outer;
                }
            }
            count++;
        }
        System.out.println("D04 : " + count);
        int steps = 0;
        long n = 27;
        while (n != 1) {
            n = n % 2 == 0 ? n / 2 : 3 * n + 1;
            steps++;
        }
        System.out.println("D05 : " + steps);
        String line = "";
        for (int i = 1; i <= 4; i++) {
            line = line + switch (i) {
                case 1 -> "I";
                case 2 -> "II";
                case 3 -> "III";
                default -> {
                    String four = "IV";
                    yield four;
                }
            } + " ";
        }
        System.out.println("D06 : " + line);
        int digits = 0;
        int value = limit;
        do {
            value /= 10;
            digits++;
        } while (value != 0);
        System.out.println("D07 : " + digits);
        String stairs = "";
        for (int r = 1; r <= 3; r++) {
            for (int c = 0; c < r; c++) {
                stairs = stairs + "#";
            }
            stairs = stairs + "|";
        }
        System.out.println("D08 : " + stairs);
    }
}
