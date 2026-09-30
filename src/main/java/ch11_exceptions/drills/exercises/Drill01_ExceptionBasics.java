package ch11_exceptions.drills.exercises;

import ch11_exceptions.ExerciseChecker;
import ch11_exceptions.drills.Ledger;

import java.io.IOException;
import java.util.List;

/**
 * DRILL 01 - try / catch / finally, multi-catch, cause, les exceptions du JDK
 * ===========================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch11_exceptions.drills.Ledger.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : parseOrZero(s)        [try/catch NumberFormatException] Integer.parseInt, 0 si ca rate.
 * TODO 2  : sumValid()            [try/catch dans une boucle] la somme des nombres valides de RAW -> 16.
 * TODO 3  : countInvalid()        [meme forme] combien d'entrees de RAW ne sont pas des nombres -> 2.
 * TODO 4  : traceOrder()          [finally] ajouter "try", parser "x", ajouter "after" ; catch -> "catch" ; finally -> "finally".
 * TODO 5  : lengthOrMinusOne(o)   [multi-catch ClassCastException | NullPointerException] ((String) o).length(), -1 si ca rate.
 * TODO 6  : wrapped(s)            [cause] RENDRE (pas lancer) une IllegalArgumentException("bad: " + s) dont la cause est la NumberFormatException.
 * TODO 7  : checkAge(age)         [throw d'une checked + throws] age < 0 -> ValidationException("age < 0").
 * TODO 8  : messageOf(action)     [getMessage] lancer action ; rendre le message de la RuntimeException, ou "ok".
 * TODO 9  : jdkNames()            [les exceptions du JDK] le nom simple de ce que lancent 5 lignes classiques (voir main).
 * TODO 10 : isChecked(type)       [RuntimeException / Error] une Class d'exception est-elle checked ?
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Throwable -> Error (unchecked) | Exception (checked) -> RuntimeException (unchecked)
 *   checked : catch OU throws obligatoire ; throw new X(...) ; methode ... throws X
 *   catch du plus precis au plus general ; multi-catch (A | B e) : A et B sans lien d'heritage, e implicitement final
 *   finally s'execute toujours (sauf System.exit)
 *   new X(message, cause) ; getMessage() ; getCause() ; getClass().getSimpleName()
 *   Integer.parseInt("x") -> NumberFormatException ; "abc".charAt(5) -> StringIndexOutOfBoundsException
 *   (Integer) objetString -> ClassCastException ; tab[2] sur new int[1] -> ArrayIndexOutOfBoundsException
 *   1 / 0 -> ArithmeticException ; methode sur null -> NullPointerException
 *   X.class est checked <=> ni RuntimeException.class.isAssignableFrom(X.class), ni Error.class...
 * ---------------------------------------------------------------------
 */
public class Drill01_ExceptionBasics {

    public static class ValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ValidationException(String message) {
            super(message);
        }
    }

    public static int parseOrZero(String s) {
        throw new UnsupportedOperationException("TODO 1 : implementer parseOrZero()");
    }

    public static int sumValid() {
        throw new UnsupportedOperationException("TODO 2 : implementer sumValid()");
    }

    public static int countInvalid() {
        throw new UnsupportedOperationException("TODO 3 : implementer countInvalid()");
    }

    public static List<String> traceOrder() {
        throw new UnsupportedOperationException("TODO 4 : implementer traceOrder()");
    }

    public static int lengthOrMinusOne(Object o) {
        throw new UnsupportedOperationException("TODO 5 : implementer lengthOrMinusOne()");
    }

    public static IllegalArgumentException wrapped(String s) {
        throw new UnsupportedOperationException("TODO 6 : implementer wrapped()");
    }

    public static void checkAge(int age) throws ValidationException {
        throw new UnsupportedOperationException("TODO 7 : implementer checkAge()");
    }

    public static String messageOf(Runnable action) {
        throw new UnsupportedOperationException("TODO 8 : implementer messageOf()");
    }

    public static List<String> jdkNames() {
        throw new UnsupportedOperationException("TODO 9 : implementer jdkNames()");
    }

    public static boolean isChecked(Class<? extends Throwable> type) {
        throw new UnsupportedOperationException("TODO 10 : implementer isChecked()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  parseOrZero(42) == 42 ; (x) == 0", parseOrZero("42") == 42 && parseOrZero("x") == 0);
        ExerciseChecker.check("2  sumValid == 16", sumValid() == 16);
        ExerciseChecker.check("3  countInvalid == 2", countInvalid() == 2);
        ExerciseChecker.check("4  traceOrder == [try, catch, finally]", traceOrder().equals(List.of("try", "catch", "finally")));
        ExerciseChecker.check("5  lengthOrMinusOne(abc) == 3 ; (42) == -1 ; (null) == -1",
                lengthOrMinusOne("abc") == 3 && lengthOrMinusOne(42) == -1 && lengthOrMinusOne(null) == -1);
        IllegalArgumentException w = wrapped("x");
        ExerciseChecker.check("6  wrapped(x) : message bad: x, cause NumberFormatException",
                w.getMessage().equals("bad: x") && w.getCause() instanceof NumberFormatException);
        String age = "rien";
        try {
            checkAge(30);
            checkAge(-1);
        } catch (ValidationException e) {
            age = e.getMessage();
        }
        ExerciseChecker.check("7  checkAge(30) passe, checkAge(-1) -> age < 0", age.equals("age < 0"));
        ExerciseChecker.check("8  messageOf(boom) == boom ; messageOf(rien) == ok",
                messageOf(() -> { throw new IllegalStateException("boom"); }).equals("boom") && messageOf(() -> { }).equals("ok"));
        ExerciseChecker.check("9  jdkNames : parseInt(x), charAt(5), (Integer) \"a\", new int[1][2], 1 / 0",
                jdkNames().equals(List.of("NumberFormatException", "StringIndexOutOfBoundsException", "ClassCastException",
                        "ArrayIndexOutOfBoundsException", "ArithmeticException")));
        ExerciseChecker.check("10 isChecked : IOException oui, Exception oui ; IllegalStateException non, StackOverflowError non",
                isChecked(IOException.class) && isChecked(Exception.class)
                        && !isChecked(IllegalStateException.class) && !isChecked(StackOverflowError.class));
        ExerciseChecker.check("   Ledger.RAW intact", Ledger.RAW.size() == 5);

        ExerciseChecker.summary();
    }
}
