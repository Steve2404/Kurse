package ch6_classdesign.projects.p01_shapes;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les points dont on calcule l'enveloppe convexe : {x, y}. */
    public static final double[][] POINTS = {{2, 1}, {0, 0}, {4, 3}, {1, 2}, {4, 0}, {3, 2}, {0, 3}, {2, 4}, {5, 1}, {2, 2}};

    /** Les points a tester : dedans ou dehors de l'enveloppe ? */
    public static final double[][] TESTS = {{2, 2}, {5, 3}, {1, 3.4}, {-1, 1}};

    private Data() {
    }
}
