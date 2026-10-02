package ch11_exceptions.projects.p02_calculator;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Calculator, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "1 + 2 * 3 = 7",
            "-(2 + 3) * 4 % 7 = -6",
            "max(3, 9, 4) - abs(-12) + min(5, 2) = -1",
            "(4 + 6) / (5 - 5) -> evaluation impossible <- java.lang.ArithmeticException: / by zero",
            "2 * (3 + 4 -> syntaxe : ')' attendu (position 10)",
            "          ^",
            "7 $ 2 -> syntaxe : caractere inattendu '$' (position 2)",
            "  ^",
            "9223372036854775807 + 1 -> evaluation impossible <- java.lang.ArithmeticException: long overflow",
            "99999999999999999999 -> syntaxe : nombre trop grand (position 0) <- NumberFormatException",
            "^",
            "foo(1) + 2 -> syntaxe : fonction inconnue : foo (position 0)",
            "^",
            "min() -> evaluation impossible <- java.lang.IllegalArgumentException: min attend au moins 1 argument",
            "1 + 2 3 -> syntaxe : fin attendue (position 6)",
            "      ^",
            "abs(-9223372036854775807 - 1) -> evaluation impossible <- java.lang.ArithmeticException: Overflow to represent absolute value of Long.MIN_VALUE",
            "imbrication 100000 -> StackOverflowError (fille de VirtualMachineError)",
            "valides [7, -6, -1], somme 0",
            "lot interrompu : dans une lambda <- ')' attendu en position 6, resultats [42], evaluations 2",
            "evaluations 27, journal [EvaluationException, SyntaxException, SyntaxException, EvaluationException, SyntaxException, SyntaxException, EvaluationException, SyntaxException, EvaluationException]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.EXPRESSIONS", "Data.DEPTH", "Data.BATCH", "class SyntaxException extends Exception",
            "class EvaluationException extends RuntimeException", "record Token(", "interface ThrowingFunction<T, R>", "throws SyntaxException",
            "Math.addExact(", "Math.subtractExact(", "Math.multiplyExact(", "Math.negateExact(",
            "Math.absExact(", "catch (NumberFormatException", "catch (ArithmeticException | IllegalArgumentException", "finally",
            "throw e;", "catch (StackOverflowError", "catch (SyntaxException | EvaluationException", "Optional.empty()",
            "instanceof SyntaxException", "throw new RuntimeException(", ".getCause()",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Calculator", args, EXPECTED, API);
    }
}
