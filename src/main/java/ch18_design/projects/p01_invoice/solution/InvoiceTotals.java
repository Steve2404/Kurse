package ch18_design.projects.p01_invoice.solution;

/** Tous les montants calcules d'une facture : le resultat du calcul, sans aucun texte. */
public record InvoiceTotals(Money parts, Money labor, Money fees, Money loyaltyDiscount, Money laborDiscount,
                            Money net, Money vat, Money total) {
}
