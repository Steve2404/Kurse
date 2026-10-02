package ch11_exceptions.drills.r02_flow;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : try [A, B, D] | catch [A, C, D]",
            "D02 : finally avale l'exception",
            "D03 : x!",
            "D04 : t1 f1 c2:interne f2",
            "D05 : catch finally seconde",
            "D06 : 23");
            // EXPECTED-END

    static final List<String> API = List.of(
            "@SuppressWarnings(\"finally\")", "catch (IllegalStateException", "catch (RuntimeException", "continue;",
            "5xfinally",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
