package ch16_testing.projects.p03_tax.solution;

import java.util.ArrayList;
import java.util.List;

/** Les regles des mots de passe du site des impots. Les violations sont toujours rendues dans le meme ordre. */
public final class PasswordPolicy {

    private PasswordPolicy() {
    }

    // Pourquoi une liste et pas un boolean : l'utilisateur doit savoir TOUT ce qui ne va pas, en une fois.
    // Piege : null et "" sont un seul cas a part ("vide"), sinon length() lancerait une NullPointerException.
    public static List<String> violations(String password) {
        if (password == null || password.isEmpty()) {
            return List.of("vide");
        }
        List<String> v = new ArrayList<>();
        if (password.length() < 12) {
            v.add("trop court");
        }
        if (password.length() > 64) {
            v.add("trop long");
        }
        if (password.chars().noneMatch(Character::isDigit)) {
            v.add("sans chiffre");
        }
        if (password.chars().noneMatch(Character::isUpperCase)) {
            v.add("sans majuscule");
        }
        if (password.chars().noneMatch(Character::isLowerCase)) {
            v.add("sans minuscule");
        }
        if (password.chars().allMatch(Character::isLetterOrDigit)) {
            v.add("sans symbole");
        }
        if (password.chars().anyMatch(Character::isWhitespace)) {
            v.add("espace interdit");
        }
        return List.copyOf(v);
    }

    public static boolean isValid(String password) {
        return violations(password).isEmpty();
    }
}
