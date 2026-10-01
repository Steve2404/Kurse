package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise20_DirectoryTreeCopyCapstone.
 */
public class Solution20_DirectoryTreeCopyCapstone {

    public static void copyDirectoryTree(Path source, Path target) throws IOException {
        // walk donne les parents avant les enfants : chaque dossier est cree avant d'y copier ses fichiers ; relativize + resolve transposent le chemin.
        List<Path> entries;
        try (Stream<Path> stream = Files.walk(source)) {
            entries = stream.collect(Collectors.toList());
        }
        for (Path path : entries) {
            Path relative = source.relativize(path);
            Path destination = target.resolve(relative);
            if (Files.isDirectory(path)) {
                Files.createDirectories(destination);
            } else {
                Files.copy(path, destination);
            }
        }
    }
}
