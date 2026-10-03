package ch14_io.drills.r02_files.solution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 2 - les operations de Files dans un bac a sable.
 */
public class Recall02 {

    public static void main(String[] args) throws IOException {
        Path box = Path.of("build/ch14/r02_files");
        if (Files.exists(box)) {
            try (Stream<Path> all = Files.walk(box)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        Path dir = Files.createDirectories(box.resolve("a/b"));
        Path f = Files.writeString(dir.resolve("f.txt"), "bonjour");
        System.out.println("D01 : " + Files.exists(f) + " " + Files.isRegularFile(f) + " " + Files.isDirectory(dir) + " " + Files.size(f) + " " + Files.readString(f));

        Path copy = Files.copy(f, box.resolve("copie.txt"));
        String again;
        try {
            Files.copy(f, copy);
            again = "ok";
        } catch (FileAlreadyExistsException e) {
            again = e.getClass().getSimpleName();
        }
        Files.writeString(f, "bonsoir");
        Files.copy(f, copy, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("D02 : " + again + " " + Files.readString(copy) + " " + Files.mismatch(f, copy) + " " + Files.mismatch(f, box.resolve("a/b/f.txt")));

        Path moved = Files.move(copy, box.resolve("a/deplace.txt"));
        System.out.println("D03 : " + Files.exists(copy) + " " + Files.exists(moved) + " " + Files.isSameFile(moved, box.resolve("a/b/../deplace.txt")));

        String nonEmpty;
        try {
            Files.delete(box.resolve("a"));
            nonEmpty = "ok";
        } catch (DirectoryNotEmptyException e) {
            nonEmpty = e.getClass().getSimpleName();
        }
        String missing;
        try {
            Files.delete(box.resolve("absent"));
            missing = "ok";
        } catch (NoSuchFileException e) {
            missing = e.getClass().getSimpleName();
        }
        System.out.println("D04 : " + nonEmpty + " " + missing + " " + Files.deleteIfExists(box.resolve("absent")) + " " + Files.deleteIfExists(moved));

        String noParent;
        try {
            Files.createDirectory(box.resolve("x/y"));
            noParent = "ok";
        } catch (NoSuchFileException e) {
            noParent = e.getClass().getSimpleName();
        }
        String exists;
        try {
            Files.createDirectory(dir);
            exists = "ok";
        } catch (FileAlreadyExistsException e) {
            exists = e.getClass().getSimpleName();
        }
        Files.createDirectories(dir);                                   // sans erreur si le dossier existe deja
        System.out.println("D05 : " + noParent + " " + exists + " " + Files.notExists(box.resolve("x")));

        // toRealPath : le VRAI chemin (absolu, normalise, liens resolus) ; le fichier doit exister.
        Path real = box.resolve("a/b/../b/f.txt").toRealPath();
        String noReal;
        try {
            box.resolve("fantome.txt").toRealPath();
            noReal = "ok";
        } catch (NoSuchFileException e) {
            noReal = e.getClass().getSimpleName();
        }
        // Files.copy avec un FLUX : InputStream -> fichier, et fichier -> OutputStream.
        Path fromStream = box.resolve("flux.txt");
        long in = Files.copy(new ByteArrayInputStream("abc".getBytes(StandardCharsets.UTF_8)), fromStream);
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        long out = Files.copy(fromStream, sink);
        // L'ancienne API java.io.File : listFiles, renameTo, delete (des booleens, pas d'exception).
        File folder = box.resolve("a/b").toFile();
        File renamed = new File(folder, "g.txt");
        boolean renamedOk = new File(folder, "f.txt").renameTo(renamed);
        String[] names = folder.list();
        File[] children = folder.listFiles();
        System.out.println("D06 : " + real.isAbsolute() + " " + real.endsWith(Path.of("a", "b", "f.txt")) + " " + noReal + " | " + in + " " + out + " " + sink + " | " + renamedOk
                + " " + names.length + " " + children[0].getName() + " " + renamed.delete() + " " + renamed.delete());
    }
}
