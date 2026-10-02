package ch5_methods.projects.p07_shop.solution.util;

/**
 * SOLUTION - des outils de formatage : methodes static publiques, surchargees, importees en static.
 */
public class Format {

    public static final int WIDTH = 9;

    public static String money(long cents) {
        long rest = cents % 100;
        return cents / 100 + "." + (rest < 10 ? "0" : "") + rest;
    }

    // Surcharge : meme nom, parametres differents (le type de retour seul ne suffirait pas).
    public static String pad(String s, int width) {
        return s.length() >= width ? s : s + " ".repeat(width - s.length());
    }

    public static String pad(long n, int width) {
        String s = String.valueOf(n);
        return s.length() >= width ? s : " ".repeat(width - s.length()) + s;   // un nombre s'aligne a droite
    }

    public static String pad(String s) {
        return pad(s, WIDTH);   // une surcharge peut deleguer a une autre
    }
}
