package ch18_design.projects.p04_orders;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * FOURNI (ne pas modifier) : l'ancien service de commandes de la boulangerie.
 * Il marche, mais essaie de le TESTER : il envoie un vrai mail (ici, il l'affiche), lit l'heure de ton
 * ordinateur, tire un numero au hasard et range tout dans une base unique et statique.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        try {
            String id = new LegacyOrderService().placeOrder("ada@example.org", List.of("baguette", "croissant", "croissant"));
            System.out.println("commande enregistree : " + id + " -> " + LegacyDatabase.INSTANCE.orders.get(id));
        } catch (IllegalStateException e) {
            System.out.println("refus : " + e.getMessage());
        }
    }

    /** La base de donnees : un singleton statique, partage par tout le programme (et par tous les tests). */
    public static final class LegacyDatabase {
        public static final LegacyDatabase INSTANCE = new LegacyDatabase();
        final Map<String, String> orders = new HashMap<>();

        private LegacyDatabase() {
        }

        void save(String id, String line) {
            orders.put(id, line);
        }
    }

    /** Le "vrai" mail : dans cette demo, il s'affiche sur la console. */
    public static final class LegacySmtpMailer {
        void send(String to, String text) {
            System.out.println("MAIL a " + to + " : " + text);
        }
    }

    public static final class LegacyOrderService {
        private static final Map<String, Long> PRICES = Map.of("baguette", 120L, "croissant", 110L, "tarte", 1850L);

        public String placeOrder(String customer, List<String> items) {
            LocalDateTime now = LocalDateTime.now();
            if (now.getDayOfWeek() == DayOfWeek.SUNDAY || now.getHour() < 7 || now.getHour() >= 19) {
                throw new IllegalStateException("boutique fermee");
            }
            long total = 0;
            List<String> lines = new ArrayList<>();
            for (String item : items) {
                Long price = PRICES.get(item);
                if (price == null) {
                    throw new IllegalArgumentException("article inconnu : " + item);
                }
                total += price;
                lines.add(item);
            }
            String id = "CMD-" + new Random().nextInt(100_000);
            LegacyDatabase.INSTANCE.save(id, customer + " " + lines + " " + total);
            new LegacySmtpMailer().send(customer, "commande " + id + " confirmee, total " + total + " centimes");
            return id;
        }
    }
}
