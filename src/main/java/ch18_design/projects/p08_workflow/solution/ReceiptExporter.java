package ch18_design.projects.p08_workflow.solution;

/**
 * La METHODE MODELE (template method) : export() fixe le squelette, une fois pour toutes (final) ;
 * les sous-classes ne remplissent que les etapes. footer() est un "crochet" : vide par defaut.
 */
public abstract class ReceiptExporter {

    public final String export(Order order) {
        StringBuilder out = new StringBuilder(header(order));
        order.transitions().forEach(t -> out.append(line(order, t)));
        return out.append(footer(order)).toString();
    }

    protected abstract String header(Order order);

    protected abstract String line(Order order, Transition transition);

    protected String footer(Order order) {
        return "";
    }
}
