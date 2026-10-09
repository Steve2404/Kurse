package ch19_final.projects.p05_resilience.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Le stock, protege : le disjoncteur evite d'insister aupres d'un fournisseur en panne, et quand il ne repond
 * pas, on se REPLIE sur la derniere valeur connue, en disant son age. Une reponse un peu ancienne vaut mieux
 * qu'une page d'erreur... a condition de le dire. Chaque appel mesure son temps de reponse.
 */
public final class ResilientStock {

    private record Known(int quantity, Instant at) {
    }

    private final StockClient supplier;
    private final CircuitBreaker breaker;
    private final LatencyRecorder latency;
    private final Clock clock;
    private final Map<String, Known> lastKnown = new ConcurrentHashMap<>();

    public ResilientStock(StockClient supplier, CircuitBreaker breaker, LatencyRecorder latency, Clock clock) {
        this.supplier = supplier;
        this.breaker = breaker;
        this.latency = latency;
        this.clock = clock;
    }

    public StockAnswer stock(String ref) {
        Instant start = clock.instant();
        try {
            int quantity = breaker.call(() -> supplier.stock(ref));
            lastKnown.put(ref, new Known(quantity, clock.instant()));
            return new StockAnswer(ref, quantity, "fournisseur");
        } catch (IllegalArgumentException e) {
            throw e; // une reference inconnue : le cache n'y peut rien, et ce n'est pas une panne
        } catch (RuntimeException e) {
            return fallback(ref, e);
        } finally {
            latency.record(Duration.between(start, clock.instant()));
        }
    }

    private StockAnswer fallback(String ref, RuntimeException cause) {
        Known known = lastKnown.get(ref);
        if (known == null) {
            throw new StockUnavailableException(ref, cause);
        }
        long age = Duration.between(known.at(), clock.instant()).toSeconds();
        return new StockAnswer(ref, known.quantity(), "cache (" + age + " s)");
    }
}
