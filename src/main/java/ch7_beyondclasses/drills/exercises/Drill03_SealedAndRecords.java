package ch7_beyondclasses.drills.exercises;

import ch7_beyondclasses.ExerciseChecker;
import ch7_beyondclasses.drills.Catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * DRILL 03 - Records et types sealed
 * ==================================
 *
 * Mode d'emploi : voir Drill01_Interfaces.
 *
 *   sealed interface Item permits Book, Magazine
 *   record Book(String title, int year, int pages)   record Magazine(String title, int issue)
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Book (constructeur compact)  [valider et nettoyer] titre vide -> IllegalArgumentException("titre") ;
 *                                         pages < 1 -> IllegalArgumentException("pages") ; title = title.strip().
 * TODO 2  : Book(String title)           [constructeur non canonique -> this(...)] annee 2000, 100 pages.
 * TODO 3  : Book.isClassic()             [methode d'un record] annee < 1970.
 * TODO 4  : Book.withPages(pages)        ["wither"] un NOUVEAU Book avec d'autres pages.
 * TODO 5  : Book.first()                 [methode static d'un record] le premier livre du Catalog.
 * TODO 6  : fromCatalog()                [creer des records] les 4 livres du Catalog.
 * TODO 7  : describe(item)               [instanceof sur un sealed] "livre Dune" ou "magazine Wired n.12".
 * TODO 8  : totalPages(items)            [accesseurs] somme des pages des Book seulement -> 1353.
 * TODO 9  : sameBook(a, b)               [equals genere] true si memes composants.
 * TODO 10 : bookToString()               [toString genere] new Book("Dune", 1965, 412).toString().
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   record R(int x) : champ private final x, accesseur x() (pas getX), constructeur canonique,
 *                     equals, hashCode, toString "R[x=1]" ; implicitement final ; n'etend rien
 *   Compact R { ... } : on travaille sur les parametres (jamais this.x = ...)
 *   Autre constructeur : 1re ligne this(...) obligatoire
 *   Pas de champ d'instance en plus (static permis), pas de bloc d'instance
 *   sealed ... permits A, B : A et B sont final, sealed ou non-sealed (record et enum : deja final)
 * ---------------------------------------------------------------------
 */
public class Drill03_SealedAndRecords {

    sealed interface Item permits Book, Magazine {
        String title();
    }

    record Book(String title, int year, int pages) implements Item {
        Book {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur compact de Book");
        }

        Book(String title) {
            this(title, 0, 0);
            throw new UnsupportedOperationException("TODO 2 : remplacer this(title, 0, 0) par les bonnes valeurs");
        }

        boolean isClassic() {
            throw new UnsupportedOperationException("TODO 3 : implementer isClassic()");
        }

        Book withPages(int pages) {
            throw new UnsupportedOperationException("TODO 4 : implementer withPages()");
        }

        static Book first() {
            throw new UnsupportedOperationException("TODO 5 : implementer first()");
        }
    }

    record Magazine(String title, int issue) implements Item {
    }

    public static List<Book> fromCatalog() {
        throw new UnsupportedOperationException("TODO 6 : implementer fromCatalog()");
    }

    public static String describe(Item item) {
        throw new UnsupportedOperationException("TODO 7 : implementer describe()");
    }

    public static int totalPages(List<Item> items) {
        throw new UnsupportedOperationException("TODO 8 : implementer totalPages()");
    }

    public static boolean sameBook(Book a, Book b) {
        throw new UnsupportedOperationException("TODO 9 : implementer sameBook()");
    }

    public static String bookToString() {
        throw new UnsupportedOperationException("TODO 10 : implementer bookToString()");
    }

    public static void main(String[] args) {
        String error = null;
        try {
            new Book(" ", 2000, 10);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("1  compact : titre nettoye, titre vide refuse", new Book(" Dune ", 1965, 412).title().equals("Dune") && "titre".equals(error));
        Book simple = new Book("Notes");
        ExerciseChecker.check("2  Book(title) : 2000, 100 pages", simple.year() == 2000 && simple.pages() == 100);
        ExerciseChecker.check("3  isClassic : 1965 oui, 1989 non", new Book("Dune", 1965, 412).isClassic() && !new Book("Hyperion", 1989, 482).isClassic());
        Book original = new Book("Dune", 1965, 412);
        ExerciseChecker.check("4  withPages : nouvel objet", original.withPages(500).pages() == 500 && original.pages() == 412);
        ExerciseChecker.check("5  first() == Dune", Book.first().title().equals("Dune"));
        List<Book> books = fromCatalog();
        ExerciseChecker.check("6  fromCatalog : 4 livres", books.size() == 4 && books.get(3).title().equals("Solaris"));
        ExerciseChecker.check("7  describe", describe(original).equals("livre Dune") && describe(new Magazine("Wired", 12)).equals("magazine Wired n.12"));
        List<Item> items = new ArrayList<>(books);
        items.add(new Magazine("Wired", 12));
        ExerciseChecker.check("8  totalPages == 1353", totalPages(items) == 1353);
        ExerciseChecker.check("9  sameBook", sameBook(original, new Book("Dune", 1965, 412)) && !sameBook(original, original.withPages(1)));
        ExerciseChecker.check("10 bookToString()", bookToString().equals("Book[title=Dune, year=1965, pages=412]"));

        ExerciseChecker.summary();
    }
}
