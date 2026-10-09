package ch18_design.drills.r06_states.solution;

/** "Journal du tourniquet", une ligne "- ..." par evenement, puis les totaux. */
public final class PlainReport extends Report {

    @Override
    protected String header() {
        return "Journal du tourniquet\n";
    }

    @Override
    protected String line(String event) {
        return "- " + event + "\n";
    }

    @Override
    protected String footer(Turnstile gate) {
        return gate.coins() + " piece(s), " + gate.passages() + " passage(s)\n";
    }
}
