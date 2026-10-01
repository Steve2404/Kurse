package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise10_FilesLinesStream.
 */
public class Solution10_FilesLinesStream {

    public static int sumOfLineLengths(Path file) throws IOException {
        // Files.lines lit paresseusement (ligne par ligne), contrairement a readAllLines qui charge tout ; a fermer.
        try (Stream<String> lines = Files.lines(file)) {
            return lines.mapToInt(String::length).sum();
        }
    }
}
