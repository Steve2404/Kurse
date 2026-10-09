package ch19_final.projects.p06_performance.solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * Le meme rapport que le legacy, au caractere pres, en UN passage sur les lignes :
 *   - les doublons dans un HashSet (contains en O(1), pas O(n) dans une liste) ;
 *   - chaque total dans une Map, mis a jour en passant (au lieu de reparcourir toutes les lignes pour chaque total) ;
 *   - le tri sur des totaux deja calcules (le legacy recalculait les points a chaque comparaison) ;
 *   - un StringBuilder pour le texte.
 * Le legacy est en O(n^3) ; ici O(n) pour la lecture, plus le tri des personnes.
 */
public final class FastReport {

    private record Person(String name, int points, int tasks) {
    }

    private FastReport() {
    }

    private record Journal(List<TaskEvent> events, int invalid, int duplicates) {
    }

    public static String report(List<String> lines) {
        Journal journal = read(lines);
        StringBuilder out = new StringBuilder();
        out.append("lignes : ").append(lines.size()).append(" (invalides : ").append(journal.invalid())
                .append(", doublons : ").append(journal.duplicates()).append(")\n");
        appendPeople(journal.events(), out);
        appendColumns(journal.events(), out);
        appendBusiestDay(journal.events(), out);
        return out.toString();
    }

    // Un seul passage : chaque ligne est lue une fois, et ses doublons sont reperes par le HashSet.
    private static Journal read(List<String> lines) {
        Set<String> seen = new HashSet<>();
        List<TaskEvent> events = new ArrayList<>();
        int invalid = 0;
        int duplicates = 0;
        for (String line : lines) {
            Optional<TaskEvent> event = TaskEvent.parse(line);
            if (event.isEmpty()) {
                invalid++;
            } else if (!seen.add(line)) {
                duplicates++;
            } else {
                events.add(event.get());
            }
        }
        return new Journal(events, invalid, duplicates);
    }

    private static void appendPeople(List<TaskEvent> events, StringBuilder out) {
        Map<String, int[]> totals = new HashMap<>();
        for (TaskEvent e : events) {
            int[] t = totals.computeIfAbsent(e.who(), k -> new int[2]);
            t[0] += e.points();
            t[1]++;
        }
        List<Person> people = new ArrayList<>();
        totals.forEach((name, t) -> people.add(new Person(name, t[0], t[1])));
        people.sort(Comparator.comparingInt(Person::points).reversed().thenComparing(Person::name));
        out.append("personnes :\n");
        for (Person p : people) {
            out.append("  ").append(p.name()).append(" : ").append(p.points()).append(" points, ")
                    .append(p.tasks()).append(" taches\n");
        }
    }

    private static void appendColumns(List<TaskEvent> events, StringBuilder out) {
        Map<String, Integer> counts = new TreeMap<>();
        for (TaskEvent e : events) {
            counts.merge(e.column(), 1, Integer::sum);
        }
        out.append("colonnes :\n");
        counts.forEach((column, n) -> out.append("  ").append(column).append(" : ").append(n).append('\n'));
    }

    // Le jour le plus charge ; a egalite, le plus ancien (TreeMap : on parcourt les jours dans l'ordre).
    private static void appendBusiestDay(List<TaskEvent> events, StringBuilder out) {
        Map<String, Integer> perDay = new TreeMap<>();
        for (TaskEvent e : events) {
            perDay.merge(e.day(), 1, Integer::sum);
        }
        String best = null;
        for (Map.Entry<String, Integer> day : perDay.entrySet()) {
            if (best == null || day.getValue() > perDay.get(best)) {
                best = day.getKey();
            }
        }
        out.append("jour le plus charge : ")
                .append(best == null ? "aucun" : best + " (" + perDay.get(best) + " evenements)").append('\n');
    }
}
