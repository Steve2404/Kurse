package ch11_exceptions.drills.r04_resources.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SOLUTION du drill de rappel 4 - try-with-resources.
 */
public class Recall04 {

    static final List<String> LOG = new ArrayList<>();

    public static void main(String[] args) {
        try (Door a = new Door("a", false); Door b = new Door("b", false)) {
            LOG.add("corps");
        }
        System.out.println("D01 : " + LOG);
        LOG.clear();
        try (Door a = new Door("a", true); Door b = new Door("b", true)) {
            throw new IllegalStateException("corps");
        } catch (IllegalStateException e) {
            LOG.add("catch " + e.getMessage() + " " + Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
        } finally {
            LOG.add("finally");
        }
        System.out.println("D02 : " + LOG);
        LOG.clear();
        try (Door a = new Door("a", true)) {
            LOG.add("corps");
        } catch (IllegalStateException e) {
            LOG.add("catch " + e.getMessage() + " " + e.getSuppressed().length);   // sans exception du corps : celle de close() est PRINCIPALE
        }
        System.out.println("D03 : " + LOG);
        LOG.clear();
        Door shared = new Door("partagee", false);
        try (shared; Door nothing = null) {                         // Java 9 : variable effectivement finale ; null = rien a fermer
            LOG.add("corps " + (nothing == null));
        }
        System.out.println("D04 : " + LOG);
        LOG.clear();
        try (AutoCloseable lambda = () -> LOG.add("ferme lambda")) {  // AutoCloseable est fonctionnelle ; close() throws Exception
            LOG.add("corps");
        } catch (Exception e) {
            LOG.add("jamais");
        }
        System.out.println("D05 : " + LOG);
        IllegalStateException primary = new IllegalStateException("principale");
        primary.addSuppressed(new IllegalArgumentException("a la main"));
        System.out.println("D06 : " + primary.getSuppressed().length + " " + primary.getSuppressed()[0].getMessage());
    }
}

// close() redefinie SANS throws : les appelants n'ont rien a attraper.
class Door implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    Door(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        Recall04.LOG.add("ouvre " + name);
    }

    @Override
    public void close() {
        Recall04.LOG.add("ferme " + name);
        if (failOnClose) {
            throw new IllegalStateException("close " + name);
        }
    }
}
