package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise09_NioStreamListWalkFind.
 */
public class Solution09_NioStreamListWalkFind {

    public static List<Path> listImmediateChildren(Path dir) throws IOException {
        // list ne descend que d'un niveau ; le Stream tient le dossier ouvert : try-with-resources.
        try (Stream<Path> stream = Files.list(dir)) {
            return stream.collect(Collectors.toList());
        }
    }

    public static long countAllEntries(Path root) throws IOException {
        // walk parcourt TOUT l'arbre en profondeur, la racine comprise.
        try (Stream<Path> stream = Files.walk(root)) {
            return stream.count();
        }
    }

    public static List<Path> findTextFiles(Path root) throws IOException {
        // find = walk + filtre qui recoit aussi les attributs (pas besoin de relire le disque).
        try (Stream<Path> stream = Files.find(root, Integer.MAX_VALUE,
                (path, attrs) -> attrs.isRegularFile() && path.toString().endsWith(".txt"))) {
            return stream.collect(Collectors.toList());
        }
    }
}
