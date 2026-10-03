package ch13_concurrency.projects.p05_life.solution;

import ch13_concurrency.projects.p05_life.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * SOLUTION du projet 5 - le jeu de la vie en parallele : chaque ouvrier calcule SES lignes ; une CyclicBarrier
 * attend que TOUS aient fini une generation avant de passer a la suivante. L'action de la barriere (executee une
 * seule fois, par le dernier thread arrive) echange les grilles et note la population.
 */
public class ParallelLife {

    static boolean[][] current;
    static boolean[][] next;

    static boolean[][] initial() {
        boolean[][] g = new boolean[Data.SIZE][Data.SIZE];
        for (int r = 0; r < Data.SIZE; r++) {
            for (int c = 0; c < Data.SIZE; c++) {
                g[r][c] = Data.alive(r, c);
            }
        }
        return g;
    }

    public static void main(String[] args) throws Exception {
        current = initial();
        next = new boolean[Data.SIZE][Data.SIZE];
        List<Integer> populations = Collections.synchronizedList(new ArrayList<>());
        populations.add(Life.population(current));
        CyclicBarrier barrier = new CyclicBarrier(Data.WORKERS, () -> {
            boolean[][] tmp = current;
            current = next;
            next = tmp;
            populations.add(Life.population(current));
        });
        ExecutorService pool = Executors.newFixedThreadPool(Data.WORKERS);   // AU MOINS autant de threads que de parties !
        try {
            int band = Data.SIZE / Data.WORKERS;
            List<Future<?>> done = new ArrayList<>();
            for (int w = 0; w < Data.WORKERS; w++) {
                int from = w * band;
                int to = w == Data.WORKERS - 1 ? Data.SIZE : from + band;
                done.add(pool.submit(() -> {
                    for (int gen = 0; gen < Data.GENERATIONS; gen++) {
                        Life.step(current, next, from, to);
                        barrier.await();                            // attend les autres ; la barriere se REARME toute seule
                    }
                    return null;
                }));
            }
            for (Future<?> f : done) {
                f.get();
            }
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(10, TimeUnit.SECONDS);

        boolean[][] seq = initial();
        for (int gen = 0; gen < Data.GENERATIONS; gen++) {
            boolean[][] n = new boolean[Data.SIZE][Data.SIZE];
            Life.step(seq, n, 0, Data.SIZE);
            seq = n;
        }
        System.out.println("populations (generation 0 a " + Data.GENERATIONS + ") : " + populations);
        System.out.println("finale " + Life.population(current) + ", empreinte " + Life.fingerprint(current) + ", identique au sequentiel "
                + (Life.fingerprint(current) == Life.fingerprint(seq)));
        System.out.println("barriere : parties " + barrier.getParties() + ", en attente " + barrier.getNumberWaiting() + ", cassee " + barrier.isBroken());

        // Une barriere CASSEE : un thread en attente est interrompu ; les autres recoivent BrokenBarrierException.
        CyclicBarrier pair = new CyclicBarrier(2);
        List<String> log = Collections.synchronizedList(new ArrayList<>());
        Thread waiting = new Thread(() -> {
            try {
                pair.await();
            } catch (InterruptedException e) {
                log.add("interrompu");
            } catch (BrokenBarrierException e) {
                log.add("cassee");
            }
        });
        waiting.start();
        while (pair.getNumberWaiting() < 1) {
            Thread.sleep(1);
        }
        waiting.interrupt();
        waiting.join();
        String mine;
        try {
            pair.await();
            mine = "passe";
        } catch (BrokenBarrierException e) {
            mine = e.getClass().getSimpleName();
        }
        boolean broken = pair.isBroken();
        pair.reset();
        System.out.println("barriere cassee : l'autre thread " + log + ", main " + mine + ", isBroken " + broken + " puis apres reset " + pair.isBroken());
    }
}
