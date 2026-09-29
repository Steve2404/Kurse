package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

/**
 * DRILL 01 - L'API String : une methode par TODO (journal de bord)
 * ================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te fait REPETER une
 * methode jusqu'a ce qu'elle sorte toute seule. Chaque TODO tient en
 * UNE ligne et vise UNE methode precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch4_coreapis.drills.Journal.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : titleLength()          [length] de Journal.TITLE -> 19.
 * TODO 2  : cleanTitle()           [strip] -> "Journal de Bord".
 * TODO 3  : levelChar()            [charAt] le 1er caractere du niveau (index 17) -> 'W'.
 * TODO 4  : datePart()             [substring(debut, fin)] -> "2024-03-10".
 * TODO 5  : messagePart()          [substring(debut)] a partir de l'index 22 -> "disque presque plein".
 * TODO 6  : firstSpace()           [indexOf(char)] -> 10.
 * TODO 7  : secondSpace()          [indexOf(char, depart)] l'espace apres l'index 10 -> 16.
 * TODO 8  : lastSpace()            [lastIndexOf] -> 36.
 * TODO 9  : isWarning()            [contains] LINE contient "WARN" -> true.
 * TODO 10 : startsIn2024()         [startsWith] -> true.
 * TODO 11 : shout()                [toUpperCase] du message -> "DISQUE PRESQUE PLEIN".
 * TODO 12 : sameIgnoringCase()     [equalsIgnoreCase] "alpha" et WORDS[1] -> true.
 * TODO 13 : dashes()               [repeat] 5 tirets -> "-----".
 * TODO 14 : noSpaces()             [replace(char, char)] espaces du message en '_' -> "disque_presque_plein".
 * TODO 15 : isBlankTitle()         [isBlank] "   " -> true (et "   ".isEmpty() est false).
 * TODO 16 : joinWords()            [String.join] les WORDS avec ", " -> "delta, Alpha, charlie, bravo, Echo".
 * TODO 17 : wordCount()            [split] nombre de mots du message -> 3.
 * TODO 18 : compareFirstTwo()      [compareTo] WORDS[0].compareTo(WORDS[1]) -> 35 ('d' - 'A').
 * TODO 19 : charPlusOne()          [le piege 'a' + 1 + ""] -> "98".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   length() charAt(i) substring(d, f) substring(d) indexOf(c) indexOf(c, d) lastIndexOf(c)
 *   contains(s) startsWith(s) endsWith(s) equals(s) equalsIgnoreCase(s) compareTo(s)
 *   toUpperCase() toLowerCase() strip() stripLeading() stripTrailing() trim()
 *   isEmpty() isBlank() repeat(n) replace(a, b) split(regex) String.join(sep, ...)
 *   concat(s) indent(n) String.format / "..".formatted(..) lines() chars()
 *   Tout String est IMMUABLE : chaque methode rend un NOUVEAU String.
 *   'a' + 1 + "" : 'a' + 1 = 98 (un int) PUIS 98 + "" = "98".
 * ---------------------------------------------------------------------
 */
public class Drill01_StringApi {

    public static int titleLength() {
        throw new UnsupportedOperationException("TODO 1 : implementer titleLength()");
    }

    public static String cleanTitle() {
        throw new UnsupportedOperationException("TODO 2 : implementer cleanTitle()");
    }

    public static char levelChar() {
        throw new UnsupportedOperationException("TODO 3 : implementer levelChar()");
    }

    public static String datePart() {
        throw new UnsupportedOperationException("TODO 4 : implementer datePart()");
    }

    public static String messagePart() {
        throw new UnsupportedOperationException("TODO 5 : implementer messagePart()");
    }

    public static int firstSpace() {
        throw new UnsupportedOperationException("TODO 6 : implementer firstSpace()");
    }

    public static int secondSpace() {
        throw new UnsupportedOperationException("TODO 7 : implementer secondSpace()");
    }

    public static int lastSpace() {
        throw new UnsupportedOperationException("TODO 8 : implementer lastSpace()");
    }

    public static boolean isWarning() {
        throw new UnsupportedOperationException("TODO 9 : implementer isWarning()");
    }

    public static boolean startsIn2024() {
        throw new UnsupportedOperationException("TODO 10 : implementer startsIn2024()");
    }

    public static String shout() {
        throw new UnsupportedOperationException("TODO 11 : implementer shout()");
    }

    public static boolean sameIgnoringCase() {
        throw new UnsupportedOperationException("TODO 12 : implementer sameIgnoringCase()");
    }

    public static String dashes() {
        throw new UnsupportedOperationException("TODO 13 : implementer dashes()");
    }

    public static String noSpaces() {
        throw new UnsupportedOperationException("TODO 14 : implementer noSpaces()");
    }

    public static boolean isBlankTitle() {
        throw new UnsupportedOperationException("TODO 15 : implementer isBlankTitle()");
    }

    public static String joinWords() {
        throw new UnsupportedOperationException("TODO 16 : implementer joinWords()");
    }

    public static int wordCount() {
        throw new UnsupportedOperationException("TODO 17 : implementer wordCount()");
    }

    public static int compareFirstTwo() {
        throw new UnsupportedOperationException("TODO 18 : implementer compareFirstTwo()");
    }

    public static String charPlusOne() {
        throw new UnsupportedOperationException("TODO 19 : implementer charPlusOne()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  titleLength() == 19", titleLength() == 19);
        ExerciseChecker.check("2  cleanTitle() == \"Journal de Bord\"", cleanTitle().equals("Journal de Bord"));
        ExerciseChecker.check("3  levelChar() == 'W'", levelChar() == 'W');
        ExerciseChecker.check("4  datePart() == \"2024-03-10\"", datePart().equals("2024-03-10"));
        ExerciseChecker.check("5  messagePart() == \"disque presque plein\"", messagePart().equals("disque presque plein"));
        ExerciseChecker.check("6  firstSpace() == 10", firstSpace() == 10);
        ExerciseChecker.check("7  secondSpace() == 16", secondSpace() == 16);
        ExerciseChecker.check("8  lastSpace() == 36", lastSpace() == 36);
        ExerciseChecker.check("9  isWarning()", isWarning());
        ExerciseChecker.check("10 startsIn2024()", startsIn2024());
        ExerciseChecker.check("11 shout() == \"DISQUE PRESQUE PLEIN\"", shout().equals("DISQUE PRESQUE PLEIN"));
        ExerciseChecker.check("12 sameIgnoringCase()", sameIgnoringCase());
        ExerciseChecker.check("13 dashes() == \"-----\"", dashes().equals("-----"));
        ExerciseChecker.check("14 noSpaces() == \"disque_presque_plein\"", noSpaces().equals("disque_presque_plein"));
        ExerciseChecker.check("15 isBlankTitle()", isBlankTitle());
        ExerciseChecker.check("16 joinWords()", joinWords().equals("delta, Alpha, charlie, bravo, Echo"));
        ExerciseChecker.check("17 wordCount() == 3", wordCount() == 3);
        ExerciseChecker.check("18 compareFirstTwo() == 35", compareFirstTwo() == 35);
        ExerciseChecker.check("19 charPlusOne() == \"98\"", charPlusOne().equals("98"));

        ExerciseChecker.summary();
    }
}
