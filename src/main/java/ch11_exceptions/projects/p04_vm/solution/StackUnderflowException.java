package ch11_exceptions.projects.p04_vm.solution;

/**
 * SOLUTION - on a depile une pile vide.
 */
public class StackUnderflowException extends VmException {

    public StackUnderflowException(int index) {
        super("pile vide (instruction " + index + ")");
    }

    @Override
    public int code() {
        return -1;
    }
}
