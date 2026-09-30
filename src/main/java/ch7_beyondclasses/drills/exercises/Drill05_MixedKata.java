package ch7_beyondclasses.drills.exercises;

import ch7_beyondclasses.ExerciseChecker;
import ch7_beyondclasses.drills.Catalog;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * DRILL 05 - Kata melange : tout le chapitre 7 sans indice de forme
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_Interfaces. Ici, PAS de crochet. Fais ce
 * drill seulement quand les drills 01 a 04 passent.
 *
 * Une mediatheque : des Media (interface sealed) qui sont des Novel ou
 * des Comic (records), un Era (enum) selon l'annee, une interface Rating
 * a une methode.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : Era.of(year)              < 1960 CLASSIC, < 1980 MODERN, sinon RECENT.
 * TODO 2  : Novel (compact)           pages < 1 -> IllegalArgumentException("pages").
 * TODO 3  : Novel.era()               l'Era de son annee.
 * TODO 4  : Comic.era()               toujours RECENT.
 * TODO 5  : Media.summary()           default : title() + " (" + era() + ")".
 * TODO 6  : novels()                  les 4 romans du Catalog.
 * TODO 7  : countByEra(media)         EnumMap -> pour les 4 romans {CLASSIC=1, MODERN=2, RECENT=1}.
 * TODO 8  : byPages()                 un Rating (lambda) : un Novel vaut pages / 10, un Comic vaut 1.
 * TODO 9  : bestRated(media, rating)  le media de note maximale.
 * TODO 10 : shelfLabels(media)        les summary() dans une liste, dans l'ordre.
 */
public class Drill05_MixedKata {

    enum Era {
        CLASSIC, MODERN, RECENT;

        static Era of(int year) {
            throw new UnsupportedOperationException("TODO 1 : implementer Era.of()");
        }
    }

    sealed interface Media permits Novel, Comic {
        String title();

        Era era();

        default String summary() {
            throw new UnsupportedOperationException("TODO 5 : implementer summary()");
        }
    }

    record Novel(String title, int year, int pages) implements Media {
        Novel {
            throw new UnsupportedOperationException("TODO 2 : implementer le constructeur compact de Novel");
        }

        @Override
        public Era era() {
            throw new UnsupportedOperationException("TODO 3 : implementer Novel.era()");
        }
    }

    record Comic(String title) implements Media {
        @Override
        public Era era() {
            throw new UnsupportedOperationException("TODO 4 : implementer Comic.era()");
        }
    }

    interface Rating {
        int rate(Media media);
    }

    public static List<Media> novels() {
        throw new UnsupportedOperationException("TODO 6 : implementer novels()");
    }

    public static Map<Era, Integer> countByEra(List<Media> media) {
        throw new UnsupportedOperationException("TODO 7 : implementer countByEra()");
    }

    public static Rating byPages() {
        throw new UnsupportedOperationException("TODO 8 : implementer byPages()");
    }

    public static Media bestRated(List<Media> media, Rating rating) {
        throw new UnsupportedOperationException("TODO 9 : implementer bestRated()");
    }

    public static List<String> shelfLabels(List<Media> media) {
        throw new UnsupportedOperationException("TODO 10 : implementer shelfLabels()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  Era.of : 1951 CLASSIC, 1965 MODERN, 1989 RECENT",
                Era.of(1951) == Era.CLASSIC && Era.of(1965) == Era.MODERN && Era.of(1989) == Era.RECENT);
        String error = null;
        try {
            new Novel("Vide", 2000, 0);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("2  Novel refuse 0 page", "pages".equals(error));
        ExerciseChecker.check("3  Novel.era()", new Novel("Dune", 1965, 412).era() == Era.MODERN);
        ExerciseChecker.check("4  Comic.era() == RECENT", new Comic("Tintin").era() == Era.RECENT);
        ExerciseChecker.check("5  summary : Dune (MODERN)", new Novel("Dune", 1965, 412).summary().equals("Dune (MODERN)"));
        List<Media> books = novels();
        ExerciseChecker.check("6  novels : 4 romans", books.size() == 4);
        ExerciseChecker.check("7  countByEra == {CLASSIC=1, MODERN=2, RECENT=1}", countByEra(books).toString().equals("{CLASSIC=1, MODERN=2, RECENT=1}"));
        ExerciseChecker.check("8  byPages : Dune 41, Comic 1", byPages().rate(new Novel("Dune", 1965, 412)) == 41 && byPages().rate(new Comic("Tintin")) == 1);
        ExerciseChecker.check("9  bestRated == Hyperion", bestRated(books, byPages()).title().equals("Hyperion"));
        ExerciseChecker.check("10 shelfLabels",
                shelfLabels(books).equals(List.of("Dune (MODERN)", "Fondation (CLASSIC)", "Hyperion (RECENT)", "Solaris (MODERN)")));

        ExerciseChecker.summary();
    }
}
