package ch19_final.projects.p03_api.solution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests d'INTEGRATION : un vrai serveur sur un port libre, un vrai client HTTP. */
class WebServerTest {

    private InMemoryTaskRepository tasks;
    private WebServer server;
    private HttpClient client;

    @BeforeEach
    void startServer() throws IOException {
        tasks = new InMemoryTaskRepository();
        tasks.create(new NewTask("Pneu", "A faire", 2));
        server = WebServer.start(0, TaskApi.routes(tasks, e -> { }));
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    }

    @AfterEach
    void stopServer() {
        server.close();
    }

    private HttpRequest.Builder request(String pathAndQuery) {
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + server.port() + pathAndQuery)).timeout(Duration.ofSeconds(5));
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpRequest.BodyPublisher json(String text) {
        return HttpRequest.BodyPublishers.ofString(text);
    }

    @Test
    void portZeroGivesARealFreePort() {
        assertTrue(server.port() > 0);
    }

    @Test
    void getReturnsJsonInUtf8() throws Exception {
        tasks.create(new NewTask("Régler la chaîne €", "Fini", 1));
        HttpResponse<String> r = send(request("/tasks/2").build());
        assertEquals(200, r.statusCode());
        assertEquals("application/json; charset=utf-8", r.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("{\"id\":2,\"title\":\"Régler la chaîne €\",\"column\":\"Fini\",\"points\":1,\"version\":0}", r.body());
    }

    @Test
    void postReturns201AndLocation() throws Exception {
        HttpResponse<String> r = send(request("/tasks").header("Content-Type", "application/json")
                .POST(json("{\"title\":\"Selle\",\"column\":\"Fini\",\"points\":1}")).build());
        assertEquals(201, r.statusCode());
        assertEquals("/tasks/2", r.headers().firstValue("Location").orElseThrow());
        assertEquals("Selle", tasks.find(2).orElseThrow().title());
    }

    @Test
    void queryParametersAreDecoded() throws Exception {
        tasks.create(new NewTask("Selle", "En cours", 1));
        HttpResponse<String> r = send(request("/tasks?column=En%20cours&size=5").build());
        assertEquals("[{\"id\":2,\"title\":\"Selle\",\"column\":\"En cours\",\"points\":1,\"version\":0}]", r.body());
        HttpResponse<String> plus = send(request("/tasks?column=En+cours").build());
        assertEquals(r.body(), plus.body());
    }

    @Test
    void queryParsing() {
        assertEquals(Map.of(), WebServer.query(null));
        assertEquals(Map.of(), WebServer.query(""));
        assertEquals(Map.of("a", "1", "b", ""), WebServer.query("a=1&b"));
        assertEquals(Map.of("q", "x&y=z", "r", "é t"), WebServer.query("q=x%26y%3Dz&r=%C3%A9+t"));
        assertEquals(Map.of("a", "1"), WebServer.query("a=1&a=2"));
    }

    @Test
    void deleteHasNoBody() throws Exception {
        HttpResponse<String> r = send(request("/tasks/1").DELETE().build());
        assertEquals(204, r.statusCode());
        assertEquals("", r.body());
        assertTrue(r.headers().firstValue("Content-Type").isEmpty());
        assertEquals(404, send(request("/tasks/1").DELETE().build()).statusCode());
    }

    @Test
    void errorsTravelAsJson() throws Exception {
        HttpResponse<String> notFound = send(request("/nowhere").build());
        assertEquals(404, notFound.statusCode());
        assertEquals("{\"error\":\"aucune route pour /nowhere\"}", notFound.body());
        HttpResponse<String> notAllowed = send(request("/tasks/1").method("PATCH", json("{}")).build());
        assertEquals(405, notAllowed.statusCode());
        assertEquals("DELETE, GET, PUT", notAllowed.headers().firstValue("Allow").orElseThrow());
        HttpResponse<String> noType = send(request("/tasks").POST(json("{}")).build());
        assertEquals(415, noType.statusCode());
    }

    @Test
    void bodyUpTo64KiBIsAccepted() throws Exception {
        String title = "x".repeat(150);
        String padding = " ".repeat(WebServer.MAX_BODY_BYTES - 200);
        String body = "{\"title\":\"" + title + "\",\"column\":\"A faire\",\"points\":1}" + padding;
        assertTrue(body.length() <= WebServer.MAX_BODY_BYTES);
        HttpResponse<String> r = send(request("/tasks").header("Content-Type", "application/json").POST(json(body)).build());
        assertEquals(201, r.statusCode());
    }

    @Test
    void bodyOver64KiBIs413() throws Exception {
        String body = "{\"title\":\"" + "x".repeat(WebServer.MAX_BODY_BYTES) + "\",\"column\":\"A faire\",\"points\":1}";
        HttpResponse<String> r = send(request("/tasks").header("Content-Type", "application/json").POST(json(body)).build());
        assertEquals(413, r.statusCode());
        assertEquals("{\"error\":\"corps trop gros (65536 octets au plus)\"}", r.body());
        assertEquals(1, tasks.count());
    }

    @Test
    void fiftyConcurrentPostsGetFiftyDistinctIds() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                futures.add(client.sendAsync(request("/tasks").header("Content-Type", "application/json")
                        .POST(json("{\"title\":\"t" + i + "\",\"column\":\"A faire\",\"points\":1}")).build(),
                        HttpResponse.BodyHandlers.ofString()));
            }
            Set<String> locations = new TreeSet<>();
            for (CompletableFuture<HttpResponse<String>> f : futures) {
                HttpResponse<String> r = f.join();
                assertEquals(201, r.statusCode());
                locations.add(r.headers().firstValue("Location").orElseThrow());
            }
            assertEquals(50, locations.size());
            assertEquals(51, tasks.count());
        });
    }

    @Test
    void closedServerRefusesConnections() throws Exception {
        int port = server.port();
        server.close();
        HttpRequest r = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/health")).timeout(Duration.ofSeconds(2)).build();
        assertTrue(org.junit.jupiter.api.Assertions.assertThrows(IOException.class, () -> send(r)) != null);
    }
}
