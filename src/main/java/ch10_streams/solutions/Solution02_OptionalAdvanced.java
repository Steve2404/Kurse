package ch10_streams.solutions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise02_OptionalAdvanced.
 */
public class Solution02_OptionalAdvanced {

    public record Address(String city, String zip) {
    }

    public record User(String name, Address address) {
    }

    public static Optional<User> findUser(Map<String, User> directory, String id) {
        // Map.get rend null si la cle manque : ofNullable (et PAS of, qui lancerait une NPE).
        return Optional.ofNullable(directory.get(id));
    }

    public static String cityOf(Map<String, User> directory, String id) {
        // Chaque map() transforme une boite vide en boite vide : un null a n'importe quel
        // etage (adresse, ville) arrete tout, sans un seul if.
        return findUser(directory, id)
                .map(User::address)
                .map(Address::city)
                .map(String::toUpperCase)
                .orElse("INCONNUE");
    }

    public static Optional<Integer> parsePositive(String text) {
        // Ordre important : on filtre le TEXTE (que des chiffres, 9 maxi pour tenir dans un int)
        // AVANT de convertir, puis on filtre le NOMBRE (> 0).
        return Optional.ofNullable(text)
                .filter(s -> s.matches("\\d{1,9}"))
                .map(Integer::parseInt)
                .filter(n -> n > 0);
    }

    public static Optional<Integer> zipAsNumber(Map<String, User> directory, String id) {
        // parsePositive rend DEJA un Optional : flatMap evite la boite dans la boite
        // (map donnerait un Optional<Optional<Integer>>).
        return findUser(directory, id)
                .map(User::address)
                .map(Address::zip)
                .flatMap(Solution02_OptionalAdvanced::parsePositive);
    }

    public static Optional<String> firstAvailable(Optional<String> primary, Supplier<Optional<String>> secondary) {
        // or() (Java 9) reste dans le monde des boites et n'appelle secondary que si primary est vide.
        return primary.or(secondary);
    }

    public static void describe(Optional<String> opt, List<String> log) {
        // ifPresentOrElse : Consumer pour la boite pleine, Runnable (sans parametre) pour la vide.
        opt.ifPresentOrElse(v -> log.add("OK:" + v), () -> log.add("VIDE"));
    }

    public static List<String> allPresentValues(List<Optional<String>> boxes) {
        // Optional::stream donne 0 ou 1 element : flatMap fait disparaitre les boites vides.
        return boxes.stream().flatMap(Optional::stream).toList();
    }

    public static double averageOrZero(int[] values) {
        // IntStream.average() rend un OptionalDouble (tableau vide = pas de moyenne).
        return IntStream.of(values).average().orElse(0.0);
    }

    public static int maxOrThrow(int[] values) {
        // IntStream.max() rend un OptionalInt ; orElseThrow(Supplier) existe aussi sur les boites primitives.
        return IntStream.of(values).max().orElseThrow(() -> new IllegalArgumentException("tableau vide"));
    }
}
