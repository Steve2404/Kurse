package ch18_design.projects.p01_invoice.solution;

/** Une piece : nom, quantite, prix unitaire. */
public record Part(String name, int quantity, Money unitPrice) implements InvoiceLine {

    public Part {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantite invalide : " + quantity);
        }
    }

    @Override
    public String label() {
        return name + " x" + quantity;
    }

    @Override
    public Money price() {
        return unitPrice.times(quantity);
    }

    @Override
    public Category category() {
        return Category.PART;
    }
}
