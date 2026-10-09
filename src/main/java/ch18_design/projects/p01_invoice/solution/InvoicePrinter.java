package ch18_design.projects.p01_invoice.solution;

import java.util.List;

/** UNE responsabilite : mettre en page. Aucun calcul de prix ici : tout vient d'InvoiceTotals. */
public final class InvoicePrinter {

    public String print(Customer customer, List<InvoiceLine> lines, InvoiceTotals totals) {
        StringBuilder out = new StringBuilder("FACTURE - Client : " + customer.name() + "\n");
        lines.forEach(line -> row(out, "  " + line.label(), line.price()));
        row(out, "Pieces", totals.parts());
        row(out, "Main-d'oeuvre", totals.labor());
        // Les lignes optionnelles : absentes quand elles valent zero, exactement comme le legacy.
        optional(out, "Forfaits : ", totals.fees());
        optional(out, "Remise fidelite : -", totals.loyaltyDiscount());
        optional(out, "Remise main-d'oeuvre : -", totals.laborDiscount());
        row(out, "Sous-total HT", totals.net());
        row(out, "TVA 20 %", totals.vat());
        row(out, "Total TTC", totals.total());
        return out.toString();
    }

    private static void row(StringBuilder out, String label, Money amount) {
        out.append(label).append(" : ").append(amount.format()).append('\n');
    }

    private static void optional(StringBuilder out, String prefix, Money amount) {
        if (!amount.isZero()) {
            out.append(prefix).append(amount.format()).append('\n');
        }
    }
}
