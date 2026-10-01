package ch14_io.drills.exercises;

import ch14_io.ExerciseChecker;
import ch14_io.drills.Workspace;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * DRILL 03 - Files et les Stream : list, walk, find, lines, readers et writers bufferises
 * =======================================================================================
 *
 * Mode d'emploi : voir Drill01_PathApi. Donnees : un Workspace neuf
 * (11 entrees, 5 fichiers). Chaque Stream de Files se ferme : try-with-resources.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : docsNames(root)        [Files.list] les noms des enfants DIRECTS de docs, tries -> [notes, readme.txt].
 * TODO 2  : entryCount(root)       [Files.walk] toutes les entrees, root compris -> 11.
 * TODO 3  : topLevelCount(root)    [Files.walk(root, 1)] profondeur 1 -> 4 (root, docs, logs, src).
 * TODO 4  : logCount(root)         [Files.find] les fichiers .log -> 2.
 * TODO 5  : appLogLines(root)      [Files.lines + count] logs/app.log -> 5.
 * TODO 6  : errors(root)           [Files.lines + filter] les lignes de logs/app.log qui commencent par ERROR.
 * TODO 7  : firstReadmeLine(root)  [Files.newBufferedReader + readLine] -> Bibliotheque.
 * TODO 8  : writeTwoLines(root)    [Files.newBufferedWriter + newLine] ecrire "un" et "deux" dans out.txt ; rendre readAllLines.
 * TODO 9  : allFiles(root)         [walk + isRegularFile + relativize] les 5 fichiers, relatifs a root, avec "/", tries.
 * TODO 10 : biggestFile(root)      [walk + size] le chemin relatif du plus gros fichier -> logs/app.log.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   try (Stream<Path> s = Files.list(dir))        un niveau, sans dir lui-meme
 *   try (Stream<Path> s = Files.walk(dir [, max]))  tout l'arbre (profondeur), dir compris ; max = profondeur maximale
 *   try (Stream<Path> s = Files.find(dir, max, (path, attrs) -> ...))  walk + filtre avec les attributs
 *   try (Stream<String> s = Files.lines(fichier))  paresseux ; readAllLines charge tout
 *   Files.newBufferedReader(p) / newBufferedWriter(p, options) : rapides et en UTF-8 par defaut
 *   les lambdas de stream ne peuvent pas lancer IOException : Files.size dans un map doit etre attrape
 * ---------------------------------------------------------------------
 */
public class Drill03_FilesStreams {

    public static List<String> docsNames(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer docsNames()");
    }

    public static long entryCount(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer entryCount()");
    }

    public static long topLevelCount(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer topLevelCount()");
    }

    public static long logCount(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer logCount()");
    }

    public static long appLogLines(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 5 : implementer appLogLines()");
    }

    public static List<String> errors(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 6 : implementer errors()");
    }

    public static String firstReadmeLine(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 7 : implementer firstReadmeLine()");
    }

    public static List<String> writeTwoLines(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 8 : implementer writeTwoLines()");
    }

    public static List<String> allFiles(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 9 : implementer allFiles()");
    }

    public static String biggestFile(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 10 : implementer biggestFile()");
    }

    public static void main(String[] args) throws IOException {
        Path root = Workspace.create();
        try {
            ExerciseChecker.check("1  docsNames == [notes, readme.txt]", List.of("notes", "readme.txt").equals(docsNames(root)));
            ExerciseChecker.check("2  entryCount == 11", entryCount(root) == 11);
            ExerciseChecker.check("3  topLevelCount == 4", topLevelCount(root) == 4);
            ExerciseChecker.check("4  logCount == 2", logCount(root) == 2);
            ExerciseChecker.check("5  appLogLines == 5", appLogLines(root) == 5);
            ExerciseChecker.check("6  errors == [ERROR paiement refuse, ERROR delai depasse]",
                    List.of("ERROR paiement refuse", "ERROR delai depasse").equals(errors(root)));
            ExerciseChecker.check("7  firstReadmeLine == Bibliotheque", "Bibliotheque".equals(firstReadmeLine(root)));
            ExerciseChecker.check("10 biggestFile == logs/app.log", "logs/app.log".equals(biggestFile(root)));
            ExerciseChecker.check("9  allFiles == les 5 fichiers",
                    List.of("docs/notes/todo.txt", "docs/readme.txt", "logs/app.log", "logs/old/app-1.log", "src/Main.java").equals(allFiles(root)));
            ExerciseChecker.check("8  writeTwoLines == [un, deux]", List.of("un", "deux").equals(writeTwoLines(root)));
        } finally {
            Workspace.delete(root);
        }

        ExerciseChecker.summary();
    }
}
