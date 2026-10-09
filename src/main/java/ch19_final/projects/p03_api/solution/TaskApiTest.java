package ch19_final.projects.p03_api.solution;

import ch19_final.projects.p03_api.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les regles de l'API, sans reseau : des Request en entree, des Response en sortie. Rapide et precis. */
class TaskApiTest {

    private InMemoryTaskRepository tasks;
    private Router api;
    private final List<Throwable> internalErrors = new ArrayList<>();

    @BeforeEach
    void seed() {
        tasks = new InMemoryTaskRepository();
        for (String[] row : Data.SEED) {
            tasks.create(new NewTask(row[0], row[1], Integer.parseInt(row[2])));
        }
        api = TaskApi.routes(tasks, internalErrors::add);
    }

    private Response call(Request request) {
        return api.handle(request);
    }

    private static String body(Response r) {
        return r.body() == null ? null : JsonWriter.compact(r.body());
    }

    private static Request get(String path, Map<String, String> query) {
        return new Request("GET", path, query, "", null);
    }

    @Test
    void health() {
        Response r = call(Request.of("GET", "/health"));
        assertEquals(200, r.status());
        assertEquals("{\"status\":\"ok\",\"tasks\":7}", body(r));
    }

    @Test
    void getOneTask() {
        Response r = call(Request.of("GET", "/tasks/2"));
        assertEquals(200, r.status());
        assertEquals("{\"id\":2,\"title\":\"Regler les freins\",\"column\":\"En cours\",\"points\":3,\"version\":0}", body(r));
    }

    @Test
    void getUnknownOrInvalidId() {
        assertEquals("{\"error\":\"tache 99 introuvable\"}", body(call(Request.of("GET", "/tasks/99"))));
        assertEquals(404, call(Request.of("GET", "/tasks/99")).status());
        Response r = call(Request.of("GET", "/tasks/abc"));
        assertEquals(400, r.status());
        assertEquals("{\"error\":\"identifiant invalide : abc\"}", body(r));
        assertEquals(400, call(Request.of("GET", "/tasks/99999999999999999999")).status());
    }

    @Test
    void listDefaultsToFirstPageOf20() {
        Response r = call(Request.of("GET", "/tasks"));
        assertEquals(200, r.status());
        assertEquals(7, ((JsonArray) r.body()).values().size());
        for (int i = 0; i < 30; i++) {
            tasks.create(new NewTask("t" + i, "A faire", 1));
        }
        assertEquals(20, ((JsonArray) call(Request.of("GET", "/tasks")).body()).values().size());
    }

    @Test
    void listWithColumnPageAndSize() {
        Response r = call(get("/tasks", Map.of("column", "A faire", "page", "1", "size", "2")));
        assertEquals("[{\"id\":5,\"title\":\"Remise 50% sur les antivols\",\"column\":\"A faire\",\"points\":2,\"version\":0}]", body(r));
        assertEquals("[]", body(call(get("/tasks", Map.of("column", "Archive")))));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "page | x   | parametre page invalide : x",
            "size | 2.5 | parametre size invalide : 2.5",
            "size | 0   | taille de page invalide : 0 (1 a 100)",
            "size | 101 | taille de page invalide : 101 (1 a 100)",
            "page | -1  | page negative : -1"})
    void badListParametersAre400(String name, String value, String message) {
        Response r = call(get("/tasks", Map.of(name, value)));
        assertEquals(400, r.status());
        assertEquals(message, ((JsonObject) r.body()).getString("error"));
    }

    @Test
    void createReturns201WithLocation() {
        Response r = call(Request.json("POST", "/tasks", "{\"title\":\"Vélo cargo\",\"column\":\"A faire\",\"points\":3}"));
        assertEquals(201, r.status());
        assertEquals(Map.of("Location", "/tasks/8"), r.headers());
        assertEquals("{\"id\":8,\"title\":\"Vélo cargo\",\"column\":\"A faire\",\"points\":3,\"version\":0}", body(r));
        assertEquals(8, tasks.count());
    }

    @Test
    void createIgnoresUnknownFieldsAndAcceptsCharset() {
        Request r = new Request("POST", "/tasks", Map.of(), "{\"points\":1,\"column\":\"Fini\",\"title\":\"Selle\",\"color\":\"rouge\"}",
                "application/json; charset=utf-8");
        assertEquals(201, call(r).status());
        assertEquals("Selle", tasks.find(8).orElseThrow().title());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', quoteCharacter = '"', value = {
            "{\"title\":\"Pneu\",\"column\":\"A faire\"}                 | champ points : nombre entier attendu",
            "{\"title\":\"Pneu\",\"column\":\"A faire\",\"points\":1    | JSON invalide : ligne 1, colonne 46 : fin du texte inattendue",
            "[1, 2]                                                    | objet JSON attendu",
            "{\"column\":\"A faire\",\"points\":1}                         | champ title : chaine attendue",
            "{\"title\":\"  \",\"column\":\"A faire\",\"points\":1}         | titre vide",
            "{\"title\":\"Pneu\",\"column\":\"A faire\",\"points\":-2}      | points negatifs : -2",
            "{\"title\":\"Pneu\",\"column\":\"A faire\",\"points\":1.5}     | champ points : nombre entier attendu",
            "{\"title\":\"Pneu\",\"column\":\"A faire\",\"points\":3000000000}  | champ points : hors limites",
            "{\"title\":\"Pneu\",\"column\":\"A faire\",\"points\":-3000000000} | champ points : hors limites",
            "{\"title\":\"Pneu\",\"column\":7,\"points\":1}              | champ column : chaine attendue"})
    void badBodiesAre400AndCreateNothing(String json, String message) {
        Response r = call(Request.json("POST", "/tasks", json.strip()));
        assertEquals(400, r.status());
        assertEquals(message, ((JsonObject) r.body()).getString("error"));
        assertEquals(7, tasks.count());
    }

    @Test
    void emptyBodyIs400() {
        Response r = call(Request.json("POST", "/tasks", ""));
        assertEquals(400, r.status());
        assertEquals("JSON invalide : ligne 1, colonne 1 : fin du texte inattendue", ((JsonObject) r.body()).getString("error"));
    }

    @Test
    void createWithoutJsonContentTypeIs415() {
        String json = "{\"title\":\"Pneu\",\"column\":\"A faire\",\"points\":1}";
        Response noType = call(new Request("POST", "/tasks", Map.of(), json, null));
        assertEquals(415, noType.status());
        assertEquals("{\"error\":\"Content-Type application/json attendu\"}", body(noType));
        assertEquals(415, call(new Request("POST", "/tasks", Map.of(), json, "text/plain")).status());
        assertEquals(201, call(new Request("POST", "/tasks", Map.of(), json, "Application/JSON")).status());
        assertEquals(8, tasks.count());
    }

    @Test
    void updateWithTheRightVersion() {
        Response r = call(Request.json("PUT", "/tasks/3", "{\"title\":\"Commander 20 chambres\",\"column\":\"En cours\",\"points\":2,\"version\":0}"));
        assertEquals(200, r.status());
        assertEquals("{\"id\":3,\"title\":\"Commander 20 chambres\",\"column\":\"En cours\",\"points\":2,\"version\":1}", body(r));
        assertEquals(1, tasks.find(3).orElseThrow().version());
    }

    @Test
    void updateWithAStaleVersionIs409() {
        String body = "{\"title\":\"X\",\"column\":\"Fini\",\"points\":2,\"version\":0}";
        assertEquals(200, call(Request.json("PUT", "/tasks/3", body)).status());
        Response r = call(Request.json("PUT", "/tasks/3", body));
        assertEquals(409, r.status());
        assertEquals("{\"error\":\"tache 3 : version 0 perimee\"}", body(r));
    }

    @Test
    void updateErrors() {
        String body = "{\"title\":\"X\",\"column\":\"Fini\",\"points\":2,\"version\":0}";
        assertEquals(404, call(Request.json("PUT", "/tasks/42", body)).status());
        assertEquals(415, call(new Request("PUT", "/tasks/3", Map.of(), body, null)).status());
        Response noVersion = call(Request.json("PUT", "/tasks/3", "{\"title\":\"X\",\"column\":\"Fini\",\"points\":2}"));
        assertEquals(400, noVersion.status());
        assertEquals("champ version : nombre entier attendu", ((JsonObject) noVersion.body()).getString("error"));
        assertEquals(400, call(Request.json("PUT", "/tasks/x", body)).status());
    }

    @Test
    void deleteIs204ThenNotFound() {
        Response r = call(Request.of("DELETE", "/tasks/4"));
        assertEquals(204, r.status());
        assertEquals(null, r.body());
        assertEquals(6, tasks.count());
        assertEquals(404, call(Request.of("DELETE", "/tasks/4")).status());
    }

    @Test
    void methodsNotAllowed() {
        Response r = call(Request.of("PATCH", "/tasks/1"));
        assertEquals(405, r.status());
        assertEquals("DELETE, GET, PUT", r.headers().get("Allow"));
        assertEquals("GET, POST", call(Request.of("DELETE", "/tasks")).headers().get("Allow"));
        assertEquals("GET", call(Request.of("POST", "/health")).headers().get("Allow"));
    }

    @Test
    void bugInTheRepositoryIs500AndIsReported() {
        Router router = TaskApi.routes(new BrokenRepository(), internalErrors::add);
        Response r = router.handle(Request.of("GET", "/health"));
        assertEquals(500, r.status());
        assertEquals("{\"error\":\"erreur interne\"}", body(r));
        assertEquals(1, internalErrors.size());
        assertTrue(internalErrors.get(0) instanceof NullPointerException);
    }

    /** Un depot bogue : count() lance une NullPointerException (une erreur de programmation, pas du client). */
    static final class BrokenRepository implements TaskRepository {
        private final TaskRepository inner = new InMemoryTaskRepository();

        public Task create(NewTask task) {
            return inner.create(task);
        }

        public java.util.Optional<Task> find(long id) {
            return inner.find(id);
        }

        public List<Task> page(String column, int page, int size) {
            return inner.page(column, page, size);
        }

        public Task update(Task task) {
            return inner.update(task);
        }

        public boolean delete(long id) {
            return inner.delete(id);
        }

        public int count() {
            String nothing = null;
            return nothing.length();
        }
    }
}
