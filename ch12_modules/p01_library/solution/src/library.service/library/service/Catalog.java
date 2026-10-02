package library.service;

import library.model.Book;
import library.model.Genre;
import library.service.internal.Normalizer;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * SOLUTION - le catalogue. Sa methode publique rend des Book : c'est pourquoi library.service
 * declare "requires transitive library.model".
 */
public class Catalog {

    private final List<Book> books;
    private final NavigableMap<String, Book> byKey = new TreeMap<>();

    public Catalog(List<Book> books) {
        this.books = List.copyOf(books);
        books.forEach(b -> byKey.put(Normalizer.sortKey(b.title()), b));
    }

    public int size() {
        return books.size();
    }

    // Tous les titres dont la cle de tri commence par le prefixe : une vue subMap de la TreeMap.
    public List<String> titlesStartingWith(String prefix) {
        return byKey.subMap(prefix, true, prefix + Character.MAX_VALUE, false).values().stream().map(Book::title).toList();
    }

    public Map<Genre, Long> countByGenre() {
        return books.stream().collect(Collectors.groupingBy(Book::genre, TreeMap::new, Collectors.counting()));
    }

    // Decennie -> titres tries par annee.
    public Map<Integer, List<String>> byDecade() {
        return books.stream().sorted((a, b) -> Integer.compare(a.year(), b.year()))
                .collect(Collectors.groupingBy(b -> b.year() / 10 * 10, TreeMap::new, Collectors.mapping(Book::title, Collectors.toList())));
    }

    public List<Book> byAuthor(String author) {
        return books.stream().filter(b -> b.author().equals(author)).toList();
    }
}
