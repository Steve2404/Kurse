package ch10_streams.projects.p03_weather.solution;

import ch10_streams.projects.p03_weather.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * SOLUTION du projet 3 - une conception possible.
 */
public class WeatherStation {

    static final int HOURS = 24;

    // Un tableau dans un record : equals/hashCode comparent la REFERENCE du tableau, pas son contenu.
    // Ici on ne compare jamais deux journees, et on ne publie le tableau que via un IntStream (pas de fuite).
    record Day(LocalDate date, int[] values) {
        IntStream temps() {
            return IntStream.of(values);
        }

        int at(int hour) {
            return values[hour];
        }
    }

    record Run(Day day, int start, int end) {
        int length() {
            return end - start;
        }
    }

    private final List<Day> days = new ArrayList<>();
    private final List<String> rejected = new ArrayList<>();

    WeatherStation(List<String> lines) {
        for (String line : lines) {
            String[] p = line.split(";");
            // Stream<String> -> IntStream : mapToInt, puis toArray donne un int[] sans boxing.
            int[] values = Arrays.stream(p[1].split(",")).mapToInt(Integer::parseInt).toArray();
            if (values.length != HOURS) {
                rejected.add("REJET " + p[0] + " : " + values.length + " releves au lieu de " + HOURS);
            } else {
                days.add(new Day(LocalDate.parse(p[0]), values));
            }
        }
    }

    // Toutes les temperatures de toutes les journees : flatMapToInt aplatit sans jamais boxer.
    IntStream all() {
        return days.stream().flatMapToInt(Day::temps);
    }

    // chars() rend un IntStream de codes : on reste en int du debut a la fin.
    int checksum(String code) {
        return code.chars().filter(Character::isLetterOrDigit).sum() % 97;
    }

    // Fenetre glissante : chaque heure de depart possible devient la somme de ses 3 heures.
    // max(Comparator) garde le PREMIER en cas d'egalite -> la fenetre la plus tot.
    String peak(Day d) {
        int start = IntStream.rangeClosed(0, HOURS - 3).boxed()
                .max(Comparator.comparingInt(h -> IntStream.range(h, h + 3).map(d::at).sum()))
                .orElseThrow();
        double avg = IntStream.range(start, start + 3).map(d::at).average().getAsDouble();
        return String.format(Locale.US, "pic %dh-%dh moy %.1f", start, start + 2, avg);
    }

    void daily(Day d) {
        IntSummaryStatistics s = d.temps().summaryStatistics();
        System.out.println(String.format(Locale.US, "%s : min %d max %d moy %.1f | %s",
                d.date(), s.getMin(), s.getMax(), s.getAverage(), peak(d)));
        // iterate a 3 arguments sur un IntStream : 0, 6, 12, 18.
        String checkpoints = IntStream.iterate(0, h -> h < HOURS, h -> h + 6)
                .mapToObj(h -> String.valueOf(d.at(h)))
                .collect(Collectors.joining(" "));
        // boxed() : IntStream -> Stream<Integer>, indispensable pour obtenir une List<Integer>.
        List<Integer> hot = IntStream.range(0, HOURS).filter(h -> d.at(h) >= 30).boxed().toList();
        System.out.println(d.date() + " : releves 0h/6h/12h/18h " + checkpoints + " | heures >= 30 " + hot);
    }

    // Mediane d'un nombre PAIR de valeurs : moyenne des deux du milieu, via skip + limit + average.
    double median() {
        long n = all().count();
        return n % 2 == 0
                ? all().sorted().skip(n / 2 - 1).limit(2).average().orElseThrow()
                : all().sorted().skip(n / 2).findFirst().orElseThrow();
    }

    void histogram() {
        IntSummaryStatistics s = all().summaryStatistics();
        IntStream.rangeClosed(s.getMin() / 5, s.getMax() / 5)
                .mapToObj(b -> {
                    long n = all().filter(t -> t / 5 == b).count();
                    return "HISTO [" + b * 5 + "-" + (b * 5 + 4) + "] " + n + " " + "#".repeat((int) n / 2);
                })
                .forEach(System.out::println);
    }

    // Plus longue suite de journees consecutives au-dessus du seuil : un etat (courant, meilleur) -> une boucle.
    Optional<Run> heatWave() {
        Run best = null;
        int start = -1;
        for (int i = 0; i <= days.size(); i++) {
            boolean hot = i < days.size() && days.get(i).temps().max().orElse(Integer.MIN_VALUE) >= Data.HEAT_WAVE_MAX;
            if (hot && start < 0) {
                start = i;
            } else if (!hot && start >= 0) {
                if (best == null || i - start > best.length()) {
                    best = new Run(days.get(start), start, i);
                }
                start = -1;
            }
        }
        return Optional.ofNullable(best);
    }

    // Plus longue hausse STRICTE heure apres heure, toutes journees confondues ; egalite -> la plus tot.
    Run longestRise() {
        Run best = null;
        for (Day d : days) {
            int start = 0;
            for (int h = 1; h <= HOURS; h++) {
                if (h == HOURS || d.at(h) <= d.at(h - 1)) {
                    if (best == null || h - 1 - start > best.length()) {
                        best = new Run(d, start, h - 1);
                    }
                    start = h;
                }
            }
        }
        return best;
    }

    void report() {
        System.out.println("STATION " + Data.STATION + " : code de controle " + checksum(Data.STATION));
        rejected.forEach(System.out::println);
        days.forEach(this::daily);

        System.out.println(String.format(Locale.US, "MEDIANE : %.1f", median()));
        // asDoubleStream : la conversion en Fahrenheit doit se faire en double, pas en division entiere.
        System.out.println(String.format(Locale.US, "MOYENNE : %.1f C / %.1f F",
                all().average().orElse(0), all().asDoubleStream().map(c -> c * 9 / 5 + 32).average().orElse(0)));
        histogram();

        System.out.println(heatWave()
                .map(r -> "CANICULE : " + r.length() + " jour(s) consecutifs (" + days.get(r.start()).date()
                        + " -> " + days.get(r.end() - 1).date() + ")")
                .orElse("CANICULE : aucune"));
        Run rise = longestRise();
        System.out.println("MONTEE : " + rise.length() + " heure(s) de hausse continue le " + rise.day().date()
                + " (" + rise.start() + "h -> " + rise.end() + "h)");

        List<String> storms = days.stream()
                .flatMap(d -> IntStream.range(1, HOURS)
                        .filter(h -> d.at(h - 1) - d.at(h) > Data.STORM_DROP)
                        .mapToObj(h -> "ORAGE : chute de " + (d.at(h - 1) - d.at(h)) + " degres le " + d.date() + " a " + h + "h"))
                .toList();
        if (storms.isEmpty()) {
            System.out.println("ORAGE : aucun");
        }
        storms.forEach(System.out::println);

        // mapToLong : (t - 24) * 350 Wh deborderait vite un int sur une annee de releves.
        long wh = all().filter(t -> t > Data.AC_THRESHOLD).mapToLong(t -> (t - Data.AC_THRESHOLD) * Data.WH_PER_DEGREE_HOUR).sum();
        System.out.println(String.format(Locale.US, "CLIMATISATION : %d Wh (%.1f kWh)", wh, wh / 1000.0));
        // mapToDouble : une valeur double par journee, puis sum.
        double degreeDays = days.stream()
                .mapToDouble(d -> Math.max(0, d.temps().average().orElse(0) - Data.AC_THRESHOLD))
                .sum();
        System.out.println(String.format(Locale.US, "DEGRES-JOURS : %.2f", degreeDays));
    }

    public static void main(String[] args) {
        new WeatherStation(Data.DAYS).report();
    }
}
