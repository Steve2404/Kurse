package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Loan;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
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
 * Corrige du drill 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill13_AdvancedPipelineConcepts.
 */
public class SolutionDrill13_AdvancedPipelineConcepts {

    public static long countAfterLateAddition() {
        // Un stream est LIE a sa source : il ne la lit qu'a l'operation terminale.
        List<String> titles = new ArrayList<>(Library.BOOKS.stream().map(Book::title).toList());
        Stream<String> stream = titles.stream();
        // Le stream n'a encore RIEN lu : il lira la liste au moment de count()...
        titles.add("Nouveau livre");
        // ... donc il voit 9 elements, pas 8.
        return stream.count();
    }

    public static List<String> longTitlesAfterAddition() {
        // Meme idee avec un pipeline complet : filter est decrit, pas encore execute.
        List<String> titles = new ArrayList<>(Library.BOOKS.stream().map(Book::title).toList());
        // Le pipeline est seulement DECRIT ici (paresse) : le filter n'a encore vu aucun titre.
        Stream<String> longOnes = titles.stream().filter(t -> t.length() > 12);
        titles.add("Le Seigneur des anneaux");
        // toList() declenche le parcours : le titre ajoute passe donc aussi dans le filter.
        return longOnes.toList();
    }

    public static Optional<String> safeSummary(String isbn) {
        // Function ne peut pas lancer IOException : on l'attrape DANS la lambda
        // et on la transforme en "boite vide".
        return Optional.of(isbn).flatMap(i -> {
            try {
                return Optional.of(Library.loadSummary(i));
            } catch (IOException e) {
                return Optional.empty();
            }
        });
    }

    public static List<String> allSummariesOrFail() {
        // La reference de methode vers summaryUnchecked compile, car cette methode
        // ne declare AUCUNE exception verifiee.
        return Library.BOOKS.stream().map(Book::isbn).map(SolutionDrill13_AdvancedPipelineConcepts::summaryUnchecked).toList();
    }

    private static String summaryUnchecked(String isbn) {
        // Emballage classique : exception verifiee -> exception NON verifiee, cause conservee.
        try {
            return Library.loadSummary(isbn);
        } catch (IOException e) {
            // On garde l'IOException d'origine comme CAUSE : l'appelant peut la retrouver.
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> summariesSkippingErrors() {
        // Chaque isbn devient un Optional (TODO 3), puis Optional::stream fait disparaitre les vides.
        return Library.BOOKS.stream().map(Book::isbn).map(SolutionDrill13_AdvancedPipelineConcepts::safeSummary)
                .flatMap(Optional::stream)
                .toList();
    }

    public static String summaryOrDefault(String isbn) {
        // Reutilise safeSummary : l'exception est deja geree, il ne reste qu'a ouvrir la boite.
        return safeSummary(isbn).orElse("Resume indisponible");
    }

    public static int[] generateIds(int n, IntSupplier supplier) {
        // generate est INFINI : limit(n) est indispensable pour que toArray() se termine.
        return IntStream.generate(supplier).limit(n).toArray();
    }

    public static int[] lateDaysAsInts() {
        // LongStream -> IntStream : conversion qui RETRECIT, donc cast explicite (int).
        return Library.LOANS.stream().mapToLong(Loan::daysLate).mapToInt(l -> (int) l).toArray();
    }

    public static List<String> priceLabels() {
        // DoubleStream n'a pas de map vers un objet : c'est mapToObj.
        return Library.BOOKS.stream().mapToDouble(Book::price).mapToObj(p -> p + " EUR").toList();
    }

    public static ArrayList<Integer> pagesIntoArrayList() {
        // IntStream.collect n'a qu'UNE forme (3 arguments) : pas de collect(Collectors.toList()) ici.
        // ArrayList::add recoit un int et le met en boite (Integer) automatiquement.
        return Library.BOOKS.stream().mapToInt(Book::pages).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static long countWithCloseHook(List<String> log) {
        // try-with-resources appelle close() a la sortie du bloc, ce qui declenche le Runnable d'onClose.
        try (Stream<Book> stream = Library.BOOKS.stream().onClose(() -> log.add("stream ferme"))) {
            return stream.count();
        }
    }

    public static IntToLongFunction pagesToMinutes() {
        // 2L force un calcul en long (pas de debordement possible d'un int).
        return pages -> pages * 2L;
    }

    public static IntPredicate isThick() {
        // IntPredicate : version int de Predicate, test(int) sans boxing.
        return pages -> pages > 300;
    }

    public static DoubleToIntFunction roundedPrice() {
        // Math.round(double) rend un long : il faut le caster en int.
        return price -> (int) Math.round(price);
    }

    public static ToIntBiFunction<Book, Integer> pagesWithBonus() {
        // ToIntBiFunction<T, U> : 2 parametres objets, resultat int (applyAsInt).
        return (book, bonus) -> book.pages() + bonus;
    }
}
