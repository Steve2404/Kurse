package io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise11_FileAttributes.
 */
public class Solution11_FileAttributes {

    public static long readSize(Path file) throws IOException {
        return Files.readAttributes(file, BasicFileAttributes.class).size();
    }

    public static boolean isDirectoryViaAttributes(Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class).isDirectory();
    }

    public static void updateLastModified(Path file, FileTime newTime) throws IOException {
        BasicFileAttributeView view = Files.getFileAttributeView(file, BasicFileAttributeView.class);
        view.setTimes(newTime, null, null);
    }
}
