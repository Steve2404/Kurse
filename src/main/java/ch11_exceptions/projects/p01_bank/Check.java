package ch11_exceptions.projects.p01_bank;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Teller, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "DEPOT A1 2500 -> ok A1 = 525.00",
            "RETRAIT B2 20000 -> REFUS solde insuffisant sur B2, manque 80.00",
            "VIREMENT A1 B2 10000 -> ok A1 = 425.00, B2 = 220.00",
            "RETRAIT Z9 100 -> INCONNU compte inconnu : Z9",
            "DEPOT A1 -5 -> INVALIDE montant invalide : -5",
            "DEPOT A1 12x -> MAL FORMEE NumberFormatException: For input string: \"12x\"",
            "RETRAIT C3 100 -> REFUS compte gele : C3",
            "VIREMENT B2 C3 5000 -> ECHEC virement B2->C3 annule <- IllegalStateException: compte gele : C3",
            "VIREMENT D4 A1 999999 -> ECHEC virement D4->A1 refuse <- InsufficientFundsException: solde insuffisant sur D4",
            "PARTAGE A1 0 -> ERREUR java.lang.ArithmeticException: / by zero",
            "PARTAGE A1 3 -> ok 3 parts de 141.66 (reste 0.02)",
            "RETRAIT -> MAL FORMEE ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1",
            "BONUS A1 100 -> INVALIDE operation inconnue : BONUS",
            "traitees 13, erreurs {ArithmeticException=1, ArrayIndexOutOfBoundsException=1, FrozenAccountException=1, IllegalArgumentException=2, InsufficientFundsException=1, NumberFormatException=1, TransferException=2, UnknownAccountException=1}",
            "frais : impayes [C3 compte gele : C3, D4 manque 2.00] ; soldes : A1=415.00 B2=210.00 C3=300.00 D4=8.00");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.ACCOUNTS", "Data.OPERATIONS", "Data.FEE", "class BankException extends Exception",
            "extends BankException", "extends RuntimeException", "super(message, cause)", "throws BankException",
            "throws InsufficientFundsException", "throws FrozenAccountException", "throws TransferException", "throw new IllegalArgumentException(",
            "throw new IllegalStateException(", "catch (InsufficientFundsException", "catch (TransferException", "catch (BankException",
            "catch (NumberFormatException | ArrayIndexOutOfBoundsException", "catch (IllegalArgumentException", "catch (UnknownAccountException", "catch (RuntimeException",
            "catch (IllegalStateException", "finally", ".getCause()", ".getMessage()",
            ".missing()",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Teller", args, EXPECTED, API);
    }
}
