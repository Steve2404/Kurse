package ch18_design.drills.r06_states.solution;

/** "evenement", puis une ligne par evenement ; pas de pied. */
public final class CsvReport extends Report {

    @Override
    protected String header() {
        return "evenement\n";
    }

    @Override
    protected String line(String event) {
        return event + "\n";
    }
}
