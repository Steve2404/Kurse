package ch15_jdbc.drills.r02_prepared;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 3",
            "D02 : 1979 true java.sql.Date",
            "D03-D04 : [90008, 90012, Alien, 90012]",
            "D05 : 1 puis 0",
            "D06 : ? entre apostrophes 90008",
            "D06 : LIKE ? avec \"%li%\" Alien",
            "D07 : 1er ferme true, 2e Brazil",
            "D08 : Statement 2, PreparedStatement 0");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".prepareStatement(", ".setInt(", ".setString(", ".setDouble(",
            ".setBoolean(", ".setNull(", "Types.DOUBLE", ".setObject(",
            "LocalDate.class", ".getDate(", ".clearParameters()", "LIKE '%?%'",
            "LIKE ?", ".isClosed()", "try (PreparedStatement",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
