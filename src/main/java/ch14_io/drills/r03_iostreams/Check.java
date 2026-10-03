package ch14_io.drills.r03_iostreams;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 65 66 67 68 255 | 5",
            "D02 : 3 trois",
            "D03 : a1/ 3.14|ok  |007/true",
            "D04 : 2 1 true 2",
            "D05 : ABCB2E true true",
            "D06 : capture true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new FileOutputStream(", "new BufferedInputStream(new FileInputStream(", ".read()", "new BufferedWriter(new FileWriter(",
            "new FileWriter(txt, true)", "new BufferedReader(new FileReader(", ".readLine()", "new PrintWriter(",
            ".printf(", ".format(", "StringWriter", ".getBytes(StandardCharsets.UTF_8)",
            ".mark(", ".reset()", ".skip(", ".markSupported()",
            "new PrintStream(",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
