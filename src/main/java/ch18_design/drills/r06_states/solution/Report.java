package ch18_design.drills.r06_states.solution;

/** La methode modele : le squelette est fixe (final), les etapes sont dans les sous-classes, le pied est un crochet. */
public abstract class Report {

    public final String render(Turnstile gate) {
        StringBuilder out = new StringBuilder(header());
        gate.events().forEach(event -> out.append(line(event)));
        return out.append(footer(gate)).toString();
    }

    protected abstract String header();

    protected abstract String line(String event);

    protected String footer(Turnstile gate) {
        return "";
    }
}
