package ch9_collections.projects.p05_ranking.solution;

import ch9_collections.projects.p05_ranking.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION du projet 5 - classements : Comparable, Comparator, TreeMap / TreeSet navigables, deux tas.
 */
public class Ranking {

    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        for (String line : Data.PLAYERS) {
            players.add(Player.parse(line));
        }
        List<Player> natural = new ArrayList<>(players);
        Collections.sort(natural);                                         // ordre naturel : compareTo
        System.out.println("ordre naturel : " + natural);

        Comparator<Player> byScoreDesc = Comparator.comparingInt(Player::score).reversed();
        Comparator<Player> board = byScoreDesc.thenComparing(Player::age).thenComparing(Comparator.naturalOrder());
        players.sort(board);
        System.out.println("classement (score desc, age, nom) : " + players);
        Comparator<Player> byTeam = Comparator.comparing(Player::team).thenComparing(Player::score, Comparator.reverseOrder()).thenComparing(Player::name);
        List<Player> teams = new ArrayList<>(players);
        teams.sort(byTeam);
        Comparator<Player> byBonus = Comparator.comparing(Player::bonus, Comparator.nullsLast(Comparator.reverseOrder()));
        List<Player> bonus = new ArrayList<>(players);
        bonus.sort(byBonus.thenComparing(Player::name));
        System.out.println("par equipe : " + teams + " ; par bonus (null a la fin) : " + bonus);

        // Rangs : "competition" (1, 1, 1, 4...) et "dense" (1, 1, 1, 2...).
        StringBuilder ranks = new StringBuilder("rangs :");
        int rank = 0;
        int dense = 0;
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (i == 0 || p.score() != players.get(i - 1).score()) {
                rank = i + 1;
                dense++;
            }
            ranks.append(' ').append(p.name()).append('=').append(rank).append('/').append(dense);
        }
        System.out.println(ranks);

        // PIEGE : un TreeSet considere egaux deux elements dont compare(...) == 0 : le second est REFUSE.
        TreeSet<Player> uniqueScores = new TreeSet<>(Comparator.comparingInt(Player::score));
        uniqueScores.addAll(players);
        System.out.println("TreeSet par score : " + uniqueScores + " (" + uniqueScores.size() + " sur " + players.size() + ")");

        // TreeMap score -> noms : la navigation.
        NavigableMap<Integer, List<String>> byScore = new TreeMap<>();
        for (Player p : players) {
            byScore.computeIfAbsent(p.score(), k -> new ArrayList<>()).add(p.name());
        }
        System.out.println("navigation : floorKey(1300) " + byScore.floorKey(1300) + ", ceilingKey(1300) " + byScore.ceilingKey(1300) + ", lowerKey(1200) "
                + byScore.lowerKey(1200) + ", higherKey(1500) " + byScore.higherKey(1500) + ", firstEntry " + byScore.firstEntry() + ", lastKey " + byScore.lastKey());
        System.out.println("vues : headMap(1200) " + byScore.headMap(1200) + ", tailMap(1200) " + byScore.tailMap(1200).keySet() + ", subMap(900, 1300) "
                + byScore.subMap(900, 1300).keySet() + ", descending " + byScore.descendingMap().keySet());

        NavigableSet<Integer> ages = new TreeSet<>();
        for (Player p : players) {
            ages.add(p.age());
        }
        System.out.println("ages : " + ages + ", first " + ages.first() + ", last " + ages.last() + ", floor(30) " + ages.floor(30) + ", ceiling(30) " + ages.ceiling(30)
                + ", headSet(31) " + ages.headSet(31) + ", tailSet(31, false) " + ages.tailSet(31, false) + ", pollFirst " + ages.pollFirst() + " -> " + ages);

        // Intervalles : fusion (tri par debut) et nombre maximal sans chevauchement (glouton, tri par FIN).
        List<Interval> intervals = new ArrayList<>();
        for (int[] i : Data.INTERVALS) {
            intervals.add(new Interval(i[0], i[1]));
        }
        Collections.sort(intervals);
        List<Interval> merged = new ArrayList<>();
        for (Interval i : intervals) {
            if (!merged.isEmpty() && merged.get(merged.size() - 1).end() >= i.start()) {
                Interval last = merged.remove(merged.size() - 1);
                merged.add(new Interval(last.start(), Math.max(last.end(), i.end())));
            } else {
                merged.add(i);
            }
        }
        List<Interval> byEnd = new ArrayList<>(intervals);
        byEnd.sort(Comparator.comparingInt(Interval::end));
        List<Interval> chosen = new ArrayList<>();
        int lastEnd = Integer.MIN_VALUE;
        for (Interval i : byEnd) {
            if (i.start() > lastEnd) {
                chosen.add(i);
                lastEnd = i.end();
            }
        }
        System.out.println("intervalles tries " + intervals + " -> fusion " + merged + " ; planning max " + chosen);

        // Mediane glissante : un tas MAX pour la moitie basse, un tas MIN pour la moitie haute.
        PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> high = new PriorityQueue<>();
        StringBuilder medians = new StringBuilder("medianes :");
        for (int x : Data.STREAM) {
            if (low.isEmpty() || x <= low.peek()) {
                low.offer(x);
            } else {
                high.offer(x);
            }
            if (low.size() > high.size() + 1) {
                high.offer(low.poll());
            } else if (high.size() > low.size()) {
                low.offer(high.poll());
            }
            double median = low.size() > high.size() ? low.peek() : (low.peek() + high.peek()) / 2.0;
            medians.append(' ').append(median);
        }
        System.out.println(medians);
    }
}
