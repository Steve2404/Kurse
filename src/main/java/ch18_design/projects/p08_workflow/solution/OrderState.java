package ch18_design.projects.p08_workflow.solution;

/**
 * Le patron ETAT : chaque etat est un objet qui sait quelles actions il accepte, et vers quel etat
 * elles menent. Par defaut, une action est REFUSEE : chaque etat n'ecrit que ses transitions permises.
 */
public interface OrderState {

    String label();

    default OrderState pay(Order order) {
        throw order.refused("payer");
    }

    default OrderState ship(Order order) {
        throw order.refused("expedier");
    }

    default OrderState deliver(Order order) {
        throw order.refused("livrer");
    }

    default OrderState cancel(Order order) {
        throw order.refused("annuler");
    }

    default OrderState refund(Order order) {
        throw order.refused("rembourser");
    }

    default boolean isClosed() {
        return false;
    }
}
