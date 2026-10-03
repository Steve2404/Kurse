package ch14_io.projects.p07_vcs;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON MiniGit, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "commit 1 \"premiere version\" (parent 0) : [courses.txt, recette.txt], objets stockes 2",
            "status : {courses.txt=supprime, notes.txt=ajoute, recette.txt=modifie}",
            "commit 2 \"ajout vanille\" (parent 1) : [notes.txt, recette.txt], objets stockes 4",
            "status : {recette.txt=modifie}",
            "commit 3 \"sans oeufs\" (parent 2) : [notes.txt, recette.txt], objets stockes 5",
            "log : 3 \"sans oeufs\" 2 \"ajout vanille\" 1 \"premiere version\"",
            "diff 1 -> 3 recette.txt :",
            "    farine",
            "  - oeufs",
            "  - lait",
            "  + lait entier",
            "    sucre",
            "  + vanille",
            "  + sel",
            "checkout 1 : fichiers [courses.txt, recette.txt], HEAD 1",
            "status : {}",
            "cat recette.txt : [farine, oeufs, lait, sucre]",
            "checkout 3 : fichiers [notes.txt, recette.txt], HEAD 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SANDBOX", "Data.SCRIPT", "record Commit(", "implements Serializable",
            "Files.newInputStream(", "Files.newOutputStream(", "new ObjectOutputStream(", "new ObjectInputStream(",
            "Files.readAllBytes(", "Files.copy(", "StandardCopyOption.REPLACE_EXISTING", "Files.writeString(",
            "Files.readString(", "Files.readAllLines(", "Files.write(", "Files.walk(",
            "Files.list(", "Files.delete(", "new int[",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "MiniGit", args, EXPECTED, API);
    }
}
