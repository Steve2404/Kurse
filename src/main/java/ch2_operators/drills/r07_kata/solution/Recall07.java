package ch2_operators.drills.r07_kata.solution;

/**
 * SOLUTION du drill de rappel 7 - kata mixte chronometre.
 */
public class Recall07 {

    static int evaluations;

    static boolean test(int value) {
        evaluations++;
        return value > 0;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        System.out.println("D01 : " + n / m + " " + n % m + " " + (n % m + m) % m + " " + (double) n / m);
        System.out.println("D02 : " + (n > m ? n : m) + " " + (n % 2 == 0 ? "pair" : "impair") + " " + ((n & 1) == 0));
        byte b = (byte) n;
        b += m;
        System.out.println("D03 : " + b + " " + (short) (n * 1000));
        int i = n;
        int j = i++ + i-- - --i;
        System.out.println("D04 : " + j + " " + i);
        boolean r = test(n) && test(m) || test(-1);
        System.out.println("D05 : " + r + " " + evaluations);
        int mask = Integer.parseInt(args[2], 2);
        System.out.println("D06 : " + (mask & 0b0110) + " " + (mask | 1 << 4) + " " + (mask >> 1) + " " + Integer.toBinaryString(~mask & 0b1111));
        char c = (char) ('a' + n % 26);
        System.out.println("D07 : " + c + " " + (char) (c - 32) + " " + (c > 'm' ? "fin" : "debut"));
        long big = Integer.MAX_VALUE;
        big += n;
        int wrapped = Integer.MAX_VALUE;
        wrapped += n;
        System.out.println("D08 : " + big + " " + wrapped);
    }
}
