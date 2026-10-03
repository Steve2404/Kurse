package ch15_jdbc.drills.r06_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (test final) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [ouvert connexion>statement>resultset, resultset, statement, connexion]",
            "D02 : [1, 2, 3, 4, 5]",
            "D03 : [3, 4]",
            "D04 : [java 3/3/12, sql 2/1/4]",
            "D05 : 3 17",
            "D06 : 3 supprimees puis restaurees, il reste 3",
            "D07 : 4 colonnes, STARS, savepoints true",
            "D08 : 42S22, stars 5 wasNull false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements AutoCloseable", "public static int words(", "CREATE ALIAS WORDS", ".setAutoCommit(false)",
            ".addBatch()", ".executeBatch()", ".getGeneratedKeys()", "LIMIT ? OFFSET ?",
            "GROUP BY", "{? = call WORDS(?)}", ".setSavepoint(", ".rollback(sp)",
            ".getColumnCount()", ".supportsSavepoints()", ".wasNull()",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
