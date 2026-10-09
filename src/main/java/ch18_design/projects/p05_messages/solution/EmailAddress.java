package ch18_design.projects.p05_messages.solution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Une adresse e-mail, normalisee et validee. On la cree par une FABRIQUE STATIQUE, of(...), et pas par
 * "new" : la fabrique a un nom, peut nettoyer l'entree, et peut rendre un objet DEJA existant (le cache).
 */
public record EmailAddress(String value) {

    private static final Map<String, EmailAddress> CACHE = new ConcurrentHashMap<>();

    public EmailAddress {
        if (!value.matches("[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}")) {
            throw new IllegalArgumentException("adresse invalide : " + value);
        }
    }

    // " Ada@Example.ORG " et "ada@example.org" sont la meme adresse : un seul objet pour les deux.
    public static EmailAddress of(String raw) {
        String normalized = raw.strip().toLowerCase();
        return CACHE.computeIfAbsent(normalized, EmailAddress::new);
    }

    @Override
    public String toString() {
        return value;
    }
}
