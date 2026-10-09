package ch19_final.projects.p08_atelier.solution;

import ch19_final.projects.p08_atelier.Data;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * De bout en bout : la vraie application (AtelierApp.create), une vraie base H2, un vrai serveur sur un port
 * libre, un vrai client HTTP. Seule l'horloge est manuelle, pour que les dates et le limiteur soient previsibles.
 */
class EndToEndTest {

    private final ManualClock clock = new ManualClock(Instant.parse("2026-10-09T10:15:00Z"));
    private final List<Throwable> internalErrors = new ArrayList<>();
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    private JdbcDataSource dataSource;
    private Connection keeper;
    private WebServer server;

    private void start(AtelierApp.Config config) throws SQLException, IOException {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID());
        keeper = dataSource.getConnection();
        server = WebServer.start(0, AtelierApp.create(dataSource, clock, config, internalErrors::add));
    }

    @AfterEach
    void stop() throws SQLException {
        if (server != null) {
            server.close();
        }
        if (keeper != null) {
            keeper.close();
        }
    }

    private HttpResponse<String> call(String method, String path, String json, String who) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + server.port() + path))
                .timeout(Duration.ofSeconds(5)).header("X-Client", who);
        if (json == null) {
            b.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            b.header("Content-Type", "application/json").method(method, HttpRequest.BodyPublishers.ofString(json));
        }
        return client.send(b.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> call(String method, String path, String json) throws Exception {
        return call(method, path, json, "test");
    }

    private static String task(String title) {
        return "{\"title\":\"" + title + "\",\"column\":\"A faire\",\"points\":2}";
    }

    private static String move(String to, int version) {
        return "{\"to\":\"" + to + "\",\"version\":" + version + "}";
    }

    @Test
    void aDayAtTheWorkshop() throws Exception {
        start(new AtelierApp.Config(2, 100, 10));
        assertEquals("{\"status\":\"ok\"}", call("GET", "/health", null).body());
        assertEquals("{\"A faire\":0,\"En cours\":0,\"Fini\":0}", call("GET", "/board", null).body());
        HttpResponse<String> created = call("POST", "/tasks", task("Pneu"));
        assertEquals(201, created.statusCode());
        assertEquals("/tasks/1", created.headers().firstValue("Location").orElseThrow());
        for (String title : List.of("Selle", "Freins", "Cadre")) {
            call("POST", "/tasks", task(title));
        }
        assertEquals("{\"id\":1,\"title\":\"Pneu\",\"column\":\"En cours\",\"points\":2,\"version\":1}",
                call("POST", "/tasks/1/move", move("En cours", 0)).body());
        assertEquals(200, call("POST", "/tasks/2/move", move("En cours", 0)).statusCode());
        HttpResponse<String> full = call("POST", "/tasks/3/move", move("En cours", 0));
        assertEquals(409, full.statusCode());
        assertEquals("{\"error\":\"limite atteinte : En cours (2 taches au plus)\"}", full.body());
        assertEquals(200, call("POST", "/tasks/1/move", move("Fini", 1)).statusCode());
        assertEquals(200, call("POST", "/tasks/3/move", move("En cours", 0)).statusCode());
        assertEquals("[\"2026-10-09T10:15 A faire -> En cours\",\"2026-10-09T10:15 En cours -> Fini\"]",
                call("GET", "/tasks/1/history", null).body());
        assertEquals("{\"A faire\":1,\"En cours\":2,\"Fini\":1}", call("GET", "/board", null).body());
        assertEquals(List.of(), internalErrors);
    }

    @Test
    void theRulesTravelAsHttpErrors() throws Exception {
        start(new AtelierApp.Config(2, 100, 10));
        call("POST", "/tasks", task("Pneu"));
        HttpResponse<String> wrongStart = call("POST", "/tasks", "{\"title\":\"X\",\"column\":\"Fini\",\"points\":1}");
        assertEquals(400, wrongStart.statusCode());
        assertEquals("{\"error\":\"une nouvelle tache commence dans A faire, pas dans Fini\"}", wrongStart.body());
        HttpResponse<String> jump = call("POST", "/tasks/1/move", move("Fini", 0));
        assertEquals("{\"error\":\"deplacement impossible : A faire -> Fini (une colonne a la fois)\"}", jump.body());
        call("POST", "/tasks/1/move", move("En cours", 0));
        HttpResponse<String> stale = call("POST", "/tasks/1/move", move("Fini", 0));
        assertEquals(409, stale.statusCode());
        assertEquals("{\"error\":\"tache 1 : version 0 perimee\"}", stale.body());
        assertEquals(400, call("PUT", "/tasks/1", "{\"title\":\"X\",\"column\":\"Fini\",\"points\":1,\"version\":1}").statusCode());
        assertEquals(200, call("PUT", "/tasks/1", "{\"title\":\"Pneu arriere\",\"column\":\"En cours\",\"points\":3,\"version\":1}").statusCode());
        assertEquals(404, call("GET", "/tasks/9/history", null).statusCode());
        assertEquals(415, call("POST", "/tasks/1/move", null).statusCode());
        assertEquals(204, call("DELETE", "/tasks/1", null).statusCode());
        assertEquals(404, call("GET", "/tasks/1", null).statusCode());
    }

    @Test
    void eachClientIsThrottledSeparately() throws Exception {
        start(new AtelierApp.Config(2, 3, 1));
        for (int i = 0; i < 3; i++) {
            assertEquals(200, call("GET", "/board", null, "ada").statusCode());
        }
        HttpResponse<String> refused = call("GET", "/board", null, "ada");
        assertEquals(429, refused.statusCode());
        assertEquals("1", refused.headers().firstValue("Retry-After").orElseThrow());
        assertEquals(200, call("GET", "/board", null, "bob").statusCode());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(200, call("GET", "/board", null, "ada").statusCode());
        String metrics = call("GET", "/metrics", null, "carl").body();
        assertTrue(metrics.startsWith("{\"requests\":6,\"clientErrors\":0,\"serverErrors\":0,\"throttled\":1,"), metrics);
    }

    @Test
    void legacyTasksAreImportedOnce() throws Exception {
        start(AtelierApp.DEFAULT);
        assertEquals(7, AtelierApp.importLegacy(dataSource, clock));
        assertEquals(0, AtelierApp.importLegacy(dataSource, clock));
        assertEquals("{\"A faire\":3,\"En cours\":2,\"Fini\":2}", call("GET", "/board", null).body());
        assertEquals(409, call("POST", "/tasks/3/move", move("En cours", 0)).statusCode());
        assertEquals(Data.SEED.size(), 7);
    }

    @Test
    void restartingKeepsTheData() throws Exception {
        start(AtelierApp.DEFAULT);
        call("POST", "/tasks", task("Pneu"));
        server.close();
        // Un nouveau demarrage sur la meme base : les migrations ne refont rien, la tache est toujours la.
        server = WebServer.start(0, AtelierApp.create(dataSource, clock, AtelierApp.DEFAULT, internalErrors::add));
        assertEquals("{\"A faire\":1,\"En cours\":0,\"Fini\":0}", call("GET", "/board", null).body());
    }
}
