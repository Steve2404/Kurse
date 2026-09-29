package ch10_streams.solutions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise03_OptionalConfigResolver.
 */
public class Solution03_OptionalConfigResolver {

    public record Source(String name, Map<String, String> values) {
    }

    public static Optional<String> lookup(Source source, String key) {
        // La brique de base : null -> vide, puis nettoyage, puis "blanc" -> vide.
        return Optional.ofNullable(source.values().get(key))
                .map(String::strip)
                .filter(v -> !v.isEmpty());
    }

    public static Optional<String> resolve(List<Source> sources, String key) {
        // Paresse : findFirst s'arrete a la 1re couche qui a une valeur, les suivantes ne sont
        // jamais ouvertes. flatMap(Optional::stream) retire les couches vides.
        return sources.stream()
                .map(s -> lookup(s, key))
                .flatMap(Optional::stream)
                .findFirst();
    }

    public static OptionalInt resolveInt(List<Source> sources, String key) {
        // Optional<String> n'a pas de mapToInt : on passe par Optional<Integer>, puis
        // map(OptionalInt::of) + orElseGet(OptionalInt::empty) pour obtenir une OptionalInt.
        return resolve(sources, key)
                .filter(v -> v.matches("-?\\d{1,9}"))
                .map(Integer::parseInt)
                .map(OptionalInt::of)
                .orElseGet(OptionalInt::empty);
    }

    public static int resolveIntInRange(List<Source> sources, String key, int min, int max, int defaultValue) {
        // OptionalInt n'a ni filter ni map : stream() (Java 9) donne un IntStream de 0 ou 1
        // element, qui lui sait filtrer.
        return resolveInt(sources, key).stream()
                .filter(v -> v >= min && v <= max)
                .findFirst()
                .orElse(defaultValue);
    }

    public static Optional<String> whoDefined(List<Source> sources, String key) {
        // On garde la SOURCE (pas la valeur) : filter sur la presence, puis map vers le nom.
        return sources.stream()
                .filter(s -> lookup(s, key).isPresent())
                .map(Source::name)
                .findFirst();
    }

    public static List<String> describeAll(List<Source> sources, List<String> keys) {
        // Chaque cle est decrite par une petite boite magique separee (describeOne).
        return keys.stream().map(k -> describeOne(sources, k)).toList();
    }

    private static String describeOne(List<Source> sources, String key) {
        // Motif "combiner 2 Optional" : flatMap sur le 1er, map sur le 2e a l'interieur.
        // Si l'un des deux est vide, le resultat est vide -> orElse.
        return resolve(sources, key)
                .flatMap(v -> whoDefined(sources, key).map(n -> key + "=" + v + " (" + n + ")"))
                .orElse(key + "=<absent>");
    }

    public static List<String> definedKeys(List<Source> sources) {
        // Le filter est A L'INTERIEUR du flatMap pour garder la source s a portee de main.
        return sources.stream()
                .flatMap(s -> s.values().keySet().stream().filter(k -> lookup(s, k).isPresent()))
                .distinct()
                .sorted()
                .toList();
    }
}
