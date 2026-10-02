package ch8_lambdas.projects.p05_calculator;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Calculator, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "3 + 4 * 2  =>  3 4 2 * +  =  11",
            "( 1 + 2 ) * ( 3 + 4 )  =>  1 2 + 3 4 + *  =  21",
            "2 ^ 3 ^ 2  =>  2 3 2 ^ ^  =  512",
            "100 / 10 / 5  =>  100 10 / 5 /  =  2",
            "sqrt ( 16 ) + abs ( -3 )  =>  16 sqrt -3 abs +  =  7",
            "max ( 3 , 7 ) * 2  =>  3 7 max 2 *  =  14",
            "hyp ( x , y ) + z  =>  x y hyp z +  =  7",
            "sq ( x - y ) / neg ( z )  =>  x y - sq z neg /  =  -0.5",
            "min ( 2 ^ 10 , 1000 ) - 1  =>  2 10 ^ 1000 min 1 -  =  999",
            "references : ref VAR true 42 3.1416 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.EXPRESSIONS", "Data.VALUES", "Math::sqrt", "Math::pow",
            "Double::sum", "vars::lookup", "fmt::format", "String::strip",
            "Token::classify", "Token[]::new", "StringBuilder::new", "Token::new",
            "String::equalsIgnoreCase", "Integer::parseInt", "ToDoubleFunction<String>", "DoubleFunction<String>",
            "DoubleUnaryOperator[]", "DoubleBinaryOperator[]",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Calculator", args, EXPECTED, API);
    }
}
