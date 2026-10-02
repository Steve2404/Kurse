package ch11_exceptions.drills.r10_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 10 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall10, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Ana a 7 ans, Ana ! | haha",
            "D02 : l'an 2,026 | lan {0} | {0} = x",
            "D03 : 3.1 | 3 | 25% | 1.234,5 | 1,234,567",
            "D04 : rouge M null Arial 1 [color, size]",
            "D05 : null 3 true",
            "D06 : Optional[42] Optional.empty 24");
            // EXPECTED-END

    static final List<String> API = List.of(
            "MessageFormat.format(", "new MessageFormat(", "{0,number,#.#}", "{0,number,integer}",
            "{1,number,percent}", "new Properties(defaults)", ".setProperty(", ".getProperty(",
            ".stringPropertyNames()", ".put(", "catch (NumberFormatException", "Optional.empty()",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall10", args, EXPECTED, API);
    }
}
