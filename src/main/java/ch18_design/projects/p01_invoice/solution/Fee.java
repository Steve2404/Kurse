package ch18_design.projects.p01_invoice.solution;

/**
 * Etape 7 : un forfait (recyclage, petites fournitures). Ajouter cette sorte de ligne n'a demande
 * qu'un nouveau record, une valeur d'enum et une ligne dans le lecteur : le calcul n'a pas bouge.
 */
public record Fee(String name, Money amount) implements InvoiceLine {

    @Override
    public String label() {
        return name + " (forfait)";
    }

    @Override
    public Money price() {
        return amount;
    }

    @Override
    public Category category() {
        return Category.FEE;
    }
}
