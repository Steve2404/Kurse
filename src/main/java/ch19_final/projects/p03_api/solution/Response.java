package ch19_final.projects.p03_api.solution;

import java.util.LinkedHashMap;
import java.util.Map;

/** Une reponse : un code de statut, un corps JSON (null : pas de corps) et des en-tetes en plus. */
public record Response(int status, Json body, Map<String, String> headers) {

    public Response {
        headers = Map.copyOf(headers);
    }

    public static Response json(int status, Json body) {
        return new Response(status, body, Map.of());
    }

    /** Toutes les erreurs ont la meme forme : {"error": "..."}. Un client n'a qu'un cas a lire. */
    public static Response error(int status, String message) {
        return json(status, JsonObject.builder().add("error", message).build());
    }

    /** 204 No Content : la reponse a un DELETE reussi, sans corps. */
    public static Response noContent() {
        return new Response(204, null, Map.of());
    }

    public Response withHeader(String name, String value) {
        Map<String, String> more = new LinkedHashMap<>(headers);
        more.put(name, value);
        return new Response(status, body, more);
    }
}
