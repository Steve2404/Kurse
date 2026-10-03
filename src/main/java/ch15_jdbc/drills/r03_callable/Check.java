package ch15_jdbc.drills.r03_callable;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [Ines=324, Lea=289, Max=900]",
            "D02 : 144 25",
            "D03 : SALUT! SALUT!!",
            "D04 : [Ines, Max]",
            "D05 : 1 ligne, Lea a 18 ans",
            "D06 : true true",
            "D07 : [90022, 90012]",
            "D08 : true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "public static int square(", "public static ResultSet adults(Connection conn", "Recall03.class.getName()", "CREATE ALIAS",
            "CallableStatement", ".prepareCall(", "{? = call SQUARE(?)}", "{call SHOUT(?)}",
            ".registerOutParameter(", "Types.INTEGER", "Types.VARCHAR", "ResultSet.TYPE_FORWARD_ONLY",
            "ResultSet.CONCUR_READ_ONLY", ".getType()", ".getConcurrency()", "isAssignableFrom(",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
