package ch18_design.drills.r06_states.solution;

/** Verrouille : une piece deverrouille (et compte). */
public final class Locked implements GateState {

    @Override
    public String label() {
        return "verrouille";
    }

    @Override
    public GateState coin(Turnstile gate) {
        gate.countCoin();
        return new Unlocked();
    }
}
