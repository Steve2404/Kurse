package config.app;

import config.binder.Binder;
import config.legacy.LegacyConfig;
import config.model.ServerConfig;
import config.model.internal.Defaults;

import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 3 - la configuration par reflexion, et les regles d'acces des modules.
 */
public class Main {

    public static void main(String[] args) throws ReflectiveOperationException {
        Map<String, String> values = new TreeMap<>(Map.of("host", "example.org", "port", "8443", "debug", "true", "tags", "web,api"));
        ServerConfig config = Binder.bind(ServerConfig.class, values);
        System.out.println("lie : " + config + " ; dump " + Binder.dump(config));

        LegacyConfig legacy = Binder.bind(LegacyConfig.class, Map.of("mode", "batch", "retries", "3"));
        System.out.println("module ouvert : " + Binder.dump(legacy));

        Module model = ServerConfig.class.getModule();
        Module binder = Binder.class.getModule();
        System.out.println("config.model ouvert a config.binder " + model.isOpen("config.model", binder) + ", a config.app " + model.isOpen("config.model", Main.class.getModule())
                + " ; config.legacy ouvert a tous " + LegacyConfig.class.getModule().isOpen("config.legacy") + " ; secret ouvert a config.binder "
                + model.isOpen("config.model.secret", binder));

        // La classe est trouvee par son NOM : on ne la connait pas a la compilation.
        Class<?> vault = Class.forName("config.model.secret.Vault");
        try {
            System.out.println("coffre : " + Binder.dump(Binder.bind(vault, Map.of("token", "s3cret"))));
        } catch (RuntimeException e) {
            // InaccessibleObjectException (java.lang, Java 9) est une RuntimeException.
            System.out.println("coffre refuse : " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.println("interne : " + Defaults.describe());
        } catch (IllegalAccessError e) {
            System.out.println("interne refuse : " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
