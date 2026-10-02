package ch4_coreapis.drills.r07_math.solution;

/**
 * SOLUTION du drill de rappel 7 - Math.
 */
public class Recall07 {

    public static void main(String[] args) {
        System.out.println("D01 : " + Math.max(3, 7) + " " + Math.min(-2, 5) + " " + Math.max(3, 7.5) + " " + Math.min(4L, 9));
        long fromDouble = Math.round(3.5);
        int fromFloat = Math.round(3.5f);
        System.out.println("D02 : " + fromDouble + " " + fromFloat + " " + Math.round(-3.5) + " " + Math.round(-3.6) + " " + Math.round(2.4));
        System.out.println("D03 : " + Math.ceil(3.2) + " " + Math.floor(3.8) + " " + Math.ceil(-3.2) + " " + Math.floor(-3.2));
        System.out.println("D04 : " + Math.pow(2, 8) + " " + Math.pow(9, 0.5) + " " + Math.sqrt(16) + " " + Math.abs(-4.5) + " " + Math.abs(-4));
        double random = Math.random();
        int dice = (int) (Math.random() * 6) + 1;
        System.out.println("D05 : " + (random >= 0 && random < 1) + " " + (dice >= 1 && dice <= 6));
        System.out.println("D06 : " + (int) Math.pow(10, 3) + " " + Math.round(12.3456 * 100) / 100.0 + " " + Math.round(1234.5) / 10 * 10);
        System.out.println("D07 : " + Math.abs(Integer.MIN_VALUE) + " " + Math.max(Double.NaN, 1) + " " + Math.sqrt(-1));
        System.out.println("D08 : " + Math.floorDiv(-7, 2) + " " + -7 / 2 + " " + Math.floorMod(-7, 3) + " " + -7 % 3);
    }
}
