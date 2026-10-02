package ch11_exceptions.drills.r03_custom.solution;

import java.io.IOException;

/**
 * SOLUTION du drill de rappel 3 - ecrire ses exceptions, les chainer, les relancer.
 */
public class Recall03 {

    static void load(String name) throws ConfigException {
        try {
            throw new IOException("disque plein");
        } catch (IOException e) {
            throw new ConfigException("chargement de " + name, e);   // on traduit en gardant la cause
        }
    }

    // Relance precise : le compilateur sait que seule ConfigException (verifiee) peut sortir.
    static void retry(String name) throws ConfigException {
        try {
            load(name);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        ConfigException none = new ConfigException();
        ConfigException message = new ConfigException("cle absente");
        ConfigException causeOnly = new ConfigException(new IllegalStateException("etat"));
        System.out.println("D01 : " + none.getMessage() + " | " + message.getMessage() + " | " + causeOnly.getMessage());
        try {
            load("app.yml");
        } catch (ConfigException e) {
            System.out.println("D02 : " + e.getMessage() + " <- " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
        }
        RuntimeException outer = new RuntimeException("haut", new IllegalArgumentException("milieu", new ArithmeticException("bas")));
        Throwable root = outer;
        int depth = 0;
        while (root.getCause() != null) {
            root = root.getCause();
            depth++;
        }
        System.out.println("D03 : " + root.getMessage() + " a la profondeur " + depth);
        IllegalStateException late = new IllegalStateException("tard");
        late.initCause(new ArithmeticException("origine"));          // une seule fois, et seulement sans cause deja fixee
        System.out.println("D04 : " + late.getCause().getMessage());
        try {
            retry("db.yml");
        } catch (ConfigException e) {
            System.out.println("D05 : " + e.getMessage());
        }
        try {
            throw new QuotaExceededException(120, 100);
        } catch (QuotaExceededException e) {
            System.out.println("D06 : " + e.getMessage() + " ; depassement " + e.excess() + " ; " + (e instanceof RuntimeException));
        }
    }
}

// Les 4 constructeurs classiques d'une exception : (), (message), (cause), (message, cause).
class ConfigException extends Exception {

    ConfigException() {
        super();
    }

    ConfigException(String message) {
        super(message);
    }

    ConfigException(Throwable cause) {
        super(cause);                                             // getMessage() vaudra alors cause.toString()
    }

    ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Une exception NON verifiee qui transporte des donnees et construit son message.
class QuotaExceededException extends RuntimeException {

    private final int used;
    private final int limit;

    QuotaExceededException(int used, int limit) {
        super("quota " + used + "/" + limit);
        this.used = used;
        this.limit = limit;
    }

    int excess() {
        return used - limit;
    }
}
