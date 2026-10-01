package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise06_FileAttributes.
 */
public class Solution06_FileAttributes {

    public static long readSize(Path file) throws IOException {
        // readAttributes lit TOUS les attributs en un seul acces disque (plus efficace que plusieurs appels).
        return Files.readAttributes(file, BasicFileAttributes.class).size();
    }

    public static boolean isDirectoryViaAttributes(Path path) throws IOException {
        // Le meme objet BasicFileAttributes repond a isDirectory, isRegularFile, size, dates...
        return Files.readAttributes(path, BasicFileAttributes.class).isDirectory();
    }

    public static void updateLastModified(Path file, FileTime newTime) throws IOException {
        // Les attributs sont en lecture seule ; pour MODIFIER il faut une vue (FileAttributeView) ; null = ne pas changer.
        BasicFileAttributeView view = Files.getFileAttributeView(file, BasicFileAttributeView.class);
        view.setTimes(newTime, null, null);
    }
}
