package ch19_final.projects.p08_atelier.solution;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Les routes de l'atelier (le TaskApi du projet 3, qui a grandi) : les taches, leurs deplacements, leur
 * historique, le tableau, et les mesures. Les handlers lisent, appellent le service, ecrivent : les regles
 * sont dans Board, les codes d'erreur dans le Router.
 */
public final class AtelierApi {

    static final int DEFAULT_PAGE_SIZE = 20;

    private final BoardService service;

    private AtelierApi(BoardService service) {
        this.service = service;
    }

    public static Router routes(BoardService service, ApiMetrics metrics, Consumer<Throwable> onInternalError) {
        AtelierApi api = new AtelierApi(service);
        return new Router(onInternalError)
                .add("GET", "/health", (r, p) -> Response.json(200, JsonObject.builder().add("status", "ok").build()))
                .add("GET", "/metrics", (r, p) -> Response.json(200, metrics.toJson()))
                .add("GET", "/board", api::board)
                .add("GET", "/tasks", api::list)
                .add("POST", "/tasks", api::create)
                .add("GET", "/tasks/{id}", (r, p) -> Response.json(200, TaskJson.toJson(service.get(id(p)))))
                .add("PUT", "/tasks/{id}", api::update)
                .add("DELETE", "/tasks/{id}", api::delete)
                .add("POST", "/tasks/{id}/move", api::move)
                .add("GET", "/tasks/{id}/history", api::history);
    }

    private Response board(Request request, Map<String, String> params) {
        JsonObject.Builder json = JsonObject.builder();
        service.board().forEach(json::add);
        return Response.json(200, json.build());
    }

    private Response list(Request request, Map<String, String> params) {
        int page = intParam(request, "page", 0);
        int size = intParam(request, "size", DEFAULT_PAGE_SIZE);
        String column = request.param("column").orElse(null);
        return Response.json(200, new JsonArray(service.page(column, page, size).stream().map(t -> (Json) TaskJson.toJson(t)).toList()));
    }

    private Response create(Request request, Map<String, String> params) {
        requireJson(request);
        Task created = service.create(TaskJson.newTask(request.body()));
        return Response.json(201, TaskJson.toJson(created)).withHeader("Location", "/tasks/" + created.id());
    }

    private Response update(Request request, Map<String, String> params) {
        requireJson(request);
        return Response.json(200, TaskJson.toJson(service.update(TaskJson.task(id(params), request.body()))));
    }

    private Response delete(Request request, Map<String, String> params) {
        service.delete(id(params));
        return Response.noContent();
    }

    private Response move(Request request, Map<String, String> params) {
        requireJson(request);
        TaskJson.Move move = TaskJson.move(request.body());
        return Response.json(200, TaskJson.toJson(service.move(id(params), move.to(), move.version())));
    }

    private Response history(Request request, Map<String, String> params) {
        return Response.json(200, new JsonArray(service.history(id(params)).stream().map(line -> (Json) new JsonString(line)).toList()));
    }

    // ------------------------------------------------------------------ lire la requete (projet 3)

    private static long id(Map<String, String> params) {
        String text = params.get("id");
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("identifiant invalide : " + text);
        }
    }

    private static int intParam(Request request, String name, int defaultValue) {
        String text = request.param(name).orElse(null);
        if (text == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("parametre " + name + " invalide : " + text);
        }
    }

    private static void requireJson(Request request) {
        String type = request.contentType();
        if (type == null || !type.toLowerCase().startsWith("application/json")) {
            throw new ApiException(415, "Content-Type application/json attendu");
        }
    }
}
