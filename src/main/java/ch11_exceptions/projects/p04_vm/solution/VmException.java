package ch11_exceptions.projects.p04_vm.solution;

/**
 * SOLUTION - la racine des erreurs que le PROGRAMME de la machine peut rattraper (verifiee).
 * Chaque erreur fournit le code empile pour le gestionnaire.
 */
public abstract class VmException extends Exception {

    protected VmException(String message) {
        super(message);
    }

    public abstract int code();
}
