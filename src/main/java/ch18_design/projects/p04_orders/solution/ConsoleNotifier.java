package ch18_design.projects.p04_orders.solution;

import java.io.PrintStream;

/**
 * Un adaptateur : ecrit les messages sur un PrintStream. Il RECOIT ce flux au lieu d'utiliser System.out
 * directement : en production on lui donne System.out, dans un test un flux en memoire.
 */
public final class ConsoleNotifier implements Notifier {

    private final PrintStream out;

    public ConsoleNotifier(PrintStream out) {
        this.out = out;
    }

    @Override
    public void orderConfirmed(Order order) {
        out.println("MAIL a " + order.customer() + " : commande " + order.id() + " confirmee, total " + order.totalCents() + " centimes");
    }

    @Override
    public void orderCancelled(Order order) {
        out.println("MAIL a " + order.customer() + " : commande " + order.id() + " annulee");
    }
}
