package ch8_lambdas.projects.p04_numeric;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON NumericApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "racine de 2 : dichotomie 1.414214 en 31 iterations, Newton 1.414214 en 5 iterations",
            "cos(x) = x : dichotomie 0.739085 en 30 iterations, Newton 0.739085 en 4 iterations",
            "integrales : x^2 sur [0,3] = 9.0, sin sur [0,pi] = 2.0, exp(-x^2) sur [-5,5] = 1.772454 (racine de pi 1.772454)",
            "maximum de -(x-1.5)^2+4 : x = 1.5 en 40 iterations ; derivee de sin en 0 = 1.0",
            "composition : 9.0 7.0 7.0 ; collatz(27) 111 pas, 22",
            "reductions : somme 455, max 120, pgcd 1, ppcm 60",
            "filtres : pairs 6, pairs et grands 4, premiers ou impairs 1, premiers <= 100 : 25",
            "voyelles : lambda=## fonction=### interface=#### java=## predicat=### 77 ; racine(49) 7.0, 20! 2432902008176640000",
            "monte-carlo : 15631 / 20000 -> pi ~ 3.1262, proche true ; hypot(3, 4) 5.0");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.NUMBERS", "Data.SAMPLES", "DoubleUnaryOperator", "static DoubleUnaryOperator derivative(",
            "IntBinaryOperator", "IntPredicate", "IntUnaryOperator", "ToIntFunction<String>",
            "IntFunction<String>", "IntToDoubleFunction", "IntToLongFunction", "ObjIntConsumer<StringBuilder>",
            "DoubleSupplier", "BooleanSupplier", "DoubleBinaryOperator", "Math::sin",
            "Integer::sum", "Numeric::gcd", ".applyAsDouble(", ".applyAsInt(",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "NumericApp", args, EXPECTED, API);
    }
}
