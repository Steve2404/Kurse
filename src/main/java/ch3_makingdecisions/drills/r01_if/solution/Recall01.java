package ch3_makingdecisions.drills.r01_if.solution;

/**
 * SOLUTION du drill de rappel 1 - if/else et pattern matching.
 */
public class Recall01 {

    static String grade(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 75) {
            return "B";
        } else if (score >= 50) {
            return "C";
        } else {
            return "D";
        }
    }

    static String describe(Object o) {
        if (o instanceof Integer i && i > 100) {
            return "grand entier " + i;
        } else if (o instanceof Integer i) {
            return "entier " + (i + 1);
        } else if (o instanceof String s) {
            return "texte \"" + s + "\"";
        }
        return "autre";
    }

    // Portee de flux : apres un "if (!(... instanceof T v)) return", v est utilisable jusqu'a la fin.
    static int doubled(Object o) {
        if (!(o instanceof Integer n)) {
            return -1;
        }
        return n * 2;
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + grade(95) + grade(75) + grade(74) + grade(10));
        int x = 7;
        String parity;
        if (x % 2 == 0) {
            parity = "pair";
        } else {
            parity = "impair";
        }
        System.out.println("D02 : " + parity);
        System.out.println("D03 : " + describe(150) + " | " + describe(5) + " | " + describe("ok") + " | " + describe(2.5));
        System.out.println("D04 : " + doubled(21) + " " + doubled("21"));
        Object value = 42;
        // La variable de pattern n'existe QUE la ou le test est forcement vrai.
        if (!(value instanceof Integer n) || n < 0) {
            System.out.println("D05 : negatif ou pas un entier");
        } else {
            System.out.println("D05 : " + (n + 8));
        }
        int a = 3;
        int b = 5;
        if (a > b)
            System.out.println("jamais");
        System.out.println("D06 : sans accolades, seule la 1re instruction est dans le if");
        Object nothing = null;
        System.out.println("D07 : " + (nothing instanceof String s ? s : "null n'est jamais une instance"));
        int temp = 18;
        String advice = temp < 0 ? "gel" : temp < 15 ? "froid" : temp < 25 ? "doux" : "chaud";
        System.out.println("D08 : " + advice);
    }
}
