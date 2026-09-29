package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Corrige du drill 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill08_ReduceAndCollectApi.
 */
public class SolutionDrill08_ReduceAndCollectApi {

    public static int totalPagesReduce() {
        // reduce(identite, operateur) : 0 est neutre pour +.
        return Library.BOOKS.stream().map(Book::pages).reduce(0, Integer::sum);
    }

    public static Optional<String> longestTitle() {
        // Sans identite, le resultat est en boite. > strict : a egalite, le 1er garde sa place.
        return Library.BOOKS.stream().map(Book::title).reduce((a, b) -> b.length() > a.length() ? b : a);
    }

    public static double totalPriceReduce3() {
        // Forme a 3 arguments : l'accumulateur combine un Double et un Book (types differents).
        return Library.BOOKS.stream().reduce(0.0, (acc, b) -> acc + b.price(), Double::sum);
    }

    public static String initials() {
        // collect a 3 arguments : on remplit un seul StringBuilder (reduction mutable).
        return Library.BOOKS.stream()
                .collect(StringBuilder::new, (sb, b) -> sb.append(b.title().charAt(0)), StringBuilder::append)
                .toString();
    }

    public static ArrayList<String> titlesArrayList() {
        // Fabrique, ajout d'UN element, fusion de deux conteneurs (pour le parallele).
        return Library.BOOKS.stream().map(Book::title).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static int totalPagesReducing() {
        // Collectors.reducing(identite, transformation, operateur) : equivalent de map + reduce.
        return Library.BOOKS.stream().collect(Collectors.reducing(0, Book::pages, Integer::sum));
    }

    public static Optional<Integer> latestYear() {
        // Collectors.reducing(operateur) sans identite rend un Optional.
        return Library.BOOKS.stream().map(Book::year).collect(Collectors.reducing(Integer::max));
    }

    public static double totalPriceReducing() {
        // Collectors.reducing(identite, operateur) apres un map.
        return Library.BOOKS.stream().map(Book::price).collect(Collectors.reducing(0.0, Double::sum));
    }

    public static TreeMap<String, Integer> pagesByGenre() {
        // reducing est surtout utile EN AVAL, ou reduce n'est pas disponible.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new, Collectors.reducing(0, Book::pages, Integer::sum)));
    }

    public static Optional<String> cheapestByReduce() {
        // BinaryOperator.minBy transforme un Comparator en operateur "garde le plus petit".
        return Library.BOOKS.stream()
                .reduce(BinaryOperator.minBy(Comparator.comparingDouble(Book::price)))
                .map(Book::title);
    }

    public static long countViaReduce() {
        // Chaque livre vaut 1, on additionne : c'est ce que fait count().
        return Library.BOOKS.stream().map(b -> 1L).reduce(0L, Long::sum);
    }

    public static String tagCountsAsText() {
        // Le resultat (String) n'a pas le type des elements (Book) : forme a 3 arguments.
        return Library.BOOKS.stream().reduce("", (acc, b) -> acc + b.tags().size(), String::concat);
    }

    public static TreeMap<String, Integer> lateDaysByMember() {
        // Le combiner additionne les cases avec merge : putAll ecraserait des totaux.
        return Library.LOANS.stream().collect(
                TreeMap::new,
                (m, l) -> m.merge(l.memberId(), l.daysLate(), Integer::sum),
                (m1, m2) -> m2.forEach((k, v) -> m1.merge(k, v, Integer::sum)));
    }
}
