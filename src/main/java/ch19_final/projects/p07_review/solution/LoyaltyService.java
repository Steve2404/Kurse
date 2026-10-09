package ch19_final.projects.p07_review.solution;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Le service de fidelite, corrige :
 *   - l'etat appartient a l'INSTANCE (le collegue avait des Map static : deux services partageaient tout) ;
 *   - des structures sures pour plusieurs fils (le serveur appelle record en parallele) ;
 *   - l'heure vient d'une Clock injectee ;
 *   - les points expirent un AN apres l'achat (plusYears(1), pas plusDays(365) : les annees bissextiles) ;
 *   - history rend une copie non modifiable.
 */
public final class LoyaltyService {

    private record Batch(int points, LocalDate earnedOn) {
    }

    private final Clock clock;
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, List<Purchase>> purchases = new ConcurrentHashMap<>();
    private final Map<String, List<Batch>> batches = new ConcurrentHashMap<>();

    public LoyaltyService(Clock clock) {
        this.clock = clock;
    }

    public void register(Customer customer) {
        if (customers.putIfAbsent(customer.id(), customer) != null) {
            throw new IllegalArgumentException("client deja inscrit : " + customer.id());
        }
        purchases.put(customer.id(), new CopyOnWriteArrayList<>());
        batches.put(customer.id(), new CopyOnWriteArrayList<>());
    }

    /** Enregistre l'achat et rend les points gagnes : 1 par euro entier, x2 pour un client GOLD, x2 avec le code DOUBLE. */
    public int record(Purchase purchase) {
        String id = purchase.customerId();
        requireKnown(id);
        if (purchase.date().isAfter(LocalDate.now(clock))) {
            throw new IllegalArgumentException("achat dans le futur : " + purchase.date());
        }
        int points = (int) (purchase.cents() / 100);
        if (tier(id) == Tier.GOLD) {
            points *= 2;
        }
        // equals, jamais == sur des String : un code lu dans un fichier est un autre objet que le texte "DOUBLE".
        if ("DOUBLE".equals(purchase.promoCode())) {
            points *= 2;
        }
        purchases.get(id).add(purchase);
        batches.get(id).add(new Batch(points, purchase.date()));
        return points;
    }

    /** Les points encore valables aujourd'hui : un lot gagne le 15 janvier vaut jusqu'au 14 janvier de l'annee suivante. */
    public int balance(String customerId) {
        requireKnown(customerId);
        LocalDate today = LocalDate.now(clock);
        return batches.get(customerId).stream()
                .filter(b -> today.isBefore(b.earnedOn().plusYears(1)))
                .mapToInt(Batch::points)
                .sum();
    }

    public Tier tier(String customerId) {
        return Tier.of(balance(customerId));
    }

    public List<Purchase> history(String customerId) {
        requireKnown(customerId);
        return List.copyOf(purchases.get(customerId));
    }

    public long totalSpentCents(String customerId) {
        requireKnown(customerId);
        return purchases.get(customerId).stream().mapToLong(Purchase::cents).sum();
    }

    private void requireKnown(String customerId) {
        if (!customers.containsKey(customerId)) {
            throw new NoSuchElementException("client inconnu : " + customerId);
        }
    }
}
