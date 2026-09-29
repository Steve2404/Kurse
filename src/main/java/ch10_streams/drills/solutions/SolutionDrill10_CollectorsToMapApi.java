package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Loan;
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
 * Corrige du drill 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill10_CollectorsToMapApi.
 */
public class SolutionDrill10_CollectorsToMapApi {

    public static Map<String, String> titleByIsbn() {
        // toMap(cle, valeur) : les isbn sont uniques, aucun doublon possible.
        return Library.BOOKS.stream().collect(Collectors.toMap(Book::isbn, Book::title));
    }

    public static Map<String, Book> bookByIsbn() {
        // Function.identity() : la valeur est l'element lui-meme.
        return Library.BOOKS.stream().collect(Collectors.toMap(Book::isbn, Function.identity()));
    }

    public static Map<String, Integer> bookCountByAuthor() {
        // Chaque livre vaut 1 ; en cas de doublon de cle, on additionne.
        return Library.BOOKS.stream().collect(Collectors.toMap(Book::author, b -> 1, Integer::sum));
    }

    public static TreeMap<String, String> titlesByAuthor() {
        // 4e argument : la Map a fabriquer (TreeMap pour trier les auteurs).
        return Library.BOOKS.stream()
                .collect(Collectors.toMap(Book::author, Book::title, (a, b) -> a + " / " + b, TreeMap::new));
    }

    public static Map<String, String> strictTitleByAuthor() {
        // Sans fonction de fusion, un doublon (Asimov) lance IllegalStateException.
        return Library.BOOKS.stream().collect(Collectors.toMap(Book::author, Book::title));
    }

    public static Map<String, Integer> lockedYearByIsbn() {
        // toUnmodifiableMap (Java 10) : put lance UnsupportedOperationException.
        return Library.BOOKS.stream().collect(Collectors.toUnmodifiableMap(Book::isbn, Book::year));
    }

    public static TreeMap<String, Book> cheapestBookPerGenre() {
        // La fusion garde le moins cher des deux : BinaryOperator.minBy(comparateur).
        return Library.BOOKS.stream().collect(Collectors.toMap(
                Book::genre, Function.identity(),
                BinaryOperator.minBy(Comparator.comparingDouble(Book::price)), TreeMap::new));
    }

    public static Map<String, Integer> lockedPagesByGenre() {
        // toUnmodifiableMap avec fusion : additionne les pages d'un meme genre.
        return Library.BOOKS.stream().collect(Collectors.toUnmodifiableMap(Book::genre, Book::pages, Integer::sum));
    }

    public static Map<String, String> invert(Map<String, String> map) {
        // On repart des entrees et on echange cle et valeur.
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static TreeMap<String, Long> loanCountByMember() {
        // 1L (et non 1) car la valeur doit etre un Long pour Long::sum.
        return Library.LOANS.stream().collect(Collectors.toMap(Loan::memberId, l -> 1L, Long::sum, TreeMap::new));
    }

    public static LinkedHashMap<String, String> memberNameById() {
        // (a, b) -> a : garde la 1re valeur ; LinkedHashMap garde l'ordre d'insertion.
        return Library.MEMBERS.stream()
                .collect(Collectors.toMap(Member::id, Member::name, (a, b) -> a, LinkedHashMap::new));
    }

    public static Map<String, String> safeEmailByName() {
        // toMap refuse les VALEURS null (NullPointerException) : on les remplace par "-".
        return Library.MEMBERS.stream()
                .collect(Collectors.toMap(Member::name, m -> Optional.ofNullable(m.email()).orElse("-")));
    }
}
