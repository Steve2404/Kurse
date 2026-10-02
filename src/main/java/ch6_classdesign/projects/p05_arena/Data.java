package ch6_classdesign.projects.p05_arena;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 * Une ligne par combattant : "classe nom pv attaque vitesse".
 * Classes : W = guerrier, M = mage, H = soigneur, T = tank.
 */
public final class Data {

    public static final String[] TEAM_A = {"W Conan 120 18 6", "M Merlin 70 20 8", "H Mira 80 6 4"};
    public static final String[] TEAM_B = {"T Golem 100 14 2", "W Brutus 110 16 5", "M Sabrina 65 22 7"};

    /** Le nombre maximal de tours. */
    public static final int MAX_ROUNDS = 12;

    private Data() {
    }
}
