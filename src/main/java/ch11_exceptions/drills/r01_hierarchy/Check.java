package ch11_exceptions.drills.r01_hierarchy;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : NumberFormatException > IllegalArgumentException > RuntimeException > Exception > Throwable",
            "D02 : FileNotFoundException > IOException > Exception > Throwable",
            "D03 : StackOverflowError > VirtualMachineError > Error > Throwable",
            "D04 : verifiee, verifiee, non verifiee, non verifiee, erreur, verifiee",
            "D05 : ExceptionInInitializerError cause NumberFormatException ; NoClassDefFoundError",
            "D06 : ClassCastException true");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".getSuperclass()", "instanceof Error", "instanceof RuntimeException", "new ParseException(",
            "new DateTimeParseException(", "catch (ExceptionInInitializerError", "catch (NoClassDefFoundError", "catch (ClassCastException",
            "class Fragile", "Integer.parseInt(\"oops\")",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
