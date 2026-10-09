package ch16_testing.projects.p06_payment.solution;

/** Une commande a payer. Immuable : changer de statut cree une NOUVELLE commande. */
public record Order(String id, String customer, String email, long totalCents, OrderStatus status) {

    public Order withStatus(OrderStatus newStatus) {
        return new Order(id, customer, email, totalCents, newStatus);
    }
}
