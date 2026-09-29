package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Member;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DRILL 10 - Toutes les formes de Collectors.toMap / toUnmodifiableMap (projet bibliotheque)
 * =========================================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : titleByIsbn()          [toMap(cle, valeur)]
 * TODO 2  : bookByIsbn()           [toMap(cle, Function.identity())]
 * TODO 3  : bookCountByAuthor()    [toMap(cle, b -> 1, Integer::sum)] Asimov -> 2.
 * TODO 4  : titlesByAuthor()       [toMap(cle, valeur, fusion, TreeMap::new)] les titres
 *           d'un meme auteur colles avec " / " dans l'ordre de BOOKS.
 * TODO 5  : strictTitleByAuthor()  [toMap sans fusion] DOIT lancer IllegalStateException
 *           (Asimov et Orwell ont 2 livres).
 * TODO 6  : lockedYearByIsbn()     [toUnmodifiableMap(cle, valeur)]
 * TODO 7  : cheapestBookPerGenre() [toMap + BinaryOperator.minBy + TreeMap::new]
 *           genre -> le livre le moins cher.
 * TODO 8  : lockedPagesByGenre()   [toUnmodifiableMap(cle, valeur, fusion)] genre -> pages totales.
 * TODO 9  : invert(map)            [entrySet().stream() + toMap(getValue, getKey)]
 * TODO 10 : loanCountByMember()    [toMap(..., Long::sum, TreeMap::new)] sur LOANS.
 * TODO 11 : memberNameById()       [toMap(..., (a, b) -> a, LinkedHashMap::new)] ordre d'insertion.
 * TODO 12 : safeEmailByName()      [toMap + valeur jamais null] nom -> email, "-" si null.
 *           (main() montre que toMap lance NullPointerException si une VALEUR est null)
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   toMap(Function cle, Function valeur)                  doublon -> IllegalStateException
 *   toMap(cle, valeur, BinaryOperator fusion)             doublon -> fusion(ancien, nouveau)
 *   toMap(cle, valeur, fusion, Supplier<Map> fabrique)    ex : TreeMap::new, LinkedHashMap::new
 *   toUnmodifiableMap(cle, valeur) / toUnmodifiableMap(cle, valeur, fusion)
 *   Function.identity()   BinaryOperator.minBy(cmp) / maxBy(cmp)
 *   Une VALEUR null -> NullPointerException (toMap utilise Map.merge)
 * ---------------------------------------------------------------------
 */
public class Drill10_CollectorsToMapApi {

    public static Map<String, String> titleByIsbn() {
        throw new UnsupportedOperationException("TODO 1 : implementer titleByIsbn()");
    }

    public static Map<String, Book> bookByIsbn() {
        throw new UnsupportedOperationException("TODO 2 : implementer bookByIsbn()");
    }

    public static Map<String, Integer> bookCountByAuthor() {
        throw new UnsupportedOperationException("TODO 3 : implementer bookCountByAuthor()");
    }

    public static TreeMap<String, String> titlesByAuthor() {
        throw new UnsupportedOperationException("TODO 4 : implementer titlesByAuthor()");
    }

    public static Map<String, String> strictTitleByAuthor() {
        throw new UnsupportedOperationException("TODO 5 : implementer strictTitleByAuthor()");
    }

    public static Map<String, Integer> lockedYearByIsbn() {
        throw new UnsupportedOperationException("TODO 6 : implementer lockedYearByIsbn()");
    }

    public static TreeMap<String, Book> cheapestBookPerGenre() {
        throw new UnsupportedOperationException("TODO 7 : implementer cheapestBookPerGenre()");
    }

    public static Map<String, Integer> lockedPagesByGenre() {
        throw new UnsupportedOperationException("TODO 8 : implementer lockedPagesByGenre()");
    }

    public static Map<String, String> invert(Map<String, String> map) {
        throw new UnsupportedOperationException("TODO 9 : implementer invert()");
    }

    public static TreeMap<String, Long> loanCountByMember() {
        throw new UnsupportedOperationException("TODO 10 : implementer loanCountByMember()");
    }

    public static LinkedHashMap<String, String> memberNameById() {
        throw new UnsupportedOperationException("TODO 11 : implementer memberNameById()");
    }

    public static Map<String, String> safeEmailByName() {
        throw new UnsupportedOperationException("TODO 12 : implementer safeEmailByName()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  titleByIsbn : 8 cles, B5 -> 1984", titleByIsbn().size() == 8 && titleByIsbn().get("B5").equals("1984"));
        ExerciseChecker.check("2  bookByIsbn : B8 -> Le Hobbit", bookByIsbn().get("B8").title().equals("Le Hobbit"));
        ExerciseChecker.check("3  bookCountByAuthor : Asimov 2, Orwell 2, Gibson 1",
                bookCountByAuthor().get("Asimov") == 2 && bookCountByAuthor().get("Orwell") == 2
                        && bookCountByAuthor().get("Gibson") == 1 && bookCountByAuthor().size() == 6);
        ExerciseChecker.check("4  titlesByAuthor", titlesByAuthor().toString().equals(
                "{Asimov=Fondation / Les Robots, Gibson=Neuromancien, Herbert=Dune, Orwell=1984 / La Ferme des animaux, "
                        + "Saint-Exupery=Le Petit Prince, Tolkien=Le Hobbit}"));

        String message = null;
        try {
            strictTitleByAuthor();
        } catch (IllegalStateException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("5  strictTitleByAuthor lance IllegalStateException(\"Duplicate key Asimov ...\")",
                message != null && message.startsWith("Duplicate key Asimov"));

        boolean locked = false;
        try {
            lockedYearByIsbn().put("B9", 2000);
        } catch (UnsupportedOperationException e) {
            locked = true;
        }
        ExerciseChecker.check("6  lockedYearByIsbn : B1 -> 1965 et refuse put", locked && lockedYearByIsbn().get("B1") == 1965);

        TreeMap<String, Book> cheapest = cheapestBookPerGenre();
        ExerciseChecker.check("7  cheapestBookPerGenre : Conte=Le Petit Prince, Dystopie=La Ferme..., Fantasy=Le Hobbit, SF=Les Robots",
                cheapest.keySet().toString().equals("[Conte, Dystopie, Fantasy, SF]")
                        && cheapest.get("SF").title().equals("Les Robots")
                        && cheapest.get("Dystopie").title().equals("La Ferme des animaux"));
        ExerciseChecker.check("8  lockedPagesByGenre : SF -> 1191, Dystopie -> 440",
                lockedPagesByGenre().get("SF") == 1191 && lockedPagesByGenre().get("Dystopie") == 440);
        ExerciseChecker.check("9  invert({B1=Dune}) == {Dune=B1}", invert(Map.of("B1", "Dune")).equals(Map.of("Dune", "B1")));
        ExerciseChecker.check("10 loanCountByMember == {M1=3, M2=3, M3=2}",
                loanCountByMember().toString().equals("{M1=3, M2=3, M3=2}"));
        ExerciseChecker.check("11 memberNameById == {M1=Lea, M2=Hugo, M3=Ines, M4=Tom}",
                memberNameById().toString().equals("{M1=Lea, M2=Hugo, M3=Ines, M4=Tom}"));

        boolean npe = false;
        try {
            Library.MEMBERS.stream().collect(Collectors.toMap(Member::name, Member::email));
        } catch (NullPointerException e) {
            npe = true;
        }
        ExerciseChecker.check("12 (demo) toMap avec l'email null de Hugo lance NullPointerException", npe);
        ExerciseChecker.check("12 safeEmailByName : Hugo -> '-', Lea -> lea@mail.fr",
                safeEmailByName().get("Hugo").equals("-") && safeEmailByName().get("Lea").equals("lea@mail.fr"));

        ExerciseChecker.summary();
    }
}
