package ch13_concurrency.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise02_ThreadStateRules.
 */
public class Solution02_ThreadStateRules {

    public static Thread.State stateOf(String situation) {
        // BLOCKED = attendre un VERROU synchronized ; WAITING = attendre sans limite un autre thread ou un signal.
        switch (situation) {
            case "notStarted":
                return Thread.State.NEW;
            case "computing":
                return Thread.State.RUNNABLE;
            case "sleeping":
                return Thread.State.TIMED_WAITING;
            case "joiningForever":
                return Thread.State.WAITING;
            case "waitingForMonitor":
                return Thread.State.BLOCKED;
            default:
                return Thread.State.TERMINATED;
        }
    }

    public static String startTwice() throws InterruptedException {
        // Un Thread ne demarre qu'une fois, meme termine : le 2e start() lance IllegalThreadStateException (unchecked).
        Thread t = new Thread(() -> { });
        t.start();
        t.join();
        try {
            t.start();
            return "aucune";
        } catch (IllegalThreadStateException e) {
            return e.getClass().getSimpleName();
        }
    }

    public static List<String> runVersusStart() throws InterruptedException {
        // run() est un simple appel de methode dans le thread courant ; seul start() cree un nouveau fil d'execution.
        String[] seen = new String[1];
        Thread t = new Thread(() -> seen[0] = Thread.currentThread().getName(), "worker");
        t.run();
        String viaRun = seen[0];
        t.start();
        t.join();
        return List.of(viaRun, seen[0]);
    }

    public static String interruptSleeper() throws InterruptedException {
        // Quand sleep lance InterruptedException, le drapeau "interrompu" est deja efface : isInterrupted() rend false.
        String[] seen = new String[1];
        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(10_000);
                seen[0] = "pas interrompu";
            } catch (InterruptedException e) {
                seen[0] = e.getClass().getSimpleName() + "/" + Thread.currentThread().isInterrupted();
            }
        });
        sleeper.start();
        sleeper.interrupt();
        sleeper.join();
        return seen[0];
    }
}
