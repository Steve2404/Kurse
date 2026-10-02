package ch5_methods.projects.p02_bank.solution.core;

/**
 * SOLUTION - formatage des montants en centimes (une methode static publique, importee en static ailleurs).
 */
public class Money {

    // Les montants restent des long en CENTIMES : aucun double, donc aucune erreur d'arrondi ni de %f.
    public static String format(long cents) {
        String sign = cents < 0 ? "-" : "";
        long abs = Math.abs(cents);
        long rest = abs % 100;
        return sign + abs / 100 + "." + (rest < 10 ? "0" : "") + rest;
    }
}
