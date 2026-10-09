package ch18_design.projects.p08_workflow.solution;

/** Le recu lisible : un titre, une ligne par transition, l'etat final et le remboursement. */
public final class TextReceipt extends ReceiptExporter {

    @Override
    protected String header(Order order) {
        return "Commande " + order.id() + "\n";
    }

    @Override
    protected String line(Order order, Transition transition) {
        return "  " + transition + "\n";
    }

    @Override
    protected String footer(Order order) {
        return "Etat final : " + order.status() + ", rembourse : " + order.refundedCents() + " centimes\n";
    }
}
