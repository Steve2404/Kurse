package ch14_io.drills.exercises;

import ch14_io.ExerciseChecker;
import ch14_io.drills.Workspace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * DRILL 05 - Kata melange : tout le chapitre 14 sans indice de forme
 * ==================================================================
 *
 * Mode d'emploi : voir Drill01_PathApi. Ici, PAS de crochet : a toi de
 * choisir entre Path, Files (operations ou Stream) et java.io. Chaque TODO
 * recoit un Workspace NEUF.
 *
 *
 * -- Les TODO --
 *
 * TODO 1 : totalLogLines(root)     le nombre total de lignes de tous les fichiers .log -> 6.
 * TODO 2 : countByExtension(root)  extension -> nombre de fichiers, cles triees -> {java=1, log=2, txt=2}.
 * TODO 3 : archiveLogs(root)       deplacer tous les .log dans root/archive (a creer) ; rendre les noms de archive tries -> [app-1.log, app.log].
 * TODO 4 : backupDocs(root)        recopier toute l'arborescence docs dans root/backup ; rendre le nombre de fichiers de backup -> 2.
 * TODO 5 : grep(root, word)        les chemins relatifs (avec "/", tries) des fichiers dont une ligne contient word.
 * TODO 6 : writeReport(root)       ecrire report.txt avec une ligne "ext=nombre" par extension (TODO 2, dans l'ordre) avec un PrintWriter ; rendre ses lignes.
 *                                  (attention : report.txt lui-meme ne doit pas etre compte.)
 * TODO 7 : saveAndLoadIndex(root)  chemin relatif -> taille de chaque fichier ; serialiser la Map dans index.ser, la relire ; rendre la Map relue.
 * TODO 8 : wordCount(root)         le nombre total de mots (separes par des espaces) dans les fichiers .txt -> 5.
 */
public class Drill05_MixedKata {

    public static long totalLogLines(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer totalLogLines()");
    }

    public static Map<String, Long> countByExtension(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer countByExtension()");
    }

    public static List<String> archiveLogs(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer archiveLogs()");
    }

    public static long backupDocs(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer backupDocs()");
    }

    public static List<String> grep(Path root, String word) throws IOException {
        throw new UnsupportedOperationException("TODO 5 : implementer grep()");
    }

    public static List<String> writeReport(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 6 : implementer writeReport()");
    }

    public static Map<String, Long> saveAndLoadIndex(Path root) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("TODO 7 : implementer saveAndLoadIndex()");
    }

    public static long wordCount(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 8 : implementer wordCount()");
    }

    public static void main(String[] args) throws Exception {
        ExerciseChecker.check("1  totalLogLines == 6", check(root -> totalLogLines(root) == 6));
        ExerciseChecker.check("2  countByExtension == {java=1, log=2, txt=2}", check(root -> "{java=1, log=2, txt=2}".equals(String.valueOf(countByExtension(root)))));
        ExerciseChecker.check("3  archiveLogs == [app-1.log, app.log] (et plus aucun .log dans logs)",
                check(root -> List.of("app-1.log", "app.log").equals(archiveLogs(root)) && !Files.exists(root.resolve("logs/app.log"))));
        ExerciseChecker.check("4  backupDocs == 2 fichiers (et backup/notes/todo.txt existe)",
                check(root -> backupDocs(root) == 2 && Files.exists(root.resolve("backup/notes/todo.txt"))));
        ExerciseChecker.check("5  grep(Dune) == [docs/readme.txt] ; grep(INFO) == [logs/app.log, logs/old/app-1.log]",
                check(root -> List.of("docs/readme.txt").equals(grep(root, "Dune"))
                        && List.of("logs/app.log", "logs/old/app-1.log").equals(grep(root, "INFO"))));
        ExerciseChecker.check("6  writeReport == [java=1, log=2, txt=2]", check(root -> List.of("java=1", "log=2", "txt=2").equals(writeReport(root))));
        ExerciseChecker.check("7  saveAndLoadIndex : 5 fichiers, docs/readme.txt -> 28 octets",
                check(root -> {
                    Map<String, Long> index = saveAndLoadIndex(root);
                    return index.size() == 5 && Long.valueOf(28).equals(index.get("docs/readme.txt"));
                }));
        ExerciseChecker.check("8  wordCount == 5", check(root -> wordCount(root) == 5));

        ExerciseChecker.summary();
    }

    interface Test {
        boolean run(Path root) throws Exception;
    }

    // Chaque test sur un Workspace neuf, toujours supprime ensuite.
    static boolean check(Test test) throws Exception {
        Path root = Workspace.create();
        try {
            return test.run(root);
        } finally {
            Workspace.delete(root);
        }
    }
}
