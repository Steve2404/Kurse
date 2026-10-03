package ch14_io.drills.r02_files;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : true true true 7 bonjour",
            "D02 : FileAlreadyExistsException bonsoir -1 -1",
            "D03 : false true true",
            "D04 : DirectoryNotEmptyException NoSuchFileException false true",
            "D05 : NoSuchFileException FileAlreadyExistsException true",
            "D06 : true true NoSuchFileException | 3 3 abc | true 1 g.txt true false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Files.createDirectories(", "Files.writeString(", "Files.readString(", "Files.copy(",
            "StandardCopyOption.REPLACE_EXISTING", "Files.mismatch(", "Files.move(", "Files.isSameFile(",
            "Files.delete(", "Files.deleteIfExists(", "Files.createDirectory(", "catch (FileAlreadyExistsException",
            "catch (DirectoryNotEmptyException", "catch (NoSuchFileException", ".toRealPath()", "new ByteArrayInputStream(",
            ".renameTo(", ".listFiles()", ".list()",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
