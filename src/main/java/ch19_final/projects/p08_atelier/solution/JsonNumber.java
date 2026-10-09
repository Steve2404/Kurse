package ch19_final.projects.p08_atelier.solution;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Un nombre. BigDecimal et pas double : JSON n'a pas de limite de precision, et 0.1 + 0.2 doit rester exact
 * (des montants, des identifiants de 19 chiffres...). Piege : BigDecimal.equals compare aussi l'echelle (1.0 != 1.00).
 */
public record JsonNumber(BigDecimal value) implements Json {

    public JsonNumber {
        Objects.requireNonNull(value, "value");
    }

    /** Fabrique pour les entiers : JsonNumber.of(42). */
    public static JsonNumber of(long n) {
        return new JsonNumber(BigDecimal.valueOf(n));
    }
}
