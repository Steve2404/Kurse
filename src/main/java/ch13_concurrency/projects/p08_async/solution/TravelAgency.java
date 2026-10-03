package ch13_concurrency.projects.p08_async.solution;

import ch13_concurrency.projects.p08_async.Data;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * SOLUTION - des "services distants" lents, et leur composition ASYNCHRONE avec CompletableFuture.
 */
public class TravelAgency {

    private final Executor executor;

    public TravelAgency(Executor executor) {
        this.executor = executor;
    }

    static void latency() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public CompletableFuture<Integer> flight(String city) {
        return CompletableFuture.supplyAsync(() -> {
            latency();
            Integer price = Data.FLIGHTS.get(city);
            if (price == null) {
                throw new IllegalStateException("aucun vol pour " + city);
            }
            return price;
        }, executor);
    }

    public CompletableFuture<Integer> hotel(String city) {
        return CompletableFuture.supplyAsync(() -> {
            latency();
            return Data.HOTELS.get(city) * Data.NIGHTS;
        }, executor);
    }

    // Une conversion qui est ELLE-MEME asynchrone : on l'enchaine avec thenCompose (et non thenApply).
    public CompletableFuture<Integer> toChf(int euros) {
        return CompletableFuture.supplyAsync(() -> euros * Data.CHF_PER_MILLE / 1000, executor);
    }

    // vol + hotel EN PARALLELE (thenCombine), remise de 10 % au-dela de 500 (thenApply), conversion (thenCompose),
    // et repli en cas d'erreur (exceptionally).
    public CompletableFuture<String> quote(String city) {
        return flight(city)
                .thenCombine(hotel(city), Integer::sum)
                .thenApply(total -> total > 500 ? total * 9 / 10 : total)
                .thenCompose(euros -> toChf(euros).thenApply(chf -> city + " " + euros + " EUR = " + chf + " CHF"))
                .exceptionally(e -> city + " indisponible (" + e.getCause().getMessage() + ")");
    }
}
