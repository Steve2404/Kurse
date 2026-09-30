package ch11_exceptions.solutions;

/**
 * Corrige de l'exercice 4.
 */
public class Solution04_FinallyFlowRules {

    public static String predict(boolean tryThrows, boolean catchMatches, boolean catchThrows, boolean finallyReturns) {
        // finally passe toujours (F) ; un return dans finally ecrase meme une exception en vol : on le teste en premier.
        boolean enteredCatch = tryThrows && catchMatches;
        String trace = "T" + (enteredCatch ? "C" : "") + "F";
        String outcome;
        if (finallyReturns) {
            outcome = "return finally";
        } else if (!tryThrows) {
            outcome = "return try";
        } else if (!catchMatches) {
            outcome = "throws IllegalArgumentException";
        } else if (catchThrows) {
            outcome = "throws ArithmeticException";
        } else {
            outcome = "return catch";
        }
        return trace + " -> " + outcome;
    }

    public static String returnedAfterFinally(String kind) {
        // return copie la valeur (ou la reference) avant finally : seul un objet modifie sur place change le resultat.
        switch (kind) {
            case "int":
                return "1";
            case "StringBuilder":
                return "ab";
            default:
                return "a";
        }
    }
}
