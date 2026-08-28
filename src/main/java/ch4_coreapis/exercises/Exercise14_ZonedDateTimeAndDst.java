package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * EXERCICE 14 - ZonedDateTime et l'heure d'ete : quand 2h30 du matin N'EXISTE TOUT SIMPLEMENT PAS (niveau : difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise12_LocalDateTimeBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ZonedDateTime, c'est un LocalDateTime AUQUEL on precise EN PLUS un
 * FUSEAU HORAIRE (ZoneId.of("America/New_York")). Le jour du
 * passage a l'heure d'ete, les horloges SAUTENT directement d'1h59
 * a 3h00 - l'heure "2h30" ne s'est TOUT SIMPLEMENT JAMAIS produite
 * CE jour-la dans CE fuseau. Si on demande quand meme
 * "LocalDateTime.of(2024, 3, 10, 2, 30).atZone(...)", Java ne plante
 * PAS : il AVANCE automatiquement au premier instant valide APRES le
 * saut (3h30, avec le NOUVEAU decalage horaire).
 *
 * Consequence encore plus subtile : le temps REELLEMENT ecoule (mesure
 * par Duration.between()) reste TOUJOURS honnete, MEME quand
 * l'horloge locale, elle, "saute" de facon trompeuse - ajouter 1
 * VRAIE heure a 1h30 du matin ce jour-la affiche bien 3h30 sur
 * l'horloge (elle a saute par-dessus 2h-3h), mais Duration.between()
 * confirme que SEULEMENT 1 heure REELLE s'est ecoulee, pas 2.
 *
 *
 * ==================================================================
 * TODO 1 : toNewYorkZone(localDateTime)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer localDateTime.atZone(ZoneId.of("America/New_York")).
 *
 *
 * ==================================================================
 * TODO 2 : hoursElapsed(start, end)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Duration.between(start, end).toHours().
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : toNewYorkZone(2024-03-10T02:30) (une heure QUI
 * N'EXISTE PAS ce jour-la) rend en realite 03:30 (avance
 * automatiquement). En partant de 01:30 le meme jour et en ajoutant 1
 * VRAIE heure (plusHours(1), deja fourni dans main()), l'horloge
 * locale affiche 03:30 (elle a saute par-dessus 2h-3h), MAIS
 * hoursElapsed() confirme que SEULEMENT 1 heure s'est reellement
 * ecoulee.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Duration (contrairement a Period, voir Exercise13) mesure des
 *     durees en heures/minutes/secondes - jamais en annees/mois/jours.
 */
public class Exercise14_ZonedDateTimeAndDst {

    public static ZonedDateTime toNewYorkZone(LocalDateTime localDateTime) {
        throw new UnsupportedOperationException("TODO 1 : implementer toNewYorkZone()");
    }

    public static long hoursElapsed(ZonedDateTime start, ZonedDateTime end) {
        throw new UnsupportedOperationException("TODO 2 : implementer hoursElapsed()");
    }

    public static void main(String[] args) {
        ZonedDateTime nonExistent = toNewYorkZone(LocalDateTime.of(2024, 3, 10, 2, 30));
        ExerciseChecker.check("2h30 (inexistante ce jour-la) est AVANCEE automatiquement a 3h30",
                nonExistent.getHour() == 3 && nonExistent.getMinute() == 30);

        ZonedDateTime before = toNewYorkZone(LocalDateTime.of(2024, 3, 10, 1, 30));
        ZonedDateTime after = before.plusHours(1);
        ExerciseChecker.check("l'horloge locale saute par-dessus 2h-3h : 1h30 + 1h affiche 3h30",
                after.getHour() == 3 && after.getMinute() == 30);

        ExerciseChecker.check("hoursElapsed() confirme que SEULEMENT 1 heure REELLE s'est ecoulee",
                hoursElapsed(before, after) == 1);

        ExerciseChecker.summary();
    }
}
