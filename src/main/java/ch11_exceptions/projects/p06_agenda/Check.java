package ch11_exceptions.projects.p06_agenda;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Agenda, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "lu \"2026-03-14T09:30\" -> 2026-03-14T09:30",
            "lu \"20/03/2026 14:00\" -> 2026-03-20T14:00",
            "lu \"March 27, 2026 4:00 PM\" -> 2026-03-27T16:00",
            "lu \"2026-02-30 10:00\" -> 2026-02-28T10:00",
            "lu \"31/04/2026 08:00\" -> 2026-04-30T08:00",
            "date illisible : 2026-04-32 10:00 (4 essais) ; dernier : Text '2026-04-32 10:00' could not be parsed: Invalid value for DayOfMonth (valid values 1 - 28/31): 32",
            "date illisible : demain matin (4 essais) ; dernier : Text 'demain matin' could not be parsed at index 0",
            "  samedi 28 février 2026 a 10h00 : budget",
            "  samedi 14 mars 2026 a 09h30 : lancement",
            "  vendredi 20 mars 2026 a 14h00 : revue",
            "  vendredi 27 mars 2026 a 16h00 : demo client",
            "  jeudi 30 avril 2026 a 08h00 : audit",
            "recurrence 2e TUESDAY : 10.03.2026, 14.04.2026, 12.05.2026, 09.06.2026",
            "echeance 2026-04-01 + 10 jours ouvres = jeu. 16/04, sautes [sam. 04/04, dim. 05/04, lun. 06/04, sam. 11/04, dim. 12/04]",
            "reunion : | Europe/Paris Fri 16:00 CET (+01:00) | America/New_York Fri 11:00 EDT (-04:00) | Asia/Tokyo Sat 00:00 JST (+09:00)",
            "reunion : | Europe/Paris Fri 16:00 CEST (+02:00) | America/New_York Fri 10:00 EDT (-04:00) | Asia/Tokyo Fri 23:00 JST (+09:00)",
            "en-US : | Saturday, March 14, 2026 | March 14, 2026 | Mar 14, 2026 | 3/14/26 | 9:30 AM",
            "fr-FR : | samedi 14 mars 2026 | 14 mars 2026 | 14 mars 2026 | 14/03/2026 | 09:30",
            "de-DE : | Samstag, 14. März 2026 | 14. März 2026 | 14.03.2026 | 14.03.26 | 09:30",
            "ISO : 2026-03-14 09:30:00 ; apostrophe : 09 o'clock AM, Mar 14",
            "date sans heure : UnsupportedTemporalTypeException Unsupported field: HourOfDay",
            "FULL sans zone : DateTimeException Unable to extract ZoneId from temporal 2026-03-14T09:30",
            "parse ISO : Text '14.03.2026' could not be parsed at index 0 (index 0, texte 14.03.2026)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.RAW", "Data.PATTERNS", "Data.NTH", "Data.HOLIDAYS",
            "Data.MEETING", "Data.ZONES", "Data.SAMPLE", "Data.LOCALES",
            "record Event(", "DateTimeFormatter.ISO_LOCAL_DATE_TIME", "Locale.ENGLISH", "catch (DateTimeParseException",
            "addSuppressed", ".getSuppressed()", "LocalDateTime.parse(", "Locale.FRANCE",
            "ofLocalizedDate(FormatStyle.MEDIUM)", ".withLocale(", "FormatStyle.values()", "ofLocalizedTime(FormatStyle.SHORT)",
            ".localizedBy(", ".withZoneSameInstant(", "ZoneId.of(", "DateTimeFormatter.ISO_LOCAL_DATE",
            "DateTimeFormatter.ISO_LOCAL_TIME", "ofLocalizedDateTime(FormatStyle.FULL)", "catch (DateTimeException", ".getErrorIndex()",
            ".getParsedString()", "YearMonth", ".plusWeeks(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Agenda", args, EXPECTED, API);
    }
}
