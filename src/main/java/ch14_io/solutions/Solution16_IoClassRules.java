package ch14_io.solutions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise16_IoClassRules.
 */
public class Solution16_IoClassRules {

    public static String kind(String className) {
        // La FIN du nom decide : Reader/Writer = caracteres (meme InputStreamReader, qui convertit des octets).
        return className.endsWith("Reader") || className.endsWith("Writer") ? "char" : "byte";
    }

    public static String direction(String className) {
        // Input ou Reader = lecture ; tout le reste (Output, Writer, Print...) = ecriture.
        return className.contains("Input") || className.endsWith("Reader") ? "input" : "output";
    }

    public static String level(String className) {
        // Les File... se branchent sur un fichier (bas niveau) ; les autres enveloppent un flux existant.
        return className.startsWith("File") ? "low" : "high";
    }

    public static String firstLineUtf8(Path file) throws IOException {
        // Octets (FileInputStream) -> caracteres UTF-8 (InputStreamReader) -> lignes (BufferedReader) ;
        // fermer le flux exterieur ferme toute la chaine.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file.toFile()), StandardCharsets.UTF_8))) {
            return reader.readLine();
        }
    }
}
