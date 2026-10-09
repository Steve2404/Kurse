package ch19_final.drills.r03_http.solution;

import java.util.LinkedHashMap;
import java.util.Map;

/** Une reponse : le code, un corps texte (null : aucun corps), des en-tetes. */
public record HttpReply(int status, String body, Map<String, String> headers) {

    public HttpReply {
        headers = Map.copyOf(headers);
    }

    public static HttpReply of(int status, String body) {
        return new HttpReply(status, body, Map.of());
    }

    public HttpReply withHeader(String name, String value) {
        Map<String, String> more = new LinkedHashMap<>(headers);
        more.put(name, value);
        return new HttpReply(status, body, more);
    }
}
