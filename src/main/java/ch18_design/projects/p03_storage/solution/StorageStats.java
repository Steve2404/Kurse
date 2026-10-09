package ch18_design.projects.p03_storage.solution;

/**
 * Un client qui ne fait que LIRE : il demande le plus petit contrat possible (le I de SOLID).
 * On peut donc lui donner une archive, une memoire, une vue prefixee...
 */
public final class StorageStats {

    private StorageStats() {
    }

    public static String describe(ReadableStorage storage) {
        int characters = storage.keys().stream()
                .mapToInt(key -> storage.read(key).orElseThrow().length())
                .sum();
        return storage.keys().size() + " cle(s), " + characters + " caractere(s)";
    }
}
