package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.IntSupplier;

/**
 * DRILL 02 - OptionalInt / OptionalLong / OptionalDouble (projet bibliotheque)
 * ============================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Les boites PRIMITIVES arrivent des qu'on fait max/min/average/findFirst
 * sur un IntStream / LongStream / DoubleStream. Elles ressemblent a
 * Optional mais n'ont NI map, NI filter, NI flatMap, NI or : il faut
 * connaitre leurs methodes propres.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : maxPages()            [IntStream.max -> OptionalInt] le plus grand nombre de pages.
 * TODO 2  : maxPagesOf(genre)     [OptionalInt.orElse] -1 si le genre n'existe pas.
 * TODO 3  : averagePrice(genre)   [average -> OptionalDouble] SF -> 8.5.
 * TODO 4  : averagePriceOrZero(genre) [OptionalDouble.orElse] 0.0 si aucun livre.
 * TODO 5  : describeAverage(genre) [isPresent + getAsDouble] "moyenne=8.5" ou "aucun livre".
 * TODO 6  : oldestYearOr(genre, fallback) [OptionalInt.orElseGet(IntSupplier)] annee
 *           minimale du genre, sinon fallback (appele SEULEMENT si besoin).
 * TODO 7  : longestDelay()        [LongStream.max -> OptionalLong] le plus gros retard
 *           (en long) parmi Library.LOANS -> 10.
 * TODO 8  : delayOrZero(memberId) [OptionalLong.orElse] le plus gros retard du membre,
 *           0 s'il n'a aucun emprunt. M2 -> 5 ; M4 -> 0.
 * TODO 9  : logMaxPages(genre, log) [OptionalInt.ifPresent(IntConsumer)] ajoute "max=" + pages.
 * TODO 10 : toOptionalInteger(box) [OptionalInt.stream + boxed + findFirst]
 *           OptionalInt -> Optional<Integer>.
 * TODO 11 : requireMaxPages(genre) [OptionalInt.orElseThrow(Supplier)] sinon
 *           IllegalArgumentException("genre inconnu : " + genre).
 * TODO 12 : describeCheapest(genre, log) [OptionalDouble.ifPresentOrElse]
 *           "min=" + prix, sinon "rien".
 * TODO 13 : pagesIfEven(book)     [OptionalInt.of / OptionalInt.empty] les pages si
 *           elles sont paires, sinon boite vide.
 * TODO 14 : firstPagesOver(limit) [IntStream.filter + findFirst + getAsInt]
 *           pages du 1er livre (ordre de BOOKS) qui depasse limit ; on SAIT
 *           qu'il existe (sinon NoSuchElementException, c'est voulu).
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   OptionalInt    : of(int) empty() isPresent() isEmpty() getAsInt()
 *                    orElse(int) orElseGet(IntSupplier) orElseThrow()
 *                    orElseThrow(Supplier) ifPresent(IntConsumer)
 *                    ifPresentOrElse(IntConsumer, Runnable) stream() -> IntStream
 *   OptionalLong   : idem avec getAsLong(), LongSupplier, LongConsumer
 *   OptionalDouble : idem avec getAsDouble(), DoubleSupplier, DoubleConsumer
 *   Qui rend quoi  : IntStream.max/min/findFirst/findAny -> OptionalInt
 *                    IntStream.average() -> OptionalDouble (!)
 *                    LongStream.average() -> OptionalDouble aussi
 *                    IntStream.sum() -> int (PAS de boite : somme vide = 0)
 * ---------------------------------------------------------------------
 */
public class Drill02_OptionalPrimitiveApi {

    public static OptionalInt maxPages() {
        throw new UnsupportedOperationException("TODO 1 : implementer maxPages()");
    }

    public static int maxPagesOf(String genre) {
        throw new UnsupportedOperationException("TODO 2 : implementer maxPagesOf()");
    }

    public static OptionalDouble averagePrice(String genre) {
        throw new UnsupportedOperationException("TODO 3 : implementer averagePrice()");
    }

    public static double averagePriceOrZero(String genre) {
        throw new UnsupportedOperationException("TODO 4 : implementer averagePriceOrZero()");
    }

    public static String describeAverage(String genre) {
        throw new UnsupportedOperationException("TODO 5 : implementer describeAverage()");
    }

    public static int oldestYearOr(String genre, IntSupplier fallback) {
        throw new UnsupportedOperationException("TODO 6 : implementer oldestYearOr()");
    }

    public static OptionalLong longestDelay() {
        throw new UnsupportedOperationException("TODO 7 : implementer longestDelay()");
    }

    public static long delayOrZero(String memberId) {
        throw new UnsupportedOperationException("TODO 8 : implementer delayOrZero()");
    }

    public static void logMaxPages(String genre, List<String> log) {
        throw new UnsupportedOperationException("TODO 9 : implementer logMaxPages()");
    }

    public static Optional<Integer> toOptionalInteger(OptionalInt box) {
        throw new UnsupportedOperationException("TODO 10 : implementer toOptionalInteger()");
    }

    public static int requireMaxPages(String genre) {
        throw new UnsupportedOperationException("TODO 11 : implementer requireMaxPages()");
    }

    public static void describeCheapest(String genre, List<String> log) {
        throw new UnsupportedOperationException("TODO 12 : implementer describeCheapest()");
    }

    public static OptionalInt pagesIfEven(Book book) {
        throw new UnsupportedOperationException("TODO 13 : implementer pagesIfEven()");
    }

    public static int firstPagesOver(int limit) {
        throw new UnsupportedOperationException("TODO 14 : implementer firstPagesOver()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  maxPages() == OptionalInt[412]", maxPages().equals(OptionalInt.of(412)));
        ExerciseChecker.check("2  maxPagesOf(Dystopie) == 328, maxPagesOf(Poesie) == -1",
                maxPagesOf("Dystopie") == 328 && maxPagesOf("Poesie") == -1);
        ExerciseChecker.check("3  averagePrice(SF) == 8.5, averagePrice(Poesie) vide",
                averagePrice("SF").equals(OptionalDouble.of(8.5)) && averagePrice("Poesie").isEmpty());
        ExerciseChecker.check("4  averagePriceOrZero(Dystopie) == 7.0, (Poesie) == 0.0",
                averagePriceOrZero("Dystopie") == 7.0 && averagePriceOrZero("Poesie") == 0.0);
        ExerciseChecker.check("5  describeAverage(SF) == moyenne=8.5, (Poesie) == aucun livre",
                describeAverage("SF").equals("moyenne=8.5") && describeAverage("Poesie").equals("aucun livre"));

        int[] calls = {0};
        IntSupplier fallback = () -> {
            calls[0]++;
            return 2000;
        };
        ExerciseChecker.check("6  oldestYearOr(SF) == 1950 sans appeler fallback",
                oldestYearOr("SF", fallback) == 1950 && calls[0] == 0);
        ExerciseChecker.check("6  oldestYearOr(Poesie) == 2000, fallback appele 1 fois",
                oldestYearOr("Poesie", fallback) == 2000 && calls[0] == 1);

        ExerciseChecker.check("7  longestDelay() == OptionalLong[10]", longestDelay().equals(OptionalLong.of(10)));
        ExerciseChecker.check("8  delayOrZero(M2) == 5, delayOrZero(M4) == 0", delayOrZero("M2") == 5 && delayOrZero("M4") == 0);

        List<String> log = new ArrayList<>();
        logMaxPages("Fantasy", log);
        logMaxPages("Poesie", log);
        ExerciseChecker.check("9  logMaxPages -> [max=310]", log.equals(List.of("max=310")));

        ExerciseChecker.check("10 toOptionalInteger(OptionalInt[7]) == Optional[7], (vide) vide",
                toOptionalInteger(OptionalInt.of(7)).equals(Optional.of(7)) && toOptionalInteger(OptionalInt.empty()).isEmpty());

        String message = null;
        try {
            requireMaxPages("Poesie");
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("11 requireMaxPages(Conte) == 96, (Poesie) lance IllegalArgumentException",
                requireMaxPages("Conte") == 96 && "genre inconnu : Poesie".equals(message));

        log.clear();
        describeCheapest("SF", log);
        describeCheapest("Poesie", log);
        ExerciseChecker.check("12 describeCheapest -> [min=7.5, rien]", log.equals(List.of("min=7.5", "rien")));

        ExerciseChecker.check("13 pagesIfEven(Dune) == OptionalInt[412], (Fondation) vide",
                pagesIfEven(Library.BOOKS.get(0)).equals(OptionalInt.of(412)) && pagesIfEven(Library.BOOKS.get(1)).isEmpty());
        ExerciseChecker.check("14 firstPagesOver(300) == 412, (100) == 412, (412) lance NoSuchElementException",
                firstPagesOver(300) == 412 && firstPagesOver(100) == 412 && throwsNoSuchElement());

        ExerciseChecker.summary();
    }

    private static boolean throwsNoSuchElement() {
        try {
            firstPagesOver(412);
            return false;
        } catch (java.util.NoSuchElementException e) {
            return true;
        }
    }
}
