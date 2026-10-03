package ch14_io.projects.p02_backup.solution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION - les operations de sauvegarde : supprimer un arbre, copier un arbre, comparer deux arbres.
 */
public final class Backup {

    private Backup() {
    }

    static String show(Path p) {
        return p.toString().replace('\\', '/');
    }

    // Supprimer un dossier non vide : du plus PROFOND au moins profond (ordre inverse du parcours).
    // Files.walk ouvre des dossiers : le Stream DOIT etre ferme (try-with-resources).
    public static void deleteTree(Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        try (Stream<Path> all = Files.walk(root)) {
            List<Path> deepestFirst = all.sorted(Comparator.reverseOrder()).toList();
            for (Path p : deepestFirst) {
                Files.delete(p);
            }
        }
    }

    // Les fichiers ordinaires d'un arbre, en chemins RELATIFS tries ("/" comme separateur).
    public static List<String> files(Path root) throws IOException {
        try (Stream<Path> all = Files.walk(root)) {
            return all.filter(Files::isRegularFile).map(p -> show(root.relativize(p))).sorted().toList();
        }
    }

    // Copie d'arbre : on recree chaque dossier, puis on copie chaque fichier a la meme place relative.
    public static int copyTree(Path from, Path to) throws IOException {
        int copied = 0;
        try (Stream<Path> all = Files.walk(from)) {
            for (Path p : all.toList()) {
                Path target = to.resolve(from.relativize(p));
                if (Files.isDirectory(p)) {
                    Files.createDirectories(target);
                } else {
                    Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
                    copied++;
                }
            }
        }
        return copied;
    }

    // Etat de chaque fichier de "current" par rapport a "saved" : ajoute, modifie (et ou), inchange, supprime.
    public static Map<String, String> diff(Path current, Path saved) throws IOException {
        Map<String, String> status = new TreeMap<>();
        for (String rel : files(current)) {
            Path old = saved.resolve(rel);
            if (!Files.exists(old)) {
                status.put(rel, "ajoute");
            } else {
                long at = Files.mismatch(current.resolve(rel), old);    // -1 : contenus identiques
                status.put(rel, at == -1 ? "inchange" : "modifie (octet " + at + ")");
            }
        }
        for (String rel : files(saved)) {
            status.putIfAbsent(rel, "supprime");
        }
        return status;
    }

    public static Map<String, Long> sizes(Path root) throws IOException {
        try (Stream<Path> all = Files.walk(root)) {
            return all.filter(Files::isRegularFile).collect(Collectors.groupingBy(p -> show(root.relativize(p).getName(0)), TreeMap::new,
                    Collectors.summingLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0;
                        }
                    })));
        }
    }
}
