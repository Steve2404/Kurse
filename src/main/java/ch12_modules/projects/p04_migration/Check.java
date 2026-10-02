package ch12_modules.projects.p04_migration;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script build.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/p04_migration,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "build.sh";

    static final String MODULES = "ch12_modules/p04_migration";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- describe-module des jars",
            "",
            "",
            "No module descriptor found. Derived automatic module.",
            "acme.text@2.1 automatic",
            "contains com.acme.text",
            "requires java.base mandated",
            "",
            "",
            "No module descriptor found. Derived automatic module.",
            "com.acme.utils automatic",
            "contains com.acme.utils",
            "requires java.base mandated",
            "--- classpath",
            "classpath : bonjour-le-monde ; module nomme false, nom null",
            "--- javac sans module path",
            "error: module not found: acme.text",
            "error: module not found: com.acme.utils",
            "--- module path",
            "slugs : [l-ete-a-paris, java-17-les-modules, l-ete-a-paris-2, ete-a-paris, les-modules-enfin]",
            "courts : Les modules~ | JPMS",
            "Main -> module blog.app, automatique false, lit le module sans nom false",
            "Slugify -> module acme.text, automatique true, lit le module sans nom true",
            "Strings -> module com.acme.utils, automatique true, lit le module sans nom true",
            "--- cycle",
            "error: cyclic dependence involving cycle.a",
            "error: cyclic dependence involving cycle.b",
            "--- jdeps",
            "acme-text-2.1.jar -> java.base",
            "blog.app -> acme.text",
            "blog.app -> com.acme.utils",
            "blog.app -> java.base");
            // SCRIPT-END

    static final List<String> API = List.of(
            "module blog.app", "requires acme.text;", "requires com.acme.utils;", "Automatic-Module-Name",
            "module cycle.a", "requires cycle.b;", "requires cycle.a;", ".isAutomatic()",
            ".getUnnamedModule()", ".merge(", "acme-text-2.1.jar", "old_utils.jar",
            "--manifest", "jar -J-Duser.language=en --describe-module", "java -cp", "com.acme.demo.Demo",
            "-p \"$OUT/jars\" --module-source-path", "-m cycle.a,cycle.b", "jdeps -s", "jdeps -s --module-path",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
