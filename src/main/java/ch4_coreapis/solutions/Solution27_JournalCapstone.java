package ch4_coreapis.solutions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Corrige de l'exercice 27. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise27_JournalCapstone.
 */
public class Solution27_JournalCapstone {

    public record Entry(LocalDateTime when, String level, String message) {
    }

    public static final String[] LINES = {
            "2024-03-11 08:00 INFO sauvegarde terminee",
            "2024-03-10 07:05 INFO demarrage du serveur",
            "2024-03-11 23:59 ERROR memoire insuffisante",
            "2024-03-10 07:42 WARN disque presque plein",
            "2024-03-10 09:15 ERROR connexion perdue"};

    public static Entry parse(String line) {
        // LocalDateTime.parse attend le format ISO "date T heure" : on recolle avec un "T".
        int end = line.indexOf(' ', 17);
        LocalDateTime when = LocalDateTime.parse(line.substring(0, 10) + "T" + line.substring(11, 16));
        return new Entry(when, line.substring(17, end), line.substring(end + 1));
    }

    public static int[] countByLevel(Entry[] entries) {
        // Un tableau de compteurs indexe par un switch expression.
        int[] counts = new int[3];
        for (Entry e : entries) {
            int index = switch (e.level()) {
                case "INFO" -> 0;
                case "WARN" -> 1;
                case "ERROR" -> 2;
                default -> throw new IllegalArgumentException(e.level());
            };
            counts[index]++;
        }
        return counts;
    }

    public static Entry firstError(Entry[] entries) {
        // Le plus tot DANS LE TEMPS (isBefore), pas dans l'ordre de la liste.
        Entry best = null;
        for (Entry e : entries) {
            if (e.level().equals("ERROR") && (best == null || e.when().isBefore(best.when()))) {
                best = e;
            }
        }
        return best;
    }

    public static Duration span(Entry[] entries) {
        // Min et max en un seul parcours, puis Duration.between (LocalDateTime a une horloge).
        LocalDateTime earliest = entries[0].when();
        LocalDateTime latest = entries[0].when();
        for (Entry e : entries) {
            if (e.when().isBefore(earliest)) {
                earliest = e.when();
            }
            if (e.when().isAfter(latest)) {
                latest = e.when();
            }
        }
        return Duration.between(earliest, latest);
    }

    public static String[] sortedMessages(Entry[] entries) {
        // Arrays.sort trie les String dans l'ordre naturel (compareTo).
        String[] messages = new String[entries.length];
        for (int i = 0; i < entries.length; i++) {
            messages[i] = entries[i].message();
        }
        Arrays.sort(messages);
        return messages;
    }

    public static String report(String[] lines) {
        // Les petites boites assemblees ; StringBuilder evite de creer un String par morceau.
        Entry[] entries = new Entry[lines.length];
        for (int i = 0; i < lines.length; i++) {
            entries[i] = parse(lines[i]);
        }
        int[] counts = countByLevel(entries);
        Duration d = span(entries);
        Entry error = firstError(entries);
        StringBuilder sb = new StringBuilder();
        sb.append("INFO=").append(counts[0]).append(" WARN=").append(counts[1]).append(" ERROR=").append(counts[2]);
        sb.append("\nduree=").append(String.format("%dh%02d", d.toHours(), d.toMinutesPart()));
        sb.append("\npremiere erreur=").append(error == null ? "aucune" : error.when() + " " + error.message());
        sb.append("\npourcentage erreurs=").append(Math.round(100.0 * counts[2] / entries.length));
        return sb.toString();
    }
}
