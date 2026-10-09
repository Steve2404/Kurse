package ch19_final.projects.p03_api.solution;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RouterTest {

    private final List<Throwable> internalErrors = new ArrayList<>();

    // Chaque handler rend une reponse qui dit qui l'a produite et avec quels parametres.
    private Router router() {
        return new Router(internalErrors::add)
                .add("GET", "/tasks", (req, p) -> Response.json(200, new JsonString("liste")))
                .add("GET", "/tasks/{id}", (req, p) -> Response.json(200, new JsonString("une " + p)))
                .add("DELETE", "/tasks/{id}", (req, p) -> Response.noContent())
                .add("GET", "/tasks/{id}/history/{n}", (req, p) -> Response.json(200, new JsonString("histo " + p.get("id") + " " + p.get("n"))))
                .add("GET", "/tasks/stats", (req, p) -> Response.json(200, new JsonString("jamais : {id} passe avant")))
                .add("GET", "/boom", (req, p) -> {
                    throw new IllegalStateException("secret interne");
                })
                .add("GET", "/bad", (req, p) -> {
                    throw new IllegalArgumentException("mauvaise demande");
                })
                .add("GET", "/missing", (req, p) -> {
                    throw new NoSuchElementException("rien ici");
                })
                .add("GET", "/conflict", (req, p) -> {
                    throw new ConflictException(3, 0);
                })
                .add("GET", "/teapot", (req, p) -> {
                    throw new ApiException(418, "je suis une theiere");
                });
    }

    private static String error(Response r) {
        return ((JsonObject) r.body()).getString("error");
    }

    @Test
    void literalAndVariableSegments() {
        assertEquals(new JsonString("liste"), router().handle(Request.of("GET", "/tasks")).body());
        assertEquals(new JsonString("une {id=42}"), router().handle(Request.of("GET", "/tasks/42")).body());
        assertEquals(new JsonString("histo 7 2"), router().handle(Request.of("GET", "/tasks/7/history/2")).body());
    }

    @Test
    void firstMatchingRouteWins() {
        assertEquals(new JsonString("une {id=stats}"), router().handle(Request.of("GET", "/tasks/stats")).body());
    }

    @Test
    void unknownPathIs404() {
        Response r = router().handle(Request.of("GET", "/nope"));
        assertEquals(404, r.status());
        assertEquals("aucune route pour /nope", error(r));
        assertEquals(404, router().handle(Request.of("GET", "/tasks/1/2")).status());
        assertEquals(404, router().handle(Request.of("GET", "/")).status());
    }

    @Test
    void trailingSlashIsAnotherPath() {
        assertEquals(404, router().handle(Request.of("GET", "/tasks/")).status());
        assertEquals(404, router().handle(Request.of("GET", "/tasks//history/1")).status());
    }

    @Test
    void wrongMethodIs405WithAllowSortedAlphabetically() {
        Response r = router().handle(Request.of("PUT", "/tasks/5"));
        assertEquals(405, r.status());
        assertEquals("methode PUT non permise sur /tasks/5", error(r));
        assertEquals(Map.of("Allow", "DELETE, GET"), r.headers());
        assertEquals(Map.of("Allow", "GET"), router().handle(Request.of("POST", "/tasks")).headers());
    }

    @Test
    void exceptionsBecomeStatusCodes() {
        Router router = router();
        assertEquals(400, router.handle(Request.of("GET", "/bad")).status());
        assertEquals("mauvaise demande", error(router.handle(Request.of("GET", "/bad"))));
        assertEquals(404, router.handle(Request.of("GET", "/missing")).status());
        assertEquals("rien ici", error(router.handle(Request.of("GET", "/missing"))));
        assertEquals(409, router.handle(Request.of("GET", "/conflict")).status());
        assertEquals("tache 3 : version 0 perimee", error(router.handle(Request.of("GET", "/conflict"))));
        assertEquals(418, router.handle(Request.of("GET", "/teapot")).status());
        assertEquals(List.of(), internalErrors);
    }

    @Test
    void unexpectedErrorIs500WithoutDetails() {
        Response r = router().handle(Request.of("GET", "/boom"));
        assertEquals(500, r.status());
        assertEquals("{\"error\":\"erreur interne\"}", JsonWriter.compact(r.body()));
        assertEquals(1, internalErrors.size());
        assertEquals("secret interne", internalErrors.get(0).getMessage());
    }

    @Test
    void responsesAreImmutable() {
        Response r = Response.noContent();
        assertEquals(204, r.status());
        assertNull(r.body());
        Response withHeader = r.withHeader("X-Test", "1");
        assertEquals(Map.of(), r.headers());
        assertEquals(Map.of("X-Test", "1"), withHeader.headers());
        assertEquals("{\"error\":\"oups\"}", JsonWriter.compact(Response.error(400, "oups").body()));
    }

    @Test
    void requestHelpers() {
        Request get = Request.of("GET", "/tasks");
        assertEquals(Map.of(), get.query());
        assertEquals("", get.body());
        assertNull(get.contentType());
        Request post = Request.json("POST", "/tasks", "{}");
        assertEquals("application/json", post.contentType());
        assertEquals("{}", post.body());
        Request q = new Request("GET", "/tasks", Map.of("page", "2"), "", null);
        assertEquals("2", q.param("page").orElseThrow());
        assertEquals(true, q.param("size").isEmpty());
    }
}
