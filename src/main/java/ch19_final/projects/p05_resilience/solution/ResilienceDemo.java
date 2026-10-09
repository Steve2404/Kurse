package ch19_final.projects.p05_resilience.solution;

import ch19_final.projects.p05_resilience.Data;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * La demonstration, sur une horloge manuelle : 20 secondes de la vie du stock (une demande par seconde),
 * avec la panne du fournisseur de 10:00:05 a 10:00:12. Puis un client trop presse face au limiteur.
 */
public final class ResilienceDemo {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneOffset.UTC);

    private ResilienceDemo() {
    }

    public static void main(String[] args) {
        ManualClock clock = new ManualClock(Data.START);
        Data.PartsSupplier supplier = new Data.PartsSupplier(clock);
        CircuitBreaker breaker = new CircuitBreaker(3, Duration.ofSeconds(4), clock, e -> !(e instanceof IllegalArgumentException));
        LatencyRecorder latency = new LatencyRecorder(100);
        ResilientStock stock = new ResilientStock(ref -> slowly(clock, supplier, ref), breaker, latency, clock);
        for (int second = 1; second <= 20; second++) {
            clock.advance(Duration.ofSeconds(1));
            String asked = TIME.format(clock.instant());
            System.out.println(asked + " " + answer(stock) + " [" + breaker.state() + "]");
        }
        System.out.println("appels au fournisseur : " + supplier.calls() + " ; " + latency.summary());
        RateLimiter limiter = new RateLimiter(5, 1, clock);
        for (int i = 1; i <= 8; i++) {
            System.out.println("requete " + i + " : " + limiter.acquire("client-presse"));
        }
    }

    // Le fournisseur repond en 30 ms quand il va bien, et met 2 s a echouer quand il est en panne.
    private static int slowly(ManualClock clock, Data.PartsSupplier supplier, String ref) {
        try {
            int quantity = supplier.stock(ref);
            clock.advance(Duration.ofMillis(30));
            return quantity;
        } catch (IllegalStateException e) {
            clock.advance(Duration.ofSeconds(2));
            throw e;
        }
    }

    private static String answer(ResilientStock stock) {
        try {
            StockAnswer a = stock.stock("CHAIN-11");
            return a.ref() + " = " + a.quantity() + " (" + a.source() + ")";
        } catch (StockUnavailableException e) {
            return e.getMessage();
        }
    }
}
