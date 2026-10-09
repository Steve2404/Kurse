package ch18_design.drills.r06_states.solution;

/** L'etat du tourniquet : par defaut, toute action est refusee ; chaque etat n'ecrit que ce qu'il accepte. */
public interface GateState {

    String label();

    default GateState coin(Turnstile gate) {
        throw gate.refused("piece");
    }

    default GateState push(Turnstile gate) {
        throw gate.refused("pousser");
    }

    default GateState breakDown(Turnstile gate) {
        return new Broken();
    }

    default GateState repair(Turnstile gate) {
        throw gate.refused("reparer");
    }
}
