package ch9_collections.projects.p04_generics;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final int[] NUMBERS = {42, 7, 19, 3, 25, 11, 30, 1};

    public static final String[] WORDS = {"lambda", "generique", "map", "set", "collection", "deque"};

    /** Les acces au cache (capacite 3), dans l'ordre. */
    public static final String[] ACCESSES = {"a", "b", "c", "a", "d", "b", "e", "a", "c", "a"};

    public static final int CAPACITY = 3;

    private Data() {
    }
}
