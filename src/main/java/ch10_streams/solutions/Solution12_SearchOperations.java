package ch10_streams.solutions;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise12_SearchOperations.
 */
public class Solution12_SearchOperations {

    public static Optional<String> firstMatch(List<String> words, Predicate<String> predicate) {
        // filter + findFirst rend une boite : aucun mot ne correspond peut-etre.
        return words.stream().filter(predicate).findFirst();
    }

    public static boolean allStartWithUppercase(List<String> words) {
        // On verifie isEmpty() AVANT charAt(0) pour eviter une exception sur un mot vide.
        return words.stream().allMatch(word -> !word.isEmpty() && Character.isUpperCase(word.charAt(0)));
    }

    public static boolean anyContainsDigit(List<String> words) {
        // Un stream dans un stream : word.chars() est un IntStream de codes de caracteres.
        return words.stream().anyMatch(word -> word.chars().anyMatch(Character::isDigit));
    }

    public static boolean noneAreEmpty(List<String> words) {
        // noneMatch s'arrete au 1er mot vide trouve (court-circuit).
        return words.stream().noneMatch(String::isEmpty);
    }
}
