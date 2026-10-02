package ch11_exceptions.projects.p04_vm.solution;

/**
 * SOLUTION - garde-fou contre les boucles infinies. Non verifiee, et HORS de la famille VmException :
 * le programme ne peut donc pas la rattraper avec TRY.
 */
public class StepLimitException extends RuntimeException {

    public StepLimitException(int limit) {
        super("plus de " + limit + " pas");
    }
}
