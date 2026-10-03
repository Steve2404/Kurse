package ch14_io.projects.p02_backup;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BackupLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "source : [budget.csv, notes/idees.txt, notes/todo.txt, photos/2025/neige.jpg, photos/2026/montagne.jpg, photos/2026/plage.jpg] ; tailles par dossier {budget.csv=25, notes=43, photos=48}",
            "sauvegarde complete : 6 fichiers copies ; diff [inchange]",
            "apres modifications :",
            "  budget.csv : supprime",
            "  notes/budget.csv : ajoute",
            "  notes/courses.txt : ajoute",
            "  notes/idees.txt : inchange",
            "  notes/todo.txt : modifie (octet 26)",
            "  photos/2025/neige.jpg : supprime",
            "  photos/2026/montagne.jpg : inchange",
            "  photos/2026/plage.jpg : inchange",
            "increment : 3 fichiers [notes/budget.csv, notes/courses.txt, notes/todo.txt]",
            "  copy sur un fichier existant : FileAlreadyExistsException",
            "  delete d'un dossier non vide : DirectoryNotEmptyException",
            "  delete d'un absent : NoSuchFileException",
            "  createDirectory sans parent : NoSuchFileException",
            "  deleteIfExists d'un absent : false",
            "isSameFile true, equals false, size 27, isDirectory(notes) true, isRegularFile(notes) false, notExists true",
            "list [notes, photos] ; find *.txt [notes/courses.txt, notes/idees.txt, notes/todo.txt]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SANDBOX", "Data.FILES", "Files.walk(", "Comparator.reverseOrder()",
            "Files.delete(", "Files.createDirectories(", "Files.writeString(", "Files.copy(",
            "StandardCopyOption.REPLACE_EXISTING", "Files.move(", "Files.mismatch(", "Files.exists(",
            "Files.size(", "Files.isRegularFile", "Files.isDirectory(", "catch (FileAlreadyExistsException",
            "catch (DirectoryNotEmptyException", "catch (NoSuchFileException", "Files.createDirectory(", "Files.deleteIfExists(",
            "Files.isSameFile(", "Files.notExists(", "Files.list(", "Files.find(",
            "try (Stream<Path>",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BackupLab", args, EXPECTED, API);
    }
}
