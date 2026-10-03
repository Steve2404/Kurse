package ch14_io.drills.r01_paths;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : a/b/c/d.txt d.txt a/b/c null 4 a b/c",
            "D02 : x/z ../../b a true",
            "D03 : projet/src/Main.java projet/test projet/src true",
            "D04 : ../c/d ../../b true",
            "D05 : true true true true false false true",
            "D06 : fichier.txt dossier false false dossier/fichier.txt fichier.txt");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Path.of(", "Paths.get(", ".getFileName()", ".getParent()",
            ".getRoot()", ".getNameCount()", ".subpath(", ".normalize()",
            ".resolve(", ".resolveSibling(", ".relativize(", ".startsWith(",
            ".endsWith(", "new File(", ".toPath()", ".toFile()",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
