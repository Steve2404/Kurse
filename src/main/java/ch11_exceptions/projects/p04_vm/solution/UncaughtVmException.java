package ch11_exceptions.projects.p04_vm.solution;

/**
 * SOLUTION - aucune instruction TRY active n'a rattrape l'erreur : la machine s'arrete et la signale.
 */
public class UncaughtVmException extends Exception {

    public UncaughtVmException(String message, Throwable cause) {
        super(message, cause);
    }
}
