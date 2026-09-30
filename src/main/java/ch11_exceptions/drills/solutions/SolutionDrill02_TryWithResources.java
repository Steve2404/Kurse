package ch11_exceptions.drills.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.drills.exercises.Drill02_TryWithResources.
 */
public class SolutionDrill02_TryWithResources {

    public static class Res implements AutoCloseable {
        private final String name;
        private final List<String> log;
        private final boolean failOnClose;

        public Res(String name, List<String> log, boolean failOnClose) {
            this.name = name;
            this.log = log;
            this.failOnClose = failOnClose;
            log.add("open " + name);
        }

        public void use() {
            log.add("use");
        }

        @Override
        public void close() {
            log.add("close " + name);
            if (failOnClose) {
                throw new IllegalStateException("close " + name);
            }
        }
    }

    public static void openOne(List<String> log) {
        // close() est appele tout seul a la sortie du bloc.
        try (Res a = new Res("A", log, false)) {
            a.use();
        }
    }

    @SuppressWarnings("try") // a n'est pas lue dans le bloc : seule sa fermeture compte
    public static void openTwo(List<String> log) {
        // Fermeture dans l'ordre INVERSE de l'ouverture : B d'abord.
        try (Res a = new Res("A", log, false); Res b = new Res("B", log, false)) {
            b.use();
        }
    }

    @SuppressWarnings("try") // la ressource ne sert qu'a montrer l'ordre close -> catch -> finally
    public static void withCatchFinally(List<String> log) {
        // La ressource est deja fermee quand le catch s'execute ; finally vient en dernier.
        try (Res a = new Res("A", log, false)) {
            throw new IllegalStateException("x");
        } catch (IllegalStateException e) {
            log.add("catch " + e.getMessage());
        } finally {
            log.add("finally");
        }
    }

    @SuppressWarnings("try") // la ressource ne sert qu'a echouer a la fermeture
    public static String primaryAndSuppressed() {
        // L'exception du bloc est la principale ; celle de close() est accrochee en suppressed.
        try (Res a = new Res("A", new ArrayList<>(), true)) {
            throw new IllegalStateException("body");
        } catch (IllegalStateException e) {
            return e.getMessage() + ":" + e.getSuppressed().length;
        }
    }

    public static String onlyCloseFails() {
        // Sans exception dans le bloc, l'exception de close() remonte seule.
        try (Res a = new Res("A", new ArrayList<>(), true)) {
            a.use();
            return "aucune";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    public static void existingVariable(List<String> log) {
        // Java 9 : une variable deja declaree, effectivement finale, peut servir de ressource.
        Res a = new Res("A", log, false);
        try (a) {
            a.use();
        }
    }

    @SuppressWarnings("try") // r n'est pas utilise dans le bloc : c'est justement le cas etudie
    public static String nullResource() {
        // Une ressource null est permise : close() n'est simplement pas appele.
        try (Res r = null) {
            return "ok";
        }
    }

    public static int scannerSum(String text) {
        // Scanner implemente Closeable : try-with-resources le ferme.
        int sum = 0;
        try (Scanner sc = new Scanner(text)) {
            while (sc.hasNextInt()) {
                sum += sc.nextInt();
            }
        }
        return sum;
    }
}
