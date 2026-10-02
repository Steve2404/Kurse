package ch12_modules.drills.r01_directives;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script recall.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/drills/r01_directives,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "recall.sh";

    static final String MODULES = "ch12_modules/drills/r01_directives";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "[Bonjour Ada] secret 42",
            "--- d.base",
            "d.base",
            "exports d.base",
            "qualified exports d.base.hidden to d.friend",
            "--- d.mid",
            "d.mid",
            "exports d.mid",
            "requires d.base transitive",
            "--- d.friend",
            "d.friend",
            "exports d.friend",
            "opens d.friend",
            "requires d.mid",
            "--- d.plugin",
            "contains d.plugin",
            "d.plugin",
            "provides d.base.Greeter with d.plugin.Hello",
            "requires d.base",
            "--- d.app",
            "contains d.app",
            "d.app",
            "requires d.friend",
            "requires d.mid",
            "uses d.base.Greeter",
            "--- d.open",
            "d.open",
            "exports d.open");
            // SCRIPT-END

    static final List<String> API = List.of(
            "exports d.base.hidden to d.friend;", "requires transitive d.base;", "opens d.friend;", "provides d.base.Greeter with d.plugin.Hello;",
            "uses d.base.Greeter;", "open module d.open", "ServiceLoader.load(", "--describe-module",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
