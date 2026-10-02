package ch7_beyondclasses.projects.p05_tree;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les valeurs inserees, dans cet ordre (les doublons sont ignores). */
    public static final int[] VALUES = {50, 30, 70, 20, 40, 60, 80, 35, 45, 65, 10, 5, 40};

    /** Les valeurs a supprimer, dans cet ordre (99 n'existe pas). */
    public static final int[] DELETE = {30, 50, 99};

    /** L'intervalle [bas, haut] pour compter les valeurs. */
    public static final int LOW = 33;
    public static final int HIGH = 66;

    private Data() {
    }
}
