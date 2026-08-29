package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 6 - Operateurs relationnels et instanceof : comparer des nombres, verifier un type (niveau : moyen)
 * ========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * &lt;, &gt;, &lt;=, &gt;= ne fonctionnent QUE sur des types
 * numeriques (jamais sur des boolean ou des objets, contrairement a
 * == et != qui, eux, acceptent aussi les objets). Chacun rend
 * TOUJOURS un boolean, jamais autre chose. instanceof, lui, est
 * different : c'est LE SEUL operateur qui teste "est-ce que cet
 * OBJET est bien de CE type-la (ou d'un sous-type) ?" - il rend AUSSI
 * un boolean, mais s'applique UNIQUEMENT a des references d'objets,
 * jamais a des primitifs.
 *
 *
 * ==================================================================
 * TODO 1 : isInRange(value, min, max)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * isInRange(5, 1, 10) : 5 est bien ENTRE 1 et 10 (bornes incluses) ->
 * true. isInRange(1, 1, 10) : 1 EST la borne min (incluse) -> true
 * aussi. isInRange(11, 1, 10) -> false.
 *
 * -- Le plan --
 *
 *   1. Renvoyer value >= min && value <= max (les 2 bornes sont
 *      INCLUSES, d'ou >= et <=, jamais > et <).
 *
 *
 * ==================================================================
 * TODO 2 : isNumberType(obj)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer obj instanceof Number - Number est la SUPER-CLASSE
 *      commune d'Integer, Double, Long... : instanceof rend true pour
 *      N'IMPORTE LEQUEL de ses sous-types, pas seulement Number
 *      lui-meme.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : isInRange(5, 1, 10) == true. isInRange(1, 1,
 * 10) == true (borne incluse). isInRange(11, 1, 10) == false.
 * isNumberType(42) == true (un int autoboxe en Integer, un
 * sous-type de Number). isNumberType("42") == false (un String n'est
 * PAS un Number, meme s'il "ressemble" a un nombre).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - isNumberType(42) : le int litteral 42, passe a une methode
 *     attendant un Object, est AUTOBOXE en Integer avant meme d'etre
 *     teste (voir le chapitre "Methods").
 */
public class Exercise06_RelationalOperatorsAndInstanceof {

    public static boolean isInRange(int value, int min, int max) {
        throw new UnsupportedOperationException("TODO 1 : implementer isInRange()");
    }

    public static boolean isNumberType(Object obj) {
        throw new UnsupportedOperationException("TODO 2 : implementer isNumberType()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("isInRange(5, 1, 10) == true", isInRange(5, 1, 10));
        ExerciseChecker.check("isInRange(1, 1, 10) == true (borne INCLUSE)", isInRange(1, 1, 10));
        ExerciseChecker.check("isInRange(11, 1, 10) == false", !isInRange(11, 1, 10));

        ExerciseChecker.check("isNumberType(42) == true (Integer est un sous-type de Number)", isNumberType(42));
        ExerciseChecker.check("isNumberType(\"42\") == false (un String n'est PAS un Number)",
                !isNumberType("42"));

        ExerciseChecker.summary();
    }
}
