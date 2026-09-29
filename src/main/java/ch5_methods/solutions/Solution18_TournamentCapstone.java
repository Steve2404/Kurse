package ch5_methods.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise18_TournamentCapstone.
 */
public class Solution18_TournamentCapstone {

    public static final class Tournament {
        public static final int MAX_PLAYERS = 4;
        private static int created;

        private final String name;
        private final List<String> players = new ArrayList<>();
        private final int[] scores = new int[MAX_PLAYERS];

        private Tournament(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<String> getPlayers() {
            return List.copyOf(players);
        }

        public static Tournament create(String name) {
            // Fabrique static : on valide AVANT de compter et de construire.
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("nom obligatoire");
            }
            created++;
            return new Tournament(name);
        }

        public static int createdCount() {
            // Le compteur est partage par tous les tournois.
            return created;
        }

        public boolean register(String... names) {
            // Tout verifier d'abord, puis tout ajouter : jamais d'inscription a moitie faite.
            if (players.size() + names.length > MAX_PLAYERS) {
                return false;
            }
            for (int i = 0; i < names.length; i++) {
                if (players.contains(names[i]) || Arrays.asList(names).subList(i + 1, names.length).contains(names[i])) {
                    return false;
                }
            }
            players.addAll(Arrays.asList(names));
            return true;
        }

        public boolean score(String player, int points) {
            // La version de base : les autres surcharges s'appuient sur elle.
            int i = players.indexOf(player);
            if (i < 0) {
                return false;
            }
            scores[i] += points;
            return true;
        }

        public boolean score(String player, int points, int bonus) {
            // Un int + int reste un int : l'appel choisit score(String, int).
            return score(player, points + bonus);
        }

        public boolean score(String player, Integer points) {
            // intValue() force la version int ; score(player, points) se rappellerait elle-meme.
            if (points == null) {
                return false;
            }
            return score(player, points.intValue());
        }

        public int[] snapshot() {
            // Copie defensive : l'appelant recoit un autre tableau.
            return Arrays.copyOf(scores, players.size());
        }

        public String leader() {
            // > strict : le premier garde la tete en cas d'egalite.
            if (players.isEmpty()) {
                return "personne";
            }
            int best = 0;
            for (int i = 1; i < players.size(); i++) {
                if (scores[i] > scores[best]) {
                    best = i;
                }
            }
            return players.get(best);
        }

        public List<String> playersAbove(int threshold) {
            // threshold n'est jamais reassigne : la lambda peut le capturer.
            IntPredicate high = score -> score > threshold;
            List<String> result = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (high.test(scores[i])) {
                    result.add(players.get(i));
                }
            }
            return result;
        }
    }
}
