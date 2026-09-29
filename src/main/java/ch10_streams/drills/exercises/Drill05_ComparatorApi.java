package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * DRILL 05 - Toute l'API Comparator (projet bibliotheque)
 * =======================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Particularite : ici chaque TODO RENVOIE UN COMPARATOR (une seule
 * expression). main() s'en sert pour trier Library.BOOKS et compare
 * les titres obtenus. C'est l'outil de sorted(), min(), max(), TreeMap,
 * maxBy/minBy...
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : byTitle()              [comparing(keyExtractor)]
 * TODO 2  : byTitleDesc()          [comparing(keyExtractor, keyComparator) + reverseOrder]
 * TODO 3  : byPages()              [comparingInt]
 * TODO 4  : byPriceDesc()          [comparingDouble + reversed]
 * TODO 5  : byGenreThenTitle()     [thenComparing(keyExtractor)]
 * TODO 6  : byAuthorThenYear()     [thenComparingInt]
 * TODO 7  : byGenreThenPriceDesc() [thenComparing(keyExtractor, keyComparator)]
 *           (ATTENTION : seul le prix est inverse, pas le genre)
 * TODO 8  : byTagCountThenTitle()  [comparingInt(lambda) + thenComparing]
 * TODO 9  : byYearLambda()         [lambda (a, b) -> Integer.compare(...)] sans comparing.
 * TODO 10 : caseInsensitive()      [String.CASE_INSENSITIVE_ORDER] Comparator<String>.
 * TODO 11 : natural()              [Comparator.naturalOrder] Comparator<String>.
 * TODO 12 : reverse()              [Comparator.reverseOrder] Comparator<String>.
 * TODO 13 : nullsFirstNatural()    [Comparator.nullsFirst] Comparator<String>.
 * TODO 14 : nullsLastReverse()     [Comparator.nullsLast] Comparator<Integer>, grands d'abord.
 * TODO 15 : entryByValueDescThenKey() [Map.Entry.comparingByValue / comparingByKey]
 *           Comparator<Map.Entry<String, Long>> : valeur decroissante, puis cle.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Comparator.comparing(Function)                  Comparator.comparing(Function, Comparator)
 *   Comparator.comparingInt / comparingLong / comparingDouble(ToXxxFunction)
 *   cmp.thenComparing(Comparator)  cmp.thenComparing(Function)
 *   cmp.thenComparing(Function, Comparator)
 *   cmp.thenComparingInt / thenComparingLong / thenComparingDouble
 *   cmp.reversed()   -> inverse TOUT ce qui precede
 *   Comparator.naturalOrder()  Comparator.reverseOrder()
 *   Comparator.nullsFirst(cmp)  Comparator.nullsLast(cmp)
 *   String.CASE_INSENSITIVE_ORDER
 *   Map.Entry.comparingByKey()  comparingByValue()  (+ versions avec Comparator)
 *   Parfois il faut aider l'inference : Map.Entry.<String, Long>comparingByValue()
 * ---------------------------------------------------------------------
 */
public class Drill05_ComparatorApi {

    public static Comparator<Book> byTitle() {
        throw new UnsupportedOperationException("TODO 1 : implementer byTitle()");
    }

    public static Comparator<Book> byTitleDesc() {
        throw new UnsupportedOperationException("TODO 2 : implementer byTitleDesc()");
    }

    public static Comparator<Book> byPages() {
        throw new UnsupportedOperationException("TODO 3 : implementer byPages()");
    }

    public static Comparator<Book> byPriceDesc() {
        throw new UnsupportedOperationException("TODO 4 : implementer byPriceDesc()");
    }

    public static Comparator<Book> byGenreThenTitle() {
        throw new UnsupportedOperationException("TODO 5 : implementer byGenreThenTitle()");
    }

    public static Comparator<Book> byAuthorThenYear() {
        throw new UnsupportedOperationException("TODO 6 : implementer byAuthorThenYear()");
    }

    public static Comparator<Book> byGenreThenPriceDesc() {
        throw new UnsupportedOperationException("TODO 7 : implementer byGenreThenPriceDesc()");
    }

    public static Comparator<Book> byTagCountThenTitle() {
        throw new UnsupportedOperationException("TODO 8 : implementer byTagCountThenTitle()");
    }

    public static Comparator<Book> byYearLambda() {
        throw new UnsupportedOperationException("TODO 9 : implementer byYearLambda()");
    }

    public static Comparator<String> caseInsensitive() {
        throw new UnsupportedOperationException("TODO 10 : implementer caseInsensitive()");
    }

    public static Comparator<String> natural() {
        throw new UnsupportedOperationException("TODO 11 : implementer natural()");
    }

    public static Comparator<String> reverse() {
        throw new UnsupportedOperationException("TODO 12 : implementer reverse()");
    }

    public static Comparator<String> nullsFirstNatural() {
        throw new UnsupportedOperationException("TODO 13 : implementer nullsFirstNatural()");
    }

    public static Comparator<Integer> nullsLastReverse() {
        throw new UnsupportedOperationException("TODO 14 : implementer nullsLastReverse()");
    }

    public static Comparator<Map.Entry<String, Long>> entryByValueDescThenKey() {
        throw new UnsupportedOperationException("TODO 15 : implementer entryByValueDescThenKey()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  byTitle", titles(byTitle()).equals(List.of("1984", "Dune", "Fondation",
                "La Ferme des animaux", "Le Hobbit", "Le Petit Prince", "Les Robots", "Neuromancien")));
        ExerciseChecker.check("2  byTitleDesc", titles(byTitleDesc()).equals(List.of("Neuromancien", "Les Robots",
                "Le Petit Prince", "Le Hobbit", "La Ferme des animaux", "Fondation", "Dune", "1984")));
        ExerciseChecker.check("3  byPages", titles(byPages()).equals(List.of("Le Petit Prince", "La Ferme des animaux",
                "Les Robots", "Fondation", "Neuromancien", "Le Hobbit", "1984", "Dune")));
        ExerciseChecker.check("4  byPriceDesc", titles(byPriceDesc()).equals(List.of("Le Hobbit", "Dune", "Neuromancien",
                "1984", "Fondation", "Les Robots", "Le Petit Prince", "La Ferme des animaux")));
        ExerciseChecker.check("5  byGenreThenTitle", titles(byGenreThenTitle()).equals(List.of("Le Petit Prince", "1984",
                "La Ferme des animaux", "Le Hobbit", "Dune", "Fondation", "Les Robots", "Neuromancien")));
        ExerciseChecker.check("6  byAuthorThenYear", titles(byAuthorThenYear()).equals(List.of("Les Robots", "Fondation",
                "Neuromancien", "Dune", "La Ferme des animaux", "1984", "Le Petit Prince", "Le Hobbit")));
        ExerciseChecker.check("7  byGenreThenPriceDesc", titles(byGenreThenPriceDesc()).equals(List.of("Le Petit Prince",
                "1984", "La Ferme des animaux", "Le Hobbit", "Dune", "Neuromancien", "Fondation", "Les Robots")));
        ExerciseChecker.check("8  byTagCountThenTitle", titles(byTagCountThenTitle()).equals(List.of("Les Robots",
                "Neuromancien", "1984", "Dune", "Fondation", "La Ferme des animaux", "Le Hobbit", "Le Petit Prince")));
        ExerciseChecker.check("9  byYearLambda", titles(byYearLambda()).equals(List.of("Le Hobbit", "Le Petit Prince",
                "La Ferme des animaux", "1984", "Les Robots", "Fondation", "Dune", "Neuromancien")));

        ExerciseChecker.check("10 caseInsensitive : [b, A, c] -> [A, b, c]",
                List.of("b", "A", "c").stream().sorted(caseInsensitive()).toList().equals(List.of("A", "b", "c")));
        ExerciseChecker.check("11 natural : [b, a, B] -> [B, a, b] (majuscules d'abord)",
                List.of("b", "a", "B").stream().sorted(natural()).toList().equals(List.of("B", "a", "b")));
        ExerciseChecker.check("12 reverse : [b, a, c] -> [c, b, a]",
                List.of("b", "a", "c").stream().sorted(reverse()).toList().equals(List.of("c", "b", "a")));
        ExerciseChecker.check("13 nullsFirstNatural : [b, null, a] -> [null, a, b]",
                Arrays.asList("b", null, "a").stream().sorted(nullsFirstNatural()).toList().equals(Arrays.asList(null, "a", "b")));
        ExerciseChecker.check("14 nullsLastReverse : [1, null, 3] -> [3, 1, null]",
                Arrays.asList(1, null, 3).stream().sorted(nullsLastReverse()).toList().equals(Arrays.asList(3, 1, null)));

        Map<String, Long> countByGenre = new TreeMap<>(Map.of("SF", 4L, "Dystopie", 2L, "Conte", 1L, "Fantasy", 1L));
        ExerciseChecker.check("15 entryByValueDescThenKey -> [SF=4, Dystopie=2, Conte=1, Fantasy=1]",
                countByGenre.entrySet().stream().sorted(entryByValueDescThenKey()).map(Object::toString).toList()
                        .equals(List.of("SF=4", "Dystopie=2", "Conte=1", "Fantasy=1")));

        ExerciseChecker.summary();
    }

    private static List<String> titles(Comparator<Book> cmp) {
        return Library.BOOKS.stream().sorted(cmp).map(Book::title).toList();
    }
}
