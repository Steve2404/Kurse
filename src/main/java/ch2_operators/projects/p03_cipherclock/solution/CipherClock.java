package ch2_operators.projects.p03_cipherclock.solution;

import ch2_operators.projects.p03_cipherclock.Data;

/**
 * SOLUTION du projet 3 - une conception possible.
 */
public class CipherClock {

    // Modulo TOUJOURS positif : en Java, -3 % 26 vaut -3 (le signe suit le dividende) ; on ajoute 26 puis on refait %.
    static int mod(int value, int base) {
        return (value % base + base) % base;
    }

    // char - 'A' : un char est numerique, la soustraction donne un int (0 pour A) ; le cast (char) revient a une lettre.
    static char shift(char c, int key) {
        return c == ' ' ? c : (char) (mod(c - 'A' + key, 26) + 'A');
    }

    static String word(int key) {
        return "" + shift(Data.C1, key) + shift(Data.C2, key) + shift(Data.C3, key) + shift(Data.C4, key)
                + shift(Data.C5, key) + shift(Data.C6, key) + shift(Data.C7, key) + shift(Data.C8, key);
    }

    static String twoDigits(int n) {
        return n < 10 ? "0" + n : "" + n;
    }

    static String clock(int hour, int minute, int delta) {
        int total = hour * 60 + minute + delta;
        int inDay = mod(total, Data.MINUTES_PER_DAY);
        // Division "plancher" : (total - inDay) est un multiple exact de 1440, meme si total est negatif.
        int days = (total - inDay) / Data.MINUTES_PER_DAY;
        return twoDigits(inDay / 60) + ":" + twoDigits(inDay % 60)
                + (days == 0 ? "" : days > 0 ? " (+" + days + " j)" : " (" + days + " j)");
    }

    public static void main(String[] args) {
        int key = Integer.parseInt(args[0]);
        int back = Integer.parseInt(args[1]);
        System.out.println("=== CESAR ===");
        System.out.println("clair     : " + word(0));
        System.out.println("chiffre " + key + " : " + word(key));
        // Dechiffrer = chiffrer le message chiffre avec -key ; -29 equivaut a -3 modulo 26.
        System.out.println("cle " + back + " = cle " + mod(back, 26) + " ; -3 % 26 = " + -3 % 26 + ", mod(-3, 26) = " + mod(-3, 26));
        System.out.println("aller-retour : " + word(key + back) + " (cle totale " + (key + back) + ")");
        System.out.println("'A' + 2 = " + ('A' + 2) + ", (char) ('A' + 2) = " + (char) ('A' + 2) + ", 'Z' - 'A' = " + ('Z' - 'A'));

        System.out.println("=== HORLOGE ===");
        int h = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        System.out.println("depart " + clock(h, m, 0) + " | +" + args[4] + " min -> " + clock(h, m, Integer.parseInt(args[4]))
                + " | " + args[5] + " min -> " + clock(h, m, Integer.parseInt(args[5])));
        System.out.println("-75 / 1440 = " + -75 / 1440 + " (division tronquee), plancher = " + (-75 - mod(-75, 1440)) / 1440);

        System.out.println("=== DEBORDEMENTS ===");
        byte counter = 120;
        counter += 10;  // compose : cast (byte) implicite, donc pas d'erreur de compilation... mais debordement
        System.out.println("byte 120 += 10 -> " + counter + ", (byte) 200 = " + (byte) 200 + ", (short) 40000 = " + (short) 40000);
        int max = Integer.MAX_VALUE;
        System.out.println("MAX_VALUE + 1 = " + (max + 1) + ", en long : " + (max + 1L));
        System.out.println("(int) 9.99 = " + (int) 9.99 + ", (int) -9.99 = " + (int) -9.99 + ", (int) 3e10 = " + (int) 3e10);
        System.out.println("7 / 2 = " + 7 / 2 + ", 7 / 2.0 = " + 7 / 2.0 + ", 7 % -3 = " + 7 % -3 + ", -7 % 3 = " + -7 % 3);
        System.out.println("0.1 + 0.2 = " + (0.1 + 0.2) + ", 0.1f + 0.2f = " + (0.1f + 0.2f));
    }
}
