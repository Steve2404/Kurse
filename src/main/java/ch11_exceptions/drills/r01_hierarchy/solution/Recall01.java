package ch11_exceptions.drills.r01_hierarchy.solution;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;

/**
 * SOLUTION du drill de rappel 1 - la hierarchie des Throwable : verifiees, non verifiees, erreurs.
 */
public class Recall01 {

    // Remonter getSuperclass() jusqu'a Throwable.
    static String chain(Class<?> type) {
        StringBuilder sb = new StringBuilder(type.getSimpleName());
        Class<?> c = type.getSuperclass();
        while (c != Object.class) {
            sb.append(" > ").append(c.getSimpleName());
            c = c.getSuperclass();
        }
        return sb.toString();
    }

    // L'ordre des tests compte : Error, puis RuntimeException, puis le reste des Exception.
    static String kind(Throwable t) {
        if (t instanceof Error) {
            return "erreur";
        }
        return t instanceof RuntimeException ? "non verifiee" : "verifiee";
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + chain(NumberFormatException.class));
        System.out.println("D02 : " + chain(FileNotFoundException.class));
        System.out.println("D03 : " + chain(StackOverflowError.class));
        System.out.println("D04 : " + kind(new IOException()) + ", " + kind(new ParseException("x", 0)) + ", " + kind(new DateTimeParseException("x", "t", 0)) + ", "
                + kind(new ArithmeticException()) + ", " + kind(new AssertionError()) + ", " + kind(new Exception()));
        String first;
        try {
            first = "valeur " + Fragile.VALUE;
        } catch (ExceptionInInitializerError e) {             // l'initialiseur static a echoue
            first = e.getClass().getSimpleName() + " cause " + e.getCause().getClass().getSimpleName();
        }
        String second;
        try {
            second = "valeur " + Fragile.VALUE;
        } catch (NoClassDefFoundError e) {                     // 2e acces : la classe est definitivement inutilisable
            second = e.getClass().getSimpleName();
        }
        System.out.println("D05 : " + first + " ; " + second);
        String caught;
        try {
            Object text = "java";
            Integer number = (Integer) text;
            caught = "jamais " + number;
        } catch (ClassCastException e) {
            caught = e.getClass().getSimpleName() + " " + (e instanceof RuntimeException);
        }
        System.out.println("D06 : " + caught);
    }
}

// Un initialiseur static qui lance une exception : la JVM l'enveloppe dans ExceptionInInitializerError.
class Fragile {
    static final int VALUE = Integer.parseInt("oops");
}
