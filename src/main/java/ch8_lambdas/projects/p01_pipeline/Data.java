package ch8_lambdas.projects.p01_pipeline;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier).
 * Une etape : trim, lower, upper, squeeze, reverse, title, novowels, caesar N, replace A B, repeat N, rle, unrle.
 * Un pipeline = des etapes separees par " | ".
 */
public final class Data {

    public static final String[] TEXTS = {"   le  Java   est   un langage   ", "aaabbbccccd", "Bonjour le Monde"};

    public static final String[] PIPELINES = {
            "trim | squeeze | title",
            "rle",
            "rle | unrle",
            "lower | caesar 3 | upper",
            "caesar 3 | caesar 23",
            "novowels | reverse | replace l 1",
            "trim | squeeze | repeat 2"};

    private Data() {
    }
}
