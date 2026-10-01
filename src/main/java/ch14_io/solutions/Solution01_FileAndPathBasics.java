package ch14_io.solutions;

import java.io.File;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise01_FileAndPathBasics.
 */
public class Solution01_FileAndPathBasics {

    public static Path buildPath(String first, String... more) {
        // Path.of (Java 11) = Paths.get : assemble les morceaux avec le separateur du systeme, sans toucher au disque.
        return Path.of(first, more);
    }

    public static File toFile(Path path) {
        // Pont NIO.2 -> java.io : utile pour les API anciennes (FileInputStream...).
        return path.toFile();
    }

    public static Path toPath(File file) {
        // Pont java.io -> NIO.2 : pour utiliser Files sur un File existant.
        return file.toPath();
    }

    public static String fileNameOf(Path path) {
        // getFileName rend un Path (le dernier nom) : toString pour avoir le texte.
        return path.getFileName().toString();
    }
}
