package ch12_modules.drills.r05_tools;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script recall.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/drills/r05_tools,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "recall.sh";

    static final String MODULES = "ch12_modules/drills/r05_tools";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- D01 describe-module java.sql",
            "exports java.sql",
            "exports javax.sql",
            "java.sql",
            "requires java.base mandated",
            "requires java.logging transitive",
            "requires java.transaction.xa transitive",
            "requires java.xml transitive",
            "uses java.sql.Driver",
            "--- D02 list-modules",
            "java.scripting",
            "java.se",
            "java.security.jgss",
            "java.security.sasl",
            "java.smartcardio",
            "java.sql",
            "java.sql.rowset",
            "--- D03 jdeps",
            "rapport du 2026-10-02 dans r.report",
            "r.report -> java.base",
            "r.report -> java.sql",
            "java.base,java.sql",
            "--- D04 jdk-internals",
            "cp -> java.base",
            "Peek -> sun.security.x509.X500Name JDK internal API (java.base)",
            "--- D05 jlink",
            "java.base",
            "java.logging",
            "java.sql",
            "java.transaction.xa",
            "java.xml",
            "r.report",
            "rapport du 2026-10-02 dans r.report",
            "--- D06 jmod",
            "",
            "contains r.report",
            "main-class r.report.Main",
            "r.report@3.1",
            "requires java.base mandated",
            "requires java.sql",
            "classes/module-info.class",
            "classes/r/report/Main.class",
            "JMOD format not supported at execution time",
            "rapport du 2026-10-02 dans r.report");
            // SCRIPT-END

    static final List<String> API = List.of(
            "requires java.sql;", "X500Name", "--describe-module java.sql", "--list-modules",
            "jdeps -s", "jdeps --print-module-deps", "--add-exports java.base/sun.security.x509=ALL-UNNAMED", "jdeps --jdk-internals",
            "jlink", "--compress=2", "jmod create", "--class-path",
            "jmod describe", "jmod list",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
