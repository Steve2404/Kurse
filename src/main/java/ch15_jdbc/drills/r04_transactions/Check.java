package ch15_jdbc.drills.r04_transactions;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : autoCommit true, l'autre voit 11",
            "D02 : moi 20, l'autre avant commit 11, apres 20",
            "D03 : apres rollback 20",
            "D04 : 15 (nom moitie)",
            "D05 : 15",
            "D06 : rollback(libere) 90063, getSavepointName(anonyme) SQLException",
            "D07 : 23513, toujours 7 dans la transaction",
            "D08 : l'autre voit 7");
            // EXPECTED-END

    static final List<String> API = List.of(
            "2xDriverManager.getConnection(", ".setAutoCommit(false)", ".setAutoCommit(true)", ".commit()",
            ".rollback()", "Savepoint", ".setSavepoint(\"moitie\")", ".setSavepoint()",
            ".rollback(half)", ".releaseSavepoint(", ".getSavepointName()",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
