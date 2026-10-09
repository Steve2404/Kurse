package ch18_design.drills.r04_wrappers;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements PriceFeed", "Math.round(", "!extends", "!instanceof",
            "in:CachedFeed.java!OldExchange##seul l'adaptateur connait le fournisseur",
            "in:RetryFeed.java!OldExchange##seul l'adaptateur connait le fournisseur",
            "in:BestFeed.java=List<PriceFeed>##le composite contient des PriceFeed");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
