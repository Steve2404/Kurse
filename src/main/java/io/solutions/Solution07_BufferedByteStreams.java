package io.solutions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise07_BufferedByteStreams.
 */
public class Solution07_BufferedByteStreams {

    public static void writeBytesBuffered(Path file, byte[] data) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file.toFile()))) {
            out.write(data);
        }
    }

    public static byte[] readAllBytesBuffered(Path file) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toFile()))) {
            return in.readAllBytes();
        }
    }
}
