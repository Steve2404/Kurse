package ch19_final.projects.p08_atelier.solution;

import java.util.Map;

/** Ce qui repond a une route : la requete, et les morceaux variables du chemin ({id} -> "42"). */
@FunctionalInterface
public interface Handler {

    Response handle(Request request, Map<String, String> pathParams);
}
