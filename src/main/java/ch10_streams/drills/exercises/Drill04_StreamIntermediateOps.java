package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * DRILL 04 - Toutes les operations INTERMEDIAIRES de Stream (projet bibliotheque)
 * ==============================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Donnees : Library.BOOKS (8 livres) et Library.LOANS.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : titlesOfGenre(genre)     [filter + map] SF -> [Dune, Fondation, Les Robots, Neuromancien].
 * TODO 2  : authorsInOrder()         [distinct] auteurs sans doublon, ordre de BOOKS.
 * TODO 3  : titlesSorted()           [sorted()] ordre naturel des titres.
 * TODO 4  : titlesByYearDesc()       [sorted(Comparator)] du plus recent au plus ancien.
 * TODO 5  : allTags()                [flatMap + distinct + sorted] tous les tags.
 * TODO 6  : firstTitles(n)           [limit] les n premiers titres.
 * TODO 7  : titlesAfter(n)           [skip] les titres apres les n premiers.
 * TODO 8  : cheapTitles(max)         [sorted par prix + takeWhile] tant que prix <= max.
 * TODO 9  : expensiveTitles(min)     [sorted par prix + dropWhile] a partir du 1er prix >= min.
 * TODO 10 : countSfWithTrace(seen)   [peek] ajoute chaque isbn a seen AVANT de filtrer
 *           les SF, puis compte -> 4 (et seen contient les 8 isbn).
 * TODO 11 : pagesOf(genre)           [mapToInt + toArray] tableau des pages du genre.
 * TODO 12 : numberedTitles()         [IntStream.range + mapToObj] "1. Dune" ... "8. Le Hobbit".
 * TODO 13 : pricesWithTax(rate)      [mapToDouble + boxed] prix * (1 + rate).
 * TODO 14 : totalTagLetters()        [flatMapToInt] somme des longueurs de tous les tags
 *           (avec doublons).
 * TODO 15 : totalPagesAsLong()       [mapToLong + sum].
 * TODO 16 : titlesBorrowedBy(id)     [filter + map + flatMap] titres empruntes par
 *           le membre, dans l'ordre des emprunts (jointure LOANS -> BOOKS).
 *           M1 -> [Dune, Le Hobbit, Le Petit Prince].
 * TODO 17 : upperTitlesOfAuthor(a)   [filter + map(String::toUpperCase) + sorted].
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   filter(Predicate)  map(Function)  flatMap(Function<T, Stream<R>>)
 *   distinct()  sorted()  sorted(Comparator)  peek(Consumer)
 *   limit(long)  skip(long)  takeWhile(Predicate) (9)  dropWhile(Predicate) (9)
 *   mapToInt / mapToLong / mapToDouble(ToXxxFunction)
 *   flatMapToInt / flatMapToLong / flatMapToDouble
 *   IntStream.mapToObj(IntFunction)  boxed()  asLongStream()  asDoubleStream()
 *   Toutes sont PARESSEUSES : rien ne se passe sans operation terminale.
 * ---------------------------------------------------------------------
 */
public class Drill04_StreamIntermediateOps {

    public static List<String> titlesOfGenre(String genre) {
        throw new UnsupportedOperationException("TODO 1 : implementer titlesOfGenre()");
    }

    public static List<String> authorsInOrder() {
        throw new UnsupportedOperationException("TODO 2 : implementer authorsInOrder()");
    }

    public static List<String> titlesSorted() {
        throw new UnsupportedOperationException("TODO 3 : implementer titlesSorted()");
    }

    public static List<String> titlesByYearDesc() {
        throw new UnsupportedOperationException("TODO 4 : implementer titlesByYearDesc()");
    }

    public static List<String> allTags() {
        throw new UnsupportedOperationException("TODO 5 : implementer allTags()");
    }

    public static List<String> firstTitles(int n) {
        throw new UnsupportedOperationException("TODO 6 : implementer firstTitles()");
    }

    public static List<String> titlesAfter(int n) {
        throw new UnsupportedOperationException("TODO 7 : implementer titlesAfter()");
    }

    public static List<String> cheapTitles(double max) {
        throw new UnsupportedOperationException("TODO 8 : implementer cheapTitles()");
    }

    public static List<String> expensiveTitles(double min) {
        throw new UnsupportedOperationException("TODO 9 : implementer expensiveTitles()");
    }

    public static long countSfWithTrace(List<String> seen) {
        throw new UnsupportedOperationException("TODO 10 : implementer countSfWithTrace()");
    }

    public static int[] pagesOf(String genre) {
        throw new UnsupportedOperationException("TODO 11 : implementer pagesOf()");
    }

    public static List<String> numberedTitles() {
        throw new UnsupportedOperationException("TODO 12 : implementer numberedTitles()");
    }

    public static List<Double> pricesWithTax(double rate) {
        throw new UnsupportedOperationException("TODO 13 : implementer pricesWithTax()");
    }

    public static int totalTagLetters() {
        throw new UnsupportedOperationException("TODO 14 : implementer totalTagLetters()");
    }

    public static long totalPagesAsLong() {
        throw new UnsupportedOperationException("TODO 15 : implementer totalPagesAsLong()");
    }

    public static List<String> titlesBorrowedBy(String memberId) {
        throw new UnsupportedOperationException("TODO 16 : implementer titlesBorrowedBy()");
    }

    public static List<String> upperTitlesOfAuthor(String author) {
        throw new UnsupportedOperationException("TODO 17 : implementer upperTitlesOfAuthor()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  titlesOfGenre(SF)",
                titlesOfGenre("SF").equals(List.of("Dune", "Fondation", "Les Robots", "Neuromancien")));
        ExerciseChecker.check("2  authorsInOrder",
                authorsInOrder().equals(List.of("Herbert", "Asimov", "Saint-Exupery", "Orwell", "Gibson", "Tolkien")));
        ExerciseChecker.check("3  titlesSorted (chiffres < majuscules, 'Le ' < 'Les')",
                titlesSorted().equals(List.of("1984", "Dune", "Fondation", "La Ferme des animaux", "Le Hobbit",
                        "Le Petit Prince", "Les Robots", "Neuromancien")));
        ExerciseChecker.check("4  titlesByYearDesc",
                titlesByYearDesc().equals(List.of("Neuromancien", "Dune", "Fondation", "Les Robots", "1984",
                        "La Ferme des animaux", "Le Petit Prince", "Le Hobbit")));
        ExerciseChecker.check("5  allTags",
                allTags().equals(List.of("aventure", "classique", "cyberpunk", "empire", "enfance", "espace",
                        "fable", "politique", "robots")));
        ExerciseChecker.check("6  firstTitles(2) == [Dune, Fondation]", firstTitles(2).equals(List.of("Dune", "Fondation")));
        ExerciseChecker.check("7  titlesAfter(6) == [Neuromancien, Le Hobbit]",
                titlesAfter(6).equals(List.of("Neuromancien", "Le Hobbit")));
        ExerciseChecker.check("8  cheapTitles(7.5) == [La Ferme des animaux, Le Petit Prince, Les Robots]",
                cheapTitles(7.5).equals(List.of("La Ferme des animaux", "Le Petit Prince", "Les Robots")));
        ExerciseChecker.check("9  expensiveTitles(9.0) == [Neuromancien, Dune, Le Hobbit]",
                expensiveTitles(9.0).equals(List.of("Neuromancien", "Dune", "Le Hobbit")));

        List<String> seen = new ArrayList<>();
        ExerciseChecker.check("10 countSfWithTrace == 4", countSfWithTrace(seen) == 4);
        ExerciseChecker.check("10 ... et seen == [B1..B8]",
                seen.equals(List.of("B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8")));

        ExerciseChecker.check("11 pagesOf(SF) == [412, 255, 253, 271]",
                Arrays.equals(pagesOf("SF"), new int[]{412, 255, 253, 271}));
        List<String> numbered = numberedTitles();
        ExerciseChecker.check("12 numberedTitles : 8 elements, 1. Dune ... 8. Le Hobbit",
                numbered.size() == 8 && numbered.get(0).equals("1. Dune") && numbered.get(7).equals("8. Le Hobbit"));
        ExerciseChecker.check("13 pricesWithTax(0.5)",
                pricesWithTax(0.5).equals(List.of(14.25, 12.0, 11.25, 9.0, 12.75, 8.25, 13.5, 15.0)));
        ExerciseChecker.check("14 totalTagLetters == 108", totalTagLetters() == 108);
        ExerciseChecker.check("15 totalPagesAsLong == 2037", totalPagesAsLong() == 2037L);
        ExerciseChecker.check("16 titlesBorrowedBy(M1) == [Dune, Le Hobbit, Le Petit Prince], (M4) == []",
                titlesBorrowedBy("M1").equals(List.of("Dune", "Le Hobbit", "Le Petit Prince")) && titlesBorrowedBy("M4").isEmpty());
        ExerciseChecker.check("17 upperTitlesOfAuthor(Orwell) == [1984, LA FERME DES ANIMAUX]",
                upperTitlesOfAuthor("Orwell").equals(List.of("1984", "LA FERME DES ANIMAUX")));

        ExerciseChecker.summary();
    }
}
