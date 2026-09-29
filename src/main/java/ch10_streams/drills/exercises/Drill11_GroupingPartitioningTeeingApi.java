package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
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
 * DRILL 11 - groupingBy, partitioningBy, tous les collecteurs "en aval", teeing (projet bibliotheque)
 * ==================================================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Presque tous les resultats sont des TreeMap : main() compare leur
 * toString(), donc l'ordre des cles doit etre trie.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : booksByGenre()          [groupingBy(classifieur)] Map<String, List<Book>>.
 * TODO 2  : countByGenre()          [groupingBy(cl, counting())]
 * TODO 3  : titlesByGenre()         [groupingBy(cl, TreeMap::new, mapping(f, toList()))]
 * TODO 4  : titlesByAuthorJoined()  [mapping(f, joining(", "))] TreeMap.
 * TODO 5  : tagsByGenre()           [flatMapping(f, toCollection(TreeSet::new))] TreeMap.
 * TODO 6  : expensiveTitlesByGenre()[filtering(p, mapping(...))] prix >= 8.0, TOUS les genres
 *           gardent leur cle (liste vide si besoin). TreeMap.
 * TODO 7  : avgPriceByGenre()       [averagingDouble] TreeMap.
 * TODO 8  : thickestTitleByGenre()  [collectingAndThen(maxBy(...), finisseur)] TreeMap.
 * TODO 9  : countByDecade()         [groupingBy(lambda calculee, TreeMap::new, counting())]
 * TODO 10 : thickOrNot()            [partitioningBy(p, mapping(...))] pages > 300.
 * TODO 11 : countCheapOrNot(limit)  [partitioningBy(p, counting())] prix < limit.
 * TODO 12 : countByGenreThenAuthor()[groupingBy imbrique] TreeMap de TreeMap.
 * TODO 13 : pageStatsByGenre()      [summarizingInt en aval]
 * TODO 14 : lateDaysByMember()      [summingInt en aval] sur LOANS, TreeMap.
 * TODO 15 : loanTitlesByMonth()     [mapping avec une recherche dans BOOKS] TreeMap mois ->
 *           titres empruntes ce mois-la, dans l'ordre des emprunts.
 * TODO 16 : yearRange()             [teeing(minBy, maxBy, fusion)] -> "1937-1984".
 * TODO 17 : genreReport()           [teeing EN AVAL de groupingBy] TreeMap genre ->
 *           "N livre(s), moyenne P".
 * TODO 18 : minorsAndAdults()       [partitioningBy sur MEMBERS + joining(",")] age >= 18.
 * TODO 19 : genresByAuthor()        [groupingBy(cl, toSet())]
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   groupingBy(f)  groupingBy(f, aval)  groupingBy(f, fabriqueMap, aval)
 *   partitioningBy(p)  partitioningBy(p, aval)   -> cles false ET true toujours presentes
 *   Avals : counting() summingXxx averagingXxx summarizingXxx minBy maxBy
 *           mapping(f, aval) filtering(p, aval) (9) flatMapping(f -> Stream, aval) (9)
 *           collectingAndThen(aval, finisseur) toList() toSet() toCollection(...)
 *           joining(...) reducing(...) teeing(c1, c2, fusion) (12)
 *           ... et un autre groupingBy / partitioningBy
 * ---------------------------------------------------------------------
 */
public class Drill11_GroupingPartitioningTeeingApi {

    public static Map<String, List<Book>> booksByGenre() {
        throw new UnsupportedOperationException("TODO 1 : implementer booksByGenre()");
    }

    public static Map<String, Long> countByGenre() {
        throw new UnsupportedOperationException("TODO 2 : implementer countByGenre()");
    }

    public static TreeMap<String, List<String>> titlesByGenre() {
        throw new UnsupportedOperationException("TODO 3 : implementer titlesByGenre()");
    }

    public static TreeMap<String, String> titlesByAuthorJoined() {
        throw new UnsupportedOperationException("TODO 4 : implementer titlesByAuthorJoined()");
    }

    public static TreeMap<String, TreeSet<String>> tagsByGenre() {
        throw new UnsupportedOperationException("TODO 5 : implementer tagsByGenre()");
    }

    public static TreeMap<String, List<String>> expensiveTitlesByGenre() {
        throw new UnsupportedOperationException("TODO 6 : implementer expensiveTitlesByGenre()");
    }

    public static TreeMap<String, Double> avgPriceByGenre() {
        throw new UnsupportedOperationException("TODO 7 : implementer avgPriceByGenre()");
    }

    public static TreeMap<String, String> thickestTitleByGenre() {
        throw new UnsupportedOperationException("TODO 8 : implementer thickestTitleByGenre()");
    }

    public static TreeMap<Integer, Long> countByDecade() {
        throw new UnsupportedOperationException("TODO 9 : implementer countByDecade()");
    }

    public static Map<Boolean, List<String>> thickOrNot() {
        throw new UnsupportedOperationException("TODO 10 : implementer thickOrNot()");
    }

    public static Map<Boolean, Long> countCheapOrNot(double limit) {
        throw new UnsupportedOperationException("TODO 11 : implementer countCheapOrNot()");
    }

    public static TreeMap<String, TreeMap<String, Long>> countByGenreThenAuthor() {
        throw new UnsupportedOperationException("TODO 12 : implementer countByGenreThenAuthor()");
    }

    public static Map<String, IntSummaryStatistics> pageStatsByGenre() {
        throw new UnsupportedOperationException("TODO 13 : implementer pageStatsByGenre()");
    }

    public static TreeMap<String, Integer> lateDaysByMember() {
        throw new UnsupportedOperationException("TODO 14 : implementer lateDaysByMember()");
    }

    public static TreeMap<Integer, List<String>> loanTitlesByMonth() {
        throw new UnsupportedOperationException("TODO 15 : implementer loanTitlesByMonth()");
    }

    public static String yearRange() {
        throw new UnsupportedOperationException("TODO 16 : implementer yearRange()");
    }

    public static TreeMap<String, String> genreReport() {
        throw new UnsupportedOperationException("TODO 17 : implementer genreReport()");
    }

    public static Map<Boolean, String> minorsAndAdults() {
        throw new UnsupportedOperationException("TODO 18 : implementer minorsAndAdults()");
    }

    public static Map<String, Set<String>> genresByAuthor() {
        throw new UnsupportedOperationException("TODO 19 : implementer genresByAuthor()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  booksByGenre : SF a 4 livres, 4 genres",
                booksByGenre().get("SF").size() == 4 && booksByGenre().size() == 4);
        ExerciseChecker.check("2  countByGenre == {Conte=1, Dystopie=2, Fantasy=1, SF=4}",
                new TreeMap<>(countByGenre()).toString().equals("{Conte=1, Dystopie=2, Fantasy=1, SF=4}"));
        ExerciseChecker.check("3  titlesByGenre", titlesByGenre().toString().equals(
                "{Conte=[Le Petit Prince], Dystopie=[1984, La Ferme des animaux], Fantasy=[Le Hobbit], "
                        + "SF=[Dune, Fondation, Les Robots, Neuromancien]}"));
        ExerciseChecker.check("4  titlesByAuthorJoined", titlesByAuthorJoined().toString().equals(
                "{Asimov=Fondation, Les Robots, Gibson=Neuromancien, Herbert=Dune, Orwell=1984, La Ferme des animaux, "
                        + "Saint-Exupery=Le Petit Prince, Tolkien=Le Hobbit}"));
        ExerciseChecker.check("5  tagsByGenre", tagsByGenre().toString().equals(
                "{Conte=[classique, enfance], Dystopie=[classique, fable, politique], Fantasy=[aventure, enfance], "
                        + "SF=[classique, cyberpunk, empire, espace, robots]}"));
        ExerciseChecker.check("6  expensiveTitlesByGenre (Conte garde sa cle vide)", expensiveTitlesByGenre().toString().equals(
                "{Conte=[], Dystopie=[1984], Fantasy=[Le Hobbit], SF=[Dune, Fondation, Neuromancien]}"));
        ExerciseChecker.check("7  avgPriceByGenre", avgPriceByGenre().toString().equals(
                "{Conte=6.0, Dystopie=7.0, Fantasy=10.0, SF=8.5}"));
        ExerciseChecker.check("8  thickestTitleByGenre", thickestTitleByGenre().toString().equals(
                "{Conte=Le Petit Prince, Dystopie=1984, Fantasy=Le Hobbit, SF=Dune}"));
        ExerciseChecker.check("9  countByDecade", countByDecade().toString().equals("{1930=1, 1940=3, 1950=2, 1960=1, 1980=1}"));
        ExerciseChecker.check("10 thickOrNot", thickOrNot().toString().equals(
                "{false=[Fondation, Les Robots, Le Petit Prince, La Ferme des animaux, Neuromancien], "
                        + "true=[Dune, 1984, Le Hobbit]}"));
        ExerciseChecker.check("11 countCheapOrNot(5.0) == {false=8, true=0}, (8.0) == {false=5, true=3}",
                countCheapOrNot(5.0).toString().equals("{false=8, true=0}")
                        && countCheapOrNot(8.0).toString().equals("{false=5, true=3}"));
        ExerciseChecker.check("12 countByGenreThenAuthor", countByGenreThenAuthor().toString().equals(
                "{Conte={Saint-Exupery=1}, Dystopie={Orwell=2}, Fantasy={Tolkien=1}, SF={Asimov=2, Gibson=1, Herbert=1}}"));
        ExerciseChecker.check("13 pageStatsByGenre : SF max 412, Dystopie somme 440",
                pageStatsByGenre().get("SF").getMax() == 412 && pageStatsByGenre().get("Dystopie").getSum() == 440);
        ExerciseChecker.check("14 lateDaysByMember == {M1=0, M2=8, M3=11}",
                lateDaysByMember().toString().equals("{M1=0, M2=8, M3=11}"));
        ExerciseChecker.check("15 loanTitlesByMonth", loanTitlesByMonth().toString().equals(
                "{1=[Dune, 1984], 2=[Le Hobbit, Fondation], 3=[Dune, Neuromancien, Le Petit Prince, La Ferme des animaux]}"));
        ExerciseChecker.check("16 yearRange == 1937-1984", yearRange().equals("1937-1984"));
        ExerciseChecker.check("17 genreReport", genreReport().toString().equals(
                "{Conte=1 livre(s), moyenne 6.0, Dystopie=2 livre(s), moyenne 7.0, "
                        + "Fantasy=1 livre(s), moyenne 10.0, SF=4 livre(s), moyenne 8.5}"));
        ExerciseChecker.check("18 minorsAndAdults == {false=Lea, true=Hugo,Ines,Tom}",
                minorsAndAdults().toString().equals("{false=Lea, true=Hugo,Ines,Tom}"));
        ExerciseChecker.check("19 genresByAuthor : Orwell -> [Dystopie], Asimov -> [SF]",
                genresByAuthor().get("Orwell").equals(Set.of("Dystopie")) && genresByAuthor().get("Asimov").equals(Set.of("SF")));

        ExerciseChecker.summary();
    }
}
