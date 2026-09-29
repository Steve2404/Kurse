package ch10_streams.drills.solutions;

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
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill03_StreamCreation.
 */
public class SolutionDrill03_StreamCreation {

    public static List<String> genresLiteral() {
        // Stream.of(varargs) : un stream a partir de valeurs ecrites en dur.
        return Stream.of("SF", "Conte", "Dystopie", "Fantasy").toList();
    }

    public static Stream<Book> noBooks() {
        // Stream.empty() : le type Book est deduit du type de retour.
        return Stream.empty();
    }

    public static List<String> emailStream(Member m) {
        // Stream.ofNullable (Java 9) : 0 element si null, 1 sinon.
        return Stream.ofNullable(m.email()).toList();
    }

    public static List<String> middleShelf(String[] shelf, int from, int to) {
        // Arrays.stream(tableau, debut, finExclue) : comme subList, la fin est exclue.
        return Arrays.stream(shelf, from, to).toList();
    }

    public static List<String> bookTitles() {
        // collection.stream() : la source la plus courante.
        return Library.BOOKS.stream().map(Book::title).toList();
    }

    public static List<String> entriesAsText(Map<String, Integer> map) {
        // Une Map n'a pas de stream() : on passe par entrySet() (ou keySet / values).
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).sorted().toList();
    }

    public static List<String> isbnSequence(int n) {
        // iterate a 2 arguments est INFINI : limit est obligatoire.
        return Stream.iterate(1, i -> i + 1).limit(n).map(i -> "B" + i).toList();
    }

    public static List<String> isbnsUpTo(int max) {
        // iterate a 3 arguments (Java 9) : s'arrete des que la condition est fausse.
        return Stream.iterate(1, i -> i <= max, i -> i + 1).map(i -> "B" + i).toList();
    }

    public static List<String> repeat(String s, int n) {
        // generate est INFINI : limit est obligatoire.
        return Stream.generate(() -> s).limit(n).toList();
    }

    public static List<String> mergeShelves(List<String> a, List<String> b) {
        // Stream.concat colle 2 streams bout a bout (a puis b).
        return Stream.concat(a.stream(), b.stream()).toList();
    }

    public static List<String> buildShelf(String... titles) {
        // Stream.builder : on ajoute les elements un par un, puis build() fabrique le stream.
        Stream.Builder<String> builder = Stream.builder();
        for (String t : titles) {
            builder.add(t);
        }
        return builder.build().toList();
    }

    public static List<Character> lettersOf(String text) {
        // chars() donne des int : le cast (char) redonne un caractere, mis en boite en Character.
        return text.chars().mapToObj(c -> (char) c).toList();
    }

    public static List<Integer> yearsBetween(int a, int b) {
        // rangeClosed : fin INCLUSE ; boxed() pour obtenir une List<Integer>.
        return IntStream.rangeClosed(a, b).boxed().toList();
    }

    public static List<Integer> powersUpTo(int limit) {
        // IntStream.iterate a 3 arguments : graine, condition pour continuer, suivant.
        return IntStream.iterate(1, x -> x <= limit, x -> x * 2).boxed().toList();
    }

    public static double sumPrices(double... prices) {
        // DoubleStream.of(double...) : un varargs de double devient un DoubleStream.
        return DoubleStream.of(prices).sum();
    }

    public static long sumIds(long from, long to) {
        // LongStream : la somme 1..100000 depasse la capacite d'un int.
        return LongStream.rangeClosed(from, to).sum();
    }

    public static List<String> csvToTitles(String csv) {
        // split rend un tableau (avec des "" pour ";;") : strip puis filtre des vides.
        return Arrays.stream(csv.split(";")).map(String::strip).filter(s -> !s.isEmpty()).toList();
    }

    public static List<Integer> concatRanges() {
        // IntStream.concat existe aussi pour les streams primitifs.
        return IntStream.concat(IntStream.range(1, 3), IntStream.range(10, 12)).boxed().toList();
    }
}
