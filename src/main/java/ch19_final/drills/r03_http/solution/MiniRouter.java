package ch19_final.drills.r03_http.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/** L'aiguillage : methode + modele de chemin ; 404, 405 avec Allow ; et les exceptions traduites en codes. */
public final class MiniRouter {

    private record Entry(String method, List<String> segments, Route route) {
    }

    private final List<Entry> entries = new ArrayList<>();

    public MiniRouter add(String method, String template, Route route) {
        entries.add(new Entry(method, segments(template), route));
        return this;
    }

    public HttpReply dispatch(String method, String path, String body) {
        try {
            return find(method, path, body);
        } catch (IllegalArgumentException e) {
            return HttpReply.of(400, e.getMessage());
        } catch (NoSuchElementException e) {
            return HttpReply.of(404, e.getMessage());
        } catch (RuntimeException e) {
            return HttpReply.of(500, "erreur interne");
        }
    }

    private HttpReply find(String method, String path, String body) {
        List<String> actual = segments(path);
        TreeSet<String> allowed = new TreeSet<>();
        for (Entry e : entries) {
            Map<String, String> params = match(e.segments(), actual);
            if (params != null && e.method().equals(method)) {
                return e.route().handle(params, body);
            }
            if (params != null) {
                allowed.add(e.method());
            }
        }
        if (allowed.isEmpty()) {
            return HttpReply.of(404, "aucune route pour " + path);
        }
        return HttpReply.of(405, "methode " + method + " non permise").withHeader("Allow", String.join(", ", allowed));
    }

    /** Les variables du chemin, ou null s'il ne correspond pas. */
    private static Map<String, String> match(List<String> template, List<String> path) {
        if (template.size() != path.size()) {
            return null;
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < template.size(); i++) {
            String t = template.get(i);
            if (t.startsWith("{") && t.endsWith("}") && !path.get(i).isEmpty()) {
                params.put(t.substring(1, t.length() - 1), path.get(i));
            } else if (!t.equals(path.get(i))) {
                return null;
            }
        }
        return params;
    }

    private static List<String> segments(String path) {
        return List.of(path.substring(1).split("/", -1));
    }
}
