package ch4_coreapis.projects.p04_scores;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Notes du groupe A (sur 100), dans l'ordre des copies. */
    public static final int[] GROUP_A = {72, 85, 58, 91, 64, 85, 47, 78};

    /** Notes du groupe B, deja triees. */
    public static final int[] GROUP_B = {50, 66, 70, 88, 95};

    /** Noms des eleves du groupe A (meme ordre que GROUP_A). */
    public static final String[] NAMES = {"lea", "Hugo", "ines", "Zoe", "adam", "Bob", "tom", "eva"};

    /** Les rangs (1 a 3) recoivent un bonus decroissant : un tableau IRREGULIER de bonus par rang. */
    public static final int[][] BONUSES = {{5, 3, 1}, {3, 1}, {1}};

    private Data() {
    }
}
