package ch7_beyondclasses.drills.solutions;

import ch7_beyondclasses.drills.Catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.drills.exercises.Drill03_SealedAndRecords.
 */
public class SolutionDrill03_SealedAndRecords {

    sealed interface Item permits Book, Magazine {
        String title();
    }

    record Book(String title, int year, int pages) implements Item {
        Book {
            // Compact : valider puis transformer le parametre (javac affecte les champs apres).
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("titre");
            }
            if (pages < 1) {
                throw new IllegalArgumentException("pages");
            }
            title = title.strip();
        }

        Book(String title) {
            // Un constructeur non canonique delegue toujours au canonique.
            this(title, 2000, 100);
        }

        boolean isClassic() {
            // year est le champ genere (lisible directement dans le record).
            return year < 1970;
        }

        Book withPages(int pages) {
            // Immuable : on rend un nouveau record.
            return new Book(title, year, pages);
        }

        static Book first() {
            // Methode static permise dans un record.
            return new Book(Catalog.TITLES[0], Catalog.YEARS[0], Catalog.PAGES[0]);
        }
    }

    record Magazine(String title, int issue) implements Item {
    }

    public static List<Book> fromCatalog() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < Catalog.TITLES.length; i++) {
            books.add(new Book(Catalog.TITLES[i], Catalog.YEARS[i], Catalog.PAGES[i]));
        }
        return books;
    }

    public static String describe(Item item) {
        // sealed : Book et Magazine sont les seuls cas.
        if (item instanceof Book b) {
            return "livre " + b.title();
        }
        if (item instanceof Magazine m) {
            return "magazine " + m.title() + " n." + m.issue();
        }
        throw new IllegalStateException();
    }

    public static int totalPages(List<Item> items) {
        // L'accesseur pages() n'existe que sur Book.
        int total = 0;
        for (Item item : items) {
            if (item instanceof Book b) {
                total += b.pages();
            }
        }
        return total;
    }

    public static boolean sameBook(Book a, Book b) {
        // equals genere : memes composants = egaux.
        return a.equals(b);
    }

    public static String bookToString() {
        // toString genere : NomDuRecord[composant=valeur, ...].
        return new Book("Dune", 1965, 412).toString();
    }
}
