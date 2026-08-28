package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 1 - String : immuable, et l'operateur + qui change de sens EN COURS DE ROUTE (niveau : moyen)
 * ================================================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un String, c'est comme un message GRAVE DANS LA PIERRE : une fois
 * ecrit, IMPOSSIBLE de le modifier - toute methode qui "a l'air" de le
 * changer (concat(), toUpperCase()...) grave en fait une TOUTE
 * NOUVELLE pierre a cote, et rend l'ADRESSE de cette nouvelle pierre -
 * la pierre ORIGINALE, elle, reste exactement comme avant. Si tu
 * n'utilises jamais cette adresse rendue (le retour de la methode),
 * ton travail est perdu.
 *
 * L'operateur + a 2 COMPORTEMENTS totalement differents, decides
 * SEULEMENT par ce qui se trouve immediatement A SA GAUCHE (le
 * resultat DEJA calcule des morceaux precedents, PAS l'expression
 * entiere) : si ce cote gauche est DEJA un String, + CONCATENE
 * (colle du texte) ; sinon (2 nombres), + ADDITIONNE normalement. Et
 * Java lit TOUJOURS de GAUCHE A DROITE.
 *
 *
 * ==================================================================
 * TODO 1 : leftToRightConcat1()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * "1" + 2 + 3 : on lit de GAUCHE A DROITE. "1" (String) + 2 -> le
 * cote gauche est DEJA un String -> concatenation -> "12". PUIS "12"
 * (String) + 3 -> encore un String a gauche -> concatenation -> "123".
 *
 * -- Le plan --
 *
 *   1. Renvoyer "1" + 2 + 3.
 *
 *
 * ==================================================================
 * TODO 2 : leftToRightConcat2()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * 1 + 2 + "3" : on lit de GAUCHE A DROITE. 1 + 2 -> le cote gauche
 * (1) est un NOMBRE, PAS encore de String en vue -> addition
 * normale -> 3 (un int). PUIS 3 (int) + "3" -> le cote gauche est
 * MAINTENANT un nombre, mais le DROIT est un String -> des qu'UN SEUL
 * des 2 cotes est un String, + concatene -> "33".
 *
 * -- Le plan --
 *
 *   1. Renvoyer 1 + 2 + "3".
 *
 *
 * ==================================================================
 * TODO 3 : shoutedVersion(original)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut la version "en majuscules avec un point d'exclamation" du
 * message, SANS jamais toucher a l'original (qui doit rester
 * intact pour l'appelant).
 *
 * -- Le plan --
 *
 *   1. Renvoyer original.toUpperCase().concat("!") - 2 appels
 *      enchaines, chacun rendant une TOUTE NOUVELLE String ; original
 *      lui-meme n'est JAMAIS modifie par ces appels.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : leftToRightConcat1() == "123".
 * leftToRightConcat2() == "33" (remarquez la DIFFERENCE avec le
 * TODO1, malgre des chiffres presque identiques !). Avec original =
 * "bonjour", shoutedVersion(original) == "BONJOUR!", ET original
 * vaut TOUJOURS "bonjour" apres l'appel (immuable).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Java ne regarde JAMAIS l'expression "dans son ensemble" pour
 *     decider + : SEULEMENT le resultat deja calcule juste avant,
 *     etape par etape, de gauche a droite.
 */
public class Exercise01_StringImmutabilityAndConcatenation {

    public static String leftToRightConcat1() {
        throw new UnsupportedOperationException("TODO 1 : implementer leftToRightConcat1()");
    }

    public static String leftToRightConcat2() {
        throw new UnsupportedOperationException("TODO 2 : implementer leftToRightConcat2()");
    }

    public static String shoutedVersion(String original) {
        throw new UnsupportedOperationException("TODO 3 : implementer shoutedVersion()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("\"1\" + 2 + 3 == \"123\" (String a gauche des le debut : concatenation)",
                leftToRightConcat1().equals("123"));
        ExerciseChecker.check("1 + 2 + \"3\" == \"33\" (addition d'abord, concatenation ensuite)",
                leftToRightConcat2().equals("33"));

        String original = "bonjour";
        String shouted = shoutedVersion(original);
        ExerciseChecker.check("shoutedVersion(\"bonjour\") == \"BONJOUR!\"", shouted.equals("BONJOUR!"));
        ExerciseChecker.check("original reste INCHANGE (String immuable)", original.equals("bonjour"));

        ExerciseChecker.summary();
    }
}
