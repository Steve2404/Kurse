package ch18_design.drills.r05_commands.solution;

/** L'observateur du compteur : l'ancienne et la nouvelle valeur. */
@FunctionalInterface
public interface CounterListener {

    void changed(int oldValue, int newValue);
}
