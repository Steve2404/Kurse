package ch14_io.drills.exercises;

import ch14_io.ExerciseChecker;
import ch14_io.drills.Workspace;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * DRILL 04 - java.io : octets, caracteres, tampons, PrintWriter, serialisation, mark/reset
 * ========================================================================================
 *
 * Mode d'emploi : voir Drill01_PathApi. Donnees : un Workspace neuf.
 * Toujours un try-with-resources : fermer le flux exterieur ferme toute la chaine.
 *
 *
 * -- Les TODO (classe visee entre crochets) --
 *
 * TODO 1  : writeThreeBytes(root)   [FileOutputStream] ecrire les octets 1, 2, 3 dans bytes.bin ; rendre sa taille -> 3.
 * TODO 2  : sumOfBytes(root)        [FileInputStream + read() jusqu'a -1] la somme des octets de bytes.bin -> 6 (apres TODO 1).
 * TODO 3  : readmeLineCount(root)   [BufferedReader + FileReader + readLine jusqu'a null] -> 3.
 * TODO 4  : writeWithBuffer(root)   [BufferedWriter + FileWriter + newLine] ecrire "a" et "b" dans w.txt ; rendre readAllLines.
 * TODO 5  : appendWithFileWriter(root) [new FileWriter(fichier, true)] ajouter "Hyperion\n" a docs/readme.txt ; rendre le nombre de lignes -> 4.
 * TODO 6  : formatted()             [PrintWriter + StringWriter + printf(Locale.US, ...)] "%.1f km" avec 12.46 -> "12.5 km".
 * TODO 7  : decodeUtf8()            [InputStreamReader + ByteArrayInputStream + UTF_8] lire le 1er caractere des octets de "été" -> 'é'.
 * TODO 8  : collectBytes()          [ByteArrayOutputStream] ecrire "ok" (en octets) puis toString() -> "ok".
 * TODO 9  : bookmarkCopy(bookmark)  [ObjectOutputStream / ObjectInputStream] aller-retour ; rendre "titre/page" -> "Dune/0" (page est transient).
 * TODO 10 : markReset(root)         [BufferedReader.mark / reset] lire 1 caractere de readme, mark(10), en lire 2, reset, en lire 1 : rendre les 4 caracteres lus -> "Bibi".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *                 octets                                caracteres
 *   bas niveau    FileInputStream / FileOutputStream    FileReader / FileWriter
 *   tampon        BufferedInputStream / BufferedOutput  BufferedReader (readLine) / BufferedWriter (newLine)
 *   objets        ObjectInputStream / ObjectOutputStream
 *   mise en forme PrintStream (System.out)              PrintWriter
 *   pont          InputStreamReader / OutputStreamWriter (octets <-> caracteres, avec un Charset)
 *   en memoire    ByteArrayInputStream / ByteArrayOutputStream   StringReader / StringWriter
 *
 *   read() rend un int (-1 a la fin) ; readLine() rend null a la fin ; new FileWriter(f, true) = ajout
 *   mark(limite) / reset() : revenir en arriere (markSupported() : true pour les Buffered...)
 *   flush() vide le tampon ; close() fait flush puis ferme
 * ---------------------------------------------------------------------
 */
public class Drill04_JavaIoStreams {

    public static class Bookmark implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String title;
        public transient int page;

        public Bookmark(String title, int page) {
            this.title = title;
            this.page = page;
        }
    }

    public static long writeThreeBytes(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer writeThreeBytes()");
    }

    public static int sumOfBytes(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer sumOfBytes()");
    }

    public static int readmeLineCount(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer readmeLineCount()");
    }

    public static List<String> writeWithBuffer(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer writeWithBuffer()");
    }

    public static int appendWithFileWriter(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 5 : implementer appendWithFileWriter()");
    }

    public static String formatted() {
        throw new UnsupportedOperationException("TODO 6 : implementer formatted()");
    }

    public static char decodeUtf8() throws IOException {
        throw new UnsupportedOperationException("TODO 7 : implementer decodeUtf8()");
    }

    public static String collectBytes() throws IOException {
        throw new UnsupportedOperationException("TODO 8 : implementer collectBytes()");
    }

    public static String bookmarkCopy(Bookmark bookmark) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("TODO 9 : implementer bookmarkCopy()");
    }

    public static String markReset(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 10 : implementer markReset()");
    }

    public static void main(String[] args) throws Exception {
        Path root = Workspace.create();
        try {
            ExerciseChecker.check("1  writeThreeBytes == 3", writeThreeBytes(root) == 3);
            ExerciseChecker.check("2  sumOfBytes == 6", sumOfBytes(root) == 6);
            ExerciseChecker.check("3  readmeLineCount == 3", readmeLineCount(root) == 3);
            ExerciseChecker.check("4  writeWithBuffer == [a, b]", List.of("a", "b").equals(writeWithBuffer(root)));
            ExerciseChecker.check("10 markReset == Bibi", "Bibi".equals(markReset(root)));
            ExerciseChecker.check("5  appendWithFileWriter == 4", appendWithFileWriter(root) == 4
                    && Files.readAllLines(root.resolve("docs/readme.txt")).get(3).equals("Hyperion"));
            ExerciseChecker.check("6  formatted == 12.5 km", "12.5 km".equals(formatted()));
            ExerciseChecker.check("7  decodeUtf8 == é", decodeUtf8() == 'é');
            ExerciseChecker.check("8  collectBytes == ok", "ok".equals(collectBytes()));
            ExerciseChecker.check("9  bookmarkCopy == Dune/0", "Dune/0".equals(bookmarkCopy(new Bookmark("Dune", 42))));
        } finally {
            Workspace.delete(root);
        }

        ExerciseChecker.summary();
    }
}
