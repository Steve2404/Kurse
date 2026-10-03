package ch14_io.projects.p07_vcs.solution;

import ch14_io.projects.p07_vcs.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 7 (capstone) - un mini gestionnaire de versions : fichiers, flux d'objets, parcours, diff.
 */
public class MiniGit {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path work = Path.of(Data.SANDBOX);
        if (Files.exists(work)) {
            try (Stream<Path> all = Files.walk(work)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        Repository repo = new Repository(work);
        for (String command : Data.SCRIPT) {
            String[] p = command.split(" ", 3);
            switch (p[0]) {
                case "write" -> Files.write(work.resolve(p[1]), List.of(p[2].split("/")));
                case "delete" -> Files.delete(work.resolve(p[1]));
                case "commit" -> {
                    Commit c = repo.commit(command.substring(7));
                    System.out.println("commit " + c.id() + " \"" + c.message() + "\" (parent " + c.parent() + ") : " + c.snapshot().keySet() + ", objets stockes "
                            + repo.objectCount());
                }
                case "status" -> System.out.println("status : " + repo.status());
                case "log" -> {
                    StringBuilder log = new StringBuilder("log :");
                    for (int id = repo.head(); id != 0; id = repo.load(id).parent()) {
                        log.append(" ").append(id).append(" \"").append(repo.load(id).message()).append('"');
                    }
                    System.out.println(log);
                }
                case "diff" -> {
                    String[] q = p[2].split(" ");
                    System.out.println("diff " + p[1] + " -> " + q[0] + " " + q[1] + " :");
                    for (String line : Repository.diff(repo.contentAt(Integer.parseInt(p[1]), q[1]), repo.contentAt(Integer.parseInt(q[0]), q[1]))) {
                        System.out.println("  " + line);
                    }
                }
                case "checkout" -> {
                    repo.checkout(Integer.parseInt(p[1]));
                    System.out.println("checkout " + p[1] + " : fichiers " + repo.scan().keySet() + ", HEAD " + repo.head());
                }
                default -> System.out.println("cat " + p[1] + " : " + Files.readAllLines(work.resolve(p[1])));
            }
        }
    }
}
