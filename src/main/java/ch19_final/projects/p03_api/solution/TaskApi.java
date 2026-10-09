package ch19_final.projects.p03_api.solution;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Les routes de l'API des taches. Chaque handler fait trois choses : lire la requete, appeler le depot,
 * ecrire la reponse. Les erreurs sont des exceptions : le Router choisit le code HTTP.
 */
public final class TaskApi {

    static final int DEFAULT_PAGE_SIZE = 20;

    private final TaskRepository tasks;

    private TaskApi(TaskRepository tasks) {
        this.tasks = tasks;
    }

    public static Router routes(TaskRepository tasks, Consumer<Throwable> onInternalError) {
        TaskApi api = new TaskApi(tasks);
        return new Router(onInternalError)
                .add("GET", "/health", api::health)
                .add("GET", "/tasks", api::list)
                .add("POST", "/tasks", api::create)
                .add("GET", "/tasks/{id}", api::get)
                .add("PUT", "/tasks/{id}", api::update)
                .add("DELETE", "/tasks/{id}", api::delete);
    }

    private Response health(Request request, Map<String, String> params) {
        return Response.json(200, JsonObject.builder().add("status", "ok").add("tasks", tasks.count()).build());
    }

    private Response list(Request request, Map<String, String> params) {
        int page = intParam(request, "page", 0);
        int size = intParam(request, "size", DEFAULT_PAGE_SIZE);
        String column = request.param("column").orElse(null);
        return Response.json(200, new JsonArray(tasks.page(column, page, size).stream().map(t -> (Json) TaskJson.toJson(t)).toList()));
    }

    private Response create(Request request, Map<String, String> params) {
        requireJson(request);
        Task created = tasks.create(TaskJson.newTask(request.body()));
        // 201 Created, et Location dit ou trouver la nouvelle ressource.
        return Response.json(201, TaskJson.toJson(created)).withHeader("Location", "/tasks/" + created.id());
    }

    private Response get(Request request, Map<String, String> params) {
        long id = id(params);
        Task task = tasks.find(id).orElseThrow(() -> new NoSuchElementException("tache " + id + " introuvable"));
        return Response.json(200, TaskJson.toJson(task));
    }

    private Response update(Request request, Map<String, String> params) {
        requireJson(request);
        return Response.json(200, TaskJson.toJson(tasks.update(TaskJson.task(id(params), request.body()))));
    }

    private Response delete(Request request, Map<String, String> params) {
        long id = id(params);
        if (!tasks.delete(id)) {
            throw new NoSuchElementException("tache " + id + " introuvable");
        }
        return Response.noContent();
    }

    // ------------------------------------------------------------------ lire la requete

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

    // 415 Unsupported Media Type : le client doit dire qu'il envoie du JSON.
    private static void requireJson(Request request) {
        String type = request.contentType();
        if (type == null || !type.toLowerCase().startsWith("application/json")) {
            throw new ApiException(415, "Content-Type application/json attendu");
        }
    }
}
