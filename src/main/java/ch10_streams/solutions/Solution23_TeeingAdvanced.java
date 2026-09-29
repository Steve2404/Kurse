package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 23. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise23_TeeingAdvanced.
 */
public class Solution23_TeeingAdvanced {

    public record SumCount(int sum, long count) {
    }

    public record MinMax(Optional<Integer> min, Optional<Integer> max) {
    }

    public record DaySales(String day, int amount) {
    }

    public record Order(String customer, double total) {
    }

    public record Stats(long count, double total) {
    }

    public record CustomerReport(String customer, long orderCount, double total) {
    }

    public static int spread(List<Integer> values) {
        // minBy et maxBy rendent des Optional : une liste vide n'a ni min ni max -> 0.
        return values.stream().collect(Collectors.teeing(
                Collectors.minBy(Comparator.<Integer>naturalOrder()),
                Collectors.maxBy(Comparator.<Integer>naturalOrder()),
                (mn, mx) -> mn.isPresent() ? mx.get() - mn.get() : 0));
    }

    public static String passReport(List<Integer> scores, int passMark) {
        // Le 1er collecteur compte les reussites, le 2e compte tout. total == 0 evite la division par zero.
        return scores.stream().collect(Collectors.teeing(
                Collectors.filtering(s -> s >= passMark, Collectors.counting()),
                Collectors.counting(),
                (passed, total) -> passed + "/" + total + " (" + (total == 0 ? 0 : passed * 100 / total) + "%)"));
    }

    public static double averageWithoutExtremes(List<Integer> values) {
        // teeing n'accepte que 2 collecteurs : pour 4 informations, on imbrique des teeing
        // (somme + nombre, min + max) dans un teeing exterieur.
        Collector<Integer, ?, SumCount> sumCount = Collectors.teeing(
                Collectors.summingInt(x -> x), Collectors.counting(), SumCount::new);
        Collector<Integer, ?, MinMax> minMax = Collectors.teeing(
                Collectors.minBy(Comparator.<Integer>naturalOrder()),
                Collectors.maxBy(Comparator.<Integer>naturalOrder()),
                MinMax::new);
        return values.stream().collect(Collectors.teeing(sumCount, minMax, (sc, mm) -> sc.count() < 3
                ? 0.0
                : (sc.sum() - mm.min().get() - mm.max().get()) / (double) (sc.count() - 2)));
    }

    public static String bestAndWorstDay(List<DaySales> sales) {
        // Un seul parcours pour le max et le min. minBy garde le 1er rencontre en cas d'egalite.
        return sales.stream().collect(Collectors.teeing(
                Collectors.maxBy(Comparator.comparingInt(DaySales::amount)),
                Collectors.minBy(Comparator.comparingInt(DaySales::amount)),
                (best, worst) -> best.isEmpty()
                        ? "aucune donnee"
                        : "meilleur=" + best.get().day() + "(" + best.get().amount() + "), pire="
                        + worst.get().day() + "(" + worst.get().amount() + ")"));
    }

    public static List<CustomerReport> reportByCustomer(List<Order> orders) {
        // Le collecteur en aval ne connait pas la CLE (le client) : on fabrique les
        // CustomerReport ensuite, a partir des entrees de la Map.
        Map<String, Stats> stats = orders.stream().collect(Collectors.groupingBy(
                Order::customer,
                Collectors.teeing(Collectors.counting(), Collectors.summingDouble(Order::total), Stats::new)));
        Comparator<CustomerReport> byTotalDescThenName = Comparator.comparingDouble(CustomerReport::total).reversed()
                .thenComparing(CustomerReport::customer);
        return stats.entrySet().stream()
                .map(e -> new CustomerReport(e.getKey(), e.getValue().count(), e.getValue().total()))
                .sorted(byTotalDescThenName)
                .toList();
    }
}
