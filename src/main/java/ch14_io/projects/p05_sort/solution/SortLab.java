package ch14_io.projects.p05_sort.solution;

import ch14_io.projects.p05_sort.Data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 5 - les fichiers texte avec NIO.2 : ecrire, lire en flux, trier un fichier trop gros.
 */
public class SortLab {

    public static void main(String[] args) throws IOException {
        Path sandbox = Path.of(Data.SANDBOX);
        if (Files.exists(sandbox)) {
            try (Stream<Path> all = Files.walk(sandbox)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        Files.createDirectories(sandbox.resolve("paquets"));
        Path input = sandbox.resolve("scores.txt");
        try (BufferedWriter w = Files.newBufferedWriter(input)) {
            for (int i = 0; i < Data.LINES; i++) {
                w.write(Data.line(i));
                w.newLine();
            }
        }

        // Files.lines : un Stream PARESSEUX, ligne par ligne, a fermer (try-with-resources).
        long count;
        long highScores;
        Map<Integer, Long> byThousand;
        try (Stream<String> lines = Files.lines(input)) {
            count = lines.count();
        }
        try (Stream<String> lines = Files.lines(input)) {
            highScores = lines.filter(l -> Integer.parseInt(l.split(";")[1]) >= 9_900).count();
        }
        try (Stream<String> lines = Files.lines(input)) {
            byThousand = lines.collect(Collectors.groupingBy(l -> Integer.parseInt(l.split(";")[1]) / 1000, TreeMap::new, Collectors.counting()));
        }
        System.out.println("fichier : " + count + " lignes, " + highScores + " scores >= 9900 ; par millier " + byThousand);

        List<Path> parts = ExternalSort.splitSorted(input, sandbox.resolve("paquets"), Data.CHUNK);
        Path sorted = sandbox.resolve("trie.txt");
        ExternalSort.merge(parts, sorted);
        // Verification : readAllLines charge TOUT en memoire (possible ici, pas pour un vrai gros fichier).
        List<String> expected = new ArrayList<>(Files.readAllLines(input));
        expected.sort(ExternalSort.ORDER);
        List<String> result = Files.readAllLines(sorted);
        System.out.println("tri externe : " + parts.size() + " paquets de " + Data.CHUNK + " lignes max, fusion de " + result.size() + " lignes, identique au tri en memoire "
                + result.equals(expected));
        System.out.println("podium : " + result.subList(0, 3) + " ; dernier " + result.get(result.size() - 1));

        // Options d'ouverture : APPEND ajoute ; CREATE_NEW echoue si le fichier existe.
        Path summary = sandbox.resolve("resume.txt");
        Files.writeString(summary, "lignes=" + count + System.lineSeparator());
        Files.writeString(summary, "paquets=" + parts.size() + System.lineSeparator(), StandardOpenOption.APPEND);
        Files.write(summary, List.of("meilleur=" + result.get(0)), StandardOpenOption.APPEND);
        String created;
        try {
            Files.writeString(summary, "ecrase ?", StandardOpenOption.CREATE_NEW);
            created = "ok";
        } catch (FileAlreadyExistsException e) {
            created = e.getClass().getSimpleName();
        }
        System.out.println("resume : " + Files.readAllLines(summary) + " ; CREATE_NEW sur un fichier existant " + created + " ; readString "
                + Files.readString(summary).lines().count() + " lignes");
    }
}
