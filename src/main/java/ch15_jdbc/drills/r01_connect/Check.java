package ch15_jdbc.drills.r01_connect;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : jdbc:h2:mem:r01 autoCommit true closed false",
            "D02 : false 3",
            "D03 : [Rex 7, Tom 3, Kiki ?]",
            "D04 : String Integer null",
            "D05 : true -1 3 / false 2",
            "D06 : [02000, 90008, 42S02, 23505]",
            "D07 : 08001",
            "D08 : connexion fermee true, ResultSet ferme true",
            "D08 : Statement apres fermeture 90007");
            // EXPECTED-END

    static final List<String> API = List.of(
            "DriverManager.getConnection(", ".getMetaData().getURL()", ".getAutoCommit()", ".isClosed()",
            ".createStatement()", ".execute(", ".executeUpdate(", ".executeQuery(",
            "rs.next()", ".wasNull()", ".getObject(", ".getUpdateCount()",
            ".getResultSet()", "catch (SQLException", ".getSQLState()",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
