package ch10_streams.projects.p01_loandesk.solution;

import java.util.Optional;

// TODO 2 : on garde la chaine brute (null possible) ; l'Optional n'est fabrique qu'a la lecture.
public record Member(String id, String name, String rawEmail) {
    public static Member parse(String line) {
        String[] p = line.split(";");
        // Piege : "M2;Hugo;" -> 2 morceaux seulement, p[2] lancerait ArrayIndexOutOfBoundsException.
        return new Member(p[0], p[1], p.length > 2 ? p[2] : null);
    }

    // ofNullable absorbe le null, map nettoie, filter rejette "  " : aucun if.
    public Optional<String> email() {
        return Optional.ofNullable(rawEmail).map(String::strip).filter(e -> !e.isBlank());
    }
}
