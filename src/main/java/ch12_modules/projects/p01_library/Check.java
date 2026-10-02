package ch12_modules.projects.p01_library;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script build.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/p01_library,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "build.sh";

    static final String MODULES = "ch12_modules/p01_library";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "catalogue : 9 livres, par genre {ROMAN=3, SF=4, POLAR=1, ESSAI=1}",
            "par decennie : {1930=[Maigret], 1940=[L'Etranger, Le Mythe de Sisyphe, Le Petit Prince, La Peste], 1950=[Les Robots, Fondation], 1960=[Dune, La Nuit des temps]}",
            "titres en 'p' : [La Peste, Le Petit Prince], en 'n' : [La Nuit des temps]",
            "Camus : [La Peste, L'Etranger, Le Mythe de Sisyphe], cle de 'L'Etranger' : etranger",
            "modules : library.app, library.service, library.model ; nomme true",
            "library.service.internal exporte a library.app true, a library.model false, a tous false",
            "library.app lit library.model true (par transitivite), library.model lit library.app false",
            "--- describe-module library.service",
            "exports library.service",
            "library.service",
            "qualified exports library.service.internal to library.app",
            "requires java.base mandated",
            "requires library.model transitive",
            "--- describe-module du jar library.app",
            "",
            "contains library.app",
            "library.app",
            "main-class library.app.Main",
            "requires java.base mandated",
            "requires library.service",
            "--- depuis les jars",
            "catalogue : 9 livres, par genre {ROMAN=3, SF=4, POLAR=1, ESSAI=1}",
            "--- intrus",
            "error: package library.service.internal is not visible",
            "(package library.service.internal is declared in module library.service, which does not export it to module library.intruder)");
            // SCRIPT-END

    static final List<String> API = List.of(
            "module library.model", "exports library.model;", "requires transitive library.model;", "exports library.service.internal to library.app;",
            "requires library.service;", "module library.intruder", "record Book(", "enum Genre",
            ".subMap(", "Collectors.groupingBy(", ".getModule()", ".isExported(",
            ".canRead(", ".isNamed()", "javac -d", "--module-source-path",
            "-m library.app,library.service,library.model", "java -p", "-m library.app/library.app.Main", "--describe-module library.service",
            "jar --create", "--main-class library.app.Main", "jar --describe-module", "-m library.app |",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
