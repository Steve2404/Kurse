package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * EXERCICE 14 - Refaire DateTimeFormatter a la main : lettres, repetitions, texte entre apostrophes (niveau : difficile)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans un motif de date, chaque LETTRE est une case a remplir, et le
 * NOMBRE de fois qu'on la repete dit comment la remplir (Locale.US) :
 *
 *   y     yy -> 2 derniers chiffres (24) ; y, yyy, yyyy -> l'annee complete (2024)
 *   M     M -> 3 ; MM -> 03 ; MMM -> Mar ; MMMM -> March        (M = MOIS)
 *   d     d -> 7 ; dd -> 07
 *   E     E, EE, EEE -> Thu ; EEEE -> Thursday
 *   H     heure 0-23 : H -> 9 ; HH -> 09
 *   h     heure 1-12 (0 h s'affiche 12) : h / hh
 *   m     MINUTES : m / mm          (piege classique : mm = minutes, MM = mois)
 *   s     secondes : s / ss
 *   a     AM ou PM
 *   'texte'  recopie tel quel (les lettres dedans ne sont PAS des cases) ; '' -> une apostrophe,
 *            y compris A L'INTERIEUR d'un texte : 'o''clock' -> o'clock
 *   tout autre caractere (/ : - , espace...) est recopie tel quel
 *
 * main() compare ton rendu au vrai DateTimeFormatter.ofPattern(p, Locale.US).
 *
 *
 * ==================================================================
 * TODO 1 : render(pattern, dt)
 * ==================================================================
 *
 * -- Essayons a la main --  (dt = 2024-03-07T14:05:09, un jeudi)
 *
 *   "dd/MM/yyyy"          -> 07/03/2024
 *   "MMM d, yy"           -> Mar 7, 24
 *   "h:mm a"              -> 2:05 PM
 *   "hh 'o''clock' a"     -> 02 o'clock PM
 *   "yyyy-MM-dd'T'HH:mm"  -> 2024-03-07T14:05
 *
 * -- Le plan --
 *
 *   1. Parcourir le motif.
 *   2. Une apostrophe : si la suivante est aussi une apostrophe -> ecrire ' ; sinon recopier jusqu'a
 *      l'apostrophe fermante (et dedans, deux apostrophes de suite -> une apostrophe).
 *   3. Une lettre : compter combien de fois elle se repete, puis ecrire la case (boite magique).
 *   4. Autre caractere : le recopier.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q3) : "une lettre et son nombre -> le texte" est sa propre
 * petite recette (un switch sur la lettre). Et "completer avec des
 * zeros a gauche" sert pour presque toutes les lettres.
 *
 *
 * ==================================================================
 * TODO 2 : formatError(pattern, type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un LocalDate n'a pas d'heure, un LocalTime n'a pas de date. Demander
 * HH a un LocalDate lance UnsupportedTemporalTypeException A L'EXECUTION
 * (ca compile tres bien !). type vaut "LocalDate" ou "LocalTime".
 *
 * -- Essayons a la main --
 *
 *   ("HH:mm", "LocalDate")   -> UnsupportedTemporalTypeException
 *   ("'at' dd", "LocalDate") -> OK ('a' et 't' sont entre apostrophes : pas des cases)
 *   ("EEEE", "LocalTime")    -> UnsupportedTemporalTypeException
 *
 * -- Le plan --
 *
 *   1. Parcourir les lettres HORS apostrophes.
 *   2. LocalDate + une lettre d'heure (H h m s a) -> l'exception ; LocalTime + une lettre de date (y M d E) -> l'exception.
 *   3. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2) : "les lettres du motif hors apostrophes" : le meme parcours
 * que le TODO 1.
 *
 * Exemple a verifier : voir les tests de main() (39 rendus, 12 verdicts).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Character.isLetter(c) ; while (j < n && pattern.charAt(j) == c) j++;
 *   - dt.getMonth() est un enum : Month.MARCH ; getDisplayName(TextStyle.SHORT / FULL, Locale.US) -> Mar / March.
 *   - Idem dt.getDayOfWeek().getDisplayName(...) -> Thu / Thursday.
 *   - String.format("%0" + n + "d", valeur) complete avec des zeros.
 */
public class Exercise14_DateTimePatternRules {

    public static String render(String pattern, LocalDateTime dt) {
        throw new UnsupportedOperationException("TODO 1 : implementer render()");
    }

    public static String formatError(String pattern, String type) {
        throw new UnsupportedOperationException("TODO 2 : implementer formatError()");
    }

    public static void main(String[] args) {
        String[] patterns = {"dd/MM/yyyy", "d/M/yy", "MMM d, yyyy", "MMMM dd", "EEE, d MMM", "EEEE", "HH:mm:ss",
                "h:mm a", "hh 'o''clock' a", "yyyy-MM-dd'T'HH:mm", "'Le' dd.MM", "mm:ss", "y"};
        LocalDateTime[] dates = {LocalDateTime.of(2024, 3, 7, 14, 5, 9), LocalDateTime.of(2009, 12, 25, 0, 30, 0),
                LocalDateTime.of(1999, 1, 1, 9, 0, 59)};
        int agree = 0;
        String firstMiss = "";
        for (String p : patterns) {
            for (LocalDateTime dt : dates) {
                String expected = DateTimeFormatter.ofPattern(p, Locale.US).format(dt);
                String mine = render(p, dt);
                if (mine.equals(expected)) {
                    agree++;
                } else if (firstMiss.isEmpty()) {
                    firstMiss = " ; 1er ecart : render(\"" + p + "\", " + dt + ") = " + mine + " au lieu de " + expected;
                }
            }
        }
        ExerciseChecker.check("render == DateTimeFormatter sur 39 cas (" + agree + " d'accord)" + firstMiss, agree == 39);

        String[] errorPatterns = {"dd/MM/yyyy", "HH:mm", "'at' dd", "h a", "EEEE", "'Time' HH"};
        agree = 0;
        for (String p : errorPatterns) {
            if (formatError(p, "LocalDate").equals(jvm(p, LocalDate.of(2024, 3, 7)))) {
                agree++;
            }
            if (formatError(p, "LocalTime").equals(jvm(p, LocalTime.of(14, 5)))) {
                agree++;
            }
        }
        ExerciseChecker.check("formatError == JVM sur 12 cas (" + agree + " d'accord)", agree == 12);

        ExerciseChecker.summary();
    }

    static String jvm(String pattern, TemporalAccessor value) {
        try {
            DateTimeFormatter.ofPattern(pattern, Locale.US).format(value);
            return "OK";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
