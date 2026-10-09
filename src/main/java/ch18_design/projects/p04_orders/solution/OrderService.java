package ch18_design.projects.p04_orders.solution;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Les REGLES de la boulangerie, et rien d'autre. Toutes ses dependances arrivent par le constructeur
 * (l'injection de dependances) : aucun "new" d'un mailer ou d'une base, aucune lecture de l'heure systeme,
 * aucun hasard. Dans un test, on lui donne des doublures ; en production, de vrais adaptateurs.
 */
public final class OrderService {

    static final Duration CANCEL_WINDOW = Duration.ofMinutes(30);

    private final OrderRepository repository;
    private final Notifier notifier;
    private final Catalog catalog;
    private final IdGenerator ids;
    private final Clock clock;

    public OrderService(OrderRepository repository, Notifier notifier, Catalog catalog, IdGenerator ids, Clock clock) {
        this.repository = repository;
        this.notifier = notifier;
        this.catalog = catalog;
        this.ids = ids;
        this.clock = clock;
    }

    public Order place(String customer, List<String> items) {
        LocalDateTime now = LocalDateTime.now(clock);
        requireOpen(now);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("commande vide");
        }
        long total = items.stream().mapToLong(this::priceOf).sum();
        Order order = new Order(ids.next(), customer, items, total, now);
        repository.save(order);
        notifySafely(order);
        return order;
    }

    // Ouvert du lundi au samedi, de 7 h (inclus) a 19 h (exclu).
    private static void requireOpen(LocalDateTime now) {
        if (now.getDayOfWeek() == DayOfWeek.SUNDAY || now.getHour() < 7 || now.getHour() >= 19) {
            throw new IllegalStateException("boutique fermee");
        }
    }

    private long priceOf(String item) {
        return catalog.price(item).orElseThrow(() -> new IllegalArgumentException("article inconnu : " + item));
    }

    // La commande est deja enregistree : une panne du mail ne doit pas la faire echouer.
    private void notifySafely(Order order) {
        try {
            notifier.orderConfirmed(order);
        } catch (RuntimeException e) {
            // le client recevra sa confirmation plus tard ; la commande, elle, est prise
        }
    }

    // Annulable pendant 30 minutes, la 30e minute comprise.
    public boolean cancel(String id) {
        Optional<Order> order = repository.find(id);
        LocalDateTime now = LocalDateTime.now(clock);
        if (order.isEmpty() || now.isAfter(order.get().placedAt().plus(CANCEL_WINDOW))) {
            return false;
        }
        repository.delete(id);
        notifier.orderCancelled(order.get());
        return true;
    }

    public List<Order> history(String customer) {
        return repository.byCustomer(customer);
    }
}
