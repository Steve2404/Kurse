package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * EXERCICE 11 - Statistiques d'un fichier de log avec Files.lines et les Collectors (niveau : avance)
 * ===================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un fichier de log peut faire des millions de lignes : on ne le charge
 * pas en memoire (readAllLines), on le lit comme un Stream paresseux
 * (Files.lines), TOUJOURS dans un try-with-resources (le Stream tient
 * le fichier ouvert). Chaque ligne a la forme :
 *
 *   2024-03-07T14:05:09 ERROR paiement Carte refusee
 *   date-heure          niveau module  message (le reste de la ligne)
 *
 * Les lignes vides ou commencant par "#" sont des commentaires a ignorer.
 *
 *
 * ==================================================================
 * TODO 1 : countByLevel(log)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> {ERROR=3, INFO=4, WARN=2} (TreeMap : cles triees)
 *
 * -- Le plan --
 *
 *   1. Files.lines(log), filtrer les lignes utiles, prendre le 2e mot.
 *   2. collect(groupingBy(niveau, TreeMap::new, counting())).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2) : "est-ce une ligne utile ?" et "couper une ligne en 4 morceaux" servent a tous les TODO.
 *
 *
 * ==================================================================
 * TODO 2 : modulesWithErrors(log)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> [paiement, stock] (les modules ayant au moins une ERROR, tries, sans doublon)
 *
 * -- Le plan --
 *
 *   1. Garder les ERROR, prendre le module, distinct(), sorted(), toList().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : les memes boites.
 *
 *
 * ==================================================================
 * TODO 3 : firstErrorAfter(log, moment)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   apres 2024-03-07T14:00 -> Optional[Stock epuise] ; apres 2024-03-08T00:00 -> Optional.empty
 *
 * -- Le plan --
 *
 *   1. Garder les ERROR dont la date est APRES moment (LocalDateTime.parse du 1er mot).
 *   2. findFirst() puis map vers le message.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : les memes boites.
 *
 *
 * ==================================================================
 * TODO 4 : linesPerHour(log)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> {9=3, 14=4, 23=2}
 *
 * -- Le plan --
 *
 *   1. groupingBy(heure de la date, TreeMap::new, counting()).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : les memes boites.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - try (Stream<String> lines = Files.lines(log)) { return lines.filter(...)...; }
 *   - line.split(" ", 4) -> [date, niveau, module, message] (le message garde ses espaces)
 *   - Collectors.groupingBy(f, TreeMap::new, Collectors.counting())
 *   - LocalDateTime.parse("2024-03-07T14:05:09").getHour()
 */
public class Exercise11_LogStatistics {

    public static Map<String, Long> countByLevel(Path log) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer countByLevel()");
    }

    public static List<String> modulesWithErrors(Path log) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer modulesWithErrors()");
    }

    public static Optional<String> firstErrorAfter(Path log, LocalDateTime moment) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer firstErrorAfter()");
    }

    public static Map<Integer, Long> linesPerHour(Path log) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer linesPerHour()");
    }

    public static void main(String[] args) throws IOException {
        Path log = Files.createTempFile("ex11", ".log");
        try {
            Files.write(log, List.of(
                    "# journal du 7 mars",
                    "2024-03-07T09:00:01 INFO demarrage Application lancee",
                    "2024-03-07T09:15:42 ERROR paiement Carte refusee",
                    "2024-03-07T09:30:00 INFO catalogue 12 livres charges",
                    "",
                    "2024-03-07T14:05:09 WARN stock Stock bas : Dune",
                    "2024-03-07T14:06:10 ERROR stock Stock epuise",
                    "2024-03-07T14:30:00 INFO paiement Paiement accepte",
                    "2024-03-07T14:45:00 WARN catalogue Image manquante",
                    "2024-03-07T23:59:00 ERROR paiement Delai depasse",
                    "2024-03-07T23:59:59 INFO arret Application arretee"));

            ExerciseChecker.check("countByLevel == {ERROR=3, INFO=4, WARN=2}", "{ERROR=3, INFO=4, WARN=2}".equals(String.valueOf(countByLevel(log))));
            ExerciseChecker.check("modulesWithErrors == [paiement, stock]", List.of("paiement", "stock").equals(modulesWithErrors(log)));
            ExerciseChecker.check("firstErrorAfter(14:00) == Stock epuise ; apres le 8 mars -> vide",
                    Optional.of("Stock epuise").equals(firstErrorAfter(log, LocalDateTime.of(2024, 3, 7, 14, 0)))
                            && firstErrorAfter(log, LocalDateTime.of(2024, 3, 8, 0, 0)).isEmpty());
            ExerciseChecker.check("linesPerHour == {9=3, 14=4, 23=2}", "{9=3, 14=4, 23=2}".equals(String.valueOf(linesPerHour(log))));
        } finally {
            Files.deleteIfExists(log);
        }

        ExerciseChecker.summary();
    }
}
