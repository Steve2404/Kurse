package ch15_jdbc.projects.p05_procedures;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON LoyaltyApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "alias : 6 procedures enregistrees",
            "cartes refusees par LUHN en SQL : [4539 1488 0343 6468, 1234 5678 9012 3456]",
            "OUT : POINTS(120, GOLD) = 41, POINTS(120, BRONZE) = 17",
            "OUT : LUHN(7992 7398 713) = true",
            "IN OUT : BRONZE -> SILVER -> GOLD -> GOLD",
            "CREDIT_PURCHASES : 6 achats credites ; {Ana=GOLD/53, Ben=SILVER/45, Cleo=BRONZE/36, Dan=BRONZE/6}",
            "TOP_CUSTOMERS(3) : [Ana=53, Ben=45, Cleo=36]",
            "PROMOTE(30) : 2 promus ; {Ana=GOLD/53, Ben=GOLD/45, Cleo=SILVER/36, Dan=BRONZE/6}",
            "procedure inconnue : 90022",
            "parametre oublie : 90012");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.SCHEMA", "Data.CUSTOMERS", "Data.PURCHASES",
            "public final class StoredProcs", "public static boolean luhn(", "StoredProcs.class.getName()", "CREATE ALIAS",
            "CallableStatement", ".prepareCall(", "{? = call", "{call",
            ".registerOutParameter(", "Types.INTEGER", "Types.BOOLEAN", "Types.VARCHAR",
            ".getBoolean(1)", "ResultSet.TYPE_FORWARD_ONLY", "ResultSet.CONCUR_READ_ONLY", "Connection conn, int",
            ".addBatch()", ".executeBatch()", "NEXT_TIER(tier)", "WHERE NOT LUHN(card)",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "LoyaltyApp", args, EXPECTED, API);
    }
}
