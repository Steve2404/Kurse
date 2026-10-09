package ch19_final.drills.r03_http.solution;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 3. */
class Recall03Test {

    private static MiniRouter router() {
        return new MiniRouter()
                .add("GET", "/tasks", (p, b) -> HttpReply.of(200, "liste"))
                .add("GET", "/tasks/{id}", (p, b) -> HttpReply.of(200, "tache " + p.get("id")))
                .add("DELETE", "/tasks/{id}", (p, b) -> new HttpReply(204, null, Map.of()))
                .add("POST", "/echo", (p, b) -> HttpReply.of(201, "reçu : " + b))
                .add("GET", "/tasks/{id}/notes/{n}", (p, b) -> HttpReply.of(200, p.get("id") + "/" + p.get("n")))
                .add("GET", "/bad", (p, b) -> {
                    throw new IllegalArgumentException("mauvaise demande");
                })
                .add("GET", "/missing", (p, b) -> {
                    throw new NoSuchElementException("rien ici");
                })
                .add("GET", "/boom", (p, b) -> {
                    throw new IllegalStateException("secret");
                });
    }

    @Test
    void d01() {
        MiniRouter r = router();
        assertEquals(HttpReply.of(200, "liste"), r.dispatch("GET", "/tasks", ""));
        assertEquals(HttpReply.of(200, "tache 42"), r.dispatch("GET", "/tasks/42", ""));
        assertEquals(HttpReply.of(200, "7/3"), r.dispatch("GET", "/tasks/7/notes/3", ""));
        assertEquals(HttpReply.of(201, "reçu : salut"), r.dispatch("POST", "/echo", "salut"));
    }

    @Test
    void d02() {
        MiniRouter r = router();
        assertEquals(HttpReply.of(404, "aucune route pour /nope"), r.dispatch("GET", "/nope", ""));
        assertEquals(404, r.dispatch("GET", "/tasks/", "").status());
        assertEquals(404, r.dispatch("GET", "/tasks/1/2", "").status());
        HttpReply notAllowed = r.dispatch("PUT", "/tasks/5", "");
        assertEquals(405, notAllowed.status());
        assertEquals("methode PUT non permise", notAllowed.body());
        assertEquals(Map.of("Allow", "DELETE, GET"), notAllowed.headers());
    }

    @Test
    void d03() {
        MiniRouter r = router();
        assertEquals(HttpReply.of(400, "mauvaise demande"), r.dispatch("GET", "/bad", ""));
        assertEquals(HttpReply.of(404, "rien ici"), r.dispatch("GET", "/missing", ""));
        assertEquals(HttpReply.of(500, "erreur interne"), r.dispatch("GET", "/boom", ""));
        HttpReply withHeader = HttpReply.of(200, "x").withHeader("X-A", "1");
        assertEquals(Map.of("X-A", "1"), withHeader.headers());
        assertEquals(Map.of(), HttpReply.of(200, "x").headers());
    }

    @Test
    void d04() {
        assertEquals(Map.of(), Query.parse(null));
        assertEquals(Map.of(), Query.parse(""));
        assertEquals(Map.of("a", "1", "b", ""), Query.parse("a=1&b"));
        assertEquals(Map.of("q", "x&y=z", "r", "é t"), Query.parse("q=x%26y%3Dz&r=%C3%A9+t"));
        assertEquals(Map.of("a", "1"), Query.parse("a=1&a=2"));
    }

    @Test
    void d05() throws Exception {
        try (MiniServer server = MiniServer.start(0, router())) {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
            String base = "http://127.0.0.1:" + server.port();
            HttpResponse<String> get = client.send(HttpRequest.newBuilder(URI.create(base + "/tasks/9")).build(),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, get.statusCode());
            assertEquals("tache 9", get.body());
            assertEquals("text/plain; charset=utf-8", get.headers().firstValue("Content-Type").orElseThrow());
            HttpResponse<String> post = client.send(HttpRequest.newBuilder(URI.create(base + "/echo"))
                    .POST(HttpRequest.BodyPublishers.ofString("vélo €")).build(), HttpResponse.BodyHandlers.ofString());
            assertEquals(201, post.statusCode());
            assertEquals("reçu : vélo €", post.body());
            HttpResponse<String> delete = client.send(HttpRequest.newBuilder(URI.create(base + "/tasks/9")).DELETE().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(204, delete.statusCode());
            assertEquals("", delete.body());
            HttpResponse<String> patch = client.send(HttpRequest.newBuilder(URI.create(base + "/tasks/9"))
                    .method("PATCH", HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString());
            assertEquals("DELETE, GET", patch.headers().firstValue("Allow").orElseThrow());
        }
    }

    @Test
    void d06() throws Exception {
        MiniServer server = MiniServer.start(0, router());
        int port = server.port();
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
        HttpResponse<String> exact = client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/echo"))
                .POST(HttpRequest.BodyPublishers.ofString("x".repeat(1024))).build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(201, exact.statusCode());
        HttpResponse<String> big = client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/echo"))
                .POST(HttpRequest.BodyPublishers.ofString("x".repeat(1025))).build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(413, big.statusCode());
        assertEquals("corps trop gros", big.body());
        server.close();
        HttpRequest after = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/tasks")).timeout(Duration.ofSeconds(2)).build();
        assertTrue(assertThrows(IOException.class, () -> client.send(after, HttpResponse.BodyHandlers.ofString())) != null);
    }
}
