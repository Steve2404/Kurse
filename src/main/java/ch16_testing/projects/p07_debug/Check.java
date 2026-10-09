package ch16_testing.projects.p07_debug;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Inventory et TES tests, ou avec l'argument "solution".
 * Les mutants 1 a 6 sont les six bugs de Data, remis un par un dans le code corrige.
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Inventory.java", "if (item.sku.equals(sku)) {\n                item.qty += qty;", "if (item.sku == sku) {\n                item.qty += qty;"),
            new Mutant("Inventory.java", "if (item.qty >= qty) {", "if (item.qty > qty) {"),
            new Mutant("Inventory.java", "total += (long) item.qty * item.priceCents;", "total += item.qty * item.priceCents;"),
            new Mutant("Inventory.java", "(sum + n / 2) / n", "sum / n"),
            new Mutant("Inventory.java", "return quantity(a) == quantity(b);", "return Integer.valueOf(quantity(a)) == Integer.valueOf(quantity(b));"),
            new Mutant("Inventory.java", "for (int i = 0; i < items.size(); i++) {", "for (int i = 1; i < items.size(); i++) {"),
            new Mutant("Inventory.java", "if (qty < 1 || price < 0) {", "if (price < 0) {"),
            new Mutant("Inventory.java", "if (parts.length != 3) {", "if (parts.length < 3) {"),
            new Mutant("Inventory.java", "if (qty < 1) {\n            throw new IllegalArgumentException(\"quantite invalide", "if (qty < 0) {\n            throw new IllegalArgumentException(\"quantite invalide"),
            new Mutant("Inventory.java", "return n == 0 ? 0 :", "return n == 0 ? -1 :"));

    static final List<String> API_CODE = List.of(
            "final class Inventory", "void receive(String line)", "int quantity(String sku)", "boolean ship(String sku, int qty)",
            "long stockValueCents()", "long averagePriceCents()", "boolean sameQuantity(String a, String b)",
            "List<String> lowStock(int threshold)", "(long)", "catch (NumberFormatException");

    static final List<String> API_TESTS = List.of(
            "Data.DELIVERIES", "@DisplayName(\"BUG 1", "@DisplayName(\"BUG 2", "@DisplayName(\"BUG 3", "@DisplayName(\"BUG 4",
            "@DisplayName(\"BUG 5", "@DisplayName(\"BUG 6", "@ParameterizedTest", "assertAll(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 14, MUTANTS, API_CODE, API_TESTS);
    }
}
