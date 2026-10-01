package ch10_streams.drills.r12_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 12 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall12, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Fantasy 9.95",
            "D02 : Zola",
            "D03 : {1870=L'Assommoir, 1880=Germinal, 1930=Le Hobbit, 1950=Fondation}",
            "D04 : true",
            "D05 : Hyperion",
            "D06 : 4 15 76",
            "D07 : SF 355.0 / autres 444.8",
            "D08 : java=2, stream=2",
            "D09 : A",
            "D10 : [5, 8, 16, 17, 26, 28, 36, 43]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "groupingBy(", "partitioningBy(", "toMap(", ".skip(", "summaryStatistics()",
            "Map.Entry", ".limit(");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall12", args, EXPECTED, API);
    }
}
