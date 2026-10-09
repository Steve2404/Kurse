package ch19_final.projects.p07_review;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 * Chaque mutant REMET dans le code corrige un defaut de la demande de fusion du collegue.
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Customer.java", "public record Customer(String id, String name, String email) {\n", "public record Customer(String id, String name, String email) {\n\n    @Override\n    public int hashCode() {\n        return System.identityHashCode(this);\n    }\n"),
            new Mutant("PurchaseImporter.java", "new BigDecimal(text).movePointRight(2).longValueExact()", "(long) (Double.parseDouble(text) * 100)"),
            new Mutant("LoyaltyService.java", "if (\"DOUBLE\".equals(purchase.promoCode())) {", "if (purchase.promoCode() == \"DOUBLE\") {"),
            new Mutant("Tier.java", "if (points >= 1000) {", "if (points > 1000) {"),
            new Mutant("Tier.java", "return points >= 300 ? SILVER : BRONZE;", "return points > 300 ? SILVER : BRONZE;"),
            new Mutant("LoyaltyService.java", "int points = (int) (purchase.cents() / 100);", "int points = (int) Math.round(purchase.cents() / 100.0);"),
            new Mutant("LoyaltyService.java", "        LocalDate today = LocalDate.now(clock);", "        LocalDate today = LocalDate.now();"),
            new Mutant("LoyaltyService.java", "b.earnedOn().plusYears(1)", "b.earnedOn().plusDays(365)"),
            new Mutant("LoyaltyService.java", "        if (purchase.date().isAfter(LocalDate.now(clock))) {\n            throw new IllegalArgumentException(\"achat dans le futur : \" + purchase.date());\n        }\n", ""),
            new Mutant("PurchaseImporter.java", "                    result.add(parse(line, number));", "                    try {\n                        result.add(parse(line, number));\n                    } catch (IllegalArgumentException e) {\n                        break;\n                    }"),
            new Mutant("PurchaseImporter.java", "        try (BufferedReader reader = new BufferedReader(in)) {", "        {\n            BufferedReader reader = new BufferedReader(in);"),
            new Mutant("LoyaltyService.java", "return List.copyOf(purchases.get(customerId));", "return purchases.get(customerId);"),
            new Mutant("LoyaltyService.java", "private final Map<String, List<Batch>> batches", "private static final Map<String, List<Batch>> batches"),
            new Mutant("LoyaltyService.java", "        purchases.put(customer.id(), new CopyOnWriteArrayList<>());", "        purchases.put(customer.id(), new java.util.ArrayList<>());"),
            new Mutant("Customer.java", "+ masked(email) +", "+ email +"),
            new Mutant("LoyaltyService.java", "if (customers.putIfAbsent(customer.id(), customer) != null) {", "if (customers.put(customer.id(), customer) == customer) {"));

    static final List<String> API_CODE = List.of(
            "record Customer(", "record Purchase(", "enum Tier", "final class PurchaseImporter", "final class LoyaltyService",
            "final class ReviewDemo", "BigDecimal", "longValueExact()", "try (", "plusYears(1)", "LocalDate.now(clock)",
            "ConcurrentHashMap", "List.copyOf(", "\"DOUBLE\".equals(", "!double", "!static final Map", "!LocalDate.now()",
            "!plusDays(365)", "!== \"", "!printStackTrace",
            "max:method=18",
            "in:PurchaseImporter.java!catch (IOException##une erreur d'entree-sortie remonte, elle n'est pas avalee",
            "in:LoyaltyService.java!new ArrayList##les listes partagees entre fils sont sures (CopyOnWriteArrayList)");

    static final List<String> API_TESTS = List.of(
            "HashSet", "new String(\"DOUBLE\")", "Clock.fixed(", "extends Reader", "CountDownLatch", "@ParameterizedTest",
            "UnsupportedOperationException", "assertThrows(", "!System.out", "!Thread.sleep", "!systemDefaultZone");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 35, MUTANTS, API_CODE, API_TESTS);
    }
}
