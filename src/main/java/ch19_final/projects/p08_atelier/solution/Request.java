package ch19_final.projects.p08_atelier.solution;

import java.util.Map;
import java.util.Optional;

/**
 * Une requete HTTP (projet 3), qui a grandi : elle sait maintenant QUI la fait (l'en-tete X-Client,
 * ou "anonyme"), pour que le limiteur de debit freine chaque client separement.
 */
public record Request(String method, String path, Map<String, String> query, String body, String contentType,
                      String client) {

    public static final String ANONYMOUS = "anonyme";

    public Request {
        query = Map.copyOf(query);
        client = client == null || client.isBlank() ? ANONYMOUS : client;
    }

    public static Request of(String method, String path) {
        return new Request(method, path, Map.of(), "", null, null);
    }

    public static Request json(String method, String path, String body) {
        return new Request(method, path, Map.of(), body, "application/json", null);
    }

    public Request withClient(String newClient) {
        return new Request(method, path, query, body, contentType, newClient);
    }

    public Request withQuery(Map<String, String> newQuery) {
        return new Request(method, path, newQuery, body, contentType, client);
    }

    public Optional<String> param(String name) {
        return Optional.ofNullable(query.get(name));
    }
}
