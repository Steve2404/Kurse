package ch5_methods.drills.solutions;

import ch5_methods.drills.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    private static int tickets;

    public static String nextTicket() {
        // Un compteur static partage entre tous les appels.
        return "T-" + (++tickets);
    }

    public static String bestPlayer() {
        // On garde l'index du meilleur score, puis on lit le nom au meme index.
        int best = 0;
        for (int i = 1; i < Team.SCORES.length; i++) {
            if (Team.SCORES[i] > Team.SCORES[best]) {
                best = i;
            }
        }
        return Team.NAMES[best];
    }

    public static int totalWithBonus() {
        // Les bonus null sont ignores avant le deballage.
        int total = 0;
        for (int s : Team.SCORES) {
            total += s;
        }
        for (Integer b : Team.BONUS) {
            if (b != null) {
                total += b;
            }
        }
        return total;
    }

    public static int[] scoresPlusBonus() {
        // Nouveau tableau : les donnees partagees restent intactes.
        int[] result = Arrays.copyOf(Team.SCORES, Team.SCORES.length);
        for (int i = 0; i < result.length; i++) {
            Integer b = Team.BONUS[i];
            result[i] += b == null ? 0 : b;
        }
        return result;
    }

    public static void addToAll(int[] values, int extra) {
        // Modification en place : l'appelant voit le changement.
        for (int i = 0; i < values.length; i++) {
            values[i] += extra;
        }
    }

    public static String format(String separator, int... values) {
        // Varargs apres le separateur ; le separateur seulement entre deux valeurs.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(values[i]);
        }
        return sb.toString();
    }

    public static List<String> namesAbove(int threshold) {
        // threshold jamais reassigne : la lambda peut le capturer.
        IntPredicate above = score -> score > threshold;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Team.SCORES.length; i++) {
            if (above.test(Team.SCORES[i])) {
                result.add(Team.NAMES[i]);
            }
        }
        return result;
    }

    public static boolean sameScore(Integer a, Integer b) {
        // equals par valeur, avec le cas null traite a part.
        return a == null ? b == null : a.equals(b);
    }

    public static Integer safeParse(String text) {
        // NumberFormatException -> null plutot qu'un crash.
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static List<String> frozenNames() {
        // List.of refuse toute modification.
        return List.of(Team.NAMES);
    }

    public static int rank(String name) {
        // Rang = 1 + le nombre de joueurs strictement meilleurs.
        int index = Arrays.asList(Team.NAMES).indexOf(name);
        int rank = 1;
        for (int s : Team.SCORES) {
            if (s > Team.SCORES[index]) {
                rank++;
            }
        }
        return rank;
    }

    public static String describe(int... values) {
        // Un varargs vide a une longueur 0 : on le traite en premier.
        if (values.length == 0) {
            return "vide";
        }
        int max = values[0];
        for (int v : values) {
            max = Math.max(max, v);
        }
        return values.length + " valeurs, max " + max;
    }
}
