package ch10_streams.solutions;

import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise18_CollectorsAdvanced.
 */
public class Solution18_CollectorsAdvanced {

    public record Product(String name, String category, double price, int stock) {
    }

    public static TreeMap<String, Integer> stockByName(List<Product> products) {
        // 4 arguments : cle, valeur, que faire des doublons (somme), quelle Map (TreeMap = triee).
        return products.stream()
                .collect(Collectors.toMap(Product::name, Product::stock, Integer::sum, TreeMap::new));
    }

    public static Map<String, Product> indexByName(List<Product> products) {
        // Volontairement SANS fusion : un doublon lance IllegalStateException("Duplicate key ...").
        return products.stream().collect(Collectors.toMap(Product::name, Function.identity()));
    }

    public static String catalogLine(List<Product> products) {
        // joining(sep, prefixe, suffixe) : les crochets sont ajoutes meme si le stream est vide.
        return products.stream()
                .map(Product::name)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static double averagePriceInStock(List<Product> products) {
        // averagingDouble rend 0.0 pour un stream vide (pas un Optional, contrairement a average()).
        return products.stream()
                .filter(p -> p.stock() > 0)
                .collect(Collectors.averagingDouble(Product::price));
    }

    public static List<String> lockedSortedNames(List<Product> products) {
        // collectingAndThen : on collecte, PUIS on applique le finisseur (verrouillage) au resultat.
        return products.stream()
                .map(Product::name)
                .sorted()
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public static IntSummaryStatistics stockStatistics(List<Product> products) {
        // summarizingInt = la version Collector de summaryStatistics().
        return products.stream().collect(Collectors.summarizingInt(Product::stock));
    }

    public static LinkedHashMap<String, String> namesByCategoryInArrivalOrder(List<Product> products) {
        // LinkedHashMap retient l'ordre d'insertion ; la fusion concatene ancienne + nouvelle valeur.
        return products.stream()
                .collect(Collectors.toMap(Product::category, Product::name, (a, b) -> a + "|" + b, LinkedHashMap::new));
    }

    public static double totalInventoryValue(List<Product> products) {
        // summingDouble = la version Collector de mapToDouble(...).sum().
        return products.stream().collect(Collectors.summingDouble(p -> p.price() * p.stock()));
    }
}
