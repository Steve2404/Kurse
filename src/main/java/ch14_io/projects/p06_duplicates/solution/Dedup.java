package ch14_io.projects.p06_duplicates.solution;

import ch14_io.projects.p06_duplicates.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 6 - attributs de fichiers, parcours avec un visiteur, et chasse aux doublons.
 */
public class Dedup {

    static String rel(Path root, Path p) {
        return root.relativize(p).toString().replace('\\', '/');
    }

    // Doublons : on groupe d'abord par TAILLE (rapide), puis on compare le contenu (mismatch) dans chaque groupe.
    static List<List<Path>> duplicates(List<Path> files) throws IOException {
        Map<Long, List<Path>> bySize = new TreeMap<>();
        for (Path f : files) {
            bySize.computeIfAbsent(Files.size(f), k -> new ArrayList<>()).add(f);
        }
        List<List<Path>> groups = new ArrayList<>();
        for (List<Path> sameSize : bySize.values()) {
            List<Path> left = new ArrayList<>(sameSize);
            while (left.size() > 1) {
                Path first = left.remove(0);
                List<Path> group = new ArrayList<>(List.of(first));
                for (Path other : List.copyOf(left)) {
                    if (Files.mismatch(first, other) == -1) {
                        group.add(other);
                        left.remove(other);
                    }
                }
                if (group.size() > 1) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    public static void main(String[] args) throws IOException {
        Path root = Path.of(Data.SANDBOX);
        if (Files.exists(root)) {
            try (Stream<Path> all = Files.walk(root)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        for (String entry : Data.FILES) {
            String[] p = entry.split("\\|");
            Path f = root.resolve(p[0]);
            Files.createDirectories(f.getParent());
            Files.writeString(f, p[1]);
            Files.setLastModifiedTime(f, FileTime.from(Instant.parse(p[2])));   // une date FIXE : sortie reproductible
        }

        DiskUsage visitor = new DiskUsage(root, Data.SKIPPED);
        Files.walkFileTree(root, visitor);
        System.out.println("parcours : " + visitor.files().size() + " fichiers, " + visitor.events() + " ; occupation (octets) " + visitor.usage());

        Path photo = root.resolve("photos/plage.jpg");
        BasicFileAttributes attrs = Files.readAttributes(photo, BasicFileAttributes.class);
        System.out.println("attributs de photos/plage.jpg : taille " + attrs.size() + ", fichier " + attrs.isRegularFile() + ", dossier " + attrs.isDirectory()
                + ", lien " + attrs.isSymbolicLink() + ", modifie " + attrs.lastModifiedTime() + " ; getAttribute(\"size\") " + Files.getAttribute(photo, "size"));

        // Une VUE d'attributs permet de les MODIFIER (null : ne pas toucher a cet horodatage).
        BasicFileAttributeView view = Files.getFileAttributeView(photo, BasicFileAttributeView.class);
        view.setTimes(FileTime.from(Instant.parse("2026-12-25T00:00:00Z")), null, null);
        System.out.println("apres setTimes : " + Files.getLastModifiedTime(photo) + " ; vue " + view.name());

        List<Path> newest = new ArrayList<>(visitor.files());
        newest.sort(Comparator.comparing((Path f) -> {
            try {
                return Files.getLastModifiedTime(f);
            } catch (IOException e) {
                return FileTime.fromMillis(0);
            }
        }).reversed());
        System.out.println("plus recents : " + newest.stream().limit(3).map(f -> rel(root, f)).toList());

        // L'ordre de parcours depend du systeme de fichiers : on trie avant de grouper.
        List<Path> sortedFiles = new ArrayList<>(visitor.files());
        sortedFiles.sort(Comparator.comparing(f -> rel(root, f)));
        List<List<Path>> groups = duplicates(sortedFiles);
        long reclaimable = 0;
        for (List<Path> g : groups) {
            System.out.println("doublons : " + g.stream().map(f -> rel(root, f)).toList());
            reclaimable += Files.size(g.get(0)) * (g.size() - 1);
        }
        System.out.println(groups.size() + " groupes, " + reclaimable + " octets recuperables (dossier " + Data.SKIPPED + " ignore)");

        // walk avec une profondeur maximale : 1 = la racine et ses enfants directs.
        Map<Integer, Long> byDepth;
        try (Stream<Path> all = Files.walk(root, 2)) {
            byDepth = all.collect(Collectors.groupingBy(p -> root.relativize(p).toString().isEmpty() ? 0 : root.relativize(p).getNameCount(), TreeMap::new,
                    Collectors.counting()));
        }
        System.out.println("walk(racine, 2) par profondeur " + byDepth);
    }
}
