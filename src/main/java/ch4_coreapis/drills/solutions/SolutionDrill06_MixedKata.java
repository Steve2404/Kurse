package ch4_coreapis.drills.solutions;

import ch4_coreapis.drills.Journal;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Corrige du drill 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill06_MixedKata.
 */
public class SolutionDrill06_MixedKata {

    public static String levelOf() {
        // Le niveau commence en 17 et s'arrete au prochain espace.
        return Journal.LINE.substring(17, Journal.LINE.indexOf(' ', 17));
    }

    public static String initials() {
        // StringBuilder pour assembler ; charAt(0) de chaque mot.
        StringBuilder sb = new StringBuilder();
        for (String word : Journal.WORDS) {
            sb.append(word.charAt(0));
        }
        return sb.toString();
    }

    public static String longestWord() {
        // On garde le meilleur ; > strict garde le premier en cas d'egalite.
        String best = Journal.WORDS[0];
        for (String word : Journal.WORDS) {
            if (word.length() > best.length()) {
                best = word;
            }
        }
        return best;
    }

    public static double medianScore() {
        // Trier une copie ; nombre pair -> moyenne des deux du milieu, en double.
        int[] sorted = Journal.SCORES.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        return n % 2 == 1 ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }

    public static int distinctScores() {
        // Une fois trie, les doublons sont voisins : on compte les changements.
        int[] sorted = Journal.SCORES.clone();
        Arrays.sort(sorted);
        int count = 1;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] != sorted[i - 1]) {
                count++;
            }
        }
        return count;
    }

    public static long passRate() {
        // 100.0 force la division en double ; Math.round arrondit au plus proche.
        int pass = 0;
        for (int s : Journal.SCORES) {
            if (s >= 20) {
                pass++;
            }
        }
        return Math.round(100.0 * pass / Journal.SCORES.length);
    }

    public static long workSeconds() {
        // getSeconds (ou toSeconds) : le total en secondes.
        return Duration.between(Journal.OPENING, Journal.CLOSING).getSeconds();
    }

    public static LocalDate nextMonday() {
        // Au moins un jour de plus, puis on avance jusqu'au lundi.
        LocalDate d = Journal.START.plusDays(1);
        while (d.getDayOfWeek() != DayOfWeek.MONDAY) {
            d = d.plusDays(1);
        }
        return d;
    }

    public static String shiftedLine() {
        // parse, plusDays, puis on recolle le reste de la ligne (substring(10) commence par l'espace).
        LocalDate date = LocalDate.parse(Journal.LINE.substring(0, 10)).plusDays(1);
        return date + Journal.LINE.substring(10);
    }

    public static String reversedMessage() {
        // insert(0, ...) inverse l'ordre des mots.
        StringBuilder sb = new StringBuilder();
        for (String word : Journal.LINE.substring(22).split(" ")) {
            if (sb.length() > 0) {
                sb.insert(0, ' ');
            }
            sb.insert(0, word);
        }
        return sb.toString();
    }

    public static String paddedScores() {
        // %3d aligne a droite sur 3 caracteres ; String.join ajoute les separateurs.
        String[] parts = new String[Journal.SCORES.length];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = String.format("%3d", Journal.SCORES[i]);
        }
        return String.join("|", parts);
    }

    public static int fridaysIn(int year, int month) {
        // On parcourt tous les jours du mois : lengthOfMonth donne la borne.
        LocalDate first = LocalDate.of(year, month, 1);
        int count = 0;
        for (int day = 1; day <= first.lengthOfMonth(); day++) {
            if (first.withDayOfMonth(day).getDayOfWeek() == DayOfWeek.FRIDAY) {
                count++;
            }
        }
        return count;
    }
}
