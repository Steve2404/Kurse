package ch19_final.projects.p07_review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Les donnees FOURNIES du projet 7 (ne pas modifier) : la DEMANDE DE FUSION (pull request) d'un collegue.
 *
 * Il a ecrit le programme de fidelite de l'atelier. "Tout marche, j'ai essaye a la main", dit-il.
 * Ta mission : la RELIRE comme un developpeur senior, trouver chaque defaut, le prouver par un test,
 * et ecrire la version corrigee. Le cahier des charges exact est dans TODO.md.
 */
public final class Data {

    private Data() {
    }

    /** Un client. */
    public static final class PrCustomer {
        public final String id;
        public final String name;
        public final String email;

        public PrCustomer(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof PrCustomer c && c.id.equals(id) && c.name.equals(name) && c.email.equals(email);
        }

        @Override
        public String toString() {
            return "Customer[" + id + ", " + name + ", " + email + "]";
        }
    }

    /** Un achat : client, montant, date, code promo (ou null). */
    public static final class PrPurchase {
        public final String customerId;
        public final double amount; // en euros
        public final LocalDate date;
        public final String promoCode;

        public PrPurchase(String customerId, double amount, LocalDate date, String promoCode) {
            this.customerId = customerId;
            this.amount = amount;
            this.date = date;
            this.promoCode = promoCode;
        }
    }

    /** Le service de fidelite. */
    public static final class PrLoyaltyService {

        private static final Map<String, List<PrPurchase>> PURCHASES = new HashMap<>();
        private static final Map<String, PrCustomer> CUSTOMERS = new HashMap<>();

        public void register(PrCustomer customer) {
            CUSTOMERS.put(customer.id, customer);
            PURCHASES.put(customer.id, new ArrayList<>());
        }

        public int record(PrPurchase purchase) {
            int points = (int) Math.round(purchase.amount);
            if (tier(purchase.customerId) == "GOLD") {
                points = points * 2;
            }
            if (purchase.promoCode == "DOUBLE") {
                points = points * 2;
            }
            PURCHASES.get(purchase.customerId).add(purchase);
            return points;
        }

        public int balance(String customerId) {
            int total = 0;
            for (PrPurchase p : PURCHASES.get(customerId)) {
                if (LocalDate.now().isBefore(p.date.plusDays(365))) {
                    total += (int) Math.round(p.amount);
                }
            }
            return total;
        }

        public String tier(String customerId) {
            int balance = balance(customerId);
            return balance > 1000 ? "GOLD" : balance > 300 ? "SILVER" : "BRONZE";
        }

        public List<PrPurchase> history(String customerId) {
            return PURCHASES.get(customerId);
        }

        public double totalSpent(String customerId) {
            double total = 0;
            for (PrPurchase p : PURCHASES.get(customerId)) {
                total += p.amount;
            }
            return total;
        }

        public List<PrPurchase> importPurchases(Reader in) {
            List<PrPurchase> result = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(in);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] f = line.split(";");
                    result.add(new PrPurchase(f[0], Double.parseDouble(f[1]), LocalDate.parse(f[2]), f.length > 3 ? f[3] : null));
                }
            } catch (IOException | RuntimeException e) {
                // une ligne fausse : on s'arrete la, ce qui a ete lu est garde
            }
            return result;
        }
    }

    public static void main(String[] args) {
        PrLoyaltyService service = new PrLoyaltyService();
        service.register(new PrCustomer("C1", "Ada", "ada@example.org"));
        service.record(new PrPurchase("C1", 0.1, LocalDate.now(), null));
        service.record(new PrPurchase("C1", 0.2, LocalDate.now(), null));
        System.out.println("total depense : " + service.totalSpent("C1") + " euros");
        System.out.println("meme client ? " + new PrCustomer("C1", "Ada", "a@b.c").equals(new PrCustomer("C1", "Ada", "a@b.c"))
                + " ; dans un HashSet : " + new java.util.HashSet<>(List.of(new PrCustomer("C1", "Ada", "a@b.c"),
                new PrCustomer("C1", "Ada", "a@b.c"))).size() + " client(s)");
        String typed = new String("DOUBLE");
        System.out.println("code tape par le client : " + service.record(new PrPurchase("C1", 50, LocalDate.now(), typed)) + " points");
        System.out.println("code ecrit dans le code : " + service.record(new PrPurchase("C1", 50, LocalDate.now(), "DOUBLE")) + " points");
        System.out.println("import : " + service.importPurchases(new java.io.StringReader("C1;10;2026-10-01\nC1;dix;2026-10-02\nC1;30;2026-10-03")).size()
                + " achat(s) lus sur 3 lignes");
        System.out.println(new PrCustomer("C2", "Bob", "bob.martin@example.org"));
        System.out.println("une autre instance du service voit-elle C1 ? " + (new PrLoyaltyService().history("C1") != null));
        System.out.println("palier avec 1000 points : " + tierFor(1000) + " (le cahier des charges dit GOLD des 1000)");
    }

    private static String tierFor(int points) {
        PrLoyaltyService s = new PrLoyaltyService();
        s.register(new PrCustomer("T", "Test", "t@t.t"));
        s.record(new PrPurchase("T", points, LocalDate.now(), null));
        return s.tier("T");
    }
}
