package ch14_io.solutions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise14_CharacterStreams.
 */
public class Solution14_CharacterStreams {

    public static void writeLines(Path file, List<String> lines) throws IOException {
        // Flux de CARACTERES ; newLine() ecrit le separateur de ligne du systeme.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static List<String> readLines(Path file) throws IOException {
        // readLine rend null a la fin du fichier (pas -1) ; le separateur n'est pas inclus dans la ligne.
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        }
    }
}
