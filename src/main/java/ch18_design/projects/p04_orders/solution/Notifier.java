package ch18_design.projects.p04_orders.solution;

/** Un port : prevenir le client. Mail, SMS, console : le service ne sait pas lequel. */
public interface Notifier {

    void orderConfirmed(Order order);

    void orderCancelled(Order order);
}
