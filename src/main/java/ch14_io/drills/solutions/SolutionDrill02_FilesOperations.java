package ch14_io.drills.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.List;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.drills.exercises.Drill02_FilesOperations.
 */
public class SolutionDrill02_FilesOperations {

    public static boolean readmeExists(Path root) {
        // exists ne lance pas d'exception (il rend false en cas de doute).
        return Files.exists(root.resolve("docs/readme.txt"));
    }

    public static boolean logsIsDirectory(Path root) {
        // isDirectory : false aussi si le chemin n'existe pas.
        return Files.isDirectory(root.resolve("logs"));
    }

    public static List<String> readmeLines(Path root) throws IOException {
        // Tout le fichier en memoire, une ligne par element, sans les fins de ligne.
        return Files.readAllLines(root.resolve("docs/readme.txt"));
    }

    public static String writeReport(Path root) throws IOException {
        // writeString ne cree pas les dossiers : createDirectories d'abord, sinon NoSuchFileException.
        Path report = root.resolve("out/report.txt");
        Files.createDirectories(report.getParent());
        Files.writeString(report, "ok");
        return Files.readString(report);
    }

    public static int appendToLog(Path root) throws IOException {
        // Sans APPEND, writeString remplacerait tout le contenu.
        Path log = root.resolve("logs/app.log");
        Files.writeString(log, "INFO fin\n", StandardOpenOption.APPEND);
        return Files.readAllLines(log).size();
    }

    public static long copyReadme(Path root) throws IOException {
        // copy vers une cible absente : pas besoin d'option.
        Path copy = Files.copy(root.resolve("docs/readme.txt"), root.resolve("docs/copy.txt"));
        return Files.size(copy);
    }

    public static String overwrite(Path root) throws IOException {
        // La cible existe : REPLACE_EXISTING obligatoire, sinon FileAlreadyExistsException.
        Path target = root.resolve("docs/readme.txt");
        Files.copy(root.resolve("src/Main.java"), target, StandardCopyOption.REPLACE_EXISTING);
        return Files.readString(target);
    }

    public static String moveTodo(Path root) throws IOException {
        // move deplace (et renomme) : la source n'existe plus.
        Path from = root.resolve("docs/notes/todo.txt");
        Path to = root.resolve("docs/done.txt");
        Files.move(from, to);
        return Files.exists(from) + " " + Files.exists(to);
    }

    public static boolean deleteTwice(Path root) throws IOException {
        // delete lancerait NoSuchFileException la 2e fois ; deleteIfExists rend simplement false.
        Path main = root.resolve("src/Main.java");
        Files.delete(main);
        return Files.deleteIfExists(main);
    }

    public static long readmeSize(Path root) throws IOException {
        // La taille en OCTETS (ici des caracteres ASCII : 1 octet chacun).
        return Files.size(root.resolve("docs/readme.txt"));
    }

    public static FileTime touch(Path root, FileTime time) throws IOException {
        // Les dates sont des FileTime (pas des LocalDateTime).
        Path readme = root.resolve("docs/readme.txt");
        Files.setLastModifiedTime(readme, time);
        return Files.getLastModifiedTime(readme);
    }

    public static boolean sameFile(Path root) throws IOException {
        // isSameFile compare les fichiers reels : deux ecritures differentes du meme chemin donnent true.
        return Files.isSameFile(root.resolve("docs/../docs/readme.txt"), root.resolve("docs/readme.txt"));
    }
}
