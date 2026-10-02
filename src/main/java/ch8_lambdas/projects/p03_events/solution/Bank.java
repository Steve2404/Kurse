package ch8_lambdas.projects.p03_events.solution;

import ch8_lambdas.projects.p03_events.Data;

import java.util.function.BiConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * SOLUTION - la simulation : les arrivees et les fins de service sont des Runnable planifies dans la file.
 */
public class Bank {

    private final EventQueue events = new EventQueue();
    private final IntSupplier nextArrival;
    private final IntSupplier nextService;
    private final Supplier<String> names;
    private final BiConsumer<Integer, String> listener;
    private int clock;
    private int seq;

    // La file d'attente des clients : un tableau circulaire de leurs heures d'arrivee et de leurs noms.
    private final int[] waitingSince = new int[64];
    private final String[] waitingName = new String[64];
    private int head;
    private int tail;
    private int freeTellers = Data.TELLERS;

    private int served;
    private int totalWait;
    private int maxWait;
    private int maxQueue;
    private int busyMinutes;

    public Bank(IntSupplier nextArrival, IntSupplier nextService, Supplier<String> names, BiConsumer<Integer, String> listener) {
        this.nextArrival = nextArrival;
        this.nextService = nextService;
        this.names = names;
        this.listener = listener;
    }

    private void schedule(int at, String label, Runnable action) {
        events.push(new Event(at, seq++, label, action));
    }

    public void run() {
        schedule(0, "ouverture", this::arrival);                     // reference de methode sur this
        while (!events.isEmpty()) {
            Event e = events.pop();
            clock = e.time();
            e.action().run();                                        // Runnable : ni parametre, ni resultat
        }
    }

    private void arrival() {
        String name = names.get();
        waitingSince[tail % 64] = clock;
        waitingName[tail++ % 64] = name;
        maxQueue = Math.max(maxQueue, tail - head);
        listener.accept(clock, name + " arrive, file " + (tail - head));
        startServices();
        int next = clock + nextArrival.getAsInt();
        if (next < Data.CLOSING) {
            schedule(next, "arrivee", this::arrival);
        }
    }

    private void startServices() {
        while (freeTellers > 0 && head < tail) {
            freeTellers--;
            int waited = clock - waitingSince[head % 64];
            String name = waitingName[head++ % 64];
            int duration = nextService.getAsInt();
            totalWait += waited;
            maxWait = Math.max(maxWait, waited);
            busyMinutes += duration;
            listener.accept(clock, name + " au guichet apres " + waited + " min, pour " + duration + " min");
            // Lambda qui capture name (effectively final) : executee plus tard, a la fin du service.
            schedule(clock + duration, "fin", () -> {
                served++;
                freeTellers++;
                listener.accept(clock, name + " repart");
                startServices();
            });
        }
    }

    // Un Supplier paresseux : le rapport n'est construit que si quelqu'un appelle get().
    public Supplier<String> report() {
        return () -> served + " clients servis, attente moyenne " + Math.round(10.0 * totalWait / served) / 10.0 + " min, max " + maxWait
                + " min, file max " + maxQueue + ", occupation " + Math.round(100.0 * busyMinutes / (Data.TELLERS * clock)) + " %, fermeture reelle " + clock;
    }
}
