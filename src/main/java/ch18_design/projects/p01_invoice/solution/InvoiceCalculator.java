package ch18_design.projects.p01_invoice.solution;

import java.util.List;

/**
 * UNE responsabilite : calculer. Aucun texte ici, donc on teste les regles de prix sans comparer
 * des chaines, et l'on pourra changer la mise en page sans risquer de toucher aux montants.
 */
public final class InvoiceCalculator {

    static final Money LOYALTY_THRESHOLD = Money.ofCents(20_000);
    static final int LOYALTY_PERCENT = 10;
    static final int LONG_JOB_MINUTES = 240;
    static final int LABOR_DISCOUNT_PERCENT = 5;
    static final int VAT_PERCENT = 20;

    public InvoiceTotals compute(Customer customer, List<InvoiceLine> lines) {
        Money parts = sum(lines, Category.PART);
        Money labor = sum(lines, Category.LABOR);
        Money fees = sum(lines, Category.FEE);
        Money loyalty = loyaltyDiscount(customer, parts);
        Money laborDiscount = laborDiscount(lines, labor);
        // Les forfaits n'ont jamais de remise, mais paient la TVA.
        Money net = parts.plus(labor).plus(fees).minus(loyalty).minus(laborDiscount);
        Money vat = net.percent(VAT_PERCENT);
        return new InvoiceTotals(parts, labor, fees, loyalty, laborDiscount, net, vat, net.plus(vat));
    }

    private static Money sum(List<InvoiceLine> lines, Category category) {
        return lines.stream().filter(l -> l.category() == category)
                .map(InvoiceLine::price).reduce(Money.ZERO, Money::plus);
    }

    // Seuil INCLUS : 200,00 de pieces exactement donne droit a la remise (t >= 20000 dans le legacy).
    private static Money loyaltyDiscount(Customer customer, Money parts) {
        boolean eligible = customer.loyal() && parts.cents() >= LOYALTY_THRESHOLD.cents();
        return eligible ? parts.percent(LOYALTY_PERCENT) : Money.ZERO;
    }

    // Seuil EXCLU : il faut PLUS de 4 h facturees (m > 240), en minutes deja arrondies au quart d'heure.
    private static Money laborDiscount(List<InvoiceLine> lines, Money labor) {
        int minutes = lines.stream().mapToInt(InvoiceLine::billedMinutes).sum();
        return minutes > LONG_JOB_MINUTES ? labor.percent(LABOR_DISCOUNT_PERCENT) : Money.ZERO;
    }
}
