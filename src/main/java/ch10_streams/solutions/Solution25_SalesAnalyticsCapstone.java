package ch10_streams.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 25. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise25_SalesAnalyticsCapstone.
 */
public class Solution25_SalesAnalyticsCapstone {

    public record Sale(String id, String seller, String region, String product, int quantity, double unitPrice, int month) {
        public double amount() {
            return quantity * unitPrice;
        }
    }

    public static Stream<Sale> validSales(List<Sale> sales) {
        // La boite magique de base : on rend un Stream, chaque appelant le consomme une seule fois.
        return sales.stream().filter(s -> s.quantity() > 0);
    }

    public static TreeMap<Integer, Double> revenueByMonth(List<Sale> sales) {
        // TreeMap pour avoir les mois dans l'ordre (le TODO 5 en a besoin).
        return validSales(sales).collect(Collectors.groupingBy(
                Sale::month, TreeMap::new, Collectors.summingDouble(Sale::amount)));
    }

    public static TreeMap<String, String> bestSellerPerRegion(List<Sale> sales) {
        // Total par vendeur DANS chaque region (groupingBy en aval), puis le finisseur
        // garde la cle de plus grande valeur.
        return validSales(sales).collect(Collectors.groupingBy(
                Sale::region, TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.groupingBy(Sale::seller, Collectors.summingDouble(Sale::amount)),
                        Solution25_SalesAnalyticsCapstone::keyOfMaxValue)));
    }

    private static String keyOfMaxValue(Map<String, Double> totals) {
        // max sur les entrees, compare par valeur, puis on garde la cle.
        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("?");
    }

    public static List<String> topProducts(List<Sale> sales, int n) {
        // 2 temps : un collect pour les totaux, puis un nouveau stream sur entrySet() pour trier.
        Map<String, Double> totals = validSales(sales).collect(Collectors.groupingBy(
                Sale::product, Collectors.summingDouble(Sale::amount)));
        return totals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .map(e -> e.getKey() + ":" + e.getValue())
                .toList();
    }

    public static List<String> monthOverMonthGrowth(List<Sale> sales) {
        // Un stream ne sait pas regarder "l'element d'avant" : on parcourt des INDICES
        // avec IntStream.range et on compare i a i - 1.
        TreeMap<Integer, Double> byMonth = revenueByMonth(sales);
        List<Integer> months = new ArrayList<>(byMonth.keySet());
        List<Double> revenues = new ArrayList<>(byMonth.values());
        return IntStream.range(1, months.size())
                .mapToObj(i -> "M" + months.get(i) + ":"
                        + signed(Math.round((revenues.get(i) - revenues.get(i - 1)) * 100 / revenues.get(i - 1))) + "%")
                .toList();
    }

    private static String signed(long value) {
        // Le "+" n'est pas ajoute automatiquement pour les valeurs positives.
        return (value >= 0 ? "+" : "") + value;
    }

    public static List<String> sellerRanking(List<Sale> sales) {
        // Le rang = la position dans la liste triee, d'ou IntStream.range sur les indices.
        List<Map.Entry<String, Double>> ranked = validSales(sales)
                .collect(Collectors.groupingBy(Sale::seller, Collectors.summingDouble(Sale::amount)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .toList();
        return IntStream.range(0, ranked.size())
                .mapToObj(i -> (i + 1) + ". " + ranked.get(i).getKey() + " (" + ranked.get(i).getValue() + ")")
                .toList();
    }

    public static TreeMap<String, String> regionSummary(List<Sale> sales) {
        // teeing EN AVAL de groupingBy : 2 informations par region en un seul passage.
        return validSales(sales).collect(Collectors.groupingBy(
                Sale::region, TreeMap::new,
                Collectors.teeing(
                        Collectors.counting(),
                        Collectors.averagingDouble(Sale::amount),
                        (count, avg) -> count + " ventes / moyenne " + Math.round(avg))));
    }

    public static Optional<String> firstBigSaleAfter(List<Sale> sales, int month, double threshold) {
        // Les ventes sont deja chronologiques : findFirst donne la premiere qui correspond.
        return validSales(sales)
                .filter(s -> s.month() > month && s.amount() >= threshold)
                .findFirst()
                .map(Sale::id);
    }
}
