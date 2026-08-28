package ch14_io.solutions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise06_LowLevelByteStreams.
 */
public class Solution06_LowLevelByteStreams {

    public static void writeBytes(Path file, byte[] data) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            out.write(data);
        }
    }

    public static byte[] readAllBytesLowLevel(Path file) throws IOException {
        try (FileInputStream in = new FileInputStream(file.toFile())) {
            return in.readAllBytes();
        }
    }
}
