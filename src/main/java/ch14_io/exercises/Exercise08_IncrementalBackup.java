package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * EXERCICE 8 - Sauvegarde incrementale : walk, date de modification, copy REPLACE_EXISTING (niveau : avance)
 * ==========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une sauvegarde complete recopie TOUT a chaque fois. Une sauvegarde
 * INCREMENTALE ne recopie que ce qui a change : un fichier absent de la
 * sauvegarde, ou plus RECENT (date de modification) que sa copie.
 *
 *   source/                 backup/
 *     notes.txt  (lundi)      notes.txt (lundi)   -> rien a faire
 *     todo.txt   (mardi)      todo.txt  (lundi)   -> a recopier (plus recent)
 *     docs/plan.txt           (absent)            -> a recopier (nouveau)
 *
 * Les chemins rendus sont RELATIFS a la source, ecrits avec "/" et
 * tries (ex. "docs/plan.txt").
 *
 *
 * ==================================================================
 * TODO 1 : backup(source, target)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Files.walk(source) (try-with-resources), en ne gardant que les fichiers (Files.isRegularFile).
 *   2. Pour chacun : relatif = source.relativize(fichier) ; copie = target.resolve(relatif).
 *   3. Si la copie n'existe pas, ou si getLastModifiedTime(fichier) est APRES celui de la copie :
 *      createDirectories(copie.getParent()), copy(fichier, copie, REPLACE_EXISTING, COPY_ATTRIBUTES),
 *      et noter le relatif.
 *   4. Rendre la liste triee.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2) : "le chemin relatif ecrit avec /" sert aux TODO 1 et 2.
 * COPY_ATTRIBUTES garde la date de modification : sans lui, la copie
 * serait datee d'AUJOURD'HUI et paraitrait toujours plus recente.
 *
 *
 * ==================================================================
 * TODO 2 : prune(source, target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un fichier supprime de la source doit disparaitre de la sauvegarde.
 * Supprimer les FICHIERS de target qui n'existent plus dans source, et
 * rendre leurs chemins relatifs tries.
 *
 * -- Le plan --
 *
 *   1. Files.walk(target), fichiers seulement.
 *   2. Si source.resolve(target.relativize(fichier)) n'existe pas : delete, et noter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : la meme boite "relatif avec /".
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - FileTime t = Files.getLastModifiedTime(p) ; t.compareTo(autre) > 0 -> plus recent.
 *   - Files.copy(a, b, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES)
 *   - for (Path p : stream.filter(Files::isRegularFile).toList()) { ... } (la lambda ne lance pas IOException)
 *   - relatif.toString().replace('\\', '/')
 */
public class Exercise08_IncrementalBackup {

    public static List<String> backup(Path source, Path target) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer backup()");
    }

    public static List<String> prune(Path source, Path target) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer prune()");
    }

    public static void main(String[] args) throws IOException {
        Path root = Files.createTempDirectory("ex08");
        try {
            Path src = root.resolve("source");
            Path bak = root.resolve("backup");
            write(src.resolve("notes.txt"), "notes", 100);
            write(src.resolve("todo.txt"), "todo", 100);
            write(src.resolve("docs/plan.txt"), "plan", 100);

            ExerciseChecker.check("1re sauvegarde : tout est copie -> [docs/plan.txt, notes.txt, todo.txt]",
                    backup(src, bak).equals(List.of("docs/plan.txt", "notes.txt", "todo.txt"))
                            && Files.readString(bak.resolve("docs/plan.txt")).equals("plan"));
            ExerciseChecker.check("2e sauvegarde sans changement : rien a copier (COPY_ATTRIBUTES a garde les dates)",
                    backup(src, bak).isEmpty());

            write(src.resolve("todo.txt"), "todo v2", 200);
            write(src.resolve("docs/new.txt"), "nouveau", 100);
            ExerciseChecker.check("3e sauvegarde : seulement le fichier modifie et le nouveau -> [docs/new.txt, todo.txt]",
                    backup(src, bak).equals(List.of("docs/new.txt", "todo.txt"))
                            && Files.readString(bak.resolve("todo.txt")).equals("todo v2"));

            Files.delete(src.resolve("notes.txt"));
            ExerciseChecker.check("prune : notes.txt a disparu de la source -> supprime de la sauvegarde",
                    prune(src, bak).equals(List.of("notes.txt")) && !Files.exists(bak.resolve("notes.txt"))
                            && prune(src, bak).isEmpty());
        } finally {
            clean(root);
        }

        ExerciseChecker.summary();
    }

    // Ecrit un fichier (avec ses dossiers) et fixe sa date de modification (en secondes depuis 1970) : tests deterministes.
    static void write(Path file, String text, long epochSeconds) throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, text);
        Files.setLastModifiedTime(file, FileTime.from(Instant.ofEpochSecond(epochSeconds)));
    }

    static void clean(Path root) throws IOException {
        try (Stream<Path> s = Files.walk(root)) {
            for (Path p : s.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(p);
            }
        }
    }
}
