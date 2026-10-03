package ch14_io.projects.p06_duplicates;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Dedup, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "parcours : 7 fichiers, [saute cache, fin du parcours] ; occupation (octets) {.=115, docs=51, docs/archives=34, musique=12, photos=52}",
            "attributs de photos/plage.jpg : taille 17, fichier true, dossier false, lien false, modifie 2026-07-14T10:00:00Z ; getAttribute(\"size\") 17",
            "apres setTimes : 2026-12-25T00:00:00Z ; vue basic",
            "plus recents : [photos/plage.jpg, photos/montagne.jpg, photos/copie de plage.jpg]",
            "doublons : [docs/archives/cv-final.pdf, docs/cv.pdf]",
            "doublons : [photos/copie de plage.jpg, photos/plage.jpg]",
            "2 groupes, 34 octets recuperables (dossier cache ignore)",
            "walk(racine, 2) par profondeur {0=1, 1=4, 2=8}");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SANDBOX", "Data.FILES", "Data.SKIPPED", "extends SimpleFileVisitor<Path>",
            "preVisitDirectory(", "visitFile(", "postVisitDirectory(", "FileVisitResult.SKIP_SUBTREE",
            "FileVisitResult.CONTINUE", "Files.walkFileTree(", "Files.setLastModifiedTime(", "FileTime.from(",
            "Files.readAttributes(", "BasicFileAttributes", ".lastModifiedTime()", "Files.getAttribute(",
            "BasicFileAttributeView", ".setTimes(", "Files.getLastModifiedTime(", "Files.mismatch(",
            "Files.walk(root, 2)",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Dedup", args, EXPECTED, API);
    }
}
