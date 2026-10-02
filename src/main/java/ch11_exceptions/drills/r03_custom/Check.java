package ch11_exceptions.drills.r03_custom;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : null | cle absente | java.lang.IllegalStateException: etat",
            "D02 : chargement de app.yml <- IOException: disque plein",
            "D03 : bas a la profondeur 2",
            "D04 : origine",
            "D05 : chargement de db.yml",
            "D06 : quota 120/100 ; depassement 20 ; true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "class ConfigException extends Exception", "super(cause)", "super(message, cause)", "class QuotaExceededException extends RuntimeException",
            "throws ConfigException", "catch (IOException", "throw e;", ".initCause(",
            ".getCause()",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
