package ch1_buildingblocks.drills.r02_literals.solution;

/**
 * SOLUTION du drill de rappel 2 - types primitifs et litteraux.
 */
public class Recall02 {

    public static void main(String[] args) {
        // Un 0 en tete = octal : 010 vaut 8.
        System.out.println("D01 : " + 010 + " " + 0x10 + " " + 0b10 + " " + 10);
        System.out.println("D02 : " + 1_000_000 + " " + 0xFF_FF + " " + 0b1010_1010);
        long big = 9_000_000_000L;
        System.out.println("D03 : " + big + " " + Long.MAX_VALUE);
        float f = 1.25f;
        double d = 1.25e2;
        System.out.println("D04 : " + f + " " + d + " " + 3.0e-1);
        char c = 'J';
        char u = '\u004A';
        char n = 74;
        int code = 'K';
        System.out.println("D05 : " + c + u + n + " " + code);
        System.out.println("D06 : " + Byte.MIN_VALUE + " " + Short.MAX_VALUE + " " + Character.SIZE + " " + Integer.SIZE);
        // Les champs ont des valeurs par defaut ; on les lit ici via des champs static.
        System.out.println("D07 : " + defaultDouble + " " + defaultBoolean + " " + defaultObject);
        System.out.println("D08 : " + Integer.toHexString(4095) + " " + Integer.toOctalString(64) + " " + Integer.toBinaryString(10));
    }

    static double defaultDouble;
    static boolean defaultBoolean;
    static String defaultObject;
}
