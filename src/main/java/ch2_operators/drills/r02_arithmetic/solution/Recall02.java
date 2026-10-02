package ch2_operators.drills.r02_arithmetic.solution;

/**
 * SOLUTION du drill de rappel 2 - arithmetique et promotion numerique.
 */
public class Recall02 {

    public static void main(String[] args) {
        System.out.println("D01 : " + 17 / 5 + " " + 17 % 5 + " " + 17.0 / 5 + " " + 17 / 5.0f);
        System.out.println("D02 : " + -17 / 5 + " " + -17 % 5 + " " + 17 % -5 + " " + 5.5 % 2);
        byte x = 10;
        byte y = 20;
        // byte + byte -> int : il faut un cast pour revenir a byte.
        byte sum = (byte) (x + y);
        int total = x + y;
        System.out.println("D03 : " + sum + " " + total);
        short s = 30_000;
        int product = s * s;     // short * short -> int (pas de debordement de short)
        System.out.println("D04 : " + product);
        int big = 1_000_000;
        long safe = big * 3000L;   // le L force le calcul en long
        long overflow = big * 3000;
        System.out.println("D05 : " + safe + " " + overflow);
        float f = 1.0f / 3;
        double d = 1.0 / 3;
        System.out.println("D06 : " + f + " " + d);
        char c = 'A';
        int code = c + 1;
        System.out.println("D07 : " + code + " " + (char) code + " " + ('a' - 'A'));
        System.out.println("D08 : " + (1 / 2 + 1.0 / 2) + " " + (2 + 3 * 4 % 5) + " " + (10 - 2 - 3));
        // Division FLOTTANTE par zero : pas d'exception (seule la division entiere en leve une).
        System.out.println("D09 : " + 1.0 / 0 + " " + -1.0 / 0 + " " + 0.0 / 0 + " " + (0.0 / 0 == 0.0 / 0) + " " + 5 % 0.0);
    }
}
