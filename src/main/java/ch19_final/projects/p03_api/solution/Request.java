package ch19_final.projects.p03_api.solution;

import java.util.Map;
import java.util.Optional;

/**
 * Une requete HTTP, sans rien de com.sun.net.httpserver : les regles de l'API se testent sans reseau,
 * avec de simples objets. contentType peut etre null (pas d'en-tete).
 */
public record Request(String method, String path, Map<String, String> query, String body, String contentType) {

    public Request {
        query = Map.copyOf(query);
    }

    /** Une requete sans corps (GET, DELETE). */
    public static Request of(String method, String path) {
        return new Request(method, path, Map.of(), "", null);
    }

    /** Une requete avec un corps JSON (POST, PUT). */
    public static Request json(String method, String path, String body) {
        return new Request(method, path, Map.of(), body, "application/json");
    }

    public Optional<String> param(String name) {
        return Optional.ofNullable(query.get(name));
    }
}
