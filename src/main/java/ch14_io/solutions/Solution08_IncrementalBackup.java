package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise08_IncrementalBackup.
 */
public class Solution08_IncrementalBackup {

    public static List<String> backup(Path source, Path target) throws IOException {
        // On ne copie que l'absent ou le plus recent ; COPY_ATTRIBUTES garde la date, sinon la copie paraitrait toujours neuve.
        List<String> copied = new ArrayList<>();
        try (Stream<Path> entries = Files.walk(source)) {
            for (Path file : entries.filter(Files::isRegularFile).toList()) {
                Path relative = source.relativize(file);
                Path copy = target.resolve(relative);
                if (!Files.exists(copy) || Files.getLastModifiedTime(file).compareTo(Files.getLastModifiedTime(copy)) > 0) {
                    Files.createDirectories(copy.getParent());
                    Files.copy(file, copy, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    copied.add(slash(relative));
                }
            }
        }
        Collections.sort(copied);
        return copied;
    }

    public static List<String> prune(Path source, Path target) throws IOException {
        // Chemin relatif a la sauvegarde, puis resolu dans la source : s'il n'y existe plus, la copie disparait.
        List<String> removed = new ArrayList<>();
        try (Stream<Path> entries = Files.walk(target)) {
            for (Path copy : entries.filter(Files::isRegularFile).toList()) {
                Path relative = target.relativize(copy);
                if (!Files.exists(source.resolve(relative))) {
                    Files.delete(copy);
                    removed.add(slash(relative));
                }
            }
        }
        Collections.sort(removed);
        return removed;
    }

    private static String slash(Path relative) {
        // Boite magique : le meme rendu sous Windows et sous Linux.
        return relative.toString().replace('\\', '/');
    }
}
