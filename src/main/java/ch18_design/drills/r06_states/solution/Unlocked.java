package ch18_design.drills.r06_states.solution;

/** Deverrouille : on passe (le tourniquet se reverrouille) ; une piece de plus est rendue, donc refusee. */
public final class Unlocked implements GateState {

    @Override
    public String label() {
        return "deverrouille";
    }

    @Override
    public GateState push(Turnstile gate) {
        gate.countPassage();
        return new Locked();
    }
}
