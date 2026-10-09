package ch18_design.drills.r03_builder.solution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Un code de ville a trois lettres majuscules ; la fabrique nettoie l'entree et partage les objets. */
public record City(String code) {

    private static final Map<String, City> CACHE = new ConcurrentHashMap<>();

    public City {
        if (!code.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("ville invalide : " + code);
        }
    }

    public static City of(String raw) {
        return CACHE.computeIfAbsent(raw.strip().toUpperCase(), City::new);
    }
}
