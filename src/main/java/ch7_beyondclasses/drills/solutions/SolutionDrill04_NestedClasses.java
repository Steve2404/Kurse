package ch7_beyondclasses.drills.solutions;

import ch7_beyondclasses.drills.Catalog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.drills.exercises.Drill04_NestedClasses.
 */
public class SolutionDrill04_NestedClasses {

    static class Library {
        private final String name;

        Library(String name) {
            this.name = name;
        }

        class Shelf {
            static int COUNT;

            Shelf() {
                // Membre static dans une classe interne : permis depuis Java 16.
                COUNT++;
            }

            String label() {
                // Une classe interne lit les champs (meme private) de son objet englobant.
                return "Shelf of " + name;
            }

            Library owner() {
                // Library.this designe l'objet englobant (this seul serait la Shelf).
                return Library.this;
            }
        }

        static class Card {
            private final int number;

            Card(int number) {
                this.number = number;
            }

            String label() {
                // Une classe static n'a pas d'objet englobant : seulement ses champs.
                return "Card #" + number;
            }
        }
    }

    interface Counter {
        int next();
    }

    public static Library.Shelf newShelf(Library library) {
        // Syntaxe speciale : l'objet englobant, puis .new.
        return library.new Shelf();
    }

    public static Library.Card newCard(int number) {
        // Une imbriquee static se cree sans objet englobant.
        return new Library.Card(number);
    }

    public static List<String> longTitles(int min) {
        // Classe locale : visible ici seulement ; elle lit min (effectivement final).
        class Filter {
            boolean accept(String title) {
                return title.length() > min;
            }
        }
        Filter filter = new Filter();
        List<String> result = new ArrayList<>();
        for (String title : Catalog.TITLES) {
            if (filter.accept(title)) {
                result.add(title);
            }
        }
        return result;
    }

    public static Comparator<String> byLength() {
        // Classe anonyme qui implemente Comparator.
        return new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return Integer.compare(a.length(), b.length());
            }
        };
    }

    public static List<String> sortedTitles() {
        // Une copie triee : les donnees partagees ne bougent pas.
        List<String> titles = new ArrayList<>(List.of(Catalog.TITLES));
        titles.sort(byLength());
        return titles;
    }

    public static Counter counterFrom(int start) {
        // Une anonyme peut avoir son propre champ ; elle lit start (effectivement final).
        return new Counter() {
            private int current = start;

            @Override
            public int next() {
                return current++;
            }
        };
    }
}
