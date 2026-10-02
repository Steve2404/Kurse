package ch8_lambdas.projects.p07_orders;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Engine, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "P1 alice : 44.30 -gold-> 37.65 -[TEN puis MINUS5]-> 28.88 + port 5.90 = 34.78",
            "P2 bob : 52.70 -silver-> 47.43 -[TEN puis MINUS5]-> 37.69 + port 7.90 = 45.59",
            "P3 chloe : REJETEE (not-empty)",
            "P4 dan : REJETEE (max-qty 30)",
            "P5 eve : REJETEE (known-skus)",
            "P6 fred : 26.70 -none-> 26.70 -[TEN]-> 24.03 + port 6.90 = 30.93",
            "P7 gina : 136.50 -silver-> 122.85 -[TEN puis BIG]-> 88.45 + port 0.00 = 88.45",
            "acceptees 4/7 | journal : P1 acceptee 34.78 ; P2 acceptee 45.59 ; P3 rejetee (not-empty) ; P4 rejetee (max-qty 30) ; P5 rejetee (known-skus) ; P6 acceptee 30.93 ; P7 acceptee 88.45 ;",
            "currying : or(10000) 8500, -10% puis -5.00 8500, -5.00 puis -10% 8550, identite 10000");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.CATALOG", "Data.PURCHASES", "Data.PROMOS", "Function<Integer, LongUnaryOperator>",
            "percent -> amount ->", "LongUnaryOperator.identity()", "LongPredicate", "LongSupplier",
            "ToLongFunction<Purchase>", "BiFunction<String, Integer, Long>", "interface Validation extends Predicate<Purchase>", "promos[i]::apply",
            "audit::append", ".andThen(", ".compose(",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Engine", args, EXPECTED, API);
    }
}
