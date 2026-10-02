package ch12_modules.drills.r06_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script recall.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/drills/r06_kata,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "recall.sh";

    static final String MODULES = "ch12_modules/drills/r06_kata";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- D01",
            "k.core v1 | eludom | x=3 | k.core.model exporte true, ouvert true",
            "--- D02 java -d",
            "exports k.core",
            "k.core",
            "opens k.core.model",
            "qualified exports k.core.util to k.app",
            "--- D03 jar -c -f -e, puis jar -d -f",
            "",
            "contains k.app",
            "k.app",
            "main-class k.app.Main",
            "requires k.core",
            "k.core v1 | eludom | x=3 | k.core.model exporte true, ouvert true");
            // SCRIPT-END

    static final List<String> API = List.of(
            "exports k.core.util to k.app;", "opens k.core.model;", "requires k.core;", ".setAccessible(true)",
            ".isOpen(", "java -p", " -d k.core", "jar -c -f",
            " -e k.app.Main", "jar -d -f", "!--describe-module", "!--create",
            "!--main-class", "!--file",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
