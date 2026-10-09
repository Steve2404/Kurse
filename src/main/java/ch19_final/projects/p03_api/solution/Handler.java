package ch19_final.projects.p03_api.solution;

import java.util.Map;

/** Ce qui repond a une route : la requete, et les morceaux variables du chemin ({id} -> "42"). */
@FunctionalInterface
public interface Handler {

    Response handle(Request request, Map<String, String> pathParams);
}
