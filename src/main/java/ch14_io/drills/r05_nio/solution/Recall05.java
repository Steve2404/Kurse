package ch14_io.drills.r05_nio.solution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 5 - NIO.2 : lecture et ecriture de texte, flux de chemins, attributs.
 */
public class Recall05 {

    static String rel(Path root, Path p) {
        String r = root.relativize(p).toString().replace('\\', '/');
        return r.isEmpty() ? "." : r;
    }

    public static void main(String[] args) throws IOException {
        Path box = Path.of("build/ch14/r05_nio");
        if (Files.exists(box)) {
            try (Stream<Path> all = Files.walk(box)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        Files.createDirectories(box.resolve("src/main"));
        Path f = box.resolve("src/data.txt");
        Files.write(f, List.of("alpha", "beta", "gamma"));
        Files.writeString(f, "delta" + System.lineSeparator(), StandardOpenOption.APPEND);
        try (BufferedWriter w = Files.newBufferedWriter(box.resolve("src/main/App.java"))) {
            w.write("class App {}");
        }
        System.out.println("D01 : " + Files.readAllLines(f) + " " + Files.readAllLines(f).size());

        try (Stream<String> lines = Files.lines(f)) {
            System.out.println("D02 : " + lines.filter(l -> l.contains("a")).map(String::toUpperCase).toList());
        }
        try (BufferedReader r = Files.newBufferedReader(f)) {
            System.out.println("D03 : " + r.readLine() + " " + r.readLine());
        }

        try (Stream<Path> list = Files.list(box.resolve("src"));
             Stream<Path> walk = Files.walk(box);
             Stream<Path> find = Files.find(box, 5, (p, a) -> a.isRegularFile() && p.toString().endsWith(".java"))) {
            System.out.println("D04 : " + list.map(p -> rel(box, p)).sorted().toList() + " " + walk.count() + " " + find.map(p -> rel(box, p)).toList());
        }
        try (Stream<Path> shallow = Files.walk(box, 1)) {
            System.out.println("D05 : " + shallow.map(p -> rel(box, p)).sorted().toList());
        }

        Files.setLastModifiedTime(f, FileTime.from(Instant.parse("2026-01-01T00:00:00Z")));
        BasicFileAttributes attrs = Files.readAttributes(f, BasicFileAttributes.class);
        System.out.println("D06 : " + attrs.isRegularFile() + " " + attrs.isDirectory() + " " + (attrs.size() == Files.size(f)) + " " + attrs.lastModifiedTime() + " "
                + Files.getAttribute(f, "isDirectory") + " " + Files.isReadable(f));
    }
}
