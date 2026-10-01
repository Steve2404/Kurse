package ch14_io.drills.solutions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    public static long totalLogLines(Path root) throws IOException {
        // Pour chaque .log, on compte ses lignes (dans une boucle : Files.lines lance IOException).
        long total = 0;
        for (Path log : files(root)) {
            if (log.toString().endsWith(".log")) {
                try (Stream<String> lines = Files.lines(log)) {
                    total += lines.count();
                }
            }
        }
        return total;
    }

    public static Map<String, Long> countByExtension(Path root) throws IOException {
        // L'extension = ce qui suit le dernier point du nom ; TreeMap pour les cles triees.
        return files(root).stream()
                .map(p -> p.getFileName().toString())
                .collect(Collectors.groupingBy(n -> n.substring(n.lastIndexOf('.') + 1), TreeMap::new, Collectors.counting()));
    }

    public static List<String> archiveLogs(Path root) throws IOException {
        // On liste d'abord (walk ferme), puis on deplace : modifier l'arbre pendant le walk serait risque.
        Path archive = Files.createDirectories(root.resolve("archive"));
        List<String> names = new ArrayList<>();
        for (Path p : files(root)) {
            if (p.toString().endsWith(".log")) {
                Files.move(p, archive.resolve(p.getFileName()));
                names.add(p.getFileName().toString());
            }
        }
        Collections.sort(names);
        return names;
    }

    public static long backupDocs(Path root) throws IOException {
        // Le patron du capstone : walk (parents d'abord) + relativize + resolve + createDirectories / copy.
        Path docs = root.resolve("docs");
        Path backup = root.resolve("backup");
        List<Path> entries;
        try (Stream<Path> s = Files.walk(docs)) {
            entries = s.collect(Collectors.toList());
        }
        long copied = 0;
        for (Path p : entries) {
            Path target = backup.resolve(docs.relativize(p));
            if (Files.isDirectory(p)) {
                Files.createDirectories(target);
            } else {
                Files.copy(p, target);
                copied++;
            }
        }
        return copied;
    }

    public static List<String> grep(Path root, String word) throws IOException {
        // anyMatch s'arrete a la 1re ligne trouvee ; le fichier est ferme par le try.
        List<String> found = new ArrayList<>();
        for (Path p : files(root)) {
            try (Stream<String> lines = Files.lines(p)) {
                if (lines.anyMatch(l -> l.contains(word))) {
                    found.add(root.relativize(p).toString().replace('\\', '/'));
                }
            }
        }
        Collections.sort(found);
        return found;
    }

    public static List<String> writeReport(Path root) throws IOException {
        // On compte AVANT d'ouvrir le fichier : sinon report.txt, deja cree, serait compte comme un .txt de plus.
        // PrintWriter sur un BufferedWriter de Files : printf et %n (fin de ligne du systeme).
        Map<String, Long> counts = countByExtension(root);
        Path report = root.resolve("report.txt");
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(report))) {
            counts.forEach((ext, n) -> out.printf("%s=%d%n", ext, n));
        }
        return Files.readAllLines(report);
    }

    public static Map<String, Long> saveAndLoadIndex(Path root) throws IOException, ClassNotFoundException {
        // TreeMap est Serializable ; Files.newOutputStream donne le flux d'octets a envelopper.
        TreeMap<String, Long> index = new TreeMap<>();
        for (Path p : files(root)) {
            index.put(root.relativize(p).toString().replace('\\', '/'), Files.size(p));
        }
        Path file = root.resolve("index.ser");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(index);
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            @SuppressWarnings("unchecked")
            Map<String, Long> loaded = (Map<String, Long>) in.readObject();
            return loaded;
        }
    }

    public static long wordCount(Path root) throws IOException {
        // flatMap transforme chaque ligne en ses mots ; les lignes vides ne comptent pas.
        long words = 0;
        for (Path p : files(root)) {
            if (p.toString().endsWith(".txt")) {
                try (Stream<String> lines = Files.lines(p)) {
                    words += lines.flatMap(l -> Stream.of(l.split(" "))).filter(w -> !w.isBlank()).count();
                }
            }
        }
        return words;
    }

    private static List<Path> files(Path root) throws IOException {
        // Boite magique : tous les fichiers ordinaires, dans une liste (le walk est ferme tout de suite).
        try (Stream<Path> s = Files.walk(root)) {
            return s.filter(Files::isRegularFile).sorted().collect(Collectors.toList());
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }
}
