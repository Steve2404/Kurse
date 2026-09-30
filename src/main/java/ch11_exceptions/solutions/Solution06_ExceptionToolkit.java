package ch11_exceptions.solutions;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 6.
 */
public class Solution06_ExceptionToolkit {

    public static class BatchException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public BatchException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static Throwable rootCause(Throwable t) {
        // On descend la chaine getCause() jusqu'au bout : la vraie origine du probleme.
        Throwable current = t;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }

    public static List<String> causeChain(Throwable t) {
        // Meme parcours que rootCause, mais on garde chaque maillon (utile dans un log).
        List<String> chain = new ArrayList<>();
        for (Throwable current = t; current != null; current = current.getCause()) {
            chain.add(current.getClass().getSimpleName() + ": " + current.getMessage());
        }
        return chain;
    }

    public static void runAll(List<Runnable> tasks) {
        // Un try/catch PAR tache pour ne pas s'arreter ; la 1re erreur devient la cause, les autres restent visibles en suppressed.
        List<RuntimeException> errors = new ArrayList<>();
        for (Runnable task : tasks) {
            try {
                task.run();
            } catch (RuntimeException e) {
                errors.add(e);
            }
        }
        if (errors.isEmpty()) {
            return;
        }
        BatchException batch = new BatchException(errors.size() + " of " + tasks.size() + " tasks failed", errors.get(0));
        for (RuntimeException e : errors.subList(1, errors.size())) {
            batch.addSuppressed(e);
        }
        throw batch;
    }

    public static <T> T retry(Callable<T> action, int attempts) throws Exception {
        // On relance la DERNIERE erreur (la plus recente) sans perdre les autres, accrochees en suppressed.
        List<Exception> failures = new ArrayList<>();
        for (int i = 0; i < attempts; i++) {
            try {
                return action.call();
            } catch (Exception e) {
                failures.add(e);
            }
        }
        Exception last = failures.get(failures.size() - 1);
        for (Exception e : failures.subList(0, failures.size() - 1)) {
            last.addSuppressed(e);
        }
        throw last;
    }

    public static <T> Supplier<T> unchecked(Callable<T> action) {
        // Ordre des catch du plus precis au plus general ; RuntimeException d'abord, sinon elle serait emballee pour rien.
        return () -> {
            try {
                return action.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
