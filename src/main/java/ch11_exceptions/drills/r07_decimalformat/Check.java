package ch11_exceptions.drills.r07_decimalformat;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 3.5 | 003.50 | 0.46 | 0.46",
            "D02 : 1,234,567 | 1,234,567.9 | 1,23,45,67",
            "D03 : 0.2 0.3 2 -2",
            "D04 : (1,234.50) | 7.9% | #7 | 12 pts",
            "D05 : 1.234,50 | #,##0.00 | 1234.5",
            "D06 : $005.10 | 1000.25 | 42");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new DecimalFormat(", "DecimalFormatSymbols.getInstance(", "\"###.##\"", "\"000.00\"",
            "\"#,##0.00;(#,##0.00)\"", "\"0.0%\"", ".toPattern()", ".setMinimumIntegerDigits(",
            ".parse(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
