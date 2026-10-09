package ch19_final.projects.p03_api.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * L'aiguillage : une requete -> la route qui correspond (methode + modele de chemin comme /tasks/{id}).
 * C'est aussi le SEUL endroit qui transforme une exception en code HTTP : les handlers lancent des
 * exceptions metier, et ne connaissent pas les codes d'erreur.
 */
public final class Router {

    private record Route(String method, List<String> segments, Handler handler) {
    }

    private final List<Route> routes = new ArrayList<>();
    private final Consumer<Throwable> onInternalError;

    /** onInternalError recoit les erreurs inattendues (un bug) : on les note, le client ne voit que "erreur interne". */
    public Router(Consumer<Throwable> onInternalError) {
        this.onInternalError = onInternalError;
    }

    public Router add(String method, String template, Handler handler) {
        routes.add(new Route(method, segments(template), handler));
        return this;
    }

    public Response handle(Request request) {
        try {
            return dispatch(request);
        } catch (ApiException e) {
            return Response.error(e.status(), e.getMessage());
        } catch (IllegalArgumentException e) {
            return Response.error(400, e.getMessage());
        } catch (NoSuchElementException e) {
            return Response.error(404, e.getMessage());
        } catch (ConflictException e) {
            return Response.error(409, e.getMessage());
        } catch (RuntimeException e) {
            // Jamais la trace ni le message au client : ils peuvent reveler le code, la base, des donnees.
            onInternalError.accept(e);
            return Response.error(500, "erreur interne");
        }
    }

    private Response dispatch(Request request) {
        List<String> path = segments(request.path());
        TreeSet<String> allowed = new TreeSet<>();
        for (Route route : routes) {
            Optional<Map<String, String>> params = match(route.segments(), path);
            if (params.isPresent() && route.method().equals(request.method())) {
                return route.handler().handle(request, params.get());
            }
            params.ifPresent(p -> allowed.add(route.method()));
        }
        if (allowed.isEmpty()) {
            return Response.error(404, "aucune route pour " + request.path());
        }
        // Le chemin existe, pas avec cette methode : 405, et l'en-tete Allow dit lesquelles sont permises.
        return Response.error(405, "methode " + request.method() + " non permise sur " + request.path())
                .withHeader("Allow", String.join(", ", allowed));
    }

    private static Optional<Map<String, String>> match(List<String> template, List<String> path) {
        if (template.size() != path.size()) {
            return Optional.empty();
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < template.size(); i++) {
            String expected = template.get(i);
            boolean variable = expected.startsWith("{") && expected.endsWith("}");
            if (variable && !path.get(i).isEmpty()) {
                params.put(expected.substring(1, expected.length() - 1), path.get(i));
            } else if (!expected.equals(path.get(i))) {
                return Optional.empty();
            }
        }
        return Optional.of(params);
    }

    // "/tasks/42" -> [tasks, 42] ; "/tasks/" -> [tasks, ""] : un / final n'est pas le meme chemin.
    private static List<String> segments(String path) {
        return List.of(path.substring(1).split("/", -1));
    }
}
