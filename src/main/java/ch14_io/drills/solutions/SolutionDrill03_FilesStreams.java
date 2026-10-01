package ch14_io.drills.solutions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.drills.exercises.Drill03_FilesStreams.
 */
public class SolutionDrill03_FilesStreams {

    public static List<String> docsNames(Path root) throws IOException {
        // list : un seul niveau ; l'ordre n'est pas garanti, d'ou le tri.
        try (Stream<Path> s = Files.list(root.resolve("docs"))) {
            return s.map(p -> p.getFileName().toString()).sorted().collect(Collectors.toList());
        }
    }

    public static long entryCount(Path root) throws IOException {
        // walk inclut le dossier de depart lui-meme.
        try (Stream<Path> s = Files.walk(root)) {
            return s.count();
        }
    }

    public static long topLevelCount(Path root) throws IOException {
        // Profondeur 1 : root et ses enfants directs.
        try (Stream<Path> s = Files.walk(root, 1)) {
            return s.count();
        }
    }

    public static long logCount(Path root) throws IOException {
        // find recoit les attributs : pas besoin de rappeler Files.isRegularFile.
        try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE,
                (p, attrs) -> attrs.isRegularFile() && p.toString().endsWith(".log"))) {
            return s.count();
        }
    }

    public static long appLogLines(Path root) throws IOException {
        // Lecture paresseuse : rien n'est garde en memoire.
        try (Stream<String> lines = Files.lines(root.resolve("logs/app.log"))) {
            return lines.count();
        }
    }

    public static List<String> errors(Path root) throws IOException {
        // Un filter classique sur le Stream de lignes.
        try (Stream<String> lines = Files.lines(root.resolve("logs/app.log"))) {
            return lines.filter(l -> l.startsWith("ERROR")).collect(Collectors.toList());
        }
    }

    public static String firstReadmeLine(Path root) throws IOException {
        // newBufferedReader : un BufferedReader UTF-8 sans enchainer FileInputStream / InputStreamReader.
        try (BufferedReader reader = Files.newBufferedReader(root.resolve("docs/readme.txt"))) {
            return reader.readLine();
        }
    }

    public static List<String> writeTwoLines(Path root) throws IOException {
        // Sans option : CREATE + TRUNCATE_EXISTING + WRITE ; newLine ajoute la fin de ligne du systeme.
        Path out = root.resolve("out.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(out)) {
            writer.write("un");
            writer.newLine();
            writer.write("deux");
            writer.newLine();
        }
        return Files.readAllLines(out);
    }

    public static List<String> allFiles(Path root) throws IOException {
        // relativize enleve le prefixe temporaire ; le tri rend l'ordre stable.
        try (Stream<Path> s = Files.walk(root)) {
            return s.filter(Files::isRegularFile)
                    .map(p -> root.relativize(p).toString().replace('\\', '/'))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    public static String biggestFile(Path root) throws IOException {
        // Files.size lance IOException : dans une lambda, on l'emballe dans UncheckedIOException.
        try (Stream<Path> s = Files.walk(root)) {
            Path biggest = s.filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }))
                    .orElseThrow();
            return root.relativize(biggest).toString().replace('\\', '/');
        }
    }
}
