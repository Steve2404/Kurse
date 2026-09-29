package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleToIntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToLongFunction;
import java.util.function.ToIntBiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * DRILL 13 - Concepts avances : stream lie a sa source, exceptions verifiees, derniers outils (projet bibliotheque)
 * ==============================================================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Ce drill couvre les derniers points du chapitre 10 que les autres ne
 * touchent pas :
 *
 *   A. Un stream est LIE a sa source : comme il est paresseux, il ne lit
 *      la liste qu'au moment de l'operation terminale. Si la liste
 *      change ENTRE la creation du stream et l'operation terminale, le
 *      resultat voit le changement.
 *   B. Les lambdas des interfaces fonctionnelles standard (Function,
 *      Supplier, Predicate...) n'ont PAS le droit de lancer une
 *      exception VERIFIEE (IOException...). Il faut l'attraper DANS la
 *      lambda, et soit la transformer (Optional vide, valeur par
 *      defaut), soit l'emballer dans une exception non verifiee
 *      (UncheckedIOException).
 *   C. Les dernieres methodes : IntStream.generate, LongStream.mapToInt,
 *      DoubleStream.mapToObj, IntStream.collect a 3 arguments,
 *      onClose + try-with-resources, et quelques interfaces
 *      fonctionnelles primitives.
 *
 * Aide : Library.loadSummary(isbn) lance IOException pour "B3".
 *
 *
 * -- Les TODO (methode ou concept vise entre crochets) --
 *
 * TODO 1  : countAfterLateAddition()   [A] copie les titres dans une ArrayList,
 *           CREE le stream, PUIS ajoute "Nouveau livre" a la liste, PUIS compte -> 9.
 * TODO 2  : longTitlesAfterAddition()  [A] meme idee avec un filter (titre > 12 lettres)
 *           prepare AVANT l'ajout de "Le Seigneur des anneaux" -> le nouveau titre
 *           apparait dans le resultat.
 * TODO 3  : safeSummary(isbn)          [B] Optional du resume, vide si IOException.
 * TODO 4  : allSummariesOrFail()       [B] les resumes de TOUS les livres ; l'IOException
 *           de B3 doit ressortir emballee dans une UncheckedIOException.
 *           (Ecris une methode privee summaryUnchecked(isbn) qui fait l'emballage.)
 * TODO 5  : summariesSkippingErrors()  [B] les resumes disponibles uniquement (7).
 * TODO 6  : summaryOrDefault(isbn)     [B] resume, ou "Resume indisponible" (Supplier
 *           sans exception verifiee).
 * TODO 7  : generateIds(n, supplier)   [IntStream.generate + limit + toArray]
 * TODO 8  : lateDaysAsInts()           [mapToLong puis LongStream.mapToInt] sur LOANS.
 * TODO 9  : priceLabels()              [mapToDouble puis DoubleStream.mapToObj] "9.5 EUR"...
 * TODO 10 : pagesIntoArrayList()       [IntStream.collect(Supplier, ObjIntConsumer, BiConsumer)]
 * TODO 11 : countWithCloseHook(log)    [onClose + try-with-resources] compte les livres ;
 *           en fermant le stream, "stream ferme" doit etre ajoute a log.
 * TODO 12 : pagesToMinutes()           [IntToLongFunction] 2 minutes par page (resultat long).
 * TODO 13 : isThick()                  [IntPredicate] pages > 300.
 * TODO 14 : roundedPrice()             [DoubleToIntFunction] arrondi a l'entier le plus proche.
 * TODO 15 : pagesWithBonus()           [ToIntBiFunction<Book, Integer>] pages + bonus.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   A. var list = new ArrayList<>(...); var s = list.stream(); list.add(x); s.count();
 *   B. x -> { try { return f(x); } catch (IOException e) { return Optional.empty(); } }
 *      x -> { try { return f(x); } catch (IOException e) { throw new UncheckedIOException(e); } }
 *   C. IntStream.generate(IntSupplier)       LongStream.mapToInt(LongToIntFunction)
 *      DoubleStream.mapToObj(DoubleFunction) IntStream.collect(Supplier, ObjIntConsumer, BiConsumer)
 *      try (Stream<T> s = ....onClose(Runnable)) { ... }   -> Runnable appele a la fermeture
 *      IntToLongFunction.applyAsLong(int)   IntPredicate.test(int)
 *      DoubleToIntFunction.applyAsInt(double)   ToIntBiFunction<T, U>.applyAsInt(T, U)
 * ---------------------------------------------------------------------
 */
public class Drill13_AdvancedPipelineConcepts {

    public static long countAfterLateAddition() {
        throw new UnsupportedOperationException("TODO 1 : implementer countAfterLateAddition()");
    }

    public static List<String> longTitlesAfterAddition() {
        throw new UnsupportedOperationException("TODO 2 : implementer longTitlesAfterAddition()");
    }

    public static Optional<String> safeSummary(String isbn) {
        throw new UnsupportedOperationException("TODO 3 : implementer safeSummary()");
    }

    public static List<String> allSummariesOrFail() {
        throw new UnsupportedOperationException("TODO 4 : implementer allSummariesOrFail()");
    }

    public static List<String> summariesSkippingErrors() {
        throw new UnsupportedOperationException("TODO 5 : implementer summariesSkippingErrors()");
    }

    public static String summaryOrDefault(String isbn) {
        throw new UnsupportedOperationException("TODO 6 : implementer summaryOrDefault()");
    }

    public static int[] generateIds(int n, IntSupplier supplier) {
        throw new UnsupportedOperationException("TODO 7 : implementer generateIds()");
    }

    public static int[] lateDaysAsInts() {
        throw new UnsupportedOperationException("TODO 8 : implementer lateDaysAsInts()");
    }

    public static List<String> priceLabels() {
        throw new UnsupportedOperationException("TODO 9 : implementer priceLabels()");
    }

    public static ArrayList<Integer> pagesIntoArrayList() {
        throw new UnsupportedOperationException("TODO 10 : implementer pagesIntoArrayList()");
    }

    public static long countWithCloseHook(List<String> log) {
        throw new UnsupportedOperationException("TODO 11 : implementer countWithCloseHook()");
    }

    public static IntToLongFunction pagesToMinutes() {
        throw new UnsupportedOperationException("TODO 12 : implementer pagesToMinutes()");
    }

    public static IntPredicate isThick() {
        throw new UnsupportedOperationException("TODO 13 : implementer isThick()");
    }

    public static DoubleToIntFunction roundedPrice() {
        throw new UnsupportedOperationException("TODO 14 : implementer roundedPrice()");
    }

    public static ToIntBiFunction<Book, Integer> pagesWithBonus() {
        throw new UnsupportedOperationException("TODO 15 : implementer pagesWithBonus()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  countAfterLateAddition == 9 (le stream voit l'ajout)", countAfterLateAddition() == 9);
        List<String> longTitles = longTitlesAfterAddition();
        ExerciseChecker.check("2  longTitlesAfterAddition contient le titre ajoute apres la creation du stream",
                longTitles.equals(List.of("Le Petit Prince", "La Ferme des animaux", "Le Seigneur des anneaux")));

        ExerciseChecker.check("3  safeSummary(B1) == Resume de Dune, (B3) vide",
                safeSummary("B1").equals(Optional.of("Resume de Dune")) && safeSummary("B3").isEmpty());

        String cause = null;
        try {
            allSummariesOrFail();
        } catch (UncheckedIOException e) {
            cause = e.getCause().getMessage();
        }
        ExerciseChecker.check("4  allSummariesOrFail lance UncheckedIOException(cause : resume introuvable : B3)",
                "resume introuvable : B3".equals(cause));

        List<String> summaries = summariesSkippingErrors();
        ExerciseChecker.check("5  summariesSkippingErrors : 7 resumes, sans Les Robots",
                summaries.size() == 7 && summaries.get(0).equals("Resume de Dune") && !summaries.contains("Resume de Les Robots"));
        ExerciseChecker.check("6  summaryOrDefault(B2) == Resume de Fondation, (B3) == Resume indisponible",
                summaryOrDefault("B2").equals("Resume de Fondation") && summaryOrDefault("B3").equals("Resume indisponible"));

        int[] counter = {100};
        ExerciseChecker.check("7  generateIds(3, compteur a partir de 101) == [101, 102, 103]",
                Arrays.equals(generateIds(3, () -> ++counter[0]), new int[]{101, 102, 103}));
        ExerciseChecker.check("8  lateDaysAsInts == [0, 3, 0, 10, 0, 1, 0, 5]",
                Arrays.equals(lateDaysAsInts(), new int[]{0, 3, 0, 10, 0, 1, 0, 5}));
        List<String> labels = priceLabels();
        ExerciseChecker.check("9  priceLabels : 9.5 EUR en premier, 10.0 EUR en dernier",
                labels.size() == 8 && labels.get(0).equals("9.5 EUR") && labels.get(7).equals("10.0 EUR"));
        ExerciseChecker.check("10 pagesIntoArrayList == [412, 255, 253, 96, 328, 112, 271, 310]",
                pagesIntoArrayList().equals(List.of(412, 255, 253, 96, 328, 112, 271, 310)));

        List<String> log = new ArrayList<>();
        ExerciseChecker.check("11 countWithCloseHook == 8 et log == [stream ferme]",
                countWithCloseHook(log) == 8 && log.equals(List.of("stream ferme")));

        ExerciseChecker.check("12 pagesToMinutes(412) == 824L", pagesToMinutes().applyAsLong(412) == 824L);
        ExerciseChecker.check("13 isThick(412) && !isThick(300)", isThick().test(412) && !isThick().test(300));
        ExerciseChecker.check("14 roundedPrice(9.5) == 10, (8.4) == 8", roundedPrice().applyAsInt(9.5) == 10 && roundedPrice().applyAsInt(8.4) == 8);
        ExerciseChecker.check("15 pagesWithBonus(Dune, 8) == 420", pagesWithBonus().applyAsInt(Library.BOOKS.get(0), 8) == 420);

        ExerciseChecker.summary();
    }
}
