package config.model.internal;

/**
 * SOLUTION - une classe PUBLIQUE dans un paquet NON exporte : invisible des autres modules,
 * sauf avec --add-exports (a la compilation ET a l'execution).
 */
public final class Defaults {

    public static final int PORT = 8080;

    private Defaults() {
    }

    public static String describe() {
        return "port par defaut " + PORT;
    }
}
