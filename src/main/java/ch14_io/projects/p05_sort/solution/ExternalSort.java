package ch14_io.projects.p05_sort.solution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * SOLUTION - le tri EXTERNE : quand un fichier ne tient pas en memoire, on trie des paquets, on les ecrit,
 * puis on FUSIONNE les k fichiers tries avec un tas (une seule ligne par fichier en memoire).
 */
public final class ExternalSort {

    // Score decroissant, puis joueur croissant.
    public static final Comparator<String> ORDER = Comparator.comparingInt((String l) -> Integer.parseInt(l.split(";")[1])).reversed()
            .thenComparing(l -> l.split(";")[0]);

    private ExternalSort() {
    }

    record Head(String line, BufferedReader reader) {
    }

    // 1re phase : lire par paquets de chunk lignes, trier chaque paquet, l'ecrire dans son propre fichier.
    public static List<Path> splitSorted(Path input, Path dir, int chunk) throws IOException {
        List<Path> parts = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(input)) {
            List<String> buffer = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.add(line);
                if (buffer.size() == chunk) {
                    parts.add(writePart(buffer, dir, parts.size()));
                    buffer.clear();
                }
            }
            if (!buffer.isEmpty()) {
                parts.add(writePart(buffer, dir, parts.size()));
            }
        }
        return parts;
    }

    private static Path writePart(List<String> buffer, Path dir, int index) throws IOException {
        buffer.sort(ORDER);
        Path part = dir.resolve("paquet-" + index + ".txt");
        Files.write(part, buffer);                       // ecrit chaque element suivi d'un saut de ligne
        return part;
    }

    // 2e phase : fusion des k paquets ; le tas contient la PREMIERE ligne non lue de chaque paquet.
    public static void merge(List<Path> parts, Path output) throws IOException {
        PriorityQueue<Head> heap = new PriorityQueue<>(Comparator.comparing(Head::line, ORDER));
        List<BufferedReader> readers = new ArrayList<>();
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (Path part : parts) {
                BufferedReader r = Files.newBufferedReader(part);
                readers.add(r);
                String first = r.readLine();
                if (first != null) {
                    heap.add(new Head(first, r));
                }
            }
            while (!heap.isEmpty()) {
                Head h = heap.poll();
                writer.write(h.line());
                writer.newLine();
                String next = h.reader().readLine();
                if (next != null) {
                    heap.add(new Head(next, h.reader()));
                }
            }
        } finally {
            for (BufferedReader r : readers) {
                r.close();
            }
        }
    }
}
