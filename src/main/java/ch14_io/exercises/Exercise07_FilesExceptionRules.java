package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * EXERCICE 7 - Ce que lancent les methodes de Files : ta regle comparee a 21 executions reelles (niveau : difficile)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Toutes les methodes de Files lancent IOException (checked), mais
 * l'examen demande QUELLE sous-classe, et QUAND il n'y a pas d'erreur.
 * main() execute vraiment chaque cas dans un dossier temporaire.
 *
 * -- Les comportements reels (Java 17) --
 *
 *   createFile sur un fichier existant                      -> FileAlreadyExistsException
 *   createDirectory sans le parent / deja existant          -> NoSuchFileException / FileAlreadyExistsException
 *   createDirectories sans le parent / deja existant        -> OK / OK
 *   delete d'un absent / deleteIfExists d'un absent         -> NoSuchFileException / OK (rend false)
 *   delete d'un dossier non vide                            -> DirectoryNotEmptyException
 *   copy ou move vers une cible existante                   -> FileAlreadyExistsException (OK avec REPLACE_EXISTING)
 *   copy, readAllLines, size, newBufferedReader d'un absent -> NoSuchFileException
 *   writeString avec CREATE_NEW sur un existant             -> FileAlreadyExistsException
 *   writeString avec APPEND (sans CREATE) sur un absent     -> NoSuchFileException
 *   writeString dans un dossier parent absent               -> NoSuchFileException
 *   list sur un fichier                                     -> NotDirectoryException
 *   isSameFile(p, p) sur un absent                          -> OK (meme chemin : true sans lire le disque)
 *   isSameFile(p, q) sur deux absents differents            -> NoSuchFileException
 *   move (renommer) d'un dossier non vide                   -> OK
 *
 *
 * ==================================================================
 * TODO 1 : outcome(method, situation)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * method : "createFile", "createDirectory", "createDirectories", "delete",
 * "deleteIfExists", "copy", "copy+REPLACE_EXISTING", "move", "readAllLines",
 * "size", "writeString", "writeString+CREATE_NEW", "writeString+APPEND",
 * "list", "isSameFile". situation : "targetExists", "missing",
 * "missingParent", "nonEmptyDir", "onFile", "samePathMissing",
 * "differentMissing". Rendre "OK" ou le nom simple de l'exception.
 *
 * -- Le plan (des REGLES, pas un tableau a apprendre) --
 *
 *   1. "nonEmptyDir" : delete -> DirectoryNotEmptyException ; move -> OK.
 *   2. "onFile" -> NotDirectoryException.
 *   3. "samePathMissing" -> OK ; "differentMissing" -> NoSuchFileException.
 *   4. "missingParent" : createDirectories -> OK ; sinon NoSuchFileException.
 *   5. "targetExists" : createDirectories ou une option REPLACE_EXISTING -> OK ; sinon FileAlreadyExistsException.
 *   6. "missing" : deleteIfExists -> OK ; sinon NoSuchFileException.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : ensureFile(path)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Garantir qu'un fichier existe (avec tous ses dossiers parents), SANS
 * erreur s'il existe deja. Rendre true s'il a ete cree, false s'il
 * existait deja.
 *
 * -- Le plan --
 *
 *   1. createDirectories du parent (jamais d'erreur s'il existe).
 *   2. Si le fichier existe -> false ; sinon createFile -> true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : deleteTree(root)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * delete refuse un dossier non vide : il faut vider d'abord, en partant
 * des FEUILLES. Rendre le nombre d'entrees supprimees (0 si root n'existe pas).
 *
 * -- Le plan --
 *
 *   1. root absent -> 0.
 *   2. Files.walk(root) (dans un try-with-resources), trie a l'ENVERS (les plus longs chemins d'abord).
 *   3. delete chacun, en comptant.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - method.contains("REPLACE_EXISTING")
 *   - Files.createDirectories(path.getParent()) ; Files.exists(path)
 *   - stream.sorted(Comparator.reverseOrder()) : un enfant ("a/b") passe avant son parent ("a").
 *   - Files.walk lance IOException (checked) ; delete dans une lambda doit l'attraper : une boucle for
 *     sur stream.sorted(...).toList() est plus simple.
 */
public class Exercise07_FilesExceptionRules {

    public static String outcome(String method, String situation) {
        throw new UnsupportedOperationException("TODO 1 : implementer outcome()");
    }

    public static boolean ensureFile(Path path) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer ensureFile()");
    }

    public static int deleteTree(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer deleteTree()");
    }

    public static void main(String[] args) throws IOException {
        String[][] cases = {
                {"createFile", "targetExists"}, {"createDirectory", "missingParent"}, {"createDirectories", "missingParent"},
                {"createDirectory", "targetExists"}, {"createDirectories", "targetExists"}, {"delete", "missing"},
                {"deleteIfExists", "missing"}, {"delete", "nonEmptyDir"}, {"copy", "targetExists"},
                {"copy+REPLACE_EXISTING", "targetExists"}, {"move", "targetExists"}, {"copy", "missing"},
                {"readAllLines", "missing"}, {"size", "missing"}, {"writeString+CREATE_NEW", "targetExists"},
                {"writeString+APPEND", "missing"}, {"writeString", "missingParent"}, {"list", "onFile"},
                {"isSameFile", "samePathMissing"}, {"isSameFile", "differentMissing"}, {"move", "nonEmptyDir"}};
        int agree = 0;
        String miss = "";
        for (String[] c : cases) {
            String real = jvm(c[0], c[1]);
            String mine = outcome(c[0], c[1]);
            if (mine.equals(real)) {
                agree++;
            } else if (miss.isEmpty()) {
                miss = " ; 1er ecart : " + c[0] + "/" + c[1] + " -> " + mine + " au lieu de " + real;
            }
        }
        ExerciseChecker.check("outcome == JVM sur 21 cas (" + agree + " d'accord)" + miss, agree == 21);

        Path dir = Files.createTempDirectory("ex07");
        try {
            Path file = dir.resolve("a/b/c/notes.txt");
            ExerciseChecker.check("ensureFile : cree a/b/c/notes.txt (true), puis false sans erreur la 2e fois",
                    ensureFile(file) && Files.isRegularFile(file) && !ensureFile(file));
            Files.writeString(dir.resolve("a/b/autre.txt"), "x");
            ExerciseChecker.check("deleteTree(a) supprime 5 entrees (a, b, c, notes.txt, autre.txt) ; absent -> 0",
                    deleteTree(dir.resolve("a")) == 5 && !Files.exists(dir.resolve("a")) && deleteTree(dir.resolve("a")) == 0);
        } finally {
            clean(dir);
        }

        ExerciseChecker.summary();
    }

    // ---- Le juge : execute VRAIMENT chaque cas dans un dossier temporaire neuf (ne pas modifier) ----

    static String jvm(String method, String situation) throws IOException {
        Path d = Files.createTempDirectory("ex07-judge");
        try {
            Path a = d.resolve("a");
            Path b = d.resolve("b");
            switch (situation) {
                case "targetExists":
                    Files.writeString(a, "A");
                    Files.writeString(b, "B");
                    if (method.startsWith("createDirector")) {
                        Files.createDirectory(d.resolve("x"));
                    }
                    break;
                case "nonEmptyDir":
                    Files.createDirectories(d.resolve("x"));
                    Files.writeString(d.resolve("x/f"), "F");
                    break;
                case "onFile":
                    Files.writeString(a, "A");
                    break;
                default:
                    break;
            }
            Path missing = d.resolve("absent");
            Path deep = d.resolve("x/y");
            switch (method) {
                case "createFile":
                    Files.createFile(b);
                    break;
                case "createDirectory":
                    Files.createDirectory(situation.equals("missingParent") ? deep : d.resolve("x"));
                    break;
                case "createDirectories":
                    Files.createDirectories(situation.equals("missingParent") ? deep : d.resolve("x"));
                    break;
                case "delete":
                    Files.delete(situation.equals("missing") ? missing : d.resolve("x"));
                    break;
                case "deleteIfExists":
                    Files.deleteIfExists(missing);
                    break;
                case "copy":
                    Files.copy(situation.equals("missing") ? missing : a, b);
                    break;
                case "copy+REPLACE_EXISTING":
                    Files.copy(a, b, StandardCopyOption.REPLACE_EXISTING);
                    break;
                case "move":
                    if (situation.equals("nonEmptyDir")) {
                        Files.move(d.resolve("x"), d.resolve("z"));
                    } else {
                        Files.move(a, b);
                    }
                    break;
                case "readAllLines":
                    Files.readAllLines(missing);
                    break;
                case "size":
                    Files.size(missing);
                    break;
                case "writeString":
                    Files.writeString(d.resolve("x/y.txt"), "B");
                    break;
                case "writeString+CREATE_NEW":
                    Files.writeString(a, "B", StandardOpenOption.CREATE_NEW);
                    break;
                case "writeString+APPEND":
                    Files.writeString(missing, "B", StandardOpenOption.APPEND);
                    break;
                case "list":
                    try (Stream<Path> s = Files.list(a)) {
                        s.count();
                    }
                    break;
                default:
                    Files.isSameFile(missing, situation.equals("samePathMissing") ? missing : d.resolve("absent2"));
                    break;
            }
            return "OK";
        } catch (IOException e) {
            return e.getClass().getSimpleName();
        } finally {
            clean(d);
        }
    }

    static void clean(Path root) throws IOException {
        if (Files.exists(root)) {
            try (Stream<Path> s = Files.walk(root)) {
                for (Path p : s.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
    }
}
