package ch11_exceptions.projects.p03_resources.solution;

import java.io.Closeable;

/**
 * SOLUTION - une ressource Closeable. Le contrat de Closeable demande un close() IDEMPOTENT :
 * le fermer une 2e fois ne doit rien faire.
 */
public class Journal implements Closeable {

    private int requested;
    private int effective;
    private boolean closed;

    // Closeable.close() declare "throws IOException" : on n'en declare aucune ici (permis).
    @Override
    public void close() {
        requested++;
        if (!closed) {
            closed = true;
            effective++;
        }
    }

    public String stats() {
        return "fermetures demandees " + requested + ", effectives " + effective;
    }
}
