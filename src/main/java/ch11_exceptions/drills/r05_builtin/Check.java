package ch11_exceptions.drills.r05_builtin;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : ArithmeticException: / by zero | ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3 | StringIndexOutOfBoundsException: String index out of range: 5",
            "D02 : NumberFormatException: For input string: \"12.5\" | IllegalArgumentException: count is negative: -1 | NegativeArraySizeException: -1",
            "D03 : ClassCastException | NullPointerException",
            "D04 : UnsupportedOperationException: null | NoSuchElementException: No value present | NoSuchElementException: null",
            "D05 : ArrayStoreException: java.lang.Integer | DateTimeException: Invalid date 'FEBRUARY 30'",
            "D06 : IllegalStateException: etat invalide | rien");
            // EXPECTED-END

    static final List<String> API = List.of(
            "catch (RuntimeException", "Integer.parseInt(\"12.5\")", ".repeat(-1)", "new int[-1]",
            "List.of(1).add(2)", "Optional.empty().get()", "new String[1]", "LocalDate.of(2026, 2, 30)",
            "throw new IllegalStateException(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
