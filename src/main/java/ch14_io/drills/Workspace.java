package ch14_io.drills;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Les donnees partagees par TOUS les drills du chapitre 14.
 * ========================================================
 *
 * create() fabrique, dans un dossier TEMPORAIRE neuf, toujours la meme
 * petite arborescence ; delete(root) la supprime (a appeler dans un
 * finally). Les fins de ligne sont des "\n" : les tailles sont fixes.
 *
 *   root/
 *     docs/readme.txt        "Bibliotheque\nDune\nFondation\n"        (3 lignes, 28 octets)
 *     docs/notes/todo.txt    "lire Hyperion\n"                        (1 ligne)
 *     logs/app.log           INFO demarrage / WARN stock bas / ERROR paiement refuse /
 *                            INFO catalogue / ERROR delai depasse      (5 lignes, 87 octets)
 *     logs/old/app-1.log     "INFO ancien\n"                          (1 ligne)
 *     src/Main.java          "class Main {}\n"
 *
 *   11 entrees au total (root compris), 5 fichiers, 6 dossiers.
 */
public final class Workspace {

    public static Path create() throws IOException {
        Path root = Files.createTempDirectory("ch14-drill");
        write(root.resolve("docs/readme.txt"), "Bibliotheque\nDune\nFondation\n");
        write(root.resolve("docs/notes/todo.txt"), "lire Hyperion\n");
        write(root.resolve("logs/app.log"),
                "INFO demarrage\nWARN stock bas\nERROR paiement refuse\nINFO catalogue\nERROR delai depasse\n");
        write(root.resolve("logs/old/app-1.log"), "INFO ancien\n");
        write(root.resolve("src/Main.java"), "class Main {}\n");
        return root;
    }

    public static void delete(Path root) throws IOException {
        if (Files.exists(root)) {
            try (Stream<Path> s = Files.walk(root)) {
                for (Path p : s.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
    }

    // Un chemin ecrit avec "/" quel que soit le systeme (Windows ecrit "\").
    public static String slash(Path p) {
        return p == null ? "null" : p.toString().replace('\\', '/');
    }

    private static void write(Path file, String text) throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, text);
    }

    private Workspace() {
    }
}
