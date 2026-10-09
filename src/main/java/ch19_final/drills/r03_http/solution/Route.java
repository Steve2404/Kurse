package ch19_final.drills.r03_http.solution;

import java.util.Map;

/** Ce qui repond a une route : les variables du chemin ({id} -> "42") et le corps de la requete. */
@FunctionalInterface
public interface Route {

    HttpReply handle(Map<String, String> params, String body);
}
