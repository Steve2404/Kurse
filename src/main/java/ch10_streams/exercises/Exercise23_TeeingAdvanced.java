package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * EXERCICE 23 - teeing() avance : 2 collecteurs en 1 passage, teeing imbrique, teeing en aval de groupingBy (niveau : difficile/capstone)
 * ==================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Rappel : Collectors.teeing(c1, c2, fusion) (Java 12) --
 *
 * Un tuyau en "T" : chaque element est envoye A LA FOIS au collecteur
 * c1 et au collecteur c2, en UN SEUL parcours du stream. A la fin,
 * fusion(resultat1, resultat2) produit le resultat final. C'est LA
 * solution quand on a besoin de 2 informations sur un stream qu'on ne
 * peut parcourir qu'une fois.
 *
 * Les petits records SumCount, MinMax, Stats, Order, DaySales et
 * CustomerReport sont DEJA ecrits pour toi en bas de la classe :
 * utilise-les comme "boites de resultat" intermediaires.
 *
 *
 * ==================================================================
 * TODO 1 : spread(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'ecart entre le plus grand et le plus petit. minBy et maxBy rendent
 * chacun une boite (Optional) - un stream vide n'a ni min ni max.
 * Dans la fusion, on rend 0 si la boite est vide.
 *
 * -- Essayons a la main --
 *
 *   [4, 9, 1, 7] -> max 9 - min 1 = 8
 *   []           -> 0
 *
 * -- Le plan --
 *
 *   1. teeing du min et du max (ordre naturel).
 *   2. Fusion : max - min si present, sinon 0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : passReport(scores, passMark)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "3/5 (60%)" : combien ont reussi (note >= passMark), sur combien, et
 * le pourcentage ENTIER (division entiere). Le 1er collecteur compte
 * seulement les reussites (filtering + counting), le 2e compte tout.
 * Attention a la division par zero si la liste est vide.
 *
 * -- Essayons a la main --
 *
 *   [12, 8, 15, 10, 9], passMark 10 -> reussis : 12, 15, 10 = 3 ; total 5
 *   3 * 100 / 5 = 60 -> "3/5 (60%)"
 *   [7, 10, 19], passMark 10 -> 2/3 -> 200 / 3 = 66 -> "2/3 (66%)"
 *   [] -> "0/0 (0%)"
 *
 * -- Le plan --
 *
 *   1. teeing (reussites comptees, total compte).
 *   2. Fusion : formater, avec 0% si le total vaut 0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : averageWithoutExtremes(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Aux concours de patinage, on retire la meilleure et la pire note
 * avant de faire la moyenne. Il faut 4 informations (somme, nombre,
 * min, max), mais teeing n'accepte que 2 collecteurs... Solution :
 * teeing de teeing ! Un teeing (somme, nombre) -> SumCount, un autre
 * teeing (min, max) -> MinMax, et un teeing exterieur qui combine les
 * deux. S'il y a moins de 3 notes, on rend 0.0.
 *
 * -- Essayons a la main --
 *
 *   [10, 2, 8, 6, 20] -> somme 46, nombre 5, min 2, max 20
 *   (46 - 2 - 20) / (5 - 2) = 24 / 3 = 8.0
 *   [1, 2] -> moins de 3 notes -> 0.0
 *
 * -- Le plan --
 *
 *   1. teeing(summingInt, counting) -> SumCount.
 *   2. teeing(minBy, maxBy)         -> MinMax.
 *   3. teeing exterieur de 1 et 2 : si count < 3 -> 0.0, sinon
 *      (sum - min - max) / (double) (count - 2).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui, pour la lisibilite : range chaque teeing interieur dans une
 * variable locale (type Collector<Integer, ?, SumCount> et
 * Collector<Integer, ?, MinMax>), puis ecris le teeing exterieur.
 *
 *
 * ==================================================================
 * TODO 4 : bestAndWorstDay(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le meilleur et le pire jour de ventes, en un passage. En cas
 * d'egalite pour le pire, on garde le PREMIER rencontre (c'est ce que
 * fait minBy).
 *
 * -- Essayons a la main --
 *
 *   [lun 120, mar 90, mer 200, jeu 90]
 *   -> "meilleur=mer(200), pire=mar(90)"
 *   [] -> "aucune donnee"
 *
 * -- Le plan --
 *
 *   1. teeing (maxBy montant, minBy montant).
 *   2. Fusion : si vide -> "aucune donnee", sinon formater.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : reportByCustomer(orders)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le rapport final : pour chaque client, son nombre de commandes et son
 * total depense, classe du plus gros total au plus petit (a egalite :
 * par nom). teeing peut servir d'AVAL a groupingBy : pour chaque
 * client, un teeing (counting, summingDouble) -> Stats. Mais le
 * collecteur en aval ne connait pas la CLE (le nom du client) : on
 * construit donc les CustomerReport ENSUITE, a partir des entrees de la
 * Map (entrySet().stream()).
 *
 * -- Essayons a la main --
 *
 *   commandes : Alice 120.0, Bob 80.0, Alice 30.5, Chloe 200.0, Bob 70.0, Dan 150.0
 *   Alice : 2 commandes, 150.5 ; Bob : 2, 150.0 ; Chloe : 1, 200.0 ; Dan : 1, 150.0
 *   tri total decroissant puis nom : Chloe 200.0, Alice 150.5, Bob 150.0, Dan 150.0
 *
 * -- Le plan --
 *
 *   1. Grouper par client ; en aval : teeing(compter, sommer) -> Stats.
 *   2. Stream des entrees : fabriquer un CustomerReport par entree.
 *   3. Trier (total decroissant, puis nom), rassembler.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Le comparateur de l'etape 3 merite une variable locale bien nommee.
 *
 *
 * Exemple a verifier :
 *
 *   spread([4, 9, 1, 7]) == 8 ; spread([]) == 0
 *   passReport([12, 8, 15, 10, 9], 10) == "3/5 (60%)" ; ([7, 10, 19], 10) == "2/3 (66%)" ; ([], 10) == "0/0 (0%)"
 *   averageWithoutExtremes([10, 2, 8, 6, 20]) == 8.0 ; ([1, 2]) == 0.0
 *   bestAndWorstDay == "meilleur=mer(200), pire=mar(90)" ; (vide) == "aucune donnee"
 *   reportByCustomer == [Chloe 1 200.0, Alice 2 150.5, Bob 2 150.0, Dan 1 150.0]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Collectors.teeing(Collectors.minBy(Comparator.naturalOrder()),
 *       Collectors.maxBy(Comparator.naturalOrder()), (mn, mx) -> ...)
 *   - Collectors.filtering(s -> s >= passMark, Collectors.counting())
 *   - Collector<Integer, ?, SumCount> sc = Collectors.teeing(
 *       Collectors.summingInt(x -> x), Collectors.counting(), SumCount::new);
 *   - Collectors.maxBy(Comparator.comparingInt(DaySales::amount))
 *   - Collectors.groupingBy(Order::customer, Collectors.teeing(
 *       Collectors.counting(), Collectors.summingDouble(Order::total), Stats::new))
 *   - Comparator.comparingDouble(CustomerReport::total).reversed()
 *       .thenComparing(CustomerReport::customer)
 */
public class Exercise23_TeeingAdvanced {

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
        throw new UnsupportedOperationException("TODO 1 : implementer spread()");
    }

    public static String passReport(List<Integer> scores, int passMark) {
        throw new UnsupportedOperationException("TODO 2 : implementer passReport()");
    }

    public static double averageWithoutExtremes(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 3 : implementer averageWithoutExtremes()");
    }

    public static String bestAndWorstDay(List<DaySales> sales) {
        throw new UnsupportedOperationException("TODO 4 : implementer bestAndWorstDay()");
    }

    public static List<CustomerReport> reportByCustomer(List<Order> orders) {
        throw new UnsupportedOperationException("TODO 5 : implementer reportByCustomer()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("spread([4, 9, 1, 7]) == 8", spread(List.of(4, 9, 1, 7)) == 8);
        ExerciseChecker.check("spread([]) == 0", spread(List.of()) == 0);

        ExerciseChecker.check("passReport([12, 8, 15, 10, 9], 10) == 3/5 (60%)",
                passReport(List.of(12, 8, 15, 10, 9), 10).equals("3/5 (60%)"));
        ExerciseChecker.check("passReport([7, 10, 19], 10) == 2/3 (66%)",
                passReport(List.of(7, 10, 19), 10).equals("2/3 (66%)"));
        ExerciseChecker.check("passReport([], 10) == 0/0 (0%)", passReport(List.of(), 10).equals("0/0 (0%)"));

        ExerciseChecker.check("averageWithoutExtremes([10, 2, 8, 6, 20]) == 8.0",
                averageWithoutExtremes(List.of(10, 2, 8, 6, 20)) == 8.0);
        ExerciseChecker.check("averageWithoutExtremes([1, 2]) == 0.0", averageWithoutExtremes(List.of(1, 2)) == 0.0);

        List<DaySales> week = List.of(new DaySales("lun", 120), new DaySales("mar", 90),
                new DaySales("mer", 200), new DaySales("jeu", 90));
        ExerciseChecker.check("bestAndWorstDay == meilleur=mer(200), pire=mar(90)",
                bestAndWorstDay(week).equals("meilleur=mer(200), pire=mar(90)"));
        ExerciseChecker.check("bestAndWorstDay(vide) == aucune donnee", bestAndWorstDay(List.of()).equals("aucune donnee"));

        List<Order> orders = List.of(new Order("Alice", 120.0), new Order("Bob", 80.0), new Order("Alice", 30.5),
                new Order("Chloe", 200.0), new Order("Bob", 70.0), new Order("Dan", 150.0));
        ExerciseChecker.check("reportByCustomer == [Chloe 1 200.0, Alice 2 150.5, Bob 2 150.0, Dan 1 150.0]",
                reportByCustomer(orders).equals(List.of(
                        new CustomerReport("Chloe", 1, 200.0),
                        new CustomerReport("Alice", 2, 150.5),
                        new CustomerReport("Bob", 2, 150.0),
                        new CustomerReport("Dan", 1, 150.0))));

        ExerciseChecker.summary();
    }
}
