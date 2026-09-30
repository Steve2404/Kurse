package ch11_exceptions.solutions;

import java.util.List;
import java.util.Map;

/**
 * Corrige de l'exercice 2.
 */
public class Solution02_CatchAndThrowsRules {

    static final Map<String, String> PARENT = Map.ofEntries(
            Map.entry("Exception", "Throwable"), Map.entry("Error", "Throwable"),
            Map.entry("StackOverflowError", "Error"), Map.entry("IOException", "Exception"),
            Map.entry("FileNotFoundException", "IOException"), Map.entry("SQLException", "Exception"),
            Map.entry("RuntimeException", "Exception"), Map.entry("IllegalArgumentException", "RuntimeException"),
            Map.entry("NumberFormatException", "IllegalArgumentException"), Map.entry("IllegalStateException", "RuntimeException"),
            Map.entry("ArithmeticException", "RuntimeException"), Map.entry("NullPointerException", "RuntimeException"));

    public static boolean isA(String a, String b) {
        // Donnee de l'exercice : remonter la chaine des parents de a.
        for (String t = a; t != null; t = PARENT.get(t)) {
            if (t.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChecked(String type) {
        // Piege : Exception et Throwable eux-memes sont checked ; seules les branches RuntimeException et Error ne le sont pas.
        return !isA(type, "RuntimeException") && !isA(type, "Error");
    }

    public static String catchChainError(List<String> thrown, List<String> catches) {
        // Un catch sous un ancetre deja present est inatteignable ; une checked precise doit pouvoir etre lancee
        // (dans un sens ou dans l'autre de l'heritage). Exception/Throwable attrapent aussi les unchecked : toujours permis.
        for (int i = 0; i < catches.size(); i++) {
            String type = catches.get(i);
            for (int j = 0; j < i; j++) {
                if (isA(type, catches.get(j))) {
                    return "exception " + type + " has already been caught";
                }
            }
            if (isChecked(type) && !type.equals("Exception") && !type.equals("Throwable") && !canBeThrown(type, thrown)) {
                return "exception " + type + " is never thrown in body of corresponding try statement";
            }
        }
        return "OK";
    }

    public static String multiCatchError(List<String> alternatives) {
        // Une alternative qui descend d'une autre est redondante : javac l'interdit (meme type deux fois aussi).
        for (int i = 0; i < alternatives.size(); i++) {
            for (int j = 0; j < alternatives.size(); j++) {
                if (i != j && isA(alternatives.get(i), alternatives.get(j))) {
                    return "Alternatives in a multi-catch statement cannot be related by subclassing";
                }
            }
        }
        return "OK";
    }

    public static String overrideError(List<String> parentThrows, List<String> childThrows) {
        // L'enfant ne peut pas elargir la promesse du parent pour les checked ; les unchecked ne sont jamais promises.
        for (String c : childThrows) {
            if (isChecked(c) && !coveredBy(c, parentThrows)) {
                return "overridden method does not throw " + c;
            }
        }
        return "OK";
    }

    public static String unreportedError(List<String> thrown, List<String> caught, List<String> declared) {
        // Regle "catch or declare" : seulement pour les checked ; un ancetre attrape ou declare suffit.
        for (String t : thrown) {
            if (isChecked(t) && !coveredBy(t, caught) && !coveredBy(t, declared)) {
                return "unreported exception " + t + "; must be caught or declared to be thrown";
            }
        }
        return "OK";
    }

    private static boolean coveredBy(String type, List<String> ancestors) {
        // Boite magique commune : une des exceptions de la liste est-elle type ou un de ses ancetres ?
        for (String a : ancestors) {
            if (isA(type, a)) {
                return true;
            }
        }
        return false;
    }

    private static boolean canBeThrown(String catchType, List<String> thrown) {
        // Le try lance t : t peut etre un catchType (catchType descend de t) ou en etre un (t descend de catchType).
        for (String t : thrown) {
            if (isA(t, catchType) || isA(catchType, t)) {
                return true;
            }
        }
        return false;
    }
}
