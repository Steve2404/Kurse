package ch18_design.projects.p01_invoice.solution;

import java.util.List;

/**
 * La FACADE : meme signature que le legacy, pour que les appelants ne voient rien du changement.
 * Elle ne fait qu'assembler les trois responsabilites : lire, calculer, imprimer.
 */
public final class Garage {

    private Garage() {
    }

    public static String invoice(String customerName, boolean loyal, List<String> textLines) {
        List<InvoiceLine> lines = textLines.stream().map(LineParser::parse).toList();
        Customer customer = new Customer(customerName, loyal);
        InvoiceTotals totals = new InvoiceCalculator().compute(customer, lines);
        return new InvoicePrinter().print(customer, lines, totals);
    }
}
