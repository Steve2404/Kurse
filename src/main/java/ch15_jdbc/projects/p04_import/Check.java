package ch15_jdbc.projects.p04_import;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ImportApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "nettoyage : 13 lignes, invalides [ligne 4 : email sans @, ligne 9 : nom vide, ligne 11 : 4 champs], doublons [ANA@MAIL.FR]",
            "clients.csv : 9 inseres, lots [4, 4, 1], cles [1, 2, 3, 4, 5, 6, 7, 8, 9], rejets []",
            "delta.csv : ANNULE (strict), rejets [ben@mail.fr (23505), gus@mail.fr (23505)]",
            "  clients apres l'import strict : [clients=9]",
            "delta.csv : 3 inseres, lots [4, 1], cles [14, 16, 18], rejets [ben@mail.fr (23505), gus@mail.fr (23505)]",
            "lot mixte : [12, 3, 1]",
            "lot vide apres clearBatch : 0 ordre",
            "zoe : cle 19",
            "par ville : [LYON=6, PARIS=3, LILLE=1]",
            "journal : [clients.csv=9/0, delta.csv=3/2, nettoyage=0/0]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.SCHEMA", "Data.CHUNK", "Data.CLIENTS",
            "Data.DELTA", "record Customer(", ".addBatch()", ".addBatch(\"",
            ".executeBatch()", ".clearBatch()", "catch (BatchUpdateException", ".getUpdateCounts()",
            "Statement.EXECUTE_FAILED", "Statement.RETURN_GENERATED_KEYS", "new String[]{\"ID\"}", ".getGeneratedKeys()",
            ".setAutoCommit(false)", ".commit()", ".rollback()", "LinkedHashMap",
            ".putIfAbsent(", ".trim()", ".toLowerCase()", "finally",
            "Arrays.toString(",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ImportApp", args, EXPECTED, API);
    }
}
