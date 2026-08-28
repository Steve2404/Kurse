package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise03_FilesHelperBasics.
 */
public class Solution03_FilesHelperBasics {

    public static void createEmptyFile(Path path) throws IOException {
        Files.createFile(path);
    }

    public static void copyFile(Path source, Path target) throws IOException {
        Files.copy(source, target);
    }

    public static void moveFile(Path source, Path target) throws IOException {
        Files.move(source, target);
    }

    public static boolean deleteIfPresent(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }
}
