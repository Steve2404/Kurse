package config.model;

import java.util.List;

/**
 * SOLUTION - des champs PRIVES, sans setter : seul un outil de reflexion autorise (opens) peut les remplir.
 */
public class ServerConfig {

    private String host;
    private int port;
    private boolean debug;
    private List<String> tags;
    private Limits limits = new Limits();

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    @Override
    public String toString() {
        return host + ":" + port + (debug ? " (debug)" : "") + " " + tags;
    }
}
