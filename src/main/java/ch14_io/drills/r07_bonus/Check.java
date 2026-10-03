package ch14_io.drills.r07_bonus;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (bonus) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : coherent true, walk avec FOLLOW_LINKS voit plus true",
            "D02 : posix coherent true, vue basic toujours presente true",
            "D03 : 7 true 2",
            "D04 : entree simulee Ada true efface true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Files.createSymbolicLink(", "Files.isSymbolicLink(", "Files.readSymbolicLink(", "LinkOption.NOFOLLOW_LINKS",
            "FileVisitOption.FOLLOW_LINKS", "catch (FileSystemException | UnsupportedOperationException", ".supportedFileAttributeViews()", "PosixFileAttributes",
            "PosixFilePermissions.toString(", "Files.readAttributes(", "\"basic:size,isRegularFile\"", "System.console()",
            ".readLine(", ".readPassword(", ".writer()", "Arrays.fill(",
            "char[]",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
