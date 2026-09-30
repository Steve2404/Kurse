package ch11_exceptions.drills.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * DRILL 02 - try-with-resources : ordre de fermeture, suppressed, formes Java 9
 * =============================================================================
 *
 * Mode d'emploi : voir Drill01_ExceptionBasics. Res (donnee plus bas)
 * ecrit "open X" a sa creation et "close X" a sa fermeture ; avec
 * failOnClose, son close() lance IllegalStateException("close X").
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1 : openOne(log)          [une ressource] Res A ; dans le bloc : log "use" -> [open A, use, close A].
 * TODO 2 : openTwo(log)          [deux ressources, separees par ;] A puis B -> [open A, open B, use, close B, close A].
 * TODO 3 : withCatchFinally(log) [ordre close -> catch -> finally] le bloc lance IllegalStateException("x") ;
 *                                catch -> log "catch x" ; finally -> log "finally".
 * TODO 4 : primaryAndSuppressed() [getSuppressed] A echoue a la fermeture ET le bloc lance IllegalStateException("body") :
 *                                rendre message principal + ":" + nombre de suppressed -> "body:1".
 * TODO 5 : onlyCloseFails()      [exception de close seule] A echoue a la fermeture, le bloc ne lance rien : rendre le message attrape.
 * TODO 6 : existingVariable(log) [Java 9 : try (r)] creer Res A AVANT le try, puis try (a) { log "use" }.
 * TODO 7 : nullResource()        [ressource null] try (Res r = null) { ... } rendre "ok" : pas de NullPointerException a la fermeture.
 * TODO 8 : scannerSum(text)      [Scanner est Closeable] additionner les entiers de "1 2 3" -> 6.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   try (A a = new A(); B b = new B()) { ... }   ferme b PUIS a (ordre inverse), avant catch et finally
 *   try (a) { }        Java 9 : a doit etre final ou effectivement final
 *   AutoCloseable.close() throws Exception ; Closeable.close() throws IOException
 *   exception du bloc + exception de close -> celle du bloc gagne, l'autre est dans getSuppressed()
 *   seule close echoue -> c'est ELLE qui remonte
 *   ressource null -> close() n'est pas appele
 *   la ressource est implicitement final et n'existe plus dans catch / finally
 * ---------------------------------------------------------------------
 */
public class Drill02_TryWithResources {

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
        throw new UnsupportedOperationException("TODO 1 : implementer openOne()");
    }

    public static void openTwo(List<String> log) {
        throw new UnsupportedOperationException("TODO 2 : implementer openTwo()");
    }

    public static void withCatchFinally(List<String> log) {
        throw new UnsupportedOperationException("TODO 3 : implementer withCatchFinally()");
    }

    public static String primaryAndSuppressed() {
        throw new UnsupportedOperationException("TODO 4 : implementer primaryAndSuppressed()");
    }

    public static String onlyCloseFails() {
        throw new UnsupportedOperationException("TODO 5 : implementer onlyCloseFails()");
    }

    public static void existingVariable(List<String> log) {
        throw new UnsupportedOperationException("TODO 6 : implementer existingVariable()");
    }

    public static String nullResource() {
        throw new UnsupportedOperationException("TODO 7 : implementer nullResource()");
    }

    public static int scannerSum(String text) {
        throw new UnsupportedOperationException("TODO 8 : implementer scannerSum()");
    }

    public static void main(String[] args) {
        List<String> log = new ArrayList<>();
        openOne(log);
        ExerciseChecker.check("1  openOne == [open A, use, close A]", log.equals(List.of("open A", "use", "close A")));
        log = new ArrayList<>();
        openTwo(log);
        ExerciseChecker.check("2  openTwo == [open A, open B, use, close B, close A]",
                log.equals(List.of("open A", "open B", "use", "close B", "close A")));
        log = new ArrayList<>();
        withCatchFinally(log);
        ExerciseChecker.check("3  withCatchFinally == [open A, close A, catch x, finally]",
                log.equals(List.of("open A", "close A", "catch x", "finally")));
        ExerciseChecker.check("4  primaryAndSuppressed == body:1", primaryAndSuppressed().equals("body:1"));
        ExerciseChecker.check("5  onlyCloseFails == close A", onlyCloseFails().equals("close A"));
        log = new ArrayList<>();
        existingVariable(log);
        ExerciseChecker.check("6  existingVariable == [open A, use, close A]", log.equals(List.of("open A", "use", "close A")));
        ExerciseChecker.check("7  nullResource == ok", nullResource().equals("ok"));
        ExerciseChecker.check("8  scannerSum(1 2 3) == 6", scannerSum("1 2 3") == 6);

        ExerciseChecker.summary();
    }
}
