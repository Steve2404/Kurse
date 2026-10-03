package ch15_jdbc.drills.r07_bonus;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (bonus) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [org.h2.Driver true, org.postgresql.Driver true, com.mysql.cj.jdbc.Driver true]",
            "D01 : oracle 08001",
            "D02 : [com.mysql.cj.jdbc.Driver, org.h2.Driver, org.postgresql.Driver]",
            "D03 : SA",
            "D04 : [last Dan row 4, absolute(2) Ana, relative(-1) Cleo, absolute(-2) Ben, previous Dan, beforeFirst true]",
            "D05 : [Ana=45, Ben=25, Cleo=60, Eve=33]",
            "D06 : READ_COMMITTED par defaut true, SERIALIZABLE supporte true, actif true",
            "D07 : maxRows 2, lignes lues 2, timeout 5 s",
            "D08 : warnings true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "DriverManager.getDriver(", ".acceptsURL(", "DriverManager.drivers()", "Properties",
            ".setProperty(\"user\"", "ResultSet.TYPE_SCROLL_INSENSITIVE", "ResultSet.CONCUR_UPDATABLE", ".last()",
            ".absolute(", ".relative(", ".previous()", ".afterLast()",
            ".beforeFirst()", ".getRow()", ".updateInt(", ".updateRow()",
            ".moveToInsertRow()", ".insertRow()", ".deleteRow()", "Connection.TRANSACTION_SERIALIZABLE",
            ".setTransactionIsolation(", ".supportsTransactionIsolationLevel(", ".setMaxRows(", ".setQueryTimeout(",
            ".getWarnings()", "static void crud(String url)",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
