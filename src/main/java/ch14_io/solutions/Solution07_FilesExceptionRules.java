package ch14_io.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise07_FilesExceptionRules.
 */
public class Solution07_FilesExceptionRules {

    public static String outcome(String method, String situation) {
        // Des regles plutot qu'un tableau : "absent" -> NoSuchFile, "deja la" -> FileAlreadyExists,
        // sauf les methodes "tolerantes" (createDirectories, deleteIfExists, REPLACE_EXISTING).
        switch (situation) {
            case "nonEmptyDir":
                return method.equals("delete") ? "DirectoryNotEmptyException" : "OK";
            case "onFile":
                return "NotDirectoryException";
            case "samePathMissing":
                return "OK";
            case "differentMissing":
                return "NoSuchFileException";
            case "missingParent":
                return method.equals("createDirectories") ? "OK" : "NoSuchFileException";
            case "targetExists":
                return method.equals("createDirectories") || method.contains("REPLACE_EXISTING") ? "OK" : "FileAlreadyExistsException";
            default:
                return method.equals("deleteIfExists") ? "OK" : "NoSuchFileException";
        }
    }

    public static boolean ensureFile(Path path) throws IOException {
        // createDirectories ne se plaint jamais d'un dossier existant ; on teste exists avant createFile, qui, lui, se plaindrait.
        Files.createDirectories(path.getParent());
        if (Files.exists(path)) {
            return false;
        }
        Files.createFile(path);
        return true;
    }

    public static int deleteTree(Path root) throws IOException {
        // Ordre inverse : chaque enfant (chemin plus long) est supprime avant son parent ; walk est ferme par le try.
        if (!Files.exists(root)) {
            return 0;
        }
        int count = 0;
        try (Stream<Path> entries = Files.walk(root)) {
            for (Path p : entries.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(p);
                count++;
            }
        }
        return count;
    }
}
