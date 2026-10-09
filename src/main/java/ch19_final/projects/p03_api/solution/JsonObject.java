package ch19_final.projects.p03_api.solution;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Un objet : des membres "cle": valeur, dans l'ordre du document.
 * Piege : Map.copyOf melangerait l'ordre (chapitre 18) ; on copie dans une LinkedHashMap, puis on l'enveloppe.
 * equals (celui de Map) ignore l'ordre : {"a":1,"b":2} est egal a {"b":2,"a":1}.
 */
public record JsonObject(Map<String, Json> members) implements Json {

    public JsonObject {
        members.forEach((key, value) -> {
            Objects.requireNonNull(key, "cle");
            Objects.requireNonNull(value, "valeur de " + key);
        });
        members = Collections.unmodifiableMap(new LinkedHashMap<>(members));
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<Json> get(String key) {
        return Optional.ofNullable(members.get(key));
    }

    // Les lectures typees : un champ absent ou du mauvais type est une erreur du client, avec le nom du champ.
    public String getString(String key) {
        if (members.get(key) instanceof JsonString s) {
            return s.value();
        }
        throw wrongType(key, "chaine attendue");
    }

    public long getLong(String key) {
        if (members.get(key) instanceof JsonNumber n) {
            try {
                // longValueExact refuse 1.5 et les nombres trop grands, au lieu de les tronquer en silence.
                return n.value().longValueExact();
            } catch (ArithmeticException e) {
                throw wrongType(key, "nombre entier attendu");
            }
        }
        throw wrongType(key, "nombre entier attendu");
    }

    public boolean getBoolean(String key) {
        if (members.get(key) instanceof JsonBool b) {
            return b.value();
        }
        throw wrongType(key, "booleen attendu");
    }

    private static IllegalArgumentException wrongType(String key, String expected) {
        return new IllegalArgumentException("champ " + key + " : " + expected);
    }

    /** Construit un objet membre par membre, dans l'ordre des appels. */
    public static final class Builder {

        private final Map<String, Json> members = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder add(String key, Json value) {
            if (members.putIfAbsent(key, Objects.requireNonNull(value, key)) != null) {
                throw new IllegalArgumentException("cle en double : " + key);
            }
            return this;
        }

        public Builder add(String key, String value) {
            return add(key, new JsonString(value));
        }

        public Builder add(String key, long value) {
            return add(key, JsonNumber.of(value));
        }

        public Builder add(String key, boolean value) {
            return add(key, new JsonBool(value));
        }

        public JsonObject build() {
            return new JsonObject(members);
        }
    }
}
