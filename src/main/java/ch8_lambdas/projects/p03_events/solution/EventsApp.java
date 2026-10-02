package ch8_lambdas.projects.p03_events.solution;

import ch8_lambdas.projects.p03_events.Data;

import java.util.function.BiConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * SOLUTION du projet 3 - la simulation d'une agence.
 */
public class EventsApp {

    private static long seed = Data.SEED;

    // Un tirage uniforme dans [lo, hi], par generateur congruentiel : deterministe.
    static int draw(int lo, int hi) {
        seed = (seed * 1103515245 + 12345) & 0x7fffffffL;
        return lo + (int) (seed % (hi - lo + 1));
    }

    public static void main(String[] args) {
        IntSupplier arrivals = () -> draw(Data.ARRIVAL_MIN, Data.ARRIVAL_MAX);
        IntSupplier services = () -> draw(Data.SERVICE_MIN, Data.SERVICE_MAX);
        int[] counter = {0};
        Supplier<String> names = () -> "C" + ++counter[0];

        // Trois ecouteurs combines par andThen : le journal (limite), un compteur, un detecteur d'attente longue.
        int[] lines = {0};
        int[] longWaits = {0};
        BiConsumer<Integer, String> journal = (t, msg) -> {
            if (lines[0]++ < Data.LOG_LINES) {
                System.out.println(String.format("%3d", t) + " | " + msg);
            }
        };
        int[] events = {0};
        BiConsumer<Integer, String> count = (t, msg) -> events[0]++;
        BiConsumer<Integer, String> alarm = (t, msg) -> {
            if (msg.contains("apres") && Integer.parseInt(msg.substring(msg.indexOf("apres ") + 6, msg.indexOf(" min"))) >= 10) {
                longWaits[0]++;
            }
        };
        Bank bank = new Bank(arrivals, services, names, journal.andThen(count).andThen(alarm));
        Supplier<String> report = bank.report();       // rien n'est calcule ici
        bank.run();
        System.out.println("...");
        System.out.println(events[0] + " evenements, " + longWaits[0] + " attentes de 10 min ou plus");
        System.out.println(report.get());               // le calcul a lieu maintenant, avec l'etat final

        // Runnable et Supplier : deux interfaces fonctionnelles sans parametre.
        Runnable hello = () -> System.out.println("runnable : fermeture de l'agence");
        Supplier<Runnable> deferred = () -> hello;
        deferred.get().run();
    }
}
