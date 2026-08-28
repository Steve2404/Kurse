package ch14_io.solutions;

import java.io.File;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise01_FileAndPathBasics.
 */
public class Solution01_FileAndPathBasics {

    public static Path buildPath(String first, String... more) {
        return Path.of(first, more);
    }

    public static File toFile(Path path) {
        return path.toFile();
    }

    public static Path toPath(File file) {
        return file.toPath();
    }

    public static String fileNameOf(Path path) {
        return path.getFileName().toString();
    }
}
