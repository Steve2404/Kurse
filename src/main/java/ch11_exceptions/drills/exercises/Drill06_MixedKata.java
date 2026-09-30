package ch11_exceptions.drills.exercises;

import ch11_exceptions.ExerciseChecker;
import ch11_exceptions.drills.Ledger;

import java.util.List;
import java.util.Locale;

/**
 * DRILL 06 - Kata melange : tout le chapitre 11 sans indice de forme
 * ==================================================================
 *
 * Mode d'emploi : voir Drill01_ExceptionBasics. Ici, PAS de crochet : a
 * toi de choisir la construction. Fais ce drill seulement quand les
 * drills 01 a 05 passent.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : validNumbers(raw)        les entrees de raw qui sont des entiers, dans l'ordre -> [12, -3, 7] pour Ledger.RAW.
 * TODO 2  : firstErrorMessage(raw)   le message de la 1re erreur de conversion -> For input string: "x".
 * TODO 3  : total(locale)            la somme des nombres valides de RAW, en devise de la Locale -> $16.00 (US).
 * TODO 4  : stamp()                  Ledger.WHEN au format annee-mois-jour heure:minute -> 2024-03-07 14:05.
 * TODO 5  : errorLine(locale, raw)   le message "invalidRecord" du bundle, rempli avec raw.
 * TODO 6  : report(locale)           une ligne par entree invalide de RAW (errorLine), dans l'ordre.
 * TODO 7  : closingLog()             deux ressources A puis B ECRITES EN LAMBDA (AutoCloseable est une interface
 *                                    fonctionnelle) qui ajoutent "close A" / "close B" au log ; le bloc lance
 *                                    IllegalStateException("boom") ; rendre le log + "caught boom" -> [close B, close A, caught boom].
 * TODO 8  : describe(t)              "Nom(message)" pour t et chacune de ses causes, separes par " <- ".
 * TODO 9  : checkedNames(types)      les noms simples des types CHECKED parmi types, dans l'ordre.
 * TODO 10 : safeDivide(a, b)         a / b ; b == 0 -> lancer IllegalArgumentException("b == 0") avec l'ArithmeticException en cause.
 */
public class Drill06_MixedKata {

    public static List<Integer> validNumbers(List<String> raw) {
        throw new UnsupportedOperationException("TODO 1 : implementer validNumbers()");
    }

    public static String firstErrorMessage(List<String> raw) {
        throw new UnsupportedOperationException("TODO 2 : implementer firstErrorMessage()");
    }

    public static String total(Locale locale) {
        throw new UnsupportedOperationException("TODO 3 : implementer total()");
    }

    public static String stamp() {
        throw new UnsupportedOperationException("TODO 4 : implementer stamp()");
    }

    public static String errorLine(Locale locale, String raw) {
        throw new UnsupportedOperationException("TODO 5 : implementer errorLine()");
    }

    public static List<String> report(Locale locale) {
        throw new UnsupportedOperationException("TODO 6 : implementer report()");
    }

    public static List<String> closingLog() {
        throw new UnsupportedOperationException("TODO 7 : implementer closingLog()");
    }

    public static String describe(Throwable t) {
        throw new UnsupportedOperationException("TODO 8 : implementer describe()");
    }

    public static List<String> checkedNames(List<Class<? extends Throwable>> types) {
        throw new UnsupportedOperationException("TODO 9 : implementer checkedNames()");
    }

    public static int safeDivide(int a, int b) {
        throw new UnsupportedOperationException("TODO 10 : implementer safeDivide()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  validNumbers(RAW) == [12, -3, 7]", validNumbers(Ledger.RAW).equals(List.of(12, -3, 7)));
        ExerciseChecker.check("2  firstErrorMessage(RAW) == For input string: \"x\"", firstErrorMessage(Ledger.RAW).equals("For input string: \"x\""));
        ExerciseChecker.check("3  total(US) == $16.00 ; total(GERMANY) == 16,00\\u00a0EUR",
                total(Locale.US).equals("$16.00") && total(Locale.GERMANY).equals("16,00 €"));
        ExerciseChecker.check("4  stamp == 2024-03-07 14:05", stamp().equals("2024-03-07 14:05"));
        ExerciseChecker.check("5  errorLine(FRENCH, x) == Invalid record: x", errorLine(Locale.FRENCH, "x").equals("Invalid record: x"));
        ExerciseChecker.check("6  report(US) == [Invalid record: x, Invalid record: ]",
                report(Locale.US).equals(List.of("Invalid record: x", "Invalid record: ")));
        ExerciseChecker.check("7  closingLog == [close B, close A, caught boom]",
                closingLog().equals(List.of("close B", "close A", "caught boom")));
        ExerciseChecker.check("8  describe == IllegalStateException(service) <- NumberFormatException(x)",
                describe(new IllegalStateException("service", new NumberFormatException("x")))
                        .equals("IllegalStateException(service) <- NumberFormatException(x)"));
        ExerciseChecker.check("9  checkedNames == [IOException, Exception]",
                checkedNames(List.of(java.io.IOException.class, IllegalStateException.class, Exception.class, OutOfMemoryError.class))
                        .equals(List.of("IOException", "Exception")));
        String divide = "rien";
        try {
            safeDivide(7, 0);
        } catch (IllegalArgumentException e) {
            divide = e.getMessage() + "/" + e.getCause().getClass().getSimpleName();
        }
        ExerciseChecker.check("10 safeDivide(7, 2) == 3 ; safeDivide(7, 0) -> b == 0 / ArithmeticException",
                safeDivide(7, 2) == 3 && divide.equals("b == 0/ArithmeticException"));

        ExerciseChecker.summary();
    }
}
