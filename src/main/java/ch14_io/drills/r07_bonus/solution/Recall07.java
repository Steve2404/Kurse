package ch14_io.drills.r07_bonus.solution;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 7 (bonus) - liens symboliques, attributs POSIX, lecture d'attributs par nom, Console.
 * Les resultats dependent du systeme : on affiche seulement des verites valables PARTOUT.
 */
public class Recall07 {

    private static final char[] SECRET = "s3cret".toCharArray();

    // Compare un mot de passe saisi, puis l'EFFACE (un char[] peut etre vide, une String reste en memoire).
    static boolean checkPassword(char[] typed) {
        try {
            return Arrays.equals(typed, SECRET);
        } finally {
            Arrays.fill(typed, '\0');
        }
    }

    public static void main(String[] args) throws IOException {
        Path box = Path.of("build/ch14/r07_bonus");
        if (Files.exists(box, LinkOption.NOFOLLOW_LINKS)) {
            try (Stream<Path> all = Files.walk(box)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        Path target = Files.createDirectories(box.resolve("donnees"));
        Files.writeString(target.resolve("a.txt"), "contenu");
        Path link = box.resolve("raccourci");

        // Lien symbolique : sous Windows, il faut souvent des droits ; sinon FileSystemException.
        boolean coherent;
        int followed = -1;
        int notFollowed = -1;
        try {
            Files.createSymbolicLink(link, target.toAbsolutePath());
            coherent = Files.isSymbolicLink(link) && Files.readSymbolicLink(link).equals(target.toAbsolutePath()) && Files.isSameFile(link, target)
                    && Files.isDirectory(link) && !Files.isDirectory(link, LinkOption.NOFOLLOW_LINKS);
            try (Stream<Path> withLinks = Files.walk(box, FileVisitOption.FOLLOW_LINKS);
                 Stream<Path> withoutLinks = Files.walk(box)) {
                followed = (int) withLinks.count();
                notFollowed = (int) withoutLinks.count();
            }
        } catch (FileSystemException | UnsupportedOperationException e) {
            coherent = !Files.exists(link, LinkOption.NOFOLLOW_LINKS);
        }
        System.out.println("D01 : coherent " + coherent + ", walk avec FOLLOW_LINKS voit plus " + (followed == -1 || followed > notFollowed));

        // POSIX (Linux, macOS) : permissions rwx ; ailleurs, la vue n'existe pas.
        boolean posix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
        boolean posixCoherent;
        try {
            PosixFileAttributes attrs = Files.readAttributes(target.resolve("a.txt"), PosixFileAttributes.class);
            posixCoherent = posix && PosixFilePermissions.toString(attrs.permissions()).length() == 9;
        } catch (UnsupportedOperationException e) {
            posixCoherent = !posix;
        }
        System.out.println("D02 : posix coherent " + posixCoherent + ", vue basic toujours presente "
                + FileSystems.getDefault().supportedFileAttributeViews().contains("basic"));

        // Lire des attributs PAR NOM : "vue:attribut1,attribut2".
        Map<String, Object> byName = Files.readAttributes(target.resolve("a.txt"), "basic:size,isRegularFile");
        System.out.println("D03 : " + byName.get("size") + " " + byName.get("isRegularFile") + " " + byName.size());

        // Console : null dans un IDE ou si la sortie est redirigee. On n'y touche que si on le DEMANDE (argument "console").
        Console console = args.length > 0 && args[0].equals("console") ? System.console() : null;
        String name;
        char[] password;
        if (console != null) {
            PrintWriter out = console.writer();
            name = console.readLine("Ton nom ? ");
            password = console.readPassword("Mot de passe (%s) ? ", "s3cret");
            out.printf("Bonjour %s%n", name);
            console.flush();
        } else {
            BufferedReader simulated = new BufferedReader(new StringReader("Ada\ns3cret\n"));
            name = simulated.readLine();
            password = simulated.readLine().toCharArray();
        }
        boolean ok = checkPassword(password);
        System.out.println("D04 : " + (console == null ? "entree simulee" : "console") + " " + name + " " + ok + " efface " + Arrays.equals(password, new char[password.length]));
    }
}
