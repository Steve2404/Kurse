package ch15_jdbc.drills.r05_batch_meta;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [1, 1, 1] cles [1, 2, 3]",
            "D02 : [2, 0, 1]",
            "D03 : [1, -3, 1], refusee en position 1",
            "D04 : 0 ordre envoye",
            "D05 : cle 8",
            "D06 : 3 [TACHE/LABEL/CHARACTER VARYING, DONE/DONE/BOOLEAN, RANG/RANG/INTEGER]",
            "D07 : H2 1 table [ID, LABEL, DONE]",
            "D08 : true 23505 23505",
            "D08 : true 42001 true");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".addBatch()", ".addBatch(\"", ".executeBatch()", ".clearBatch()",
            "catch (BatchUpdateException", ".getUpdateCounts()", "Statement.EXECUTE_FAILED", "Statement.RETURN_GENERATED_KEYS",
            "new String[]{\"ID\"}", ".getGeneratedKeys()", "ResultSetMetaData", ".getColumnLabel(",
            ".getColumnName(", ".getColumnTypeName(", "DatabaseMetaData", ".getColumns(",
            ".getTables(", "SQLIntegrityConstraintViolationException", "SQLSyntaxErrorException", ".getErrorCode()",
            ".getNextException()",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
