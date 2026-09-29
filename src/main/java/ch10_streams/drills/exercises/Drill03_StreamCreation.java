package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * DRILL 03 - Toutes les facons de CREER un stream (projet bibliotheque)
 * =====================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : genresLiteral()        [Stream.of] -> [SF, Conte, Dystopie, Fantasy].
 * TODO 2  : noBooks()              [Stream.empty] un Stream<Book> vide.
 * TODO 3  : emailStream(m)         [Stream.ofNullable] 0 ou 1 element (email brut).
 * TODO 4  : middleShelf(shelf, from, to) [Arrays.stream(tab, debut, finExclue)].
 * TODO 5  : bookTitles()           [Collection.stream] titres de Library.BOOKS dans l'ordre.
 * TODO 6  : entriesAsText(map)     [Map.entrySet().stream()] "cle=valeur", tries.
 * TODO 7  : isbnSequence(n)        [Stream.iterate(graine, suivant) + limit] "B1".."Bn".
 * TODO 8  : isbnsUpTo(max)         [Stream.iterate(graine, condition, suivant)] "B1".."Bmax".
 * TODO 9  : repeat(s, n)           [Stream.generate + limit] n fois s.
 * TODO 10 : mergeShelves(a, b)     [Stream.concat] a puis b.
 * TODO 11 : buildShelf(titles...)  [Stream.builder / add / build].
 * TODO 12 : lettersOf(text)        [String.chars + mapToObj] liste de Character.
 * TODO 13 : yearsBetween(a, b)     [IntStream.rangeClosed + boxed] a..b inclus.
 * TODO 14 : powersUpTo(limit)      [IntStream.iterate a 3 arguments] 1, 2, 4... <= limit.
 * TODO 15 : sumPrices(prices...)   [DoubleStream.of + sum].
 * TODO 16 : sumIds(from, to)       [LongStream.rangeClosed + sum] (resultat en long).
 * TODO 17 : csvToTitles(csv)       [Arrays.stream(split)] decoupe sur ";", strip, sans vides.
 * TODO 18 : concatRanges()         [IntStream.concat] range(1, 3) puis range(10, 12) -> [1, 2, 10, 11].
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Stream.of(a, b, c)       Stream.empty()       Stream.ofNullable(x) (9)
 *   collection.stream()      map.entrySet().stream()
 *   Arrays.stream(tab)       Arrays.stream(tab, debut, finExclue)
 *   Stream.iterate(graine, UnaryOperator)                 -> INFINI
 *   Stream.iterate(graine, Predicate hasNext, UnaryOperator) (9) -> fini
 *   Stream.generate(Supplier)                             -> INFINI
 *   Stream.concat(s1, s2)    IntStream.concat(i1, i2)
 *   Stream.<T>builder().add(x).add(y).build()
 *   "texte".chars() -> IntStream
 *   IntStream.range(a, bExclu) / rangeClosed(a, bInclus) / of(...) / iterate(...)
 *   LongStream.* et DoubleStream.of(...) : memes idees
 * ---------------------------------------------------------------------
 */
public class Drill03_StreamCreation {

    public static List<String> genresLiteral() {
        throw new UnsupportedOperationException("TODO 1 : implementer genresLiteral()");
    }

    public static Stream<Book> noBooks() {
        throw new UnsupportedOperationException("TODO 2 : implementer noBooks()");
    }

    public static List<String> emailStream(Member m) {
        throw new UnsupportedOperationException("TODO 3 : implementer emailStream()");
    }

    public static List<String> middleShelf(String[] shelf, int from, int to) {
        throw new UnsupportedOperationException("TODO 4 : implementer middleShelf()");
    }

    public static List<String> bookTitles() {
        throw new UnsupportedOperationException("TODO 5 : implementer bookTitles()");
    }

    public static List<String> entriesAsText(Map<String, Integer> map) {
        throw new UnsupportedOperationException("TODO 6 : implementer entriesAsText()");
    }

    public static List<String> isbnSequence(int n) {
        throw new UnsupportedOperationException("TODO 7 : implementer isbnSequence()");
    }

    public static List<String> isbnsUpTo(int max) {
        throw new UnsupportedOperationException("TODO 8 : implementer isbnsUpTo()");
    }

    public static List<String> repeat(String s, int n) {
        throw new UnsupportedOperationException("TODO 9 : implementer repeat()");
    }

    public static List<String> mergeShelves(List<String> a, List<String> b) {
        throw new UnsupportedOperationException("TODO 10 : implementer mergeShelves()");
    }

    public static List<String> buildShelf(String... titles) {
        throw new UnsupportedOperationException("TODO 11 : implementer buildShelf()");
    }

    public static List<Character> lettersOf(String text) {
        throw new UnsupportedOperationException("TODO 12 : implementer lettersOf()");
    }

    public static List<Integer> yearsBetween(int a, int b) {
        throw new UnsupportedOperationException("TODO 13 : implementer yearsBetween()");
    }

    public static List<Integer> powersUpTo(int limit) {
        throw new UnsupportedOperationException("TODO 14 : implementer powersUpTo()");
    }

    public static double sumPrices(double... prices) {
        throw new UnsupportedOperationException("TODO 15 : implementer sumPrices()");
    }

    public static long sumIds(long from, long to) {
        throw new UnsupportedOperationException("TODO 16 : implementer sumIds()");
    }

    public static List<String> csvToTitles(String csv) {
        throw new UnsupportedOperationException("TODO 17 : implementer csvToTitles()");
    }

    public static List<Integer> concatRanges() {
        throw new UnsupportedOperationException("TODO 18 : implementer concatRanges()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  genresLiteral", genresLiteral().equals(List.of("SF", "Conte", "Dystopie", "Fantasy")));
        ExerciseChecker.check("2  noBooks().count() == 0", noBooks().count() == 0);
        ExerciseChecker.check("3  emailStream(Lea) == [lea@mail.fr], (Hugo) == []",
                emailStream(Library.MEMBERS.get(0)).equals(List.of("lea@mail.fr")) && emailStream(Library.MEMBERS.get(1)).isEmpty());
        ExerciseChecker.check("4  middleShelf([a, b, c, d, e], 1, 4) == [b, c, d]",
                middleShelf(new String[]{"a", "b", "c", "d", "e"}, 1, 4).equals(List.of("b", "c", "d")));
        ExerciseChecker.check("5  bookTitles a 8 titres, commence par Dune et finit par Le Hobbit",
                bookTitles().size() == 8 && bookTitles().get(0).equals("Dune") && bookTitles().get(7).equals("Le Hobbit"));
        ExerciseChecker.check("6  entriesAsText({SF=4, Conte=1}) == [Conte=1, SF=4]",
                entriesAsText(Map.of("SF", 4, "Conte", 1)).equals(List.of("Conte=1", "SF=4")));
        ExerciseChecker.check("7  isbnSequence(3) == [B1, B2, B3]", isbnSequence(3).equals(List.of("B1", "B2", "B3")));
        ExerciseChecker.check("8  isbnsUpTo(4) == [B1, B2, B3, B4], isbnsUpTo(0) == []",
                isbnsUpTo(4).equals(List.of("B1", "B2", "B3", "B4")) && isbnsUpTo(0).isEmpty());
        ExerciseChecker.check("9  repeat(SF, 3) == [SF, SF, SF]", repeat("SF", 3).equals(List.of("SF", "SF", "SF")));
        ExerciseChecker.check("10 mergeShelves([a], [b, c]) == [a, b, c]",
                mergeShelves(List.of("a"), List.of("b", "c")).equals(List.of("a", "b", "c")));
        ExerciseChecker.check("11 buildShelf(Dune, 1984) == [Dune, 1984], buildShelf() == []",
                buildShelf("Dune", "1984").equals(List.of("Dune", "1984")) && buildShelf().isEmpty());
        ExerciseChecker.check("12 lettersOf(Dune) == [D, u, n, e]", lettersOf("Dune").equals(List.of('D', 'u', 'n', 'e')));
        ExerciseChecker.check("13 yearsBetween(1949, 1951) == [1949, 1950, 1951]",
                yearsBetween(1949, 1951).equals(List.of(1949, 1950, 1951)));
        ExerciseChecker.check("14 powersUpTo(100) == [1, 2, 4, 8, 16, 32, 64]",
                powersUpTo(100).equals(List.of(1, 2, 4, 8, 16, 32, 64)));
        ExerciseChecker.check("15 sumPrices(9.5, 8.0, 0.5) == 18.0", sumPrices(9.5, 8.0, 0.5) == 18.0);
        ExerciseChecker.check("16 sumIds(1, 100000) == 5000050000", sumIds(1, 100000) == 5000050000L);
        ExerciseChecker.check("17 csvToTitles(\" Dune ; 1984;;Le Hobbit \") == [Dune, 1984, Le Hobbit]",
                csvToTitles(" Dune ; 1984;;Le Hobbit ").equals(List.of("Dune", "1984", "Le Hobbit")));
        ExerciseChecker.check("18 concatRanges() == [1, 2, 10, 11]", concatRanges().equals(List.of(1, 2, 10, 11)));

        ExerciseChecker.summary();
    }
}
