package ch12_modules.drills.r03_services;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script recall.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/drills/r03_services,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "recall.sh";

    static final String MODULES = "ch12_modules/drills/r03_services";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- D02 tous",
            "formes (cote 6) : carre=36 triangle=18 ; types [Shape, Square] ; findFirst present true",
            "--- D03 carre seul",
            "formes (cote 6) : carre=36 ; types [Square] ; findFirst present true",
            "--- D04 aucun fournisseur",
            "formes (cote 6) : aucune ; types [] ; findFirst present false",
            "--- D05",
            "contains s.triangle",
            "provides s.api.Shape with s.triangle.TriangleFactory",
            "requires s.api",
            "s.triangle",
            "exports s.locator",
            "requires s.api transitive",
            "s.locator",
            "uses s.api.Shape");
            // SCRIPT-END

    static final List<String> API = List.of(
            "requires transitive s.api;", "uses s.api.Shape;", "provides s.api.Shape with s.square.Square;", "provides s.api.Shape with s.triangle.TriangleFactory;",
            "public static Shape provider()", "ServiceLoader.load(", ".findFirst()", ".type()",
            "--limit-modules s.app,s.square", "--limit-modules s.app", "--describe-module s.triangle",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
