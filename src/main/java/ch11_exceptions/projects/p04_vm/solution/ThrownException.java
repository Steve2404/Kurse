package ch11_exceptions.projects.p04_vm.solution;

/**
 * SOLUTION - l'instruction THROW du programme : une exception qui porte le code choisi par le programme.
 */
public class ThrownException extends VmException {

    private final int code;

    public ThrownException(int code) {
        super("code " + code);
        this.code = code;
    }

    @Override
    public int code() {
        return code;
    }
}
