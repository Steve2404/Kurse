package ch14_io.drills.solutions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.drills.exercises.Drill04_JavaIoStreams.
 */
public class SolutionDrill04_JavaIoStreams {

    public static class Bookmark implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String title;
        public transient int page;

        public Bookmark(String title, int page) {
            this.title = title;
            this.page = page;
        }
    }

    public static long writeThreeBytes(Path root) throws IOException {
        // Bas niveau, octets : write(byte[]) ecrit le tableau entier.
        Path file = root.resolve("bytes.bin");
        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            out.write(new byte[]{1, 2, 3});
        }
        return Files.size(file);
    }

    public static int sumOfBytes(Path root) throws IOException {
        // read() rend un int de 0 a 255, ou -1 a la fin du fichier.
        int sum = 0;
        try (FileInputStream in = new FileInputStream(root.resolve("bytes.bin").toFile())) {
            int b;
            while ((b = in.read()) != -1) {
                sum += b;
            }
        }
        return sum;
    }

    public static int readmeLineCount(Path root) throws IOException {
        // readLine rend null a la fin (pas -1, ce n'est pas un int).
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(root.resolve("docs/readme.txt").toFile()))) {
            while (reader.readLine() != null) {
                count++;
            }
        }
        return count;
    }

    public static List<String> writeWithBuffer(Path root) throws IOException {
        // Fermer le BufferedWriter vide son tampon (flush) puis ferme le FileWriter.
        Path file = root.resolve("w.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            writer.write("a");
            writer.newLine();
            writer.write("b");
        }
        return Files.readAllLines(file);
    }

    public static int appendWithFileWriter(Path root) throws IOException {
        // Le 2e argument true = mode ajout ; sans lui, FileWriter vide le fichier a l'ouverture.
        Path readme = root.resolve("docs/readme.txt");
        try (FileWriter writer = new FileWriter(readme.toFile(), true)) {
            writer.write("Hyperion\n");
        }
        return Files.readAllLines(readme).size();
    }

    public static String formatted() {
        // printf avec une Locale explicite : le point decimal, et l'arrondi de %.1f (12.46 -> 12.5).
        StringWriter text = new StringWriter();
        try (PrintWriter writer = new PrintWriter(text)) {
            writer.printf(Locale.US, "%.1f km", 12.46);
        }
        return text.toString();
    }

    public static char decodeUtf8() throws IOException {
        // Le pont octets -> caracteres a besoin du bon Charset : "e accent" fait 2 octets en UTF-8.
        byte[] bytes = "été".getBytes(StandardCharsets.UTF_8);
        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8)) {
            return (char) reader.read();
        }
    }

    public static String collectBytes() throws IOException {
        // Un flux de sortie en memoire : utile pour tester sans fichier.
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write("ok".getBytes(StandardCharsets.UTF_8));
            return out.toString(StandardCharsets.UTF_8);
        }
    }

    public static String bookmarkCopy(Bookmark bookmark) throws IOException, ClassNotFoundException {
        // Aller-retour en memoire ; transient revient a sa valeur par defaut (0).
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(bookmark);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            Bookmark copy = (Bookmark) in.readObject();
            return copy.title + "/" + copy.page;
        }
    }

    public static String markReset(Path root) throws IOException {
        // mark pose un repere (valable pour 10 caracteres lus au plus) ; reset y ramene la lecture.
        StringBuilder read = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(root.resolve("docs/readme.txt"))) {
            read.append((char) reader.read());
            reader.mark(10);
            read.append((char) reader.read()).append((char) reader.read());
            reader.reset();
            read.append((char) reader.read());
        }
        return read.toString();
    }
}
