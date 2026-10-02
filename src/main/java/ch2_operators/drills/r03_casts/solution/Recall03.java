package ch2_operators.drills.r03_casts.solution;

/**
 * SOLUTION du drill de rappel 3 - casts, debordements, affectations composees.
 */
public class Recall03 {

    public static void main(String[] args) {
        System.out.println("D01 : " + (byte) 130 + " " + (byte) -129 + " " + (byte) 256 + " " + (short) 65_535);
        System.out.println("D02 : " + (int) 3.99 + " " + (int) -3.99 + " " + (long) 1e19 + " " + (int) 'Z');
        System.out.println("D03 : " + (char) 66 + " " + (char) ('a' + 25) + " " + (float) 0.1 + " " + (double) 0.1f);
        byte b = 100;
        b += 100;            // compose : cast (byte) implicite
        short s = 10;
        s *= 1000;           // 10000 tient dans un short
        s *= 10;             // 100000 ne tient pas : debordement silencieux
        System.out.println("D04 : " + b + " " + s);
        int i = 7;
        i /= 2;
        i -= -3;
        i %= 4;
        System.out.println("D05 : " + i);
        int x;
        int y = (x = 3) + 1;
        System.out.println("D06 : " + x + " " + y);
        long l = 10;
        int fromLong = (int) l + 5;
        double dd = 9;
        System.out.println("D07 : " + fromLong + " " + dd + " " + (int) (dd / 2) + " " + (int) dd / 2);
        int counter = 0;
        counter += counter++ + ++counter;   // 0 + (0 + 2)
        System.out.println("D08 : " + counter);
    }
}
