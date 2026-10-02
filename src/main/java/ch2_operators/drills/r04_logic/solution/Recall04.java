package ch2_operators.drills.r04_logic.solution;

/**
 * SOLUTION du drill de rappel 4 - comparaisons, logique, court-circuit, instanceof.
 */
public class Recall04 {

    static int calls;

    static boolean check(boolean result) {
        calls++;
        return result;
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + (5 > 3) + " " + (5 >= 5) + " " + (5 != 5) + " " + ('a' < 'b') + " " + (10 == 10.0));
        boolean t = true;
        boolean f = false;
        System.out.println("D02 : " + (t & f) + " " + (t | f) + " " + (t ^ t) + " " + (f ^ t) + " " + (!t || f));
        calls = 0;
        boolean r1 = check(false) && check(true);
        int afterAnd = calls;
        calls = 0;
        boolean r2 = check(false) & check(true);
        System.out.println("D03 : " + r1 + " " + afterAnd + " " + r2 + " " + calls);
        calls = 0;
        boolean r3 = check(true) || check(false) || check(true);
        System.out.println("D04 : " + r3 + " " + calls);
        int n = 5;
        boolean r4 = n > 10 && n++ > 0;
        boolean r5 = n > 1 || n++ > 0;
        boolean r6 = n > 1 | n++ > 0;
        System.out.println("D05 : " + r4 + " " + r5 + " " + r6 + " " + n);
        // && passe avant || : true || (false && false)
        System.out.println("D06 : " + (true || false && false) + " " + ((true || false) && false));
        Object number = Double.valueOf(2.5);
        Object empty = null;
        System.out.println("D07 : " + (number instanceof Double) + " " + (number instanceof Number) + " "
                + (number instanceof Integer) + " " + (empty instanceof Double));
        Recall04 a = new Recall04();
        Recall04 b = new Recall04();
        Recall04 c = a;
        System.out.println("D08 : " + (a == b) + " " + (a == c) + " " + (a != b));
    }
}
