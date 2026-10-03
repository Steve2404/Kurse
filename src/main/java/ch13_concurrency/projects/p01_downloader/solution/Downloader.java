package ch13_concurrency.projects.p01_downloader.solution;

import ch13_concurrency.projects.p01_downloader.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SOLUTION du projet 1 - les threads "a la main" : creer, demarrer, attendre, interrompre, observer les etats.
 */
public class Downloader {

    // Attendre qu'un thread ATTEIGNE un etat (on ne peut pas le deviner : il faut l'observer).
    static void waitFor(Thread t, Thread.State state) throws InterruptedException {
        while (t.getState() != state) {
            Thread.sleep(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int size = Data.SIZE / Data.CHUNKS;
        ChunkStats[] results = new ChunkStats[Data.CHUNKS];
        String[] workers = new String[Data.CHUNKS];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Data.CHUNKS; i++) {
            int from = i * size;
            int to = i == Data.CHUNKS - 1 ? Data.SIZE : from + size;
            // Les morceaux pairs : un Runnable confie a un Thread ; les impairs : une sous-classe de Thread.
            threads.add(i % 2 == 0 ? new Thread(new ChunkTask(i, from, to, results, workers), "dl-" + i) : new ChunkThread("dl-" + i, i, from, to, results, workers));
        }
        for (Thread t : threads) {
            t.start();                                   // start() cree un NOUVEAU thread, qui appelle run()
        }
        for (Thread t : threads) {
            t.join();                                    // join() attend la fin ; il rend aussi visibles les ecritures du thread
        }
        System.out.println("decoupage : " + Data.CHUNKS + " morceaux de " + size + " octets");
        ChunkStats total = results[0];
        for (int i = 0; i < Data.CHUNKS; i++) {
            ChunkStats c = results[i];
            System.out.println("morceau " + i + " [" + c.from() + "," + c.to() + ") somme " + c.sum() + ", serie max " + c.best() + ", tete " + c.prefix()
                    + ", queue " + c.suffix() + ", par " + workers[i]);
            if (i > 0) {
                total = total.merge(c);
            }
        }
        ChunkStats sequential = ChunkStats.of(0, Data.SIZE);
        System.out.println("total : somme " + total.sum() + ", plus longue serie " + total.best() + " ; identique au calcul sequentiel " + total.equals(sequential));

        // run() appele directement : AUCUN nouveau thread, c'est main qui execute.
        String[] who = new String[1];
        Runnable whoAmI = () -> who[0] = Thread.currentThread().getName();
        whoAmI.run();
        String direct = who[0];
        Thread named = new Thread(whoAmI, "dl-run");
        named.start();
        named.join();
        System.out.println("run() direct execute par " + direct + " ; start() execute par " + who[0]);

        List<String> log = Collections.synchronizedList(new ArrayList<>());
        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(60_000);
            } catch (InterruptedException e) {
                // En levant InterruptedException, sleep EFFACE le drapeau d'interruption.
                log.add("dormeur : InterruptedException, drapeau apres catch " + Thread.currentThread().isInterrupted());
            }
        }, "dormeur");
        Thread waiter = new Thread(() -> {
            try {
                sleeper.join();
            } catch (InterruptedException e) {
                log.add("attente interrompue");
            }
        }, "attente");
        Object lock = new Object();
        Thread blocked = new Thread(() -> {
            synchronized (lock) {
                log.add("bloque : verrou obtenu");
            }
        }, "bloque");
        Thread.State before = sleeper.getState();
        String states;
        synchronized (lock) {                            // main tient le verrou : "bloque" restera BLOCKED
            sleeper.start();
            waiter.start();
            blocked.start();
            waitFor(sleeper, Thread.State.TIMED_WAITING);
            waitFor(waiter, Thread.State.WAITING);
            waitFor(blocked, Thread.State.BLOCKED);
            states = "dormeur " + sleeper.getState() + ", attente " + waiter.getState() + ", bloque " + blocked.getState();
        }
        blocked.join();
        sleeper.interrupt();
        sleeper.join();
        waiter.join();
        System.out.println("etats : avant start " + before + " ; " + states + " ; apres join " + sleeper.getState());

        // Un thread qui calcule ne voit l'interruption que s'il la TESTE.
        Thread spinner = new Thread(() -> {
            long n = 0;
            while (!Thread.currentThread().isInterrupted()) {
                n++;
            }
            log.add("boucle : arretee, drapeau " + Thread.currentThread().isInterrupted());
        });
        spinner.start();
        spinner.interrupt();
        spinner.join();
        System.out.println("journal : " + log);

        Thread daemon = new Thread(() -> {
            try {
                Thread.sleep(60_000);
            } catch (InterruptedException e) {
                log.add("daemon interrompu");
            }
        });
        daemon.setDaemon(true);                          // un daemon n'empeche pas la JVM de s'arreter
        daemon.start();
        String late;
        try {
            daemon.setDaemon(false);                     // interdit une fois le thread demarre
            late = "permis";
        } catch (IllegalThreadStateException e) {
            late = e.getClass().getSimpleName();
        }
        String twice;
        try {
            named.start();                               // un thread ne se demarre qu'UNE fois
            twice = "permis";
        } catch (IllegalThreadStateException e) {
            twice = e.getClass().getSimpleName();
        }
        System.out.println("daemon " + daemon.isDaemon() + ", setDaemon apres start " + late + ", start deux fois " + twice + ", priorite par defaut "
                + named.getPriority() + " (min " + Thread.MIN_PRIORITY + ", max " + Thread.MAX_PRIORITY + ")");
    }
}
