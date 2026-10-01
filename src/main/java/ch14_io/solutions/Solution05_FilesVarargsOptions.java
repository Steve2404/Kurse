package ch14_io.solutions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise05_FilesVarargsOptions.
 */
public class Solution05_FilesVarargsOptions {

    public static void copyReplaceExisting(Path source, Path target) throws IOException {
        // Les options sont des varargs : REPLACE_EXISTING autorise l'ecrasement.
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void appendLine(Path file, String line) throws IOException {
        // APPEND seul echoue sur un fichier absent : CREATE + APPEND = creer si besoin, sinon ajouter a la fin.
        Files.writeString(file, line + System.lineSeparator(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
