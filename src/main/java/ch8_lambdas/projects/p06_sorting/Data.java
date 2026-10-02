package ch8_lambdas.projects.p06_sorting;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier). "nom age ville score".
 */
public final class Data {

    public static final String[] PEOPLE = {
            "Lea 31 Lyon 88", "Hugo 25 Paris 92", "Ines 31 Paris 75", "Adam 42 Lyon 92", "Zoe 25 Nice 60",
            "Bob 37 Paris 81", "Emma 29 Nice 95", "Noah 42 Lyon 70", "Lina 29 Paris 88", "Theo 33 Nice 77"};

    /** Les ages recherches par dichotomie dans le tableau trie par age. */
    public static final int[] AGES = {29, 42, 30};

    /** Combien de meilleurs scores extraire. */
    public static final int TOP = 3;

    private Data() {
    }
}
