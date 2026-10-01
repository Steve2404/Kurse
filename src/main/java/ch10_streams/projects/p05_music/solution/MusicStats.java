package ch10_streams.projects.p05_music.solution;

import ch10_streams.projects.p05_music.Data;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.minBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.teeing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * SOLUTION du projet 5 - une conception possible.
 */
public class MusicStats {

    record Play(String user, String artist, String title, String genre, int seconds, List<String> tags) {
        static Play parse(String line) {
            String[] p = line.split(";");
            return new Play(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]), List.of(p[5].split("\\|")));
        }

        boolean valid() {
            return seconds >= Data.VALID_SECONDS;
        }
    }

    // Une recommandation candidate : un voisin et sa similarite.
    record Neighbour(String user, double similarity) {
    }

    private final List<Play> plays;

    MusicStats(List<String> lines) {
        plays = lines.stream().map(Play::parse).toList();
    }

    void report() {
        // groupingBy a 3 arguments : la fabrique TreeMap::new donne un affichage trie et deterministe.
        Map<String, Long> perGenre = plays.stream().collect(groupingBy(Play::genre, TreeMap::new, counting()));
        System.out.println("ECOUTES PAR GENRE : " + perGenre);

        // partitioningBy a TOUJOURS les deux cles true/false, meme si une partition est vide.
        Map<Boolean, Long> validity = plays.stream().collect(partitioningBy(Play::valid, counting()));
        System.out.println("VALIDEES : " + validity.get(true) + ", ZAPPEES : " + validity.get(false));
        Map<Boolean, Set<String>> skippers = plays.stream()
                .collect(partitioningBy(Play::valid, mapping(Play::user, toCollection(TreeSet::new))));
        System.out.println("ONT ZAPPE : " + skippers.get(false));

        topArtistPerGenre();

        // filtering EN AVAL : zoe garde sa cle avec 0 ; un filter AVANT groupingBy l'aurait fait disparaitre.
        Map<String, Integer> validTime = plays.stream()
                .collect(groupingBy(Play::user, TreeMap::new, filtering(Play::valid, summingInt(Play::seconds))));
        System.out.println("TEMPS VALIDE PAR UTILISATEUR : " + validTime);

        // flatMapping : chaque ecoute apporte PLUSIEURS ambiances, aplaties dans l'ensemble du genre.
        Map<String, TreeSet<String>> moods = plays.stream()
                .collect(groupingBy(Play::genre, TreeMap::new, flatMapping(p -> p.tags().stream(), toCollection(TreeSet::new))));
        System.out.println("AMBIANCES PAR GENRE : " + moods);

        // maxBy rend un Optional (groupe vide impossible ici, mais le type ne le sait pas) -> collectingAndThen le deballe.
        Map<String, String> longest = plays.stream()
                .collect(groupingBy(Play::user, TreeMap::new,
                        collectingAndThen(maxBy(Comparator.comparingInt(Play::seconds)), o -> o.map(Play::title).orElseThrow())));
        System.out.println("ECOUTE LA PLUS LONGUE : " + longest);

        // reducing a 3 arguments : identite, transformation, operateur -> l'equivalent de summingInt.
        Map<String, Integer> genreTime = plays.stream()
                .collect(groupingBy(Play::genre, TreeMap::new, reducing(0, Play::seconds, Integer::sum)));
        System.out.println("SECONDES PAR GENRE : " + genreTime);

        // toMap sans fonction de fusion lancerait IllegalStateException : "Hello" est ecoute 3 fois.
        Map<String, Integer> perTitle = plays.stream().collect(toMap(Play::title, Play::seconds, Integer::sum, TreeMap::new));
        System.out.println("TOP 3 TITRES : " + perTitle.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(3)
                .map(e -> e.getKey() + " (" + e.getValue() + " s)")
                .collect(joining(", ", "[", "]")));

        IntSummaryStatistics stats = plays.stream().collect(summarizingInt(Play::seconds));
        System.out.println(String.format(Locale.US, "STATS : %d ecoutes, min %d s, max %d s, moyenne %.1f s",
                stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage()));

        // teeing : deux collecteurs sur le MEME passage, fusionnes a la fin.
        // Piege : println(collect(teeing(...))) ne compile pas (println(char[]) ou println(String) ?) -> variable typee.
        String extremes = plays.stream().collect(teeing(
                minBy(Comparator.comparingInt(Play::seconds)),
                maxBy(Comparator.comparingInt(Play::seconds)),
                (min, max) -> "EXTREMES : " + min.map(p -> p.title() + " par " + p.user()).orElse("-")
                        + " / " + max.map(p -> p.title() + " par " + p.user()).orElse("-")));
        System.out.println(extremes);
        String average = plays.stream().filter(Play::valid).collect(teeing(
                counting(),
                Collectors.averagingInt(Play::seconds),
                (n, avg) -> String.format(Locale.US, "MOYENNE VALIDEE : %.1f s sur %d ecoutes", avg, n)));
        System.out.println(average);

        explorers();
        recommendations();
    }

    // Groupement imbrique : genre -> (artiste -> nombre), puis on reduit chaque sous-table a son meilleur artiste.
    void topArtistPerGenre() {
        Map<String, String> top = plays.stream().filter(Play::valid)
                .collect(groupingBy(Play::genre, TreeMap::new, collectingAndThen(
                        groupingBy(Play::artist, counting()),
                        counts -> counts.entrySet().stream()
                                .max(Map.Entry.<String, Long>comparingByValue()
                                        .thenComparing(Map.Entry.<String, Long>comparingByKey().reversed()))
                                .map(e -> e.getKey() + " (" + e.getValue() + ")")
                                .orElseThrow())));
        System.out.println("TOP ARTISTE PAR GENRE : " + top);
    }

    Map<String, Set<String>> validArtistsByUser() {
        return plays.stream().filter(Play::valid)
                .collect(groupingBy(Play::user, TreeMap::new, mapping(Play::artist, toCollection(TreeSet::new))));
    }

    void explorers() {
        long genres = plays.stream().map(Play::genre).distinct().count();
        Map<String, Set<String>> genresByUser = plays.stream().filter(Play::valid)
                .collect(groupingBy(Play::user, TreeMap::new, mapping(Play::genre, toSet())));
        System.out.println("EXPLORATEURS (tous les genres) : " + genresByUser.entrySet().stream()
                .filter(e -> e.getValue().size() == genres)
                .map(Map.Entry::getKey)
                .collect(joining(", ", "[", "]")));
    }

    // Jaccard : |A inter B| / |A union B| ; 1 = memes gouts, 0 = rien en commun.
    static double jaccard(Set<String> a, Set<String> b) {
        Set<String> inter = new TreeSet<>(a);
        inter.retainAll(b);
        Set<String> union = new TreeSet<>(a);
        union.addAll(b);
        return union.isEmpty() ? 0 : (double) inter.size() / union.size();
    }

    void recommendations() {
        Map<String, Set<String>> artists = validArtistsByUser();
        Set<String> users = plays.stream().map(Play::user).collect(toCollection(TreeSet::new));
        for (String user : users) {
            Set<String> mine = artists.get(user);
            if (mine == null) {
                System.out.println("RECO " + user + " : aucune ecoute validee");
                continue;
            }
            // Le voisin le plus proche ; a egalite, le premier dans l'ordre alphabetique.
            Optional<Neighbour> best = artists.entrySet().stream()
                    .filter(e -> !e.getKey().equals(user))
                    .map(e -> new Neighbour(e.getKey(), jaccard(mine, e.getValue())))
                    .filter(n -> n.similarity() > 0)
                    .max(Comparator.comparingDouble(Neighbour::similarity)
                            .thenComparing(Neighbour::user, Comparator.reverseOrder()));
            System.out.println(best.map(n -> {
                String news = artists.get(n.user()).stream().filter(a -> !mine.contains(a)).sorted()
                        .collect(collectingAndThen(joining(", "), s -> s.isEmpty() ? "rien de nouveau" : s));
                return String.format(Locale.US, "RECO %s : voisin %s (%.2f) -> %s", user, n.user(), n.similarity(), news);
            }).orElse("RECO " + user + " : aucun voisin"));
        }
    }

    public static void main(String[] args) {
        new MusicStats(Data.PLAYS).report();
    }
}
