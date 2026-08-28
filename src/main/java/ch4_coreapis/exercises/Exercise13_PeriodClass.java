package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.LocalDate;
import java.time.Period;

/**
 * EXERCICE 13 - Period : la "distance" entre 2 dates, en annees/mois/jours (niveau : difficile)
 * =======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise12_LocalDateTimeBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Period represente une DUREE exprimee en annees/mois/jours (jamais
 * en heures/minutes : pour ca, il existe Duration, hors programme de
 * ce chapitre) - soit CALCULEE entre 2 dates (Period.between(debut,
 * fin)), soit construite directement (Period.ofMonths(1)). Comme
 * LocalDate (voir Exercise12), un Period peut s'AJOUTER a une date
 * via plus(period) - et Java "rattrape" intelligemment les mois de
 * longueurs differentes : ajouter 1 mois au 31 janvier ne donne
 * JAMAIS un "31 fevrier" (qui n'existe pas) - Java ramene
 * automatiquement au DERNIER jour valide de fevrier.
 *
 *
 * ==================================================================
 * TODO 1 : yearsBetween(start, end)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Entre le 15 mars 2020 et le 20 juillet 2023 : 3 annees completes,
 * PUIS 4 mois de plus (mars a juillet), PUIS 5 jours de plus (15 a
 * 20) - Period.between() calcule tout ca EXACTEMENT.
 *
 * -- Le plan --
 *
 *   1. Renvoyer Period.between(start, end).getYears().
 *
 *
 * ==================================================================
 * TODO 2 : addOneMonthClamped(date)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Le 31 janvier 2024 + 1 mois : fevrier 2024 n'a que 29 jours (2024
 * est bissextile) - Java rend donc le 29 fevrier 2024, PAS une
 * erreur, PAS un "31 fevrier" impossible.
 *
 * -- Le plan --
 *
 *   1. Renvoyer date.plus(Period.ofMonths(1)).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : yearsBetween(15 mars 2020, 20 juillet 2023)
 * == 3 (SEULEMENT les annees completes - les 4 mois et 5 jours
 * restants ne comptent PAS dans getYears(), ils sont dans
 * getMonths()/getDays() a part). addOneMonthClamped(31 janvier 2024)
 * == 29 fevrier 2024.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - getYears()/getMonths()/getDays() sur un Period rendent CHACUN
 *     leur PROPRE morceau, jamais un total "tout convertit en
 *     jours" - 3 ans 4 mois 5 jours reste 3/4/5, jamais "quelque
 *     chose comme 1220 jours".
 */
public class Exercise13_PeriodClass {

    public static int yearsBetween(LocalDate start, LocalDate end) {
        throw new UnsupportedOperationException("TODO 1 : implementer yearsBetween()");
    }

    public static LocalDate addOneMonthClamped(LocalDate date) {
        throw new UnsupportedOperationException("TODO 2 : implementer addOneMonthClamped()");
    }

    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2020, 3, 15);
        LocalDate end = LocalDate.of(2023, 7, 20);
        ExerciseChecker.check("yearsBetween() ne compte que les annees COMPLETES", yearsBetween(start, end) == 3);

        LocalDate result = addOneMonthClamped(LocalDate.of(2024, 1, 31));
        ExerciseChecker.check("addOneMonthClamped() ramene au DERNIER jour valide de fevrier",
                result.equals(LocalDate.of(2024, 2, 29)));

        ExerciseChecker.summary();
    }
}
