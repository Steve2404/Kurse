package ch12_modules.drills.r02_commands;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script recall.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/drills/r02_commands,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "recall.sh";

    static final String MODULES = "ch12_modules/drills/r02_commands";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- D01",
            "c.app",
            "c.lib",
            "--- D02",
            "somme 5 dans c.app",
            "somme 5 dans c.app",
            "--- D03",
            "somme 5 dans c.app",
            "--- D04",
            "META-INF/",
            "META-INF/MANIFEST.MF",
            "module-info.class",
            "c/",
            "c/app/",
            "c/app/Main.class",
            "--- D05",
            "c.app@2.0",
            "c.lib",
            "--- D06",
            "classpath : somme 15, module sans nom true",
            "--- D07",
            "root c.app",
            "c.app requires c.lib",
            "--- D08",
            "c.app -> c.lib",
            "c.app -> java.base");
            // SCRIPT-END

    static final List<String> API = List.of(
            "module c.lib", "requires c.lib;", "--module-source-path", "java -p",
            "java --module-path", "--module c.app/c.app.Main", "--module-version 2.0", "--main-class c.app.Main",
            "jar --list --file", "--list-modules", "--add-modules c.lib", "-cp",
            "--show-module-resolution", "jdeps -s",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
