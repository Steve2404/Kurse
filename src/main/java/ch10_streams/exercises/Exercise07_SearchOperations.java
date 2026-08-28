package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * EXERCICE 7 - Chercher dans un stream : findFirst, allMatch, anyMatch, noneMatch (niveau : moyen)
 * ==============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Les 4 outils du jour, en une phrase chacun --
 *
 *   - findFirst()          : renvoie un Optional<T> - le PREMIER
 *                             element du stream (ou vide si le stream
 *                             est vide).
 *   - allMatch(Predicate)  : TOUS les elements doivent passer le test
 *                             pour que ce soit vrai (un stream VIDE
 *                             renvoie toujours true - vide raisonnement,
 *                             "aucun contre-exemple trouve").
 *   - anyMatch(Predicate)  : AU MOINS UN element doit passer le test.
 *   - noneMatch(Predicate) : AUCUN element ne doit passer le test.
 *
 * Ces 4 methodes sont TERMINALES, et COURT-CIRCUITENT : elles
 * s'arretent des qu'elles ont assez d'information pour repondre (par
 * exemple, anyMatch() s'arrete au tout premier "oui" trouve, sans
 * regarder le reste).
 *
 *
 * ==================================================================
 * TODO 1 : firstMatch(words, predicate)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. words.stream().filter(predicate).findFirst().
 *
 *
 * ==================================================================
 * TODO 2, 3, 4 : allStartWithUppercase / anyContainsDigit / noneAreEmpty
 * ==================================================================
 *
 * -- Le plan --
 *
 *   TODO 2 : words.stream().allMatch(mot -> !mot.isEmpty() &&
 *            Character.isUpperCase(mot.charAt(0))).
 *   TODO 3 : words.stream().anyMatch(mot -> mot.chars().anyMatch(Character::isDigit)).
 *   TODO 4 : words.stream().noneMatch(String::isEmpty).
 *
 * -- Ces 4 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX du bon outil
 * (findFirst / allMatch / anyMatch / noneMatch) qui est le vrai coeur
 * de l'exercice.
 *
 * Exemple a verifier : words = ["Alpha", "Beta2", "Gamma"].
 *   firstMatch(mot commence par 'G') -> Optional.of("Gamma").
 *   allStartWithUppercase -> true (les 3 commencent par une majuscule).
 *   anyContainsDigit -> true ("Beta2" contient '2').
 *   noneAreEmpty -> true (aucun mot n'est une chaine vide).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - String.chars() renvoie un IntStream des VALEURS UNICODE de
 *     chaque caractere (utile pour tester chaque caractere un par un
 *     avec anyMatch(Character::isDigit) - Character.isDigit(int)
 *     existe bien, en plus de la version char).
 *   - Sur un stream VIDE : allMatch -> true, anyMatch -> false,
 *     noneMatch -> true (aucun contre-exemple trouve, dans les 2 cas).
 */
public class Exercise07_SearchOperations {

    public static Optional<String> firstMatch(List<String> words, Predicate<String> predicate) {
        throw new UnsupportedOperationException("TODO 1 : implementer firstMatch()");
    }

    public static boolean allStartWithUppercase(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer allStartWithUppercase()");
    }

    public static boolean anyContainsDigit(List<String> words) {
        throw new UnsupportedOperationException("TODO 3 : implementer anyContainsDigit()");
    }

    public static boolean noneAreEmpty(List<String> words) {
        throw new UnsupportedOperationException("TODO 4 : implementer noneAreEmpty()");
    }

    public static void main(String[] args) {
        List<String> words = List.of("Alpha", "Beta2", "Gamma");

        ExerciseChecker.check("firstMatch(commence par G) == Optional.of(Gamma)",
                firstMatch(words, w -> w.startsWith("G")).equals(Optional.of("Gamma")));
        ExerciseChecker.check("firstMatch(commence par Z) == Optional.empty()",
                firstMatch(words, w -> w.startsWith("Z")).isEmpty());

        ExerciseChecker.check("allStartWithUppercase == true", allStartWithUppercase(words));
        ExerciseChecker.check("anyContainsDigit == true", anyContainsDigit(words));
        ExerciseChecker.check("noneAreEmpty == true", noneAreEmpty(words));

        List<String> withLowercase = List.of("Alpha", "beta", "Gamma");
        ExerciseChecker.check("allStartWithUppercase == false des qu'un seul mot ne matche pas",
                !allStartWithUppercase(withLowercase));

        List<String> withEmpty = List.of("Alpha", "", "Gamma");
        ExerciseChecker.check("noneAreEmpty == false si une chaine vide est presente",
                !noneAreEmpty(withEmpty));

        ExerciseChecker.summary();
    }
}
