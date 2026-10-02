package ch12_modules.drills.r04_types;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script recall.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/drills/r04_types,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "recall.sh";

    static final String MODULES = "ch12_modules/drills/r04_types";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- D01 noms automatiques",
            "math-utils-3.0.jar -> math.utils@3.0 automatic",
            "string_tools.jar -> string.tools automatic",
            "parser2-1.0.0-SNAPSHOT.jar -> parser2@1.0.0-SNAPSHOT automatic",
            "my.cool.lib.jar -> my.cool.lib automatic",
            "--- D02 paquet partage",
            "ResolutionException : paquet  dans deux modules",
            "--- D03 module nomme -> module automatique",
            "AUTO! module math.utils automatique true, exporte t.util true, lit le module sans nom true, t.app le lit true",
            "--- D04 Automatic-Module-Name",
            "org.acme.tools@9.9 automatic");
            // SCRIPT-END

    static final List<String> API = List.of(
            "requires math.utils;", "Automatic-Module-Name: org.acme.tools", ".isAutomatic()", ".getUnnamedModule()",
            "math-utils-3.0.jar", "string_tools.jar", "parser2-1.0.0-SNAPSHOT.jar", "my.cool.lib.jar",
            "--add-modules ALL-MODULE-PATH", "--manifest",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
