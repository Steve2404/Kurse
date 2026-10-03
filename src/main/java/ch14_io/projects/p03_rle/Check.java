package ch14_io.projects.p03_rle;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON StreamLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "dossier cree true, existe true, isDirectory true, nom p03_rle",
            "RLE : 59800 -> 922 octets (1 %), restaure identique true",
            "Adler-32 maison bb8f19b3, JDK bb8f19b3, identiques true",
            "rapport : 5 lignes, derniere \"total;;24.10\"",
            "encodages : 10 caracteres, UTF-8 14 octets, ISO-8859-1 10 octets ; UTF-8 relu en ISO-8859-1 : 14 caracteres, identique false",
            "mark/reset : markSupported true, lecture artt|5|q",
            "DataStream : 2026 3.5 fin, 17 octets, puis EOFException");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SANDBOX", "Data.RUNS", "Data.ITEMS", "Data.ACCENTS",
            "new File(", ".mkdirs()", "new BufferedOutputStream(new FileOutputStream(", "new BufferedInputStream(new FileInputStream(",
            ".read()", "re:\\(\\w+ = in\\.read\\(\\)\\) != -1##(b = in.read()) != -1", ".read(buffer)", "& 0xFF",
            "new BufferedWriter(new FileWriter(", ".newLine()", "new PrintWriter(new FileWriter(", ", true)",
            ".printf(", "new BufferedReader(new FileReader(", ".readLine()", "new OutputStreamWriter(",
            "new InputStreamReader(", "StandardCharsets.UTF_8", "StandardCharsets.ISO_8859_1", ".mark(",
            ".reset()", ".skip(", ".markSupported()", "new DataOutputStream(",
            "new DataInputStream(", "catch (EOFException", "Adler32",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "StreamLab", args, EXPECTED, API);
    }
}
