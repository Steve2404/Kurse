package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise11_LogStatistics.
 */
public class Solution11_LogStatistics {

    public static Map<String, Long> countByLevel(Path log) throws IOException {
        // Files.lines est paresseux et tient le fichier ouvert : try-with-resources obligatoire.
        try (Stream<String> lines = Files.lines(log)) {
            return lines.filter(Solution11_LogStatistics::useful)
                    .collect(Collectors.groupingBy(l -> parts(l)[1], TreeMap::new, Collectors.counting()));
        }
    }

    public static List<String> modulesWithErrors(Path log) throws IOException {
        // distinct puis sorted : sans doublon, dans l'ordre alphabetique.
        try (Stream<String> lines = Files.lines(log)) {
            return lines.filter(Solution11_LogStatistics::useful)
                    .map(Solution11_LogStatistics::parts)
                    .filter(p -> p[1].equals("ERROR"))
                    .map(p -> p[2])
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    public static Optional<String> firstErrorAfter(Path log, LocalDateTime moment) throws IOException {
        // findFirst s'arrete des la 1re ligne qui convient : le reste du fichier n'est jamais lu.
        try (Stream<String> lines = Files.lines(log)) {
            return lines.filter(Solution11_LogStatistics::useful)
                    .map(Solution11_LogStatistics::parts)
                    .filter(p -> p[1].equals("ERROR") && LocalDateTime.parse(p[0]).isAfter(moment))
                    .findFirst()
                    .map(p -> p[3]);
        }
    }

    public static Map<Integer, Long> linesPerHour(Path log) throws IOException {
        // La cle de groupement est calculee : l'heure de la date en tete de ligne.
        try (Stream<String> lines = Files.lines(log)) {
            return lines.filter(Solution11_LogStatistics::useful)
                    .collect(Collectors.groupingBy(l -> LocalDateTime.parse(parts(l)[0]).getHour(), TreeMap::new, Collectors.counting()));
        }
    }

    private static boolean useful(String line) {
        // Boite magique : ni ligne vide, ni commentaire.
        return !line.isBlank() && !line.startsWith("#");
    }

    private static String[] parts(String line) {
        // split avec limite 4 : le message (4e morceau) garde ses espaces.
        return line.split(" ", 4);
    }
}
