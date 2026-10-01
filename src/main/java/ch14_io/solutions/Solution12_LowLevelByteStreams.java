package ch14_io.solutions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise12_LowLevelByteStreams.
 */
public class Solution12_LowLevelByteStreams {

    public static void writeBytes(Path file, byte[] data) throws IOException {
        // FileOutputStream : bas niveau, ecrit des octets directement dans le fichier (l'ecrase par defaut).
        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            out.write(data);
        }
    }

    public static byte[] readAllBytesLowLevel(Path file) throws IOException {
        // readAllBytes (Java 9) lit tout ; read() octet par octet rendrait -1 a la fin.
        try (FileInputStream in = new FileInputStream(file.toFile())) {
            return in.readAllBytes();
        }
    }
}
