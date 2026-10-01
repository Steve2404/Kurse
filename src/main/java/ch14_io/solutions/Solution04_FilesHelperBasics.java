package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise04_FilesHelperBasics.
 */
public class Solution04_FilesHelperBasics {

    public static void createEmptyFile(Path path) throws IOException {
        // createFile echoue si le fichier existe deja (FileAlreadyExistsException).
        Files.createFile(path);
    }

    public static void copyFile(Path source, Path target) throws IOException {
        // Sans option, copy refuse d'ecraser une cible existante.
        Files.copy(source, target);
    }

    public static void moveFile(Path source, Path target) throws IOException {
        // move = renommer ou deplacer ; la source n'existe plus ensuite.
        Files.move(source, target);
    }

    public static boolean deleteIfPresent(Path path) throws IOException {
        // deleteIfExists rend false au lieu de lancer NoSuchFileException.
        return Files.deleteIfExists(path);
    }
}
