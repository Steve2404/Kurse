package ch16_testing.projects.p02_cart;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Cart et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Cart.java", "total > MAX_PER_ITEM", "total >= MAX_PER_ITEM"),
            new Mutant("Cart.java", "old != null && old.unitCents() != unitCents", "false"),
            new Mutant("Cart.java", "subtotalCents() >= FREE_SHIPPING_FROM", "subtotalCents() > FREE_SHIPPING_FROM"),
            new Mutant("Cart.java", "subtotal >= DISCOUNT_FROM", "subtotal > DISCOUNT_FROM"),
            new Mutant("Cart.java", "(subtotal * 10 + 50) / 100", "subtotal * 10 / 100"),
            new Mutant("Cart.java", "new TreeMap<>()", "new java.util.LinkedHashMap<>()"),
            new Mutant("Cart.java", "return List.copyOf(result);", "return result;"),
            new Mutant("Cart.java", "quantity >= old.quantity()", "quantity > old.quantity()"),
            new Mutant("Cart.java", "lines.isEmpty() || ", ""),
            new Mutant("Cart.java", "if (code != null) {", "if (false) {"),
            new Mutant("Cart.java", "\"LIVRAISON\".equals(code) || ", ""),
            new Mutant("Cart.java", "int total = (old == null ? 0 : old.quantity()) + quantity;", "int total = quantity;"),
            new Mutant("Cart.java", "if (unitCents < 0) {", "if (unitCents < 1) {"),
            new Mutant("Cart.java", "return SHIPPING;", "return SHIPPING + 10;"));

    static final List<String> API_CODE = List.of(
            "final class Cart", "void add(String sku, int quantity, long unitCents)", "void remove(String sku, int quantity)",
            "int quantity(String sku)", "boolean isEmpty()", "List<String> lines()", "long subtotalCents()",
            "void applyCode(", "long discountCents()", "long shippingCents()", "long totalCents()",
            "NoSuchElementException(", "IllegalStateException(", "!double", "!float");

    static final List<String> API_TESTS = List.of(
            "@BeforeEach", "@Nested", "@DisplayName(", "assertAll(", "assertEquals(", "assertTrue(", "assertFalse(",
            "assertThrows(", "NoSuchElementException.class", "UnsupportedOperationException.class", "List.of(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 20, MUTANTS, API_CODE, API_TESTS);
    }
}
