package ch10_streams.solutions;

import java.util.Arrays;
import java.util.List;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise08_FlatMapAdvanced.
 */
public class Solution08_FlatMapAdvanced {

    public record Item(String name, String category, int quantity, double unitPrice) {
    }

    public record Order(String id, String customer, List<Item> items) {
    }

    public static List<String> allItemNames(List<Order> orders) {
        // map(o -> o.items()) donnerait des LISTES ; flatMap vide tous les sacs sur la meme table.
        return orders.stream()
                .flatMap(o -> o.items().stream())
                .map(Item::name)
                .distinct()
                .sorted()
                .toList();
    }

    public static int totalQuantity(List<Order> orders) {
        // flatMapToInt aplatit directement en IntStream (pas de Stream<Integer> intermediaire).
        return orders.stream()
                .flatMapToInt(o -> o.items().stream().mapToInt(Item::quantity))
                .sum();
    }

    public static List<String> distinctWords(List<String> sentences) {
        // \\s+ = un ou plusieurs blancs. Une phrase qui commence par un blanc donne un "" en tete :
        // on le filtre. Minuscules AVANT distinct, sinon "Le" et "le" seraient differents.
        return sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .filter(w -> !w.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }

    public static List<String> page(List<String> items, int pageNumber, int pageSize) {
        // Pages numerotees a partir de 1 : on saute (page - 1) * taille elements. Le cast en long
        // evite un debordement d'int pour de tres grands numeros de page.
        return items.stream()
                .skip((long) (pageNumber - 1) * pageSize)
                .limit(pageSize)
                .toList();
    }

    public static List<String> customersWhoBought(List<Order> orders, String category) {
        // Pas besoin d'aplatir : on garde les COMMANDES dont un article correspond (anyMatch).
        // distinct car un client peut avoir plusieurs commandes.
        return orders.stream()
                .filter(o -> containsCategory(o, category))
                .map(Order::customer)
                .distinct()
                .sorted()
                .toList();
    }

    private static boolean containsCategory(Order order, String category) {
        // Petite boite magique : un stream DANS le predicat d'un autre stream.
        return order.items().stream().anyMatch(i -> i.category().equals(category));
    }

    public static List<String> itemLabels(List<Order> orders) {
        // Le map est A L'INTERIEUR du flatMap : c'est la seule facon de garder o (la commande)
        // visible au moment de fabriquer l'etiquette.
        return orders.stream()
                .flatMap(o -> o.items().stream().map(i -> o.id() + ":" + i.name()))
                .toList();
    }
}
