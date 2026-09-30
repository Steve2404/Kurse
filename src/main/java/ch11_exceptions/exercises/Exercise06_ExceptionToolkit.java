package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * EXERCICE 6 - Boite a outils d'exceptions : cause racine, lot avec suppressed, retry, checked -> unchecked (niveau : avance)
 * ===========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans un vrai programme, une exception en cache souvent une autre
 * (getCause()), et plusieurs echecs doivent parfois etre rapportes EN
 * MEME TEMPS (addSuppressed). Tu vas ecrire les petits outils qu'on
 * retrouve dans toutes les bases de code.
 *
 *
 * ==================================================================
 * TODO 1 : rootCause(t)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   IllegalStateException("service") <- cause IOException("disque") <- cause FileNotFound("a.txt")
 *   -> le FileNotFound ; une exception sans cause -> elle-meme
 *
 * -- Le plan --
 *
 *   1. Tant que getCause() n'est pas null : descendre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : causeChain(t)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> [IllegalStateException: service, IOException: disque, FileNotFoundException: a.txt]
 *
 * -- Le plan --
 *
 *   1. Pour chaque maillon (lui, puis ses causes) : NomSimple + ": " + getMessage().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : runAll(tasks)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Lancer TOUTES les taches, meme si certaines echouent (ne pas s'arreter
 * a la premiere). A la fin, si au moins une a echoue : lancer une
 * BatchException "N of M tasks failed", avec la 1re erreur comme CAUSE
 * et les suivantes en SUPPRESSED. Sinon, rien.
 *
 * -- Essayons a la main --
 *
 *   [ok, boom(A), ok, boom(B)] -> BatchException("2 of 4 tasks failed"), cause A, suppressed [B]
 *
 * -- Le plan --
 *
 *   1. Chaque tache dans son propre try/catch (RuntimeException) ; garder les erreurs dans une liste.
 *   2. Liste vide -> fin.
 *   3. Sinon creer la BatchException (message, 1re erreur), addSuppressed pour les autres, la lancer.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : retry(action, attempts)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Appeler action (un Callable, qui peut lancer une Exception checked)
 * jusqu'a attempts fois. Succes -> rendre la valeur. Tous rates ->
 * lancer la DERNIERE exception, avec les precedentes en suppressed (on
 * ne perd aucune information).
 *
 * -- Essayons a la main --
 *
 *   rate, rate, "ok" avec 3 essais -> "ok"
 *   rate(1), rate(2), rate(3) avec 3 essais -> lance "3", suppressed ["1", "2"]
 *
 * -- Le plan --
 *
 *   1. Une liste des echecs.
 *   2. Boucle : try { return action.call(); } catch (Exception e) { ajouter e }.
 *   3. Apres la boucle : la derniere, addSuppressed des autres, throw.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : unchecked(action)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Supplier (lambda des streams) ne peut pas lancer de checked. On
 * emballe : IOException -> UncheckedIOException ; autre checked ->
 * RuntimeException ; une RuntimeException repart telle quelle. Toujours
 * garder l'originale comme CAUSE.
 *
 * -- Le plan --
 *
 *   1. Rendre () -> { try { return action.call(); } catch (...) {...} }.
 *   2. Ordre des catch : RuntimeException, puis IOException, puis Exception.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - while (t.getCause() != null) t = t.getCause();
 *   - e.getClass().getSimpleName() + ": " + e.getMessage()
 *   - batch.addSuppressed(errors.get(i)); getSuppressed() rend un tableau.
 *   - new UncheckedIOException(ioException) ; new RuntimeException(e).
 */
public class Exercise06_ExceptionToolkit {

    public static class BatchException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public BatchException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static Throwable rootCause(Throwable t) {
        throw new UnsupportedOperationException("TODO 1 : implementer rootCause()");
    }

    public static List<String> causeChain(Throwable t) {
        throw new UnsupportedOperationException("TODO 2 : implementer causeChain()");
    }

    public static void runAll(List<Runnable> tasks) {
        throw new UnsupportedOperationException("TODO 3 : implementer runAll()");
    }

    public static <T> T retry(Callable<T> action, int attempts) throws Exception {
        throw new UnsupportedOperationException("TODO 4 : implementer retry()");
    }

    public static <T> Supplier<T> unchecked(Callable<T> action) {
        throw new UnsupportedOperationException("TODO 5 : implementer unchecked()");
    }

    public static void main(String[] args) throws Exception {
        Exception deep = new IllegalStateException("service",
                new IOException("disque", new java.io.FileNotFoundException("a.txt")));
        ExerciseChecker.check("rootCause -> FileNotFoundException(a.txt) ; sans cause -> elle-meme",
                rootCause(deep).getMessage().equals("a.txt") && rootCause(deep).getClass().getSimpleName().equals("FileNotFoundException")
                        && rootCause(new ArithmeticException("x")).getMessage().equals("x"));
        ExerciseChecker.check("causeChain -> [IllegalStateException: service, IOException: disque, FileNotFoundException: a.txt]",
                causeChain(deep).equals(List.of("IllegalStateException: service", "IOException: disque", "FileNotFoundException: a.txt")));

        List<String> ran = new ArrayList<>();
        BatchException batch = null;
        try {
            runAll(List.of(() -> ran.add("1"), () -> { throw new IllegalStateException("A"); },
                    () -> ran.add("3"), () -> { throw new IllegalArgumentException("B"); }));
        } catch (BatchException e) {
            batch = e;
        }
        ExerciseChecker.check("runAll : toutes les taches tournent malgre les echecs", ran.equals(List.of("1", "3")));
        ExerciseChecker.check("runAll : BatchException(2 of 4 tasks failed), cause A, suppressed [B]",
                batch != null && batch.getMessage().equals("2 of 4 tasks failed") && batch.getCause().getMessage().equals("A")
                        && batch.getSuppressed().length == 1 && batch.getSuppressed()[0].getMessage().equals("B"));
        boolean quiet = true;
        try {
            runAll(List.of(() -> ran.add("x")));
        } catch (BatchException e) {
            quiet = false;
        }
        ExerciseChecker.check("runAll sans echec -> aucune exception", quiet);

        int[] calls = {0};
        String value = retry(() -> {
            calls[0]++;
            if (calls[0] < 3) {
                throw new IOException("essai " + calls[0]);
            }
            return "ok";
        }, 3);
        ExerciseChecker.check("retry : rate, rate, ok -> ok en 3 appels", value.equals("ok") && calls[0] == 3);
        int[] n = {0};
        Exception last = null;
        try {
            retry(() -> {
                n[0]++;
                throw new IOException(String.valueOf(n[0]));
            }, 3);
        } catch (IOException e) {
            last = e;
        }
        ExerciseChecker.check("retry : 3 echecs -> lance 3, suppressed [1, 2]",
                last != null && last.getMessage().equals("3") && last.getSuppressed().length == 2
                        && last.getSuppressed()[0].getMessage().equals("1") && last.getSuppressed()[1].getMessage().equals("2"));

        ExerciseChecker.check("unchecked : valeur rendue", unchecked(() -> 42).get() == 42);
        ExerciseChecker.check("unchecked : IOException -> UncheckedIOException (cause gardee)",
                thrownBy(unchecked(() -> { throw new IOException("io"); })).equals("UncheckedIOException<-IOException"));
        ExerciseChecker.check("unchecked : autre checked -> RuntimeException (cause gardee)",
                thrownBy(unchecked(() -> { throw new Exception("e"); })).equals("RuntimeException<-Exception"));
        ExerciseChecker.check("unchecked : RuntimeException -> telle quelle",
                thrownBy(unchecked(() -> { throw new IllegalStateException("s"); })).equals("IllegalStateException<-null"));

        ExerciseChecker.summary();
    }

    static String thrownBy(Supplier<?> supplier) {
        try {
            supplier.get();
            return "rien";
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            return e.getClass().getSimpleName() + "<-" + (cause == null ? "null" : cause.getClass().getSimpleName());
        }
    }
}
