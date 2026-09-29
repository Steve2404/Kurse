package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.IntSupplier;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill02_OptionalPrimitiveApi.
 */
public class SolutionDrill02_OptionalPrimitiveApi {

    public static OptionalInt maxPages() {
        // IntStream.max() rend une OptionalInt (pas un Optional<Integer>).
        return Library.BOOKS.stream().mapToInt(Book::pages).max();
    }

    public static int maxPagesOf(String genre) {
        // orElse(int) : valeur par defaut directement en int.
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToInt(Book::pages).max().orElse(-1);
    }

    public static OptionalDouble averagePrice(String genre) {
        // average() rend TOUJOURS une OptionalDouble, meme sur un IntStream.
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToDouble(Book::price).average();
    }

    public static double averagePriceOrZero(String genre) {
        // orElse(0.0) ouvre la boite, avec 0.0 si aucun livre du genre.
        return averagePrice(genre).orElse(0.0);
    }

    public static String describeAverage(String genre) {
        // getAsDouble (et non get) pour ouvrir une OptionalDouble, apres isPresent.
        OptionalDouble avg = averagePrice(genre);
        return avg.isPresent() ? "moyenne=" + avg.getAsDouble() : "aucun livre";
    }

    public static int oldestYearOr(String genre, IntSupplier fallback) {
        // orElseGet prend un IntSupplier : appele seulement si la boite est vide.
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToInt(Book::year).min().orElseGet(fallback);
    }

    public static OptionalLong longestDelay() {
        // mapToLong -> LongStream, dont max() rend une OptionalLong.
        return Library.LOANS.stream().mapToLong(Library.Loan::daysLate).max();
    }

    public static long delayOrZero(String memberId) {
        // Un membre sans emprunt donne un stream vide, donc une boite vide, donc 0.
        return Library.LOANS.stream()
                .filter(l -> l.memberId().equals(memberId))
                .mapToLong(Library.Loan::daysLate)
                .max()
                .orElse(0);
    }

    public static void logMaxPages(String genre, List<String> log) {
        // ifPresent recoit un IntConsumer : p est un int, pas un Integer.
        Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToInt(Book::pages).max()
                .ifPresent(p -> log.add("max=" + p));
    }

    public static Optional<Integer> toOptionalInteger(OptionalInt box) {
        // OptionalInt.stream() -> IntStream, boxed() -> Stream<Integer>, findFirst() -> Optional<Integer>.
        return box.stream().boxed().findFirst();
    }

    public static int requireMaxPages(String genre) {
        // orElseThrow(Supplier) existe aussi sur les boites primitives.
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToInt(Book::pages).max()
                .orElseThrow(() -> new IllegalArgumentException("genre inconnu : " + genre));
    }

    public static void describeCheapest(String genre, List<String> log) {
        // ifPresentOrElse existe aussi sur OptionalDouble (DoubleConsumer + Runnable).
        Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToDouble(Book::price).min()
                .ifPresentOrElse(p -> log.add("min=" + p), () -> log.add("rien"));
    }

    public static OptionalInt pagesIfEven(Book book) {
        // OptionalInt.of / OptionalInt.empty pour fabriquer soi-meme une boite primitive.
        return book.pages() % 2 == 0 ? OptionalInt.of(book.pages()) : OptionalInt.empty();
    }

    public static int firstPagesOver(int limit) {
        // getAsInt lance NoSuchElementException si aucun livre ne correspond (voulu ici).
        return Library.BOOKS.stream().mapToInt(Book::pages).filter(p -> p > limit).findFirst().getAsInt();
    }
}
