package ch7_beyondclasses.drills.solutions;

import ch7_beyondclasses.drills.Catalog;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    enum Era {
        CLASSIC, MODERN, RECENT;

        static Era of(int year) {
            // Methode static d'enum qui choisit une constante.
            if (year < 1960) {
                return CLASSIC;
            }
            return year < 1980 ? MODERN : RECENT;
        }
    }

    sealed interface Media permits Novel, Comic {
        String title();

        Era era();

        default String summary() {
            // Un default construit sur deux methodes abstraites (fournies par les records).
            return title() + " (" + era() + ")";
        }
    }

    record Novel(String title, int year, int pages) implements Media {
        Novel {
            // Validation dans le constructeur compact.
            if (pages < 1) {
                throw new IllegalArgumentException("pages");
            }
        }

        @Override
        public Era era() {
            return Era.of(year);
        }
    }

    record Comic(String title) implements Media {
        @Override
        public Era era() {
            return Era.RECENT;
        }
    }

    interface Rating {
        int rate(Media media);
    }

    public static List<Media> novels() {
        List<Media> result = new ArrayList<>();
        for (int i = 0; i < Catalog.TITLES.length; i++) {
            result.add(new Novel(Catalog.TITLES[i], Catalog.YEARS[i], Catalog.PAGES[i]));
        }
        return result;
    }

    public static Map<Era, Integer> countByEra(List<Media> media) {
        // EnumMap : l'ordre des constantes, pas l'ordre d'insertion.
        Map<Era, Integer> counts = new EnumMap<>(Era.class);
        for (Media m : media) {
            counts.merge(m.era(), 1, Integer::sum);
        }
        return counts;
    }

    public static Rating byPages() {
        // Lambda + instanceof sur le sealed.
        return media -> media instanceof Novel n ? n.pages() / 10 : 1;
    }

    public static Media bestRated(List<Media> media, Rating rating) {
        Media best = media.get(0);
        for (Media m : media) {
            if (rating.rate(m) > rating.rate(best)) {
                best = m;
            }
        }
        return best;
    }

    public static List<String> shelfLabels(List<Media> media) {
        List<String> labels = new ArrayList<>();
        for (Media m : media) {
            labels.add(m.summary());
        }
        return labels;
    }
}
