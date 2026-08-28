package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * EXERCICE 12 - LocalDate, LocalTime, LocalDateTime : IMMUABLES, comme des String (niveau : moyen/difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * LocalDate ne contient QU'UNE date (jour/mois/annee, JAMAIS
 * d'heure). LocalTime ne contient QU'UNE heure (JAMAIS de date).
 * LocalDateTime contient LES DEUX a la fois. Aucune des 3 classes n'a
 * de constructeur PUBLIC (impossible d'ecrire "new LocalDate(...)")
 * : on les cree TOUJOURS via LocalDate.now() (la date d'AUJOURD'HUI)
 * ou LocalDate.of(annee, mois, jour) (une date PRECISE) - et les
 * equivalents pour les 2 autres classes.
 *
 * EXACTEMENT comme un String (voir Exercise01), les 3 sont IMMUABLES :
 * plusWeeks(), plusDays(), minusHours()... ne modifient JAMAIS
 * l'objet d'origine, elles rendent TOUJOURS une TOUTE NOUVELLE
 * instance - piege classique de l'examen : appeler
 * "date.plusDays(5);" SANS RIEN recuperer ne fait absolument RIEN de
 * visible (le calcul est fait, puis jete a la poubelle aussitot).
 *
 *
 * ==================================================================
 * TODO 1 : buildBirthday(year, month, day)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer LocalDate.of(year, month, day).
 *
 *
 * ==================================================================
 * TODO 2 : oneWeekLater(start)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer start.plusWeeks(1) - le retour est OBLIGATOIREMENT
 *      recupere ici (start, lui, reste TOUJOURS inchange).
 *
 *
 * ==================================================================
 * TODO 3 : combineDateAndTime(date, time)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer date.atTime(time) - fusionne une LocalDate et une
 *      LocalTime en une seule LocalDateTime.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : buildBirthday(1990, 5, 15) a bien annee=1990,
 * mois=5, jour=15. Avec start = LocalDateTime.of(2024, 1, 1, 10, 0),
 * oneWeekLater(start) vaut 2024-01-08T10:00, ET start vaut TOUJOURS
 * 2024-01-01T10:00 apres l'appel (immuable). combineDateAndTime(...)
 * fusionne correctement les 2.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - LocalDate.of(year, month, day) : month va de 1 (janvier) a 12
 *     (decembre) - PAS de 0 a 11 comme dans certains autres langages.
 */
public class Exercise12_LocalDateTimeBasics {

    public static LocalDate buildBirthday(int year, int month, int day) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildBirthday()");
    }

    public static LocalDateTime oneWeekLater(LocalDateTime start) {
        throw new UnsupportedOperationException("TODO 2 : implementer oneWeekLater()");
    }

    public static LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        throw new UnsupportedOperationException("TODO 3 : implementer combineDateAndTime()");
    }

    public static void main(String[] args) {
        LocalDate birthday = buildBirthday(1990, 5, 15);
        ExerciseChecker.check("buildBirthday() cree la bonne date",
                birthday.getYear() == 1990 && birthday.getMonthValue() == 5 && birthday.getDayOfMonth() == 15);

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime later = oneWeekLater(start);
        ExerciseChecker.check("oneWeekLater() ajoute bien 7 jours",
                later.equals(LocalDateTime.of(2024, 1, 8, 10, 0)));
        ExerciseChecker.check("start reste INCHANGE (LocalDateTime immuable)",
                start.equals(LocalDateTime.of(2024, 1, 1, 10, 0)));

        LocalDateTime combined = combineDateAndTime(LocalDate.of(2024, 6, 1), LocalTime.of(14, 30));
        ExerciseChecker.check("combineDateAndTime() fusionne date et heure",
                combined.equals(LocalDateTime.of(2024, 6, 1, 14, 30)));

        ExerciseChecker.summary();
    }
}
