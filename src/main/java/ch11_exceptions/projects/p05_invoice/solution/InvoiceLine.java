package ch11_exceptions.projects.p05_invoice.solution;

/**
 * SOLUTION - une ligne de facture. Les montants restent en centimes (long) : aucun arrondi flottant cache.
 */
public record InvoiceLine(int quantity, String label, long unitCents, int vatPerMille) {

    public static InvoiceLine parse(String text) {
        String[] p = text.split("\\|");
        return new InvoiceLine(Integer.parseInt(p[0]), p[1], Long.parseLong(p[2]), Integer.parseInt(p[3]));
    }

    public long net() {
        return quantity * unitCents;
    }
}
