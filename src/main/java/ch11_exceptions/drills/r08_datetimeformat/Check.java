package ch11_exceptions.drills.r08_datetimeformat;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 2026-07-04 15:05:09 | 4/7/26 3:5 PM | Jul July Sat Saturday",
            "D02 : samedi 4 juillet | samedi 4 juillet | Samstag 4 Juli",
            "D03 : Le 04 a 15h05 | 03 o'clock",
            "D04 : 7/4/26 | 4 juillet 2026 | Jul 4, 2026, 3:05:09 PM | 15:05",
            "D05 : 15:05 CEST Europe/Paris +02:00 | 2026-07-04 | 15:05:09",
            "D06 : UnsupportedTemporalTypeException | index 2 | 2026-07-04");
            // EXPECTED-END

    static final List<String> API = List.of(
            "DateTimeFormatter.ofPattern(", ".withLocale(", "ofLocalizedDate(FormatStyle.SHORT)", "ofLocalizedDate(FormatStyle.LONG)",
            "ofLocalizedDateTime(FormatStyle.MEDIUM)", "ofLocalizedTime(FormatStyle.SHORT)", "ZoneId.of(", "DateTimeFormatter.ISO_LOCAL_DATE",
            "catch (DateTimeException", "catch (DateTimeParseException", ".getErrorIndex()",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
