package ch10_streams.solutions;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise07_SearchOperations.
 */
public class Solution07_SearchOperations {

    public static Optional<String> firstMatch(List<String> words, Predicate<String> predicate) {
        return words.stream().filter(predicate).findFirst();
    }

    public static boolean allStartWithUppercase(List<String> words) {
        return words.stream().allMatch(word -> !word.isEmpty() && Character.isUpperCase(word.charAt(0)));
    }

    public static boolean anyContainsDigit(List<String> words) {
        return words.stream().anyMatch(word -> word.chars().anyMatch(Character::isDigit));
    }

    public static boolean noneAreEmpty(List<String> words) {
        return words.stream().noneMatch(String::isEmpty);
    }
}
