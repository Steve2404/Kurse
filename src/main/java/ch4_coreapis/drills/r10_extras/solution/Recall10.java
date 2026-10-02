package ch4_coreapis.drills.r10_extras.solution;

/**
 * SOLUTION du drill de rappel 10 - les methodes moins frequentes de String, StringBuilder, Math et char.
 */
public class Recall10 {

    public static void main(String[] args) {
        // compareTo : majuscules (65..90) AVANT minuscules (97..122) ; compareToIgnoreCase compare en ignorant la casse.
        System.out.println("D01 : " + "apple".compareToIgnoreCase("APPLE") + " " + "a".compareToIgnoreCase("B") + " " + "Zoo".compareTo("apple") + " "
                + "Zoo".compareToIgnoreCase("apple") + " " + "ab".compareTo("abc"));
        // replaceAll / replaceFirst / matches / split prennent une EXPRESSION REGULIERE, pas un texte.
        System.out.println("D02 : " + "a1b22c333".replaceAll("[0-9]+", "#") + " " + "a1b22c333".replaceFirst("[0-9]", "_") + " "
                + "2026-10-02".matches("\\d{4}-\\d{2}-\\d{2}") + " " + "a.b.c".split("\\.").length + " " + "a.b.c".split(".").length);
        StringBuilder sb = new StringBuilder("java");
        sb.setCharAt(0, 'J');
        // capacite par defaut 16 ; avec un texte : 16 + longueur ; elle n'a rien a voir avec length().
        System.out.println("D03 : " + sb + " " + new StringBuilder().capacity() + " " + new StringBuilder("abc").capacity() + " "
                + new StringBuilder(5).capacity() + " " + sb.length() + " " + sb.lastIndexOf("a") + " " + new StringBuilder("a").compareTo(new StringBuilder("b")));
        char[] ok = {'o', 'k'};
        System.out.println("D04 : " + String.valueOf(3.0) + " " + String.valueOf(true) + " " + String.valueOf('x') + " " + String.valueOf(ok) + " "
                + Integer.toString(42) + " " + Integer.parseInt("-17") + " " + Integer.valueOf("08"));
        System.out.println("D05 : " + "hello".indexOf("") + " " + "hello".lastIndexOf("l", 2) + " " + "abc".contains("") + " "
                + "Mississippi".replace("ss", "s") + " " + "a-b-c".lastIndexOf('-') + " " + "x".repeat(0).isEmpty());
        System.out.println("D06 : " + Math.signum(-4.2) + " " + Math.cbrt(27) + " " + Math.hypot(3, 4) + " " + Math.round(Math.PI * 10_000) / 10_000.0 + " "
                + (Math.E > 2.7) + " " + Math.log10(1000));
        System.out.println("D07 : " + (Integer.MAX_VALUE + 1) + " " + ((long) Integer.MAX_VALUE + 1) + " " + (0.1 + 0.2) + " " + Math.min(-0.0, 0.0) + " "
                + Math.max(-0.0, 0.0) + " " + (int) 3.99 + " " + (int) -3.99);
        System.out.println("D08 : " + (char) ('a' + 1) + " " + ('z' - 'a') + " " + (int) '0' + " " + Character.isDigit('7') + " " + Character.isLetter('_') + " "
                + Character.toUpperCase('q') + " " + ('7' - '0'));
    }
}
