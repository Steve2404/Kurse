package ch18_design.drills.r06_states.solution;

/** En panne : seule la reparation est acceptee, et le tourniquet repart verrouille. Tomber en panne a nouveau est refuse. */
public final class Broken implements GateState {

    @Override
    public String label() {
        return "en panne";
    }

    @Override
    public GateState breakDown(Turnstile gate) {
        throw gate.refused("panne");
    }

    @Override
    public GateState repair(Turnstile gate) {
        return new Locked();
    }
}
