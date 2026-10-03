package ch14_io.projects.p05_sort;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON SortLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "fichier : 60000 lignes, 622 scores >= 9900 ; par millier {0=6010, 1=5990, 2=5984, 3=5995, 4=5991, 5=5989, 6=6006, 7=6015, 8=6006, 9=6014}",
            "tri externe : 9 paquets de 7000 lignes max, fusion de 60000 lignes, identique au tri en memoire true",
            "podium : [joueur25366;9999, joueur39869;9999, joueur50438;9999] ; dernier joueur82825;0",
            "resume : [lignes=60000, paquets=9, meilleur=joueur25366;9999] ; CREATE_NEW sur un fichier existant FileAlreadyExistsException ; readString 3 lignes");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SANDBOX", "Data.LINES", "Data.CHUNK", "Data.line(",
            "Files.newBufferedWriter(", "Files.newBufferedReader(", "Files.lines(", "Stream<String>",
            "Files.readAllLines(", "Files.write(", "Files.writeString(", "StandardOpenOption.APPEND",
            "StandardOpenOption.CREATE_NEW", "catch (FileAlreadyExistsException", "Files.readString(", "PriorityQueue<",
            "Collectors.groupingBy(",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "SortLab", args, EXPECTED, API);
    }
}
