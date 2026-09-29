package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DRILL 06 - Toutes les operations TERMINALES simples de Stream (projet bibliotheque)
 * ==================================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * (reduce et collect ont leurs propres drills : 08 a 11.)
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : countOfGenre(genre)      [count] SF -> 4.
 * TODO 2  : cheapestTitle()          [min(Comparator)] -> Optional[La Ferme des animaux].
 * TODO 3  : thickestTitle()          [max(Comparator)] -> Optional[Dune].
 * TODO 4  : firstAfterYear(year)     [findFirst] 1er titre (ordre de BOOKS) publie apres year.
 * TODO 5  : anyAfterYear(year)       [findAny] UN livre publie apres year (n'importe lequel).
 * TODO 6  : hasAuthor(author)        [anyMatch]
 * TODO 7  : allHavePagesOver(n)      [allMatch]
 * TODO 8  : noneCostsMoreThan(p)     [noneMatch]
 * TODO 9  : allPoesieAreCheap()      [allMatch sur un stream VIDE] les livres du genre
 *           "Poesie" coutent-ils tous moins de 1 euro ? (il n'y en a aucun...)
 * TODO 10 : collectTitles(out)       [forEach] ajoute chaque titre a out.
 * TODO 11 : collectSortedTitles(out) [sorted + forEachOrdered]
 * TODO 12 : isbnArray()              [toArray()] -> Object[].
 * TODO 13 : titleArray()             [toArray(IntFunction)] -> String[].
 * TODO 14 : immutableTitles()        [toList()] (Java 16) non modifiable.
 * TODO 15 : mutableTitles()          [collect(Collectors.toList())] modifiable (en pratique).
 * TODO 16 : firstTwoTitles()         [iterator()] les 2 premiers titres via next().
 * TODO 17 : lateLoansCount()         [filter + count] emprunts rendus en retard -> 4.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   long count()
 *   Optional<T> min(Comparator) / max(Comparator)   (Comparator OBLIGATOIRE)
 *   Optional<T> findFirst() / findAny()
 *   boolean anyMatch / allMatch / noneMatch(Predicate)
 *        stream vide : anyMatch -> false, allMatch -> true, noneMatch -> true
 *   void forEach(Consumer)  void forEachOrdered(Consumer)
 *   Object[] toArray()      A[] toArray(IntFunction<A[]>)  ex : String[]::new
 *   List<T> toList() (16)   Iterator<T> iterator()
 * ---------------------------------------------------------------------
 */
public class Drill06_StreamTerminalOps {

    public static long countOfGenre(String genre) {
        throw new UnsupportedOperationException("TODO 1 : implementer countOfGenre()");
    }

    public static Optional<String> cheapestTitle() {
        throw new UnsupportedOperationException("TODO 2 : implementer cheapestTitle()");
    }

    public static Optional<String> thickestTitle() {
        throw new UnsupportedOperationException("TODO 3 : implementer thickestTitle()");
    }

    public static Optional<String> firstAfterYear(int year) {
        throw new UnsupportedOperationException("TODO 4 : implementer firstAfterYear()");
    }

    public static Optional<Book> anyAfterYear(int year) {
        throw new UnsupportedOperationException("TODO 5 : implementer anyAfterYear()");
    }

    public static boolean hasAuthor(String author) {
        throw new UnsupportedOperationException("TODO 6 : implementer hasAuthor()");
    }

    public static boolean allHavePagesOver(int n) {
        throw new UnsupportedOperationException("TODO 7 : implementer allHavePagesOver()");
    }

    public static boolean noneCostsMoreThan(double price) {
        throw new UnsupportedOperationException("TODO 8 : implementer noneCostsMoreThan()");
    }

    public static boolean allPoesieAreCheap() {
        throw new UnsupportedOperationException("TODO 9 : implementer allPoesieAreCheap()");
    }

    public static void collectTitles(List<String> out) {
        throw new UnsupportedOperationException("TODO 10 : implementer collectTitles()");
    }

    public static void collectSortedTitles(List<String> out) {
        throw new UnsupportedOperationException("TODO 11 : implementer collectSortedTitles()");
    }

    public static Object[] isbnArray() {
        throw new UnsupportedOperationException("TODO 12 : implementer isbnArray()");
    }

    public static String[] titleArray() {
        throw new UnsupportedOperationException("TODO 13 : implementer titleArray()");
    }

    public static List<String> immutableTitles() {
        throw new UnsupportedOperationException("TODO 14 : implementer immutableTitles()");
    }

    public static List<String> mutableTitles() {
        throw new UnsupportedOperationException("TODO 15 : implementer mutableTitles()");
    }

    public static List<String> firstTwoTitles() {
        throw new UnsupportedOperationException("TODO 16 : implementer firstTwoTitles()");
    }

    public static long lateLoansCount() {
        throw new UnsupportedOperationException("TODO 17 : implementer lateLoansCount()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  countOfGenre(SF) == 4, (Poesie) == 0", countOfGenre("SF") == 4 && countOfGenre("Poesie") == 0);
        ExerciseChecker.check("2  cheapestTitle == La Ferme des animaux", cheapestTitle().equals(Optional.of("La Ferme des animaux")));
        ExerciseChecker.check("3  thickestTitle == Dune", thickestTitle().equals(Optional.of("Dune")));
        ExerciseChecker.check("4  firstAfterYear(1950) == Dune, (1990) vide",
                firstAfterYear(1950).equals(Optional.of("Dune")) && firstAfterYear(1990).isEmpty());
        ExerciseChecker.check("5  anyAfterYear(1960) present et publie apres 1960, (1990) vide",
                anyAfterYear(1960).map(b -> b.year() > 1960).orElse(false) && anyAfterYear(1990).isEmpty());
        ExerciseChecker.check("6  hasAuthor(Orwell) && !hasAuthor(Hugo)", hasAuthor("Orwell") && !hasAuthor("Hugo"));
        ExerciseChecker.check("7  allHavePagesOver(90) && !allHavePagesOver(100)", allHavePagesOver(90) && !allHavePagesOver(100));
        ExerciseChecker.check("8  noneCostsMoreThan(10.0) && !noneCostsMoreThan(9.9)",
                noneCostsMoreThan(10.0) && !noneCostsMoreThan(9.9));
        ExerciseChecker.check("9  allPoesieAreCheap() == true (allMatch sur vide)", allPoesieAreCheap());

        List<String> out = new ArrayList<>();
        collectTitles(out);
        ExerciseChecker.check("10 collectTitles : 8 titres, Dune en premier", out.size() == 8 && out.get(0).equals("Dune"));
        out.clear();
        collectSortedTitles(out);
        ExerciseChecker.check("11 collectSortedTitles : 1984 en premier, Neuromancien en dernier",
                out.size() == 8 && out.get(0).equals("1984") && out.get(7).equals("Neuromancien"));

        ExerciseChecker.check("12 isbnArray == [B1..B8] (Object[])",
                Arrays.equals(isbnArray(), new Object[]{"B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8"}));
        String[] titles = titleArray();
        ExerciseChecker.check("13 titleArray est un String[] de 8 titres", titles.length == 8 && titles[7].equals("Le Hobbit"));

        boolean refused = false;
        try {
            immutableTitles().add("X");
        } catch (UnsupportedOperationException e) {
            refused = true;
        }
        ExerciseChecker.check("14 immutableTitles() refuse add", refused && immutableTitles().size() == 8);
        List<String> mutable = mutableTitles();
        mutable.add("X");
        ExerciseChecker.check("15 mutableTitles() accepte add", mutable.size() == 9);

        ExerciseChecker.check("16 firstTwoTitles == [Dune, Fondation]", firstTwoTitles().equals(List.of("Dune", "Fondation")));
        ExerciseChecker.check("17 lateLoansCount == 4", lateLoansCount() == 4);

        ExerciseChecker.summary();
    }
}
