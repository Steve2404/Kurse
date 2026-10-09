package ch19_final.projects.p08_atelier.solution;

import java.util.Objects;

/** Une chaine, deja decodee (les echappements comme \n sont devenus de vrais caracteres). */
public record JsonString(String value) implements Json {

    public JsonString {
        Objects.requireNonNull(value, "value");
    }
}
