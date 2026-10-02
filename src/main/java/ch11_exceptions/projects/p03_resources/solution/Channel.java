package ch11_exceptions.projects.p03_resources.solution;

import java.util.List;

/**
 * SOLUTION - une ressource simulee : chaque ouverture, envoi et fermeture est note dans le journal.
 */
public class Channel implements AutoCloseable {

    private final String name;
    private final List<String> log;
    private boolean open;

    // Si le constructeur echoue, la ressource n'existe pas : try-with-resources ne la fermera donc pas.
    public Channel(String name, List<String> log) throws ResourceException {
        this.name = name;
        this.log = log;
        if (name.startsWith("!")) {
            throw new ResourceException("ouverture ratee de " + name);
        }
        log.add("ouvre " + name);
        open = true;
    }

    public void send(String message) {
        if (!open) {
            throw new IllegalStateException("canal ferme : " + name);
        }
        log.add(name + " <- " + message);
    }

    // AutoCloseable.close() declare "throws Exception" : on peut declarer PLUS PRECIS (ou rien).
    @Override
    public void close() throws ResourceException {
        open = false;
        log.add("ferme " + name);
        if (name.startsWith("~")) {
            throw new ResourceException("fermeture ratee de " + name);
        }
    }
}
