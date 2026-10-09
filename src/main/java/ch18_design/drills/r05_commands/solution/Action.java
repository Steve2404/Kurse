package ch18_design.drills.r05_commands.solution;

/** Une commande : elle s'applique et sait se defaire. */
public interface Action {

    void apply();

    void revert();
}
