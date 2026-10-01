package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * EXERCICE 2 - Les etats d'un Thread, start() contre run(), interrupt() : ta regle comparee a la JVM (niveau : difficile)
 * =======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Thread passe par des etats (Thread.State), comme un ouvrier :
 *
 *   NEW            embauche, mais start() pas encore appele
 *   RUNNABLE       au travail (ou pret a travailler, en attente du processeur)
 *   BLOCKED        attend d'entrer dans un synchronized qu'un AUTRE tient
 *   WAITING        attend sans limite de temps (join(), wait(), un await() de latch...)
 *   TIMED_WAITING  attend avec une limite (sleep(ms), join(ms), wait(ms)...)
 *   TERMINATED     run() est fini (normalement ou par exception)
 *
 * main() fabrique VRAIMENT chaque situation (avec des latchs et join,
 * jamais de sleep "au hasard") et lit getState() : c'est le juge.
 *
 *
 * ==================================================================
 * TODO 1 : stateOf(situation)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * situation vaut : "notStarted", "computing" (boucle de calcul),
 * "sleeping" (dans Thread.sleep(10000)), "joiningForever" (dans join()
 * sans delai), "waitingForMonitor" (veut entrer dans un synchronized
 * tenu par un autre thread), "finished".
 *
 * -- Essayons a la main --
 *
 *   "sleeping" -> TIMED_WAITING ; "joiningForever" -> WAITING ; "waitingForMonitor" -> BLOCKED
 *
 * -- Le plan --
 *
 *   1. Un switch : une situation -> un Thread.State.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Piege : attendre un VERROU synchronized = BLOCKED, mais attendre
 * un AUTRE THREAD (join) ou un signal = WAITING.
 *
 *
 * ==================================================================
 * TODO 2 : startTwice()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ne rembauche pas un ouvrier deja embauche : appeler start() une
 * 2e fois sur le meme Thread lance une exception (meme s'il a fini).
 * Rendre le nom simple de cette exception, obtenu POUR DE VRAI.
 *
 * -- Le plan --
 *
 *   1. Creer un Thread, start(), join().
 *   2. start() encore, dans un try ; rendre e.getClass().getSimpleName().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : runVersusStart()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * run() appele directement N'EST PAS un nouveau thread : c'est un
 * simple appel de methode, dans le thread courant. Rendre [le nom du
 * thread qui execute la tache via run(), le nom via start()], avec un
 * Thread nomme "worker".
 *
 * -- Essayons a la main --
 *
 *   depuis main() -> [main, worker]
 *
 * -- Le plan --
 *
 *   1. Une tache qui note Thread.currentThread().getName() dans un tableau.
 *   2. new Thread(tache, "worker") : run() puis start() + join().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : interruptSleeper()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un thread dort (sleep(10000)) ; on l'interrompt. Il se reveille avec
 * une InterruptedException... et, piege, le drapeau "interrompu" est
 * EFFACE quand l'exception est lancee. Rendre "NomDeLException/drapeau"
 * tel que vu DANS le catch du thread qui dormait.
 *
 * -- Essayons a la main --
 *
 *   -> "InterruptedException/false"
 *
 * -- Le plan --
 *
 *   1. Un thread qui dort, et qui dans son catch note le nom et isInterrupted().
 *   2. start(), interrupt(), join() ; rendre ce qu'il a note.
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
 *   - return Thread.State.TIMED_WAITING;
 *   - String[] seen = new String[1]; une lambda peut ecrire seen[0] (tableau effectivement final).
 *   - Thread.currentThread().isInterrupted() dans le catch.
 */
public class Exercise02_ThreadStateRules {

    public static Thread.State stateOf(String situation) {
        throw new UnsupportedOperationException("TODO 1 : implementer stateOf()");
    }

    public static String startTwice() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer startTwice()");
    }

    public static List<String> runVersusStart() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 3 : implementer runVersusStart()");
    }

    public static String interruptSleeper() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 4 : implementer interruptSleeper()");
    }

    public static void main(String[] args) throws Exception {
        List<String> situations = List.of("notStarted", "computing", "sleeping", "joiningForever", "waitingForMonitor", "finished");
        int agree = 0;
        String firstMiss = "";
        for (String s : situations) {
            Thread.State real = observe(s);
            Thread.State mine = stateOf(s);
            if (mine == real) {
                agree++;
            } else if (firstMiss.isEmpty()) {
                firstMiss = " ; 1er ecart : " + s + " -> " + mine + " au lieu de " + real;
            }
        }
        ExerciseChecker.check("stateOf == JVM sur 6 situations (" + agree + " d'accord)" + firstMiss, agree == 6);
        ExerciseChecker.check("startTwice -> IllegalThreadStateException", "IllegalThreadStateException".equals(startTwice()));
        ExerciseChecker.check("runVersusStart -> [main, worker]", List.of("main", "worker").equals(runVersusStart()));
        ExerciseChecker.check("interruptSleeper -> InterruptedException/false", "InterruptedException/false".equals(interruptSleeper()));

        ExerciseChecker.summary();
    }

    // ---- Le juge : fabrique chaque situation et lit getState() (ne pas modifier) ----

    private static volatile boolean keepComputing;

    static Thread.State observe(String situation) throws Exception {
        switch (situation) {
            case "notStarted":
                return new Thread(() -> { }).getState();
            case "finished": {
                Thread t = new Thread(() -> { });
                t.start();
                t.join();
                return t.getState();
            }
            case "computing": {
                keepComputing = true;
                CountDownLatch running = new CountDownLatch(1);
                Thread t = new Thread(() -> {
                    running.countDown();
                    long x = 0;
                    while (keepComputing) {
                        x++;
                    }
                });
                t.start();
                running.await();
                Thread.State s = t.getState();
                keepComputing = false;
                t.join();
                return s;
            }
            case "sleeping": {
                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        // reveil force par le juge
                    }
                });
                t.start();
                Thread.State s = waitFor(t, Thread.State.TIMED_WAITING);
                t.interrupt();
                t.join();
                return s;
            }
            case "joiningForever": {
                CountDownLatch release = new CountDownLatch(1);
                Thread target = new Thread(() -> {
                    try {
                        release.await();
                    } catch (InterruptedException e) {
                        // fin
                    }
                });
                Thread joiner = new Thread(() -> {
                    try {
                        target.join();
                    } catch (InterruptedException e) {
                        // fin
                    }
                });
                target.start();
                joiner.start();
                Thread.State s = waitFor(joiner, Thread.State.WAITING);
                release.countDown();
                joiner.join();
                return s;
            }
            default: {
                Object lock = new Object();
                Thread t;
                Thread.State s;
                synchronized (lock) {
                    t = new Thread(() -> {
                        synchronized (lock) {
                            lock.hashCode();
                        }
                    });
                    t.start();
                    s = waitFor(t, Thread.State.BLOCKED);
                }
                t.join();
                return s;
            }
        }
    }

    // Lit l'etat jusqu'a ce qu'il devienne celui attendu (max 5 s) : la transition prend quelques microsecondes.
    static Thread.State waitFor(Thread t, Thread.State expected) throws InterruptedException {
        long end = System.nanoTime() + 5_000_000_000L;
        Thread.State s = t.getState();
        while (s != expected && System.nanoTime() < end) {
            Thread.sleep(1);
            s = t.getState();
        }
        return s;
    }
}
