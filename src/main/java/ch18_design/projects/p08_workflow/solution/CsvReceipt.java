package ch18_design.projects.p08_workflow.solution;

/** L'export pour le tableur : un en-tete de colonnes, une ligne par transition, pas de pied (le crochet reste vide). */
public final class CsvReceipt extends ReceiptExporter {

    @Override
    protected String header(Order order) {
        return "commande;de;vers\n";
    }

    @Override
    protected String line(Order order, Transition transition) {
        return order.id() + ";" + transition.from() + ";" + transition.to() + "\n";
    }
}
