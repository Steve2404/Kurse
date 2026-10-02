package ch2_operators.drills.r05_bits.solution;

/**
 * SOLUTION du drill de rappel 5 - operateurs bit a bit et decalages.
 */
public class Recall05 {

    public static void main(String[] args) {
        int a = 0b1100;
        int b = 0b1010;
        System.out.println("D01 : " + (a & b) + " " + (a | b) + " " + (a ^ b) + " " + Integer.toBinaryString(a ^ b));
        System.out.println("D02 : " + ~0 + " " + ~5 + " " + (~5 & 0xF) + " " + (~~5));
        System.out.println("D03 : " + (1 << 4) + " " + (3 << 2) + " " + (256 >> 3) + " " + (1 << 31));
        System.out.println("D04 : " + (-32 >> 3) + " " + (-1 >>> 28) + " " + (-1 >> 28) + " " + (8 >>> 1));
        int flags = 0;
        flags |= 1 << 2;
        flags |= 1 << 0;
        flags ^= 1 << 0;
        flags &= ~(1 << 3);
        System.out.println("D05 : " + flags + " " + Integer.toBinaryString(flags) + " " + ((flags & 1 << 2) != 0));
        int color = 0xFF8800;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        System.out.println("D06 : " + red + " " + green + " " + blue);
        int packed = red << 16 | green << 8 | blue;
        System.out.println("D07 : " + (packed == color) + " " + Integer.toHexString(packed));
        // Pair ou impair sans % : le bit de poids faible.
        System.out.println("D08 : " + (7 & 1) + " " + (10 & 1) + " " + (1 << 1 + 1) + " " + ((1 << 1) + 1));
    }
}
