package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 6 - Blocs de texte (""") et classes wrapper : un String multi-lignes, et des methodes sur un "nombre" (niveau : moyen)
 * =========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise04_PrimitivesVsReferenceTypes.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un bloc de texte commence par """ (3 guillemets) SUR SA PROPRE
 * LIGNE, PUIS le VRAI contenu commence a la ligne SUIVANTE, et se
 * termine par """ (encore 3 guillemets). PIEGE : si le """ final est,
 * LUI AUSSI, sur sa propre ligne (une ligne a lui tout seul), le
 * texte se termine par un SAUT DE LIGNE INVISIBLE - mais si """ est
 * colle a la FIN de la derniere ligne de texte (sans jamais aller a
 * la ligne avant), il n'y a PAS de saut de ligne final.
 *
 * Une classe wrapper (Integer pour int, Boolean pour boolean...) est
 * un type de REFERENCE (voir Exercise04) qui "enveloppe" un
 * primitif - et lui ajoute des METHODES que le primitif NU n'a
 * jamais eues (compareTo(), par exemple : un int, lui, n'a AUCUNE
 * methode du tout, seul Integer en a).
 *
 *
 * ==================================================================
 * TODO 1 : withTrailingBreak()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer """ suivi d'un retour a la ligne, PUIS "Hello", PUIS
 *      un retour a la ligne, PUIS """ SUR SA PROPRE LIGNE (pas collee
 *      a "Hello").
 *
 *
 * ==================================================================
 * TODO 2 : withoutTrailingBreak()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Meme contenu ("Hello"), mais cette fois """ colle
 *      DIRECTEMENT a la fin de "Hello", sans jamais aller a la ligne
 *      avant.
 *
 *
 * ==================================================================
 * TODO 3 : compareBoxedValue(value, other)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ranger value (un int) dans un Integer (autoboxing automatique,
 *      voir le chapitre "Methods").
 *   2. Renvoyer boxed.compareTo(other) - une methode que SEUL le
 *      wrapper possede, jamais le int nu.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : withTrailingBreak().equals("Hello\n") (le
 * saut de ligne final est present). withoutTrailingBreak().equals("Hello")
 * (AUCUN saut de ligne final). compareBoxedValue(5, 10) < 0 (5 est
 * plus petit que 10, compareTo() rend un nombre negatif).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Integer.compareTo(autre) rend un nombre NEGATIF si "this" est
 *     plus petit, POSITIF s'il est plus grand, 0 si egal - PAS
 *     forcement -1/0/1 exactement, juste le SIGNE compte.
 */
public class Exercise06_TextBlocksAndWrapperClasses {

    public static String withTrailingBreak() {
        throw new UnsupportedOperationException("TODO 1 : implementer withTrailingBreak()");
    }

    public static String withoutTrailingBreak() {
        throw new UnsupportedOperationException("TODO 2 : implementer withoutTrailingBreak()");
    }

    public static int compareBoxedValue(int value, Integer other) {
        throw new UnsupportedOperationException("TODO 3 : implementer compareBoxedValue()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("withTrailingBreak() garde le saut de ligne final (\"\"\" sur sa propre ligne)",
                withTrailingBreak().equals("Hello\n"));
        ExerciseChecker.check("withoutTrailingBreak() n'a AUCUN saut de ligne final (\"\"\" colle au texte)",
                withoutTrailingBreak().equals("Hello"));

        ExerciseChecker.check("compareBoxedValue(5, 10) < 0 (Integer.compareTo(), une methode du wrapper)",
                compareBoxedValue(5, 10) < 0);

        ExerciseChecker.summary();
    }
}
