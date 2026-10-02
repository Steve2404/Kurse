package ch8_lambdas.projects.p04_numeric;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les nombres a reduire (somme, max, pgcd, ppcm) et a filtrer. */
    public static final int[] NUMBERS = {84, 36, 120, 48, 60, 17, 90};

    /** Les mots a mesurer. */
    public static final String[] WORDS = {"lambda", "fonction", "interface", "java", "predicat"};

    /** La precision des methodes iteratives et le nombre de tirages de Monte-Carlo. */
    public static final double EPSILON = 1e-9;
    public static final int SAMPLES = 20_000;
    public static final long SEED = 7;

    private Data() {
    }
}
