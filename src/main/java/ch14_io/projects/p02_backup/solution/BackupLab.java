package ch14_io.projects.p02_backup.solution;

import ch14_io.projects.p02_backup.Data;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 2 - la sauvegarde incrementale : creer, ecrire, copier, deplacer, supprimer, comparer.
 */
public class BackupLab {

    public static void main(String[] args) throws IOException {
        Path sandbox = Path.of(Data.SANDBOX);
        Backup.deleteTree(sandbox);
        Path src = sandbox.resolve("source");
        for (String entry : Data.FILES) {
            String[] parts = entry.split("\\|");
            Path file = src.resolve(parts[0]);
            Files.createDirectories(file.getParent());           // cree aussi les parents manquants
            Files.writeString(file, parts[1]);
        }
        System.out.println("source : " + Backup.files(src) + " ; tailles par dossier " + Backup.sizes(src));

        Path full = sandbox.resolve("sauvegarde-complete");
        System.out.println("sauvegarde complete : " + Backup.copyTree(src, full) + " fichiers copies ; diff " + Backup.diff(src, full).values().stream().distinct().toList());

        // On modifie la source : un ajout, une modification, une suppression, un renommage (move).
        Files.writeString(src.resolve("notes/todo.txt"), "acheter du pain\nappeler Leo");
        Files.writeString(src.resolve("notes/courses.txt"), "lait");
        Files.delete(src.resolve("photos/2025/neige.jpg"));
        Files.move(src.resolve("budget.csv"), src.resolve("notes/budget.csv"));
        System.out.println("apres modifications :");
        for (Map.Entry<String, String> e : Backup.diff(src, full).entrySet()) {
            System.out.println("  " + e.getKey() + " : " + e.getValue());
        }

        // Increment : on ne copie que ce qui est nouveau ou modifie.
        Path inc = sandbox.resolve("increment");
        int copied = 0;
        for (Map.Entry<String, String> e : Backup.diff(src, full).entrySet()) {
            if (e.getValue().equals("ajoute") || e.getValue().startsWith("modifie")) {
                Path target = inc.resolve(e.getKey());
                Files.createDirectories(target.getParent());
                Files.copy(src.resolve(e.getKey()), target);
                copied++;
            }
        }
        System.out.println("increment : " + copied + " fichiers " + Backup.files(inc));

        // Les operations qui ECHOUENT, et leurs exceptions (toutes des IOException).
        List<String> errors = new ArrayList<>();
        try {
            Files.copy(src.resolve("notes/todo.txt"), inc.resolve("notes/todo.txt"));          // sans REPLACE_EXISTING
        } catch (FileAlreadyExistsException e) {
            errors.add("copy sur un fichier existant : " + e.getClass().getSimpleName());
        }
        try {
            Files.delete(src.resolve("notes"));
        } catch (DirectoryNotEmptyException e) {
            errors.add("delete d'un dossier non vide : " + e.getClass().getSimpleName());
        }
        try {
            Files.delete(src.resolve("absent.txt"));
        } catch (NoSuchFileException e) {
            errors.add("delete d'un absent : " + e.getClass().getSimpleName());
        }
        try {
            Files.createDirectory(sandbox.resolve("a/b/c"));                                  // createDirectory : le parent doit exister
        } catch (NoSuchFileException e) {
            errors.add("createDirectory sans parent : " + e.getClass().getSimpleName());
        }
        errors.add("deleteIfExists d'un absent : " + Files.deleteIfExists(src.resolve("absent.txt")));
        errors.forEach(s -> System.out.println("  " + s));

        Files.copy(src.resolve("notes/todo.txt"), inc.resolve("notes/todo.txt"), StandardCopyOption.REPLACE_EXISTING);
        Path weird = src.resolve("notes/../notes/./todo.txt");
        // Files.list : un seul niveau ; Files.find : un critere sur le chemin ET les attributs.
        List<String> top;
        try (Stream<Path> level = Files.list(src)) {
            top = level.map(p -> Backup.show(src.relativize(p))).sorted().toList();
        }
        List<String> texts;
        try (Stream<Path> found = Files.find(src, 10, (p, attrs) -> attrs.isRegularFile() && p.toString().endsWith(".txt"))) {
            texts = found.map(p -> Backup.show(src.relativize(p))).sorted().toList();
        }
        System.out.println("isSameFile " + Files.isSameFile(weird, src.resolve("notes/todo.txt")) + ", equals " + weird.equals(src.resolve("notes/todo.txt"))
                + ", size " + Files.size(weird) + ", isDirectory(notes) " + Files.isDirectory(src.resolve("notes")) + ", isRegularFile(notes) "
                + Files.isRegularFile(src.resolve("notes")) + ", notExists " + Files.notExists(src.resolve("photos/2025/neige.jpg")));
        System.out.println("list " + top + " ; find *.txt " + texts);
    }
}
