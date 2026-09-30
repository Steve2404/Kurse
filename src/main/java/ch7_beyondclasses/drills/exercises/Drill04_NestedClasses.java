package ch7_beyondclasses.drills.exercises;

import ch7_beyondclasses.ExerciseChecker;
import ch7_beyondclasses.drills.Catalog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * DRILL 04 - Classes imbriquees : interne, static, locale, anonyme
 * ================================================================
 *
 * Mode d'emploi : voir Drill01_Interfaces.
 *
 *   class Library (bibliotheque) avec un champ name.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Library.Shelf()              [membre static dans une classe interne, permis depuis Java 16]
 *                                         incrementer COUNT (le champ static est deja declare).
 * TODO 2  : Library.Shelf.label()        [classe interne] lit name de la Library englobante -> "Shelf of Centre".
 * TODO 3  : Library.Card.label()         [classe imbriquee static] ne lit que son champ -> "Card #7".
 * TODO 4  : newShelf(library)            [creer une interne] library.new Shelf().
 * TODO 5  : newCard(number)              [creer une static] new Library.Card(number).
 * TODO 6  : Library.Shelf.owner()        [NomEnglobant.this] rendre Library.this (l'objet englobant).
 * TODO 7  : longTitles(min)              [classe locale] une classe locale Filter avec accept(title) ;
 *                                         titres du catalogue de longueur > min.
 * TODO 8  : byLength()                   [Comparator anonyme] compare les longueurs de titres.
 * TODO 9  : sortedTitles()               [utiliser le comparateur anonyme] -> [Dune, Solaris, Hyperion, Fondation].
 * TODO 10 : counterFrom(start)           [anonyme qui capture un parametre] un Counter dont next() rend start, start + 1, ...
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Interne : new Outer().new Inner() ; voit les champs d'instance et Outer.this
 *   static nested : new Outer.Nested() ; ne voit PAS les champs d'instance de Outer
 *   Locale : dans une methode ; pas de public/private/static ; final ou abstract permis
 *   Anonyme : new I() { ... } ; un seul super-type ; pas de constructeur
 *   Locale et anonyme : lisent seulement des variables final ou effectivement final
 *   Depuis Java 16 : une classe interne peut avoir des membres static
 * ---------------------------------------------------------------------
 */
public class Drill04_NestedClasses {

    static class Library {
        private final String name;

        Library(String name) {
            this.name = name;
        }

        class Shelf {
            static int COUNT;

            Shelf() {
                throw new UnsupportedOperationException("TODO 1 : incrementer COUNT");
            }

            String label() {
                throw new UnsupportedOperationException("TODO 2 : implementer Shelf.label()");
            }

            Library owner() {
                throw new UnsupportedOperationException("TODO 6 : implementer Shelf.owner()");
            }
        }

        static class Card {
            private final int number;

            Card(int number) {
                this.number = number;
            }

            String label() {
                throw new UnsupportedOperationException("TODO 3 : implementer Card.label()");
            }
        }
    }

    interface Counter {
        int next();
    }

    public static Library.Shelf newShelf(Library library) {
        throw new UnsupportedOperationException("TODO 4 : implementer newShelf()");
    }

    public static Library.Card newCard(int number) {
        throw new UnsupportedOperationException("TODO 5 : implementer newCard()");
    }

    public static List<String> longTitles(int min) {
        throw new UnsupportedOperationException("TODO 7 : implementer longTitles()");
    }

    public static Comparator<String> byLength() {
        throw new UnsupportedOperationException("TODO 8 : implementer byLength()");
    }

    public static List<String> sortedTitles() {
        throw new UnsupportedOperationException("TODO 9 : implementer sortedTitles()");
    }

    public static Counter counterFrom(int start) {
        throw new UnsupportedOperationException("TODO 10 : implementer counterFrom()");
    }

    public static void main(String[] args) {
        Library centre = new Library("Centre");
        int before = Library.Shelf.COUNT;
        Library.Shelf shelf = centre.new Shelf();
        ExerciseChecker.check("1  Shelf.COUNT augmente a chaque new", Library.Shelf.COUNT == before + 1);
        ExerciseChecker.check("2  Shelf.label() lit le champ de la Library", shelf.label().equals("Shelf of Centre"));
        ExerciseChecker.check("3  Card.label()", new Library.Card(7).label().equals("Card #7"));
        ExerciseChecker.check("4  newShelf : liee a SA Library", newShelf(new Library("Nord")).label().equals("Shelf of Nord"));
        ExerciseChecker.check("5  newCard(3)", newCard(3).label().equals("Card #3"));
        ExerciseChecker.check("6  owner() == la Library englobante", newShelf(centre).owner() == centre);
        ExerciseChecker.check("7  longTitles(6) == [Fondation, Hyperion, Solaris]", longTitles(6).equals(List.of("Fondation", "Hyperion", "Solaris")));
        ExerciseChecker.check("8  byLength : Dune < Solaris", byLength().compare("Dune", "Solaris") < 0);
        ExerciseChecker.check("9  sortedTitles", sortedTitles().equals(List.of("Dune", "Solaris", "Hyperion", "Fondation")));
        Counter counter = counterFrom(5);
        ExerciseChecker.check("10 counterFrom(5) : 5, 6, 7", counter.next() == 5 && counter.next() == 6 && counter.next() == 7);

        ExerciseChecker.summary();
    }
}
