package ch14_io.drills.solutions;

import java.nio.file.Path;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.drills.exercises.Drill01_PathApi.
 */
public class SolutionDrill01_PathApi {

    public static Path build() {
        // Path.of assemble avec le separateur du systeme.
        return Path.of("docs", "notes", "todo.txt");
    }

    public static Path fileName(Path p) {
        // Le dernier nom, sous forme de Path.
        return p.getFileName();
    }

    public static Path parent(Path p) {
        // null quand il n'y a qu'un nom (aucun parent ecrit dans le chemin).
        return p.getParent();
    }

    public static int count(Path p) {
        // Le nombre de noms (la racine ne compte pas).
        return p.getNameCount();
    }

    public static Path second(Path p) {
        // Les indices commencent a 0.
        return p.getName(1);
    }

    public static Path firstTwo(Path p) {
        // Fin EXCLUE, comme substring.
        return p.subpath(0, 2);
    }

    public static Path sibling(Path p) {
        // resolveSibling = getParent().resolve(nom).
        return p.resolveSibling("index.txt");
    }

    public static Path intoOld() {
        // resolve colle les deux chemins (relatifs).
        return Path.of("logs").resolve("old/app-1.log");
    }

    public static Path fromNotesToLog() {
        // Remonter deux fois (notes, docs) puis descendre vers logs.
        return Path.of("docs/notes").relativize(Path.of("logs/app.log"));
    }

    public static Path clean() {
        // normalize enleve "." et "notes/.." sans consulter le disque.
        return Path.of("docs/./notes/../readme.txt").normalize();
    }

    public static String absoluteOrNot() {
        // toAbsolutePath colle le dossier courant devant : le resultat est absolu.
        Path x = Path.of("x");
        return x.isAbsolute() + " " + x.toAbsolutePath().isAbsolute();
    }

    public static String ends() {
        // endsWith compare des NOMS : "todo" n'est pas le nom "todo.txt".
        Path p = Path.of("docs/notes/todo.txt");
        return p.endsWith("notes/todo.txt") + " " + p.endsWith("todo");
    }
}
