package ch4_coreapis.drills.solutions;

import ch4_coreapis.drills.Journal;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * Corrige du drill 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill06_ApiExtras.
 */
public class SolutionDrill06_ApiExtras {

    public static String indented() {
        // indent ajoute les espaces ET un \n final.
        return "a\nb".indent(2);
    }

    public static String unindented() {
        // Au plus 1 blanc enleve par ligne : "   a" garde 2 espaces, "c" ne bouge pas.
        return "   a\n b\nc".indent(-1);
    }

    public static String normalized() {
        // indent(0) ne decale rien mais ajoute (normalise) la fin de ligne.
        return "x".indent(0);
    }

    public static String dedented() {
        // stripIndent enleve l'indentation COMMUNE (4) ; "b" garde ses 2 espaces de plus.
        return "    a\n      b\n    c".stripIndent();
    }

    public static String escaped() {
        // Dans le code, "a\\tb" contient un antislash et un t ; translateEscapes en fait une tabulation.
        return "a\\tb".translateEscapes();
    }

    public static boolean trimKeepsInstance() {
        // Rien a enlever : trim rend this, donc le meme objet du pool.
        return "hello".trim() == "hello";
    }

    public static boolean upperIsNew() {
        // toUpperCase cree un nouveau String (hors pool) : == est faux, equals serait vrai.
        return "hello".toUpperCase() == "HELLO";
    }

    public static boolean internIsPooled() {
        // intern rend l'objet du pool qui a le meme contenu.
        return new String("hello").intern() == "hello";
    }

    public static long countA() {
        // chars() rend un IntStream des codes de caracteres.
        return "banana".chars().filter(c -> c == 'a').count();
    }

    public static int nullFirst() {
        // Arrays.compare range null avant toute valeur.
        return Arrays.compare(new String[] {null, "a"}, new String[] {"a"});
    }

    public static int longerAfter() {
        // Meme debut : le plus long passe apres.
        return Arrays.compare(new int[] {1, 2, 3}, new int[] {1, 2});
    }

    public static int mismatchNulls() {
        // mismatch compare avec equals en acceptant null : deux null sont egaux.
        return Arrays.mismatch(new String[] {"a", null}, new String[] {"a", null});
    }

    public static String epochText() {
        // L'epoque : 1970-01-01 a minuit UTC.
        return Instant.ofEpochSecond(0).toString();
    }

    public static long epochSeconds() {
        // parse exige les secondes et le Z.
        return Instant.parse("2024-06-01T07:00:00Z").getEpochSecond();
    }

    public static int hourInParis() {
        // Instant n'a pas getHour : atZone donne une heure lisible (UTC+2 en juin).
        return Instant.parse("2024-06-01T07:00:00Z").atZone(Journal.PARIS).getHour();
    }

    public static String thirtyHoursLater() {
        // Instant accepte une Duration (des secondes).
        return Instant.parse("2024-06-01T07:00:00Z").plus(Duration.ofHours(30)).toString();
    }

    public static String winterInstant() {
        // En hiver Paris est a UTC+1 : 09:00 -> 08:00Z.
        return ZonedDateTime.of(2024, 1, 1, 9, 0, 0, 0, Journal.PARIS).toInstant().toString();
    }

    public static long minutesUntil() {
        // until(autre, unite) : equivalent de ChronoUnit.MINUTES.between.
        return Instant.parse("2024-06-01T07:00:00Z").until(Instant.parse("2024-06-02T09:30:00Z"), ChronoUnit.MINUTES);
    }
}
