package ch11_exceptions.drills.solutions;

import ch11_exceptions.drills.Ledger;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.drills.exercises.Drill01_ExceptionBasics.
 */
public class SolutionDrill01_ExceptionBasics {

    public static class ValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ValidationException(String message) {
            super(message);
        }
    }

    public static int parseOrZero(String s) {
        // parseInt lance NumberFormatException (unchecked) : on l'attrape pour donner une valeur de secours.
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int sumValid() {
        // Le try/catch est DANS la boucle : une entree ratee n'arrete pas les suivantes ("" rate aussi).
        int sum = 0;
        for (String raw : Ledger.RAW) {
            try {
                sum += Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                // on ignore l'entree invalide
            }
        }
        return sum;
    }

    public static int countInvalid() {
        // Meme forme : on compte dans le catch.
        int invalid = 0;
        for (String raw : Ledger.RAW) {
            try {
                Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                invalid++;
            }
        }
        return invalid;
    }

    public static List<String> traceOrder() {
        // "after" n'est jamais ajoute : l'exception saute la fin du try ; finally passe apres le catch.
        List<String> trace = new ArrayList<>();
        try {
            trace.add("try");
            Integer.parseInt("x");
            trace.add("after");
        } catch (NumberFormatException e) {
            trace.add("catch");
        } finally {
            trace.add("finally");
        }
        return trace;
    }

    public static int lengthOrMinusOne(Object o) {
        // Deux types sans lien d'heritage : un seul bloc pour les deux.
        try {
            return ((String) o).length();
        } catch (ClassCastException | NullPointerException e) {
            return -1;
        }
    }

    public static IllegalArgumentException wrapped(String s) {
        // Le 2e argument du constructeur est la cause : getCause() rendra l'exception d'origine.
        try {
            Integer.parseInt(s);
            return new IllegalArgumentException("ok: " + s);
        } catch (NumberFormatException e) {
            return new IllegalArgumentException("bad: " + s, e);
        }
    }

    public static void checkAge(int age) throws ValidationException {
        // Une checked lancee doit etre declaree dans throws.
        if (age < 0) {
            throw new ValidationException("age < 0");
        }
    }

    public static String messageOf(Runnable action) {
        // getMessage rend le texte passe au constructeur.
        try {
            action.run();
            return "ok";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public static List<String> jdkNames() {
        // Les 5 exceptions unchecked les plus frequentes a l'examen, obtenues pour de vrai.
        Object text = "a";
        int[] one = new int[1];
        int zero = 0;
        return List.of(
                nameOf(() -> Integer.parseInt("x")),
                nameOf(() -> "abc".charAt(5)),
                nameOf(() -> System.out.print((Integer) text)),
                nameOf(() -> System.out.print(one[2])),
                nameOf(() -> System.out.print(1 / zero)));
    }

    public static boolean isChecked(Class<? extends Throwable> type) {
        // Checked = tout ce qui n'est ni sous RuntimeException ni sous Error (Exception et Throwable compris).
        return !RuntimeException.class.isAssignableFrom(type) && !Error.class.isAssignableFrom(type);
    }

    private static String nameOf(Runnable action) {
        // Boite magique : executer et rendre le nom simple de l'exception.
        try {
            action.run();
            return "aucune";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
