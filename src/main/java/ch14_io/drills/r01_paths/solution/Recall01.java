package ch14_io.drills.r01_paths.solution;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * SOLUTION du drill de rappel 1 - Path et File, sans toucher au disque.
 */
public class Recall01 {

    static String s(Path p) {
        return p.toString().replace('\\', '/');
    }

    public static void main(String[] args) {
        Path p = Path.of("a", "b", "c", "d.txt");
        System.out.println("D01 : " + s(p) + " " + p.getFileName() + " " + s(p.getParent()) + " " + p.getRoot() + " " + p.getNameCount() + " " + p.getName(0) + " "
                + s(p.subpath(1, 3)));
        System.out.println("D02 : " + s(Paths.get("x/./y/../z").normalize()) + " " + s(Path.of("../a/../../b").normalize()) + " " + s(Path.of("a/b/..").normalize())
                + " " + s(Path.of("./").normalize()).isEmpty());
        Path base = Path.of("projet/src");
        System.out.println("D03 : " + s(base.resolve("Main.java")) + " " + s(base.resolveSibling("test")) + " " + s(base.resolve("")) + " "
                + base.resolve(base.toAbsolutePath()).isAbsolute());
        System.out.println("D04 : " + s(Path.of("a/b").relativize(Path.of("a/c/d"))) + " " + s(Path.of("a/c/d").relativize(Path.of("a/b"))) + " "
                + s(Path.of("x").relativize(Path.of("x"))).isEmpty());
        System.out.println("D05 : " + p.startsWith("a") + " " + p.startsWith("a/b") + " " + p.startsWith("a/") + " " + p.endsWith("d.txt") + " " + p.endsWith(".txt")
                + " " + Path.of("a/b").equals(Path.of("a/./b")) + " " + Path.of("a/b").equals(Path.of("a/./b").normalize()));
        File f = new File("dossier", "fichier.txt");
        System.out.println("D06 : " + f.getName() + " " + f.getParent() + " " + f.exists() + " " + f.isAbsolute() + " " + s(f.toPath()) + " "
                + f.toPath().toFile().getName());
    }
}
