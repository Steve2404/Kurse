package io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise05_FilesLinesStream.
 */
public class Solution05_FilesLinesStream {

    public static int sumOfLineLengths(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.mapToInt(String::length).sum();
        }
    }
}
