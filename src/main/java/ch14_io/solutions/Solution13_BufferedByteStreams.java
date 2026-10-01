package ch14_io.solutions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise13_BufferedByteStreams.
 */
public class Solution13_BufferedByteStreams {

    public static void writeBytesBuffered(Path file, byte[] data) throws IOException {
        // Haut niveau : le Buffered enveloppe le flux bas niveau ; fermer l'exterieur ferme les deux (et vide le tampon).
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file.toFile()))) {
            out.write(data);
        }
    }

    public static byte[] readAllBytesBuffered(Path file) throws IOException {
        // Meme resultat qu'en bas niveau, mais lecture par gros blocs : beaucoup moins d'acces disque.
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toFile()))) {
            return in.readAllBytes();
        }
    }
}
