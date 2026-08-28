package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 4 - StringBuilder : MUTABLE, et le chainage de methodes (niveau : moyen)
 * ==========================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Contrairement a un String (grave dans la pierre, voir Exercise01),
 * un StringBuilder est un CARNET DE BROUILLON : append(), insert(),
 * delete()... modifient DIRECTEMENT le MEME objet en memoire, sans
 * jamais en creer un nouveau. La PLUPART des methodes de
 * StringBuilder rendent en plus une reference vers CE MEME objet
 * (this) - ce qui permet de les ENCHAINER les unes a la suite des
 * autres (sb.append(...).append(...).append(...)), un peu comme
 * ajouter plusieurs ingredients au MEME bol de cuisine, sans jamais
 * changer de bol entre 2 ajouts.
 *
 *
 * ==================================================================
 * TODO 1 : buildViaChaining(name)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec name = "Ada", le resultat attendu est "Bonjour, Ada !".
 *
 * -- Le plan --
 *
 *   1. Renvoyer new StringBuilder().append("Bonjour, ").append(name).append(" !").toString()
 *      - 3 append() ENCHAINES sur le MEME objet, puis toString() a la
 *      toute fin pour recuperer un vrai String.
 *
 *
 * ==================================================================
 * TODO 2 : appendExclamations(sb, count)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Contrairement a l'Exercise01 (ou il fallait TOUJOURS recuperer le
 * retour), ici PAS BESOIN de renvoyer quoi que ce soit : sb est
 * DIRECTEMENT modifie en memoire, et l'appelant (qui a le MEME objet
 * entre les mains) voit le changement sans rien recuperer.
 *
 * -- Le plan --
 *
 *   1. Repeter count fois : sb.append("!").
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne (ou une petite boucle).
 *
 * Exemple a verifier : buildViaChaining("Ada") == "Bonjour, Ada !".
 * StringBuilder sb = new StringBuilder("Wow"); appendExclamations(sb, 3);
 * sb.toString() == "Wow!!!" (sb, le MEME objet, a bien change - AUCUN
 * retour n'a ete recupere pour ca, contrairement a un String).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "for (int i = 0; i < count; i++) sb.append(\"!\");"
 */
public class Exercise04_StringBuilderBasics {

    public static String buildViaChaining(String name) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildViaChaining()");
    }

    public static void appendExclamations(StringBuilder sb, int count) {
        throw new UnsupportedOperationException("TODO 2 : implementer appendExclamations()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("buildViaChaining() enchaine 3 append() sur le MEME objet",
                buildViaChaining("Ada").equals("Bonjour, Ada !"));

        StringBuilder sb = new StringBuilder("Wow");
        appendExclamations(sb, 3);
        ExerciseChecker.check("appendExclamations() mute sb SANS aucun retour a recuperer",
                sb.toString().equals("Wow!!!"));

        ExerciseChecker.summary();
    }
}
