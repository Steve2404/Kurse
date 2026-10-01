package ch14_io.drills.exercises;

import ch14_io.ExerciseChecker;
import ch14_io.drills.Workspace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.List;

/**
 * DRILL 02 - Les operations de Files
 * ==================================
 *
 * Mode d'emploi : voir Drill01_PathApi. Chaque methode recoit root, la
 * racine d'un Workspace tout neuf (voir Workspace.java).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : readmeExists(root)      [Files.exists] docs/readme.txt existe ? -> true.
 * TODO 2  : logsIsDirectory(root)   [Files.isDirectory] logs ? -> true.
 * TODO 3  : readmeLines(root)       [Files.readAllLines] -> [Bibliotheque, Dune, Fondation].
 * TODO 4  : writeReport(root)       [createDirectories + writeString + readString] ecrire "ok" dans out/report.txt (out n'existe pas), puis le relire.
 * TODO 5  : appendToLog(root)       [writeString + StandardOpenOption.APPEND] ajouter "INFO fin\n" a logs/app.log ; rendre le nombre de lignes -> 6.
 * TODO 6  : copyReadme(root)        [Files.copy] docs/readme.txt -> docs/copy.txt ; rendre la taille de la copie -> 28.
 * TODO 7  : overwrite(root)         [copy + REPLACE_EXISTING] copier src/Main.java SUR docs/readme.txt (qui existe) ; rendre son contenu.
 * TODO 8  : moveTodo(root)          [Files.move] docs/notes/todo.txt -> docs/done.txt ; rendre "ancienExiste nouveauExiste" -> "false true".
 * TODO 9  : deleteTwice(root)       [delete + deleteIfExists] supprimer src/Main.java, puis deleteIfExists dessus : rendre ce que rend deleteIfExists -> false.
 * TODO 10 : readmeSize(root)        [Files.size] -> 28.
 * TODO 11 : touch(root, time)       [setLastModifiedTime + getLastModifiedTime] fixer la date de docs/readme.txt puis la relire.
 * TODO 12 : sameFile(root)          [Files.isSameFile] docs/../docs/readme.txt et docs/readme.txt -> true.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   exists / notExists / isDirectory / isRegularFile / isReadable / isHidden / isSameFile(a, b)
 *   createFile / createDirectory (parent obligatoire) / createDirectories (tout le chemin, jamais d'erreur s'il existe)
 *   copy(a, b, options) / move(a, b, options) : REPLACE_EXISTING, COPY_ATTRIBUTES, ATOMIC_MOVE
 *   delete (NoSuchFileException si absent, DirectoryNotEmptyException) / deleteIfExists -> boolean
 *   readString / readAllLines / readAllBytes ; writeString / write(path, lignes) ; options : CREATE, APPEND, CREATE_NEW, TRUNCATE_EXISTING
 *   size / getLastModifiedTime / setLastModifiedTime ; toutes lancent IOException (checked)
 * ---------------------------------------------------------------------
 */
public class Drill02_FilesOperations {

    public static boolean readmeExists(Path root) {
        throw new UnsupportedOperationException("TODO 1 : implementer readmeExists()");
    }

    public static boolean logsIsDirectory(Path root) {
        throw new UnsupportedOperationException("TODO 2 : implementer logsIsDirectory()");
    }

    public static List<String> readmeLines(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer readmeLines()");
    }

    public static String writeReport(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer writeReport()");
    }

    public static int appendToLog(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 5 : implementer appendToLog()");
    }

    public static long copyReadme(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 6 : implementer copyReadme()");
    }

    public static String overwrite(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 7 : implementer overwrite()");
    }

    public static String moveTodo(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 8 : implementer moveTodo()");
    }

    public static boolean deleteTwice(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 9 : implementer deleteTwice()");
    }

    public static long readmeSize(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 10 : implementer readmeSize()");
    }

    public static FileTime touch(Path root, FileTime time) throws IOException {
        throw new UnsupportedOperationException("TODO 11 : implementer touch()");
    }

    public static boolean sameFile(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 12 : implementer sameFile()");
    }

    public static void main(String[] args) throws IOException {
        Path root = Workspace.create();
        try {
            ExerciseChecker.check("1  readmeExists", readmeExists(root));
            ExerciseChecker.check("2  logsIsDirectory", logsIsDirectory(root));
            ExerciseChecker.check("3  readmeLines == [Bibliotheque, Dune, Fondation]", List.of("Bibliotheque", "Dune", "Fondation").equals(readmeLines(root)));
            ExerciseChecker.check("4  writeReport == ok", "ok".equals(writeReport(root)));
            ExerciseChecker.check("5  appendToLog == 6 lignes", appendToLog(root) == 6);
            ExerciseChecker.check("6  copyReadme == 28 octets", copyReadme(root) == 28);
            ExerciseChecker.check("10 readmeSize == 28 (avant ecrasement)", readmeSize(root) == 28);
            ExerciseChecker.check("7  overwrite == class Main {}", "class Main {}\n".equals(overwrite(root)));
            ExerciseChecker.check("8  moveTodo == false true", "false true".equals(moveTodo(root)));
            ExerciseChecker.check("9  deleteTwice == false", !deleteTwice(root) && !Files.exists(root.resolve("src/Main.java")));
            FileTime monday = FileTime.fromMillis(1_700_000_000_000L);
            ExerciseChecker.check("11 touch relit la date fixee", monday.equals(touch(root, monday)));
            ExerciseChecker.check("12 sameFile == true", sameFile(root));
        } finally {
            Workspace.delete(root);
        }

        ExerciseChecker.summary();
    }
}
