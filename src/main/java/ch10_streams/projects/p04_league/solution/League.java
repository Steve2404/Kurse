package ch10_streams.projects.p04_league.solution;

import ch10_streams.projects.p04_league.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 4 - une conception possible (sans la classe Collectors, interdite ici).
 */
public class League {

    record Match(String round, String home, String away, int homeGoals, int awayGoals) {
        static Match parse(String line) {
            String[] p = line.split(";");
            String[] teams = p[1].split("-");
            String[] score = p[2].split("-");
            return new Match(p[0], teams[0], teams[1], Integer.parseInt(score[0]), Integer.parseInt(score[1]));
        }

        int goals() {
            return homeGoals + awayGoals;
        }

        int margin() {
            return Math.abs(homeGoals - awayGoals);
        }

        boolean involves(String team) {
            return home.equals(team) || away.equals(team);
        }

        // Le match vu par une equipe : on se ramene a "mes buts / leurs buts".
        Stats statsFor(String team) {
            int mine = team.equals(home) ? homeGoals : awayGoals;
            int theirs = team.equals(home) ? awayGoals : homeGoals;
            return new Stats(1, mine > theirs ? 1 : 0, mine == theirs ? 1 : 0, mine < theirs ? 1 : 0, mine, theirs);
        }

        @Override
        public String toString() {
            return home + " " + homeGoals + "-" + awayGoals + " " + away;
        }
    }

    // Monoide : ZERO est neutre et plus est associatif -> utilisable comme identite de reduce, meme en parallele.
    record Stats(int played, int won, int drawn, int lost, int goalsFor, int goalsAgainst) {
        static final Stats ZERO = new Stats(0, 0, 0, 0, 0, 0);

        Stats plus(Stats o) {
            return new Stats(played + o.played, won + o.won, drawn + o.drawn, lost + o.lost,
                    goalsFor + o.goalsFor, goalsAgainst + o.goalsAgainst);
        }

        int points() {
            return won * Data.POINTS_WIN + drawn * Data.POINTS_DRAW;
        }

        int diff() {
            return goalsFor - goalsAgainst;
        }
    }

    record Row(String team, Stats stats) {
    }

    /**
     * Resume d'un MORCEAU de la saison pour la serie sans defaite :
     * longueur, serie au debut (prefix), serie a la fin (suffix), meilleure serie interne.
     * Deux morceaux voisins se fusionnent sans revoir les matchs : c'est ce qui rend le calcul parallelisable.
     */
    record Segment(int length, int prefix, int suffix, int best) {
        static final Segment EMPTY = new Segment(0, 0, 0, 0);

        static Segment of(boolean unbeaten) {
            int v = unbeaten ? 1 : 0;
            return new Segment(1, v, v, v);
        }

        // La serie qui traverse la frontiere = fin du morceau gauche + debut du morceau droit.
        Segment plus(Segment right) {
            int newPrefix = prefix == length ? length + right.prefix : prefix;
            int newSuffix = right.suffix == right.length ? right.length + suffix : right.suffix;
            int newBest = Math.max(Math.max(best, right.best), suffix + right.prefix);
            return new Segment(length + right.length, newPrefix, newSuffix, newBest);
        }
    }

    static final Comparator<Row> TABLE_ORDER = Comparator.comparingInt((Row r) -> r.stats().points()).reversed()
            .thenComparing(Comparator.comparingInt((Row r) -> r.stats().diff()).reversed())
            .thenComparing(Comparator.comparingInt((Row r) -> r.stats().goalsFor()).reversed())
            .thenComparing(Row::team);

    // Collector.of : conteneur MUTABLE (HashMap) ; le combiner fusionne deux tables partielles d'un calcul parallele.
    static final Collector<Match, Map<String, Stats>, List<Row>> TABLE = Collector.of(
            HashMap::new,
            (table, m) -> {
                table.merge(m.home(), m.statsFor(m.home()), Stats::plus);
                table.merge(m.away(), m.statsFor(m.away()), Stats::plus);
            },
            (left, right) -> {
                right.forEach((team, s) -> left.merge(team, s, Stats::plus));
                return left;
            },
            table -> table.entrySet().stream().map(e -> new Row(e.getKey(), e.getValue())).sorted(TABLE_ORDER).toList());

    private final List<Match> matches;

    League(List<String> lines) {
        matches = lines.stream().map(Match::parse).toList();
    }

    // collect a 3 arguments : le combiner doit AUSSI poser le separateur, sinon le resultat parallele colle les morceaux.
    String results(Stream<Match> stream) {
        return stream.collect(StringBuilder::new,
                (sb, m) -> {
                    if (sb.length() > 0) {
                        sb.append(" | ");
                    }
                    sb.append(m);
                },
                (left, right) -> {
                    if (left.length() > 0 && right.length() > 0) {
                        left.append(" | ");
                    }
                    left.append(right);
                }).toString();
    }

    // reduce a 3 arguments : le resultat (int) n'est pas du type des elements (Match) -> il faut un combiner.
    int goals(Stream<Match> stream) {
        return stream.reduce(0, (sum, m) -> sum + m.goals(), Integer::sum);
    }

    // reduce sans identite -> Optional (saison vide) ; ">" strict garde le PREMIER a egalite, et reste associatif.
    Optional<Match> biggestWin() {
        return matches.stream().filter(m -> m.margin() > 0).reduce((a, b) -> b.margin() > a.margin() ? b : a);
    }

    // reduce a 2 arguments : identite + operateur associatif, le type ne change pas (Stats -> Stats).
    Stats seasonOf(String team) {
        return matches.stream().filter(m -> m.involves(team)).map(m -> m.statsFor(team)).reduce(Stats.ZERO, Stats::plus);
    }

    int unbeatenStreak(Stream<Match> stream, String team) {
        return stream.filter(m -> m.involves(team))
                .reduce(Segment.EMPTY, (seg, m) -> seg.plus(Segment.of(m.statsFor(team).lost() == 0)), Segment::plus)
                .best();
    }

    record Streak(String team, int best) {
    }

    String streaks(boolean parallel, List<Row> table) {
        return table.stream()
                .map(r -> new Streak(r.team(), unbeatenStreak(parallel ? matches.parallelStream() : matches.stream(), r.team())))
                .sorted(Comparator.comparingInt(Streak::best).reversed().thenComparing(Streak::team))
                .map(st -> st.team() + " " + st.best())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    void report() {
        String results = results(matches.stream());
        System.out.println("RESULTATS : " + results);

        int goals = goals(matches.stream());
        System.out.println(String.format(Locale.US, "BUTS : %d en %d matchs, moyenne %.2f", goals, matches.size(), (double) goals / matches.size()));

        System.out.println("PLUS LARGE VICTOIRE : " + biggestWin()
                .map(m -> m.round() + " " + m + " (ecart " + m.margin() + ")")
                .orElse("aucune"));

        List<Row> table = matches.stream().collect(TABLE);
        System.out.println("CLASSEMENT");
        for (int i = 0; i < table.size(); i++) {
            Stats s = table.get(i).stats();
            System.out.println(String.format("%d. %s %d pts (G%d N%d P%d) %d:%d %+d", i + 1, table.get(i).team(),
                    s.points(), s.won(), s.drawn(), s.lost(), s.goalsFor(), s.goalsAgainst(), s.diff()));
        }

        Stats lions = seasonOf("Lions");
        System.out.println("BILAN Lions (reduce) : " + lions.played() + " matchs, " + lions.points()
                + " pts, identique au classement : " + yesNo(table.stream().anyMatch(r -> r.stats().equals(lions))));

        String streaks = streaks(false, table);
        System.out.println("SERIES SANS DEFAITE : " + streaks);

        // Controle croise : les points distribues ne dependent que du nombre de victoires et de nuls.
        int points = table.stream().reduce(0, (sum, r) -> sum + r.stats().points(), Integer::sum);
        int wins = matches.stream().reduce(0, (n, m) -> n + (m.homeGoals() != m.awayGoals() ? 1 : 0), Integer::sum);
        int draws = matches.size() - wins;
        System.out.println("CONTROLE : " + points + " points distribues = " + wins + " victoires x " + Data.POINTS_WIN
                + " + " + draws + " nuls x " + (2 * Data.POINTS_DRAW) + " : " + yesNo(points == wins * Data.POINTS_WIN + draws * 2 * Data.POINTS_DRAW));

        // Le meme calcul en parallele doit donner exactement le meme resultat si identite, accumulateur et combiner sont corrects.
        System.out.println("PARALLELE : resultats " + yesNo(results.equals(results(matches.parallelStream())))
                + ", buts " + yesNo(goals == goals(matches.parallelStream()))
                + ", classement " + yesNo(table.equals(matches.parallelStream().collect(TABLE)))
                + ", series " + yesNo(streaks.equals(streaks(true, table))));
    }

    static String yesNo(boolean b) {
        return b ? "oui" : "non";
    }

    public static void main(String[] args) {
        new League(Data.MATCHES).report();
    }
}
