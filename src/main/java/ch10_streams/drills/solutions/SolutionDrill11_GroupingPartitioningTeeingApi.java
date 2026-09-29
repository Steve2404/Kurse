package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Loan;
import ch10_streams.drills.Library.Member;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Corrige du drill 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill11_GroupingPartitioningTeeingApi.
 */
public class SolutionDrill11_GroupingPartitioningTeeingApi {

    public static Map<String, List<Book>> booksByGenre() {
        // groupingBy(classifieur) : Map<cle, List<element>>, sans ordre garanti (HashMap).
        return Library.BOOKS.stream().collect(Collectors.groupingBy(Book::genre));
    }

    public static Map<String, Long> countByGenre() {
        // counting() en aval : chaque liste est remplacee par son nombre d'elements.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(Book::genre, Collectors.counting()));
    }

    public static TreeMap<String, List<String>> titlesByGenre() {
        // mapping(f, toList()) : on ne garde que le titre dans chaque groupe.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new, Collectors.mapping(Book::title, Collectors.toList())));
    }

    public static TreeMap<String, String> titlesByAuthorJoined() {
        // mapping(f, joining(", ")) : les titres du groupe colles en une String.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::author, TreeMap::new, Collectors.mapping(Book::title, Collectors.joining(", "))));
    }

    public static TreeMap<String, TreeSet<String>> tagsByGenre() {
        // flatMapping (Java 9) : chaque livre fournit un stream de tags, aplati DANS le groupe.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new,
                Collectors.flatMapping(b -> b.tags().stream(), Collectors.toCollection(TreeSet::new))));
    }

    public static TreeMap<String, List<String>> expensiveTitlesByGenre() {
        // filtering en aval (Java 9) : les genres sans livre cher gardent leur cle, avec une liste vide.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new,
                Collectors.filtering(b -> b.price() >= 8.0, Collectors.mapping(Book::title, Collectors.toList()))));
    }

    public static TreeMap<String, Double> avgPriceByGenre() {
        // averagingDouble en aval : une moyenne par groupe.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new, Collectors.averagingDouble(Book::price)));
    }

    public static TreeMap<String, String> thickestTitleByGenre() {
        // maxBy rend un Optional ; collectingAndThen l'ouvre pour ne garder que le titre.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingInt(Book::pages)),
                        o -> o.map(Book::title).orElse("?"))));
    }

    public static TreeMap<Integer, Long> countByDecade() {
        // Le classifieur peut etre n'importe quel calcul, pas seulement un getter.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                b -> b.year() / 10 * 10, TreeMap::new, Collectors.counting()));
    }

    public static Map<Boolean, List<String>> thickOrNot() {
        // partitioningBy : toujours exactement 2 cles, false puis true.
        return Library.BOOKS.stream().collect(Collectors.partitioningBy(
                b -> b.pages() > 300, Collectors.mapping(Book::title, Collectors.toList())));
    }

    public static Map<Boolean, Long> countCheapOrNot(double limit) {
        // Meme si aucun livre ne correspond, la cle true existe (a 0).
        return Library.BOOKS.stream().collect(Collectors.partitioningBy(b -> b.price() < limit, Collectors.counting()));
    }

    public static TreeMap<String, TreeMap<String, Long>> countByGenreThenAuthor() {
        // Un groupingBy en aval d'un autre : deux niveaux de tiroirs.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new,
                Collectors.groupingBy(Book::author, TreeMap::new, Collectors.counting())));
    }

    public static Map<String, IntSummaryStatistics> pageStatsByGenre() {
        // summarizingInt en aval : des statistiques completes par groupe.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(Book::genre, Collectors.summarizingInt(Book::pages)));
    }

    public static TreeMap<String, Integer> lateDaysByMember() {
        // summingInt en aval : un total par membre.
        return Library.LOANS.stream().collect(Collectors.groupingBy(
                Loan::memberId, TreeMap::new, Collectors.summingInt(Loan::daysLate)));
    }

    public static TreeMap<Integer, List<String>> loanTitlesByMonth() {
        // mapping peut faire une recherche (ici dans BOOKS) pour transformer chaque element.
        return Library.LOANS.stream().collect(Collectors.groupingBy(
                Loan::month, TreeMap::new, Collectors.mapping(l -> titleOf(l.isbn()), Collectors.toList())));
    }

    private static String titleOf(String isbn) {
        // Petite recherche isbn -> titre, reutilisee dans le mapping.
        return Library.BOOKS.stream().filter(b -> b.isbn().equals(isbn)).map(Book::title).findFirst().orElse("?");
    }

    public static String yearRange() {
        // teeing : min et max en un seul parcours, puis la fusion assemble le texte.
        return Library.BOOKS.stream().collect(Collectors.teeing(
                Collectors.minBy(Comparator.comparingInt(Book::year)),
                Collectors.maxBy(Comparator.comparingInt(Book::year)),
                (mn, mx) -> mn.get().year() + "-" + mx.get().year()));
    }

    public static TreeMap<String, String> genreReport() {
        // teeing en aval : 2 informations (nombre et moyenne) par groupe.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::genre, TreeMap::new,
                Collectors.teeing(Collectors.counting(), Collectors.averagingDouble(Book::price),
                        (n, avg) -> n + " livre(s), moyenne " + avg)));
    }

    public static Map<Boolean, String> minorsAndAdults() {
        // partitioningBy + mapping + joining : les noms de chaque partie colles.
        return Library.MEMBERS.stream().collect(Collectors.partitioningBy(
                m -> m.age() >= 18, Collectors.mapping(Member::name, Collectors.joining(","))));
    }

    public static Map<String, Set<String>> genresByAuthor() {
        // mapping(f, toSet()) : les genres d'un auteur, sans doublon.
        return Library.BOOKS.stream().collect(Collectors.groupingBy(
                Book::author, Collectors.mapping(Book::genre, Collectors.toSet())));
    }
}
