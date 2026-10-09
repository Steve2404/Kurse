package ch19_final.projects.p03_api.solution;

import java.util.List;

/** Un tableau. List.copyOf : une copie non modifiable, et une valeur null est refusee (on ecrit new JsonNull()). */
public record JsonArray(List<Json> values) implements Json {

    public JsonArray {
        values = List.copyOf(values);
    }
}
