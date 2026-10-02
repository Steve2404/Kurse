package ch11_exceptions.projects.p01_bank.solution;

import ch11_exceptions.projects.p01_bank.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 1 - le guichet : lancer, attraper, ordonner les catch, chainer, finally.
 */
public class Teller {

    static int processed;

    // Une seule operation. Elle peut lancer des exceptions VERIFIEES (BankException) : on les declare.
    static String execute(Bank bank, String operation) throws BankException {
        String[] p = operation.split(" ");
        switch (p[0]) {
            case "DEPOT" -> {
                Account a = bank.find(p[1]);
                a.deposit(Long.parseLong(p[2]));
                return "ok " + a.id() + " = " + Bank.money(a.balance());
            }
            case "RETRAIT" -> {
                Account a = bank.find(p[1]);                 // "RETRAIT" seul : p[1] -> ArrayIndexOutOfBoundsException
                a.withdraw(Long.parseLong(p[2]));
                return "ok " + a.id() + " = " + Bank.money(a.balance());
            }
            case "VIREMENT" -> {
                bank.transfer(p[1], p[2], Long.parseLong(p[3]));
                return "ok " + p[1] + " = " + Bank.money(bank.find(p[1]).balance()) + ", " + p[2] + " = " + Bank.money(bank.find(p[2]).balance());
            }
            case "PARTAGE" -> {
                return "ok " + bank.share(p[1], Integer.parseInt(p[2]));
            }
            default -> throw new IllegalArgumentException("operation inconnue : " + p[0]);
        }
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
        for (String line : Data.ACCOUNTS) {
            bank.open(line);
        }
        Map<String, Integer> errors = new TreeMap<>();
        for (String operation : Data.OPERATIONS) {
            String result;
            Exception failure = null;
            try {
                result = execute(bank, operation);
            } catch (InsufficientFundsException e) {          // les plus PRECISES d'abord...
                failure = e;
                result = "REFUS " + e.getMessage() + ", manque " + Bank.money(e.missing());
            } catch (TransferException e) {
                failure = e;
                result = "ECHEC " + e.getMessage() + " <- " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage();
            } catch (BankException e) {                       // ... puis leur mere
                failure = e;
                result = "REFUS " + e.getMessage();
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {   // multi-catch : types sans lien d'heritage
                failure = e;
                result = "MAL FORMEE " + e.getClass().getSimpleName() + ": " + e.getMessage();
            } catch (IllegalArgumentException e) {           // APRES NumberFormatException, qui en est une sous-classe
                failure = e;
                result = "INVALIDE " + e.getMessage();
            } catch (UnknownAccountException e) {
                failure = e;
                result = "INCONNU " + e.getMessage();
            } catch (RuntimeException e) {                    // le filet : toString = nom complet + message
                failure = e;
                result = "ERREUR " + e;
            } finally {
                processed++;                                  // execute dans TOUS les cas
            }
            if (failure != null) {
                errors.merge(failure.getClass().getSimpleName(), 1, Integer::sum);
            }
            System.out.println(operation + " -> " + result);
        }
        System.out.println("traitees " + processed + ", erreurs " + errors);

        // Une lambda (Consumer) ne peut pas laisser sortir une exception verifiee : on l'attrape DANS la lambda.
        List<String> unpaid = new ArrayList<>();
        bank.accounts().forEach(a -> {
            try {
                a.withdraw(Data.FEE);
            } catch (InsufficientFundsException e) {
                unpaid.add(a.id() + " manque " + Bank.money(e.missing()));
            } catch (BankException e) {
                unpaid.add(a.id() + " " + e.getMessage());
            }
        });
        StringBuilder balances = new StringBuilder();
        for (Account a : bank.accounts()) {
            balances.append(' ').append(a.id()).append('=').append(Bank.money(a.balance()));
        }
        System.out.println("frais : impayes " + unpaid + " ; soldes :" + balances);
    }
}
