package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

import java.time.LocalDate;

/**
 * DRILL 06 - Kata melange : tout le chapitre 4 sans indice de methode
 * ===================================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Ici, PAS de crochet : c'est a
 * toi de choisir String, StringBuilder, Arrays, Math ou java.time. Fais
 * ce drill seulement quand les drills 01 a 05 passent.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : levelOf()             le niveau de Journal.LINE -> "WARN".
 * TODO 2  : initials()            la 1re lettre de chaque mot de WORDS, collees -> "dAcbE".
 * TODO 3  : longestWord()         le mot le plus long de WORDS -> "charlie".
 * TODO 4  : medianScore()         la mediane de SCORES (sans modifier SCORES) -> 30.5.
 * TODO 5  : distinctScores()      le nombre de valeurs differentes dans SCORES -> 5.
 * TODO 6  : passRate()            le pourcentage arrondi des SCORES >= 20 -> 50.
 * TODO 7  : workSeconds()         les secondes entre OPENING et CLOSING -> 30600.
 * TODO 8  : nextMonday()          le premier lundi APRES START -> 2024-02-05.
 * TODO 9  : shiftedLine()         LINE avec sa date avancee d'un jour -> "2024-03-11 07:42 WARN disque presque plein".
 * TODO 10 : reversedMessage()     les mots du message de LINE a l'envers -> "plein presque disque".
 * TODO 11 : paddedScores()        chaque score sur 3 caracteres, separes par "|" -> " 42|  7| 19| 88|  7| 63".
 * TODO 12 : fridaysIn(year, month) le nombre de vendredis dans le mois ; (2024, 3) -> 5.
 */
public class Drill06_MixedKata {

    public static String levelOf() {
        throw new UnsupportedOperationException("TODO 1 : implementer levelOf()");
    }

    public static String initials() {
        throw new UnsupportedOperationException("TODO 2 : implementer initials()");
    }

    public static String longestWord() {
        throw new UnsupportedOperationException("TODO 3 : implementer longestWord()");
    }

    public static double medianScore() {
        throw new UnsupportedOperationException("TODO 4 : implementer medianScore()");
    }

    public static int distinctScores() {
        throw new UnsupportedOperationException("TODO 5 : implementer distinctScores()");
    }

    public static long passRate() {
        throw new UnsupportedOperationException("TODO 6 : implementer passRate()");
    }

    public static long workSeconds() {
        throw new UnsupportedOperationException("TODO 7 : implementer workSeconds()");
    }

    public static LocalDate nextMonday() {
        throw new UnsupportedOperationException("TODO 8 : implementer nextMonday()");
    }

    public static String shiftedLine() {
        throw new UnsupportedOperationException("TODO 9 : implementer shiftedLine()");
    }

    public static String reversedMessage() {
        throw new UnsupportedOperationException("TODO 10 : implementer reversedMessage()");
    }

    public static String paddedScores() {
        throw new UnsupportedOperationException("TODO 11 : implementer paddedScores()");
    }

    public static int fridaysIn(int year, int month) {
        throw new UnsupportedOperationException("TODO 12 : implementer fridaysIn()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  levelOf() == \"WARN\"", levelOf().equals("WARN"));
        ExerciseChecker.check("2  initials() == \"dAcbE\"", initials().equals("dAcbE"));
        ExerciseChecker.check("3  longestWord() == \"charlie\"", longestWord().equals("charlie"));
        ExerciseChecker.check("4  medianScore() == 30.5 et SCORES intact", medianScore() == 30.5 && Journal.SCORES[0] == 42);
        ExerciseChecker.check("5  distinctScores() == 5", distinctScores() == 5);
        ExerciseChecker.check("6  passRate() == 50", passRate() == 50);
        ExerciseChecker.check("7  workSeconds() == 30600", workSeconds() == 30600);
        ExerciseChecker.check("8  nextMonday() == 2024-02-05", nextMonday().equals(LocalDate.of(2024, 2, 5)));
        ExerciseChecker.check("9  shiftedLine()", shiftedLine().equals("2024-03-11 07:42 WARN disque presque plein"));
        ExerciseChecker.check("10 reversedMessage()", reversedMessage().equals("plein presque disque"));
        ExerciseChecker.check("11 paddedScores()", paddedScores().equals(" 42|  7| 19| 88|  7| 63"));
        ExerciseChecker.check("12 fridaysIn(2024, 3) == 5 et fridaysIn(2024, 2) == 4", fridaysIn(2024, 3) == 5 && fridaysIn(2024, 2) == 4);

        ExerciseChecker.summary();
    }
}
