package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise04_NioStreamListWalkFind.
 */
public class Solution04_NioStreamListWalkFind {

    public static List<Path> listImmediateChildren(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream.collect(Collectors.toList());
        }
    }

    public static long countAllEntries(Path root) throws IOException {
        try (Stream<Path> stream = Files.walk(root)) {
            return stream.count();
        }
    }

    public static List<Path> findTextFiles(Path root) throws IOException {
        try (Stream<Path> stream = Files.find(root, Integer.MAX_VALUE,
                (path, attrs) -> attrs.isRegularFile() && path.toString().endsWith(".txt"))) {
            return stream.collect(Collectors.toList());
        }
    }
}
