package ch16_testing.projects.p02_cart.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

/** Le panier d'une epicerie en ligne. Les montants sont en centimes. */
public final class Cart {

    static final int MAX_PER_ITEM = 99;
    static final long FREE_SHIPPING_FROM = 3000;
    static final long SHIPPING = 490;
    static final long DISCOUNT_FROM = 5000;

    private record Line(int quantity, long unitCents) {
    }

    // Pourquoi un TreeMap : lines() doit etre trie par code article, quel que soit l'ordre d'ajout.
    private final Map<String, Line> lines = new TreeMap<>();
    private String code;

    // Piege : TOUS les controles avant de modifier ; un ajout refuse ne doit rien changer au panier.
    public void add(String sku, int quantity, long unitCents) {
        Objects.requireNonNull(sku, "sku absent");
        if (sku.isBlank()) {
            throw new IllegalArgumentException("sku vide");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantite invalide : " + quantity);
        }
        if (unitCents < 0) {
            throw new IllegalArgumentException("prix negatif : " + unitCents);
        }
        Line old = lines.get(sku);
        if (old != null && old.unitCents() != unitCents) {
            throw new IllegalStateException("prix different pour " + sku);
        }
        int total = (old == null ? 0 : old.quantity()) + quantity;
        if (total > MAX_PER_ITEM) {
            throw new IllegalArgumentException("maximum 99 par article : " + sku);
        }
        lines.put(sku, new Line(total, unitCents));
    }

    // Piege : retirer exactement la quantite presente supprime la ligne (pas de ligne a 0).
    public void remove(String sku, int quantity) {
        Line old = lines.get(sku);
        if (old == null) {
            throw new NoSuchElementException("article absent : " + sku);
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantite invalide : " + quantity);
        }
        if (quantity >= old.quantity()) {
            lines.remove(sku);
        } else {
            lines.put(sku, new Line(old.quantity() - quantity, old.unitCents()));
        }
    }

    public int quantity(String sku) {
        Line line = lines.get(sku);
        return line == null ? 0 : line.quantity();
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    // Pourquoi List.copyOf : l'appelant ne doit pas pouvoir modifier le panier par la liste rendue.
    public List<String> lines() {
        List<String> result = new ArrayList<>();
        lines.forEach((sku, line) -> result.add(sku + " x " + line.quantity() + " = " + line.quantity() * line.unitCents()));
        return List.copyOf(result);
    }

    public long subtotalCents() {
        long sum = 0;
        for (Line line : lines.values()) {
            sum += line.quantity() * line.unitCents();
        }
        return sum;
    }

    public void applyCode(String newCode) {
        if (code != null) {
            throw new IllegalStateException("un seul code par panier");
        }
        if (!newCode.equals("MOINS10") && !newCode.equals("LIVRAISON")) {
            throw new IllegalArgumentException("code inconnu : " + newCode);
        }
        code = newCode;
    }

    // Pourquoi + 50 : 10 % arrondi au centime le plus proche ; la remise se calcule a chaque appel (le panier change).
    public long discountCents() {
        long subtotal = subtotalCents();
        return "MOINS10".equals(code) && subtotal >= DISCOUNT_FROM ? (subtotal * 10 + 50) / 100 : 0;
    }

    // Piege : un panier vide ne paie pas de livraison ; le seuil se compare au sous-total AVANT remise.
    public long shippingCents() {
        if (lines.isEmpty() || "LIVRAISON".equals(code) || subtotalCents() >= FREE_SHIPPING_FROM) {
            return 0;
        }
        return SHIPPING;
    }

    public long totalCents() {
        return subtotalCents() - discountCents() + shippingCents();
    }
}
