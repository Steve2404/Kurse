package ch19_final.projects.p05_resilience;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Les donnees FOURNIES du projet 5 (ne pas modifier).
 *
 * PartsSupplier imite le service de stock du fournisseur de pieces. Il lit l'heure sur l'horloge qu'on lui
 * donne : avec une horloge manuelle, la panne arrive toujours au meme moment, et la demo est reproductible.
 *   - de 10:00:05 (compris) a 10:00:12 (non compris), il est en panne : IllegalStateException ;
 *   - une reference inconnue : IllegalArgumentException.
 */
public final class Data {

    private Data() {
    }

    public static final Instant START = Instant.parse("2026-10-09T10:00:00Z");

    public static final Map<String, Integer> STOCK = Map.of("CHAIN-11", 14, "BRAKE-V", 3, "TUBE-700", 42);

    public static final class PartsSupplier {

        private static final Instant DOWN_FROM = Instant.parse("2026-10-09T10:00:05Z");
        private static final Instant DOWN_UNTIL = Instant.parse("2026-10-09T10:00:12Z");

        private final Clock clock;
        private final AtomicInteger calls = new AtomicInteger();

        public PartsSupplier(Clock clock) {
            this.clock = clock;
        }

        public int stock(String ref) {
            calls.incrementAndGet();
            Instant now = clock.instant();
            if (!now.isBefore(DOWN_FROM) && now.isBefore(DOWN_UNTIL)) {
                throw new IllegalStateException("fournisseur indisponible");
            }
            Integer quantity = STOCK.get(ref);
            if (quantity == null) {
                throw new IllegalArgumentException("reference inconnue : " + ref);
            }
            return quantity;
        }

        /** Le nombre d'appels recus : combien de fois on a vraiment derange le fournisseur. */
        public int calls() {
            return calls.get();
        }
    }
}
