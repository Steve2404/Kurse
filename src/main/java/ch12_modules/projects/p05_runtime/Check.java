package ch12_modules.projects.p05_runtime;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script build.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/p05_runtime,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "build.sh";

    static final String MODULES = "ch12_modules/p05_runtime";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- execution sur le JDK complet",
            "reassort (budget 30) : [cafe, the, miel], cout 30, valeur 69",
            "journal : inv.core.Restock",
            "inv.app requiert [inv.core, java.base], inv.core requiert [java.base, java.logging]",
            "ordre de chargement : [java.base, java.logging, inv.core, inv.app]",
            "inv.app : paquets [inv.app], classe principale inv.app.Main, version 1.2",
            "--- jdeps",
            "inv.app -> inv.core",
            "inv.app -> java.base",
            "inv.core -> java.base",
            "inv.core -> java.logging",
            "inv.core,java.base",
            "--- modules de l'image (versions du JDK retirees)",
            "inv.app@1.2",
            "inv.core",
            "java.base",
            "java.logging",
            "--- lanceur",
            "reassort (budget 30) : [cafe, the, miel], cout 30, valeur 69");
            // SCRIPT-END

    static final List<String> API = List.of(
            "requires java.logging;", "module inv.app", "requires inv.core;", "Logger.getLogger(",
            "ModuleLayer.boot()", ".findModule(", ".getDescriptor()", ".requires()",
            ".packages()", ".mainClass()", ".version()", "new int[",
            "--module-version 1.2", "jdeps -s -R --module-path", "jdeps --print-module-deps", "jlink --module-path",
            "--add-modules inv.app", "--output", "--launcher inventaire=inv.app", "--strip-debug",
            "--no-header-files", "--no-man-pages", "/image/bin/java\" --list-modules", "/image/bin/inventaire\"",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
