package ch14_io.projects.p06_duplicates.solution;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION - un visiteur de fichiers : walkFileTree l'appelle avant chaque dossier, pour chaque fichier,
 * et apres chaque dossier. On y cumule la taille de chaque dossier (sous-dossiers compris) et on saute un dossier.
 */
public class DiskUsage extends SimpleFileVisitor<Path> {

    private final Path root;
    private final String skipped;
    private final Map<String, Long> usage = new TreeMap<>();
    private final List<Path> files = new ArrayList<>();
    private final List<String> events = new ArrayList<>();

    public DiskUsage(Path root, String skipped) {
        this.root = root;
        this.skipped = skipped;
    }

    static String show(Path p) {
        String s = p.toString().replace('\\', '/');
        return s.isEmpty() ? "." : s;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if (dir.getFileName().toString().equals(skipped)) {
            events.add("saute " + show(root.relativize(dir)));
            return FileVisitResult.SKIP_SUBTREE;                  // ni ses fichiers, ni ses sous-dossiers
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        files.add(file);
        // La taille compte pour CHAQUE dossier ancetre, jusqu'a la racine.
        for (Path d = root.relativize(file).getParent(); d != null; d = d.getParent()) {
            usage.merge(show(d), attrs.size(), Long::sum);
        }
        usage.merge(".", attrs.size(), Long::sum);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) {
        if (dir.equals(root)) {
            events.add("fin du parcours");
        }
        return FileVisitResult.CONTINUE;
    }

    public Map<String, Long> usage() {
        return usage;
    }

    public List<Path> files() {
        return files;
    }

    public List<String> events() {
        return events;
    }
}
