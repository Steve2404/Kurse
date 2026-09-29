package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

import java.time.Instant;

/**
 * DRILL 06 - Les methodes qu'on oublie : indent, stripIndent, translateEscapes, pool, compare avec null, Instant
 * ===========================================================================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Chaque TODO tient en UNE ligne.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : indented()          [indent(2)] "a\nb" -> "  a\n  b\n" (indent AJOUTE un \n final).
 * TODO 2  : unindented()        [indent(-1)] "   a\n b\nc" -> "  a\nb\nc\n" (enleve au plus 1 blanc par ligne).
 * TODO 3  : normalized()        [indent(0)] "x" -> "x\n" (0 : ne decale rien mais normalise les fins de ligne).
 * TODO 4  : dedented()          [stripIndent] "    a\n      b\n    c" -> "a\n  b\nc" (garde l'indentation RELATIVE).
 * TODO 5  : escaped()           [translateEscapes] le texte "a\\tb" (antislash + t) -> "a" + tabulation + "b".
 * TODO 6  : trimKeepsInstance() [pool] "hello".trim() == "hello" -> true (rien a enlever : le MEME objet).
 * TODO 7  : upperIsNew()        [pool] "hello".toUpperCase() == "HELLO" -> false (un NOUVEL objet).
 * TODO 8  : internIsPooled()    [intern] new String("hello").intern() == "hello" -> true.
 * TODO 9  : countA()            [chars] nombre de 'a' dans "banana" -> 3.
 * TODO 10 : nullFirst()         [Arrays.compare avec null] {null, "a"} contre {"a"} -> -1 (null passe avant).
 * TODO 11 : longerAfter()       [Arrays.compare, prefixe] {1, 2, 3} contre {1, 2} -> 1.
 * TODO 12 : mismatchNulls()     [Arrays.mismatch avec null] {"a", null} contre {"a", null} -> -1 (identiques).
 * TODO 13 : epochText()         [Instant.ofEpochSecond] 0 -> "1970-01-01T00:00:00Z".
 * TODO 14 : epochSeconds()      [Instant.parse + getEpochSecond] "2024-06-01T07:00:00Z" -> 1717225200.
 * TODO 15 : hourInParis()       [Instant.atZone] cet instant a Journal.PARIS -> 9.
 * TODO 16 : thirtyHoursLater()  [Instant.plus(Duration)] + 30 h -> "2024-06-02T13:00:00Z".
 * TODO 17 : winterInstant()     [ZonedDateTime.toInstant] 2024-01-01T09:00 a Paris -> "2024-01-01T08:00:00Z".
 * TODO 18 : minutesUntil()      [Instant.until] de 2024-06-01T07:00:00Z a 2024-06-02T09:30:00Z -> 1590.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   indent(n) : n > 0 ajoute n espaces, n < 0 en enleve (au plus n), TOUJOURS un \n final
 *   stripIndent() : enleve l'indentation commune et les blancs de fin de ligne, pas de \n ajoute
 *   translateEscapes() : "\\t" (2 caracteres) devient une vraie tabulation ; "\\q" -> IllegalArgumentException
 *   Une methode de String qui ne change rien rend souvent le MEME objet (trim, strip, concat(""), substring(0))
 *   Arrays.compare : null < tout ; plus court (prefixe) < plus long ; Arrays.mismatch : -1 si identiques
 *   Instant : parse("...T07:00:00Z") (secondes ET Z obligatoires), ofEpochSecond, ofEpochMilli,
 *             getEpochSecond, toEpochMilli, plus(Duration), until(i, unite), atZone(zone), isBefore
 *   Instant n'a ni getHour ni getYear ; plus(1, YEARS) -> UnsupportedTemporalTypeException
 * ---------------------------------------------------------------------
 */
public class Drill06_ApiExtras {

    public static String indented() {
        throw new UnsupportedOperationException("TODO 1 : implementer indented()");
    }

    public static String unindented() {
        throw new UnsupportedOperationException("TODO 2 : implementer unindented()");
    }

    public static String normalized() {
        throw new UnsupportedOperationException("TODO 3 : implementer normalized()");
    }

    public static String dedented() {
        throw new UnsupportedOperationException("TODO 4 : implementer dedented()");
    }

    public static String escaped() {
        throw new UnsupportedOperationException("TODO 5 : implementer escaped()");
    }

    public static boolean trimKeepsInstance() {
        throw new UnsupportedOperationException("TODO 6 : implementer trimKeepsInstance()");
    }

    public static boolean upperIsNew() {
        throw new UnsupportedOperationException("TODO 7 : implementer upperIsNew()");
    }

    public static boolean internIsPooled() {
        throw new UnsupportedOperationException("TODO 8 : implementer internIsPooled()");
    }

    public static long countA() {
        throw new UnsupportedOperationException("TODO 9 : implementer countA()");
    }

    public static int nullFirst() {
        throw new UnsupportedOperationException("TODO 10 : implementer nullFirst()");
    }

    public static int longerAfter() {
        throw new UnsupportedOperationException("TODO 11 : implementer longerAfter()");
    }

    public static int mismatchNulls() {
        throw new UnsupportedOperationException("TODO 12 : implementer mismatchNulls()");
    }

    public static String epochText() {
        throw new UnsupportedOperationException("TODO 13 : implementer epochText()");
    }

    public static long epochSeconds() {
        throw new UnsupportedOperationException("TODO 14 : implementer epochSeconds()");
    }

    public static int hourInParis() {
        throw new UnsupportedOperationException("TODO 15 : implementer hourInParis()");
    }

    public static String thirtyHoursLater() {
        throw new UnsupportedOperationException("TODO 16 : implementer thirtyHoursLater()");
    }

    public static String winterInstant() {
        throw new UnsupportedOperationException("TODO 17 : implementer winterInstant()");
    }

    public static long minutesUntil() {
        throw new UnsupportedOperationException("TODO 18 : implementer minutesUntil()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  indented()", indented().equals("  a\n  b\n"));
        ExerciseChecker.check("2  unindented()", unindented().equals("  a\nb\nc\n"));
        ExerciseChecker.check("3  normalized()", normalized().equals("x\n"));
        ExerciseChecker.check("4  dedented()", dedented().equals("a\n  b\nc"));
        ExerciseChecker.check("5  escaped()", escaped().equals("a\tb"));
        ExerciseChecker.check("6  trimKeepsInstance()", trimKeepsInstance());
        ExerciseChecker.check("7  upperIsNew() == false", !upperIsNew());
        ExerciseChecker.check("8  internIsPooled()", internIsPooled());
        ExerciseChecker.check("9  countA() == 3", countA() == 3);
        ExerciseChecker.check("10 nullFirst() == -1", nullFirst() == -1);
        ExerciseChecker.check("11 longerAfter() == 1", longerAfter() == 1);
        ExerciseChecker.check("12 mismatchNulls() == -1", mismatchNulls() == -1);
        ExerciseChecker.check("13 epochText()", epochText().equals("1970-01-01T00:00:00Z"));
        ExerciseChecker.check("14 epochSeconds() == 1717225200", epochSeconds() == 1717225200L);
        ExerciseChecker.check("15 hourInParis() == 9", hourInParis() == 9);
        ExerciseChecker.check("16 thirtyHoursLater()", thirtyHoursLater().equals("2024-06-02T13:00:00Z"));
        ExerciseChecker.check("17 winterInstant()", winterInstant().equals("2024-01-01T08:00:00Z"));
        ExerciseChecker.check("18 minutesUntil() == 1590", minutesUntil() == 1590);

        ExerciseChecker.summary();
    }
}
