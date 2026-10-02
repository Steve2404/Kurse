package ch11_exceptions.projects.p04_vm;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON VmLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "calcul : affiche [42], pile [42] (5 pas)",
            "division rattrapee : affiche [-2], pile [-2] (7 pas)",
            "throw imbrique : affiche [7, 9], pile [9] (10 pas)",
            "compte a rebours : affiche [3, 2, 1], pile [0] (23 pas)",
            "pile vide : erreur non rattrapee <- StackUnderflowException pile vide (instruction 1) ; affiche [], pile [] (2 pas)",
            "throw perdu : erreur non rattrapee <- ThrownException code 4 ; affiche [], pile [2] (2 pas)",
            "boucle infinie : arret StepLimitException plus de 50 pas",
            "instruction inconnue : arret IllegalArgumentException instruction inconnue : SQUARE",
            "pile vide rattrapee : affiche [-1], pile [-1] (7 pas)",
            "finally : finallyWins 2, valueAlreadyComputed 1, retour du catch [try, catch, finally]",
            "masquee : IllegalStateException (lancee dans finally), cause null, supprimees 0");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.PROGRAMS", "Data.MAX_STEPS", "abstract class VmException extends Exception", "extends VmException",
            "class StepLimitException extends RuntimeException", "record Handler(", "Deque<Handler>", "throws VmException",
            "catch (VmException | ArithmeticException", "throw new UncaughtVmException(", "instanceof VmException", "catch (StepLimitException | IllegalArgumentException",
            "catch (UncaughtVmException", "2x@SuppressWarnings(\"finally\")", "3xfinally", ".getSuppressed()",
            ".getCause()",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "VmLab", args, EXPECTED, API);
    }
}
