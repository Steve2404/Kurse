package ch19_final.projects.p07_review.solution;

import java.io.IOException;
import java.io.StringReader;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/** Le scenario de la demo du collegue (Data.main), rejoue avec le code corrige. */
public final class ReviewDemo {

    private ReviewDemo() {
    }

    public static void main(String[] args) throws IOException {
        Clock clock = Clock.systemDefaultZone();
        LocalDate today = LocalDate.now(clock);
        LoyaltyService service = new LoyaltyService(clock);
        service.register(new Customer("C1", "Ada", "ada@example.org"));
        service.record(new Purchase("C1", 10, today, null));
        service.record(new Purchase("C1", 20, today, null));
        System.out.println("total depense : " + service.totalSpentCents("C1") + " centimes");
        System.out.println("dans un HashSet : " + new HashSet<>(List.of(new Customer("C1", "Ada", "a@b.c"),
                new Customer("C1", "Ada", "a@b.c"))).size() + " client(s)");
        System.out.println("code tape par le client : " + service.record(new Purchase("C1", 5000, today, new String("DOUBLE"))) + " points");
        try {
            PurchaseImporter.read(new StringReader("C1;10;2026-10-01\nC1;dix;2026-10-02\nC1;30;2026-10-03"));
        } catch (IllegalArgumentException e) {
            System.out.println("import refuse : " + e.getMessage());
        }
        System.out.println(new Customer("C2", "Bob", "bob.martin@example.org"));
        System.out.println("palier avec 1000 points : " + Tier.of(1000));
    }
}
