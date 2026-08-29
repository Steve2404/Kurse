package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 5 - Les operateurs composes (+=, *=...) cachent un CAST GRATUIT que "=" tout seul n'a JAMAIS (niveau : difficile)
 * =======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise04_CastingAndNarrowing.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "byte b = 10; b += 5;" COMPILE parfaitement - mais "byte b = 10; b
 * = b + 5;" (qui a POURTANT l'air de faire EXACTEMENT la meme chose)
 * NE COMPILE PAS DU TOUT ! Le secret : "b += 5" n'est PAS un simple
 * raccourci d'ecriture pour "b = b + 5" - c'est en realite "b = (byte)
 * (b + 5)" - un CAST est INSERE GRATUITEMENT, tout seul, par le
 * compilateur, UNIQUEMENT quand on utilise la forme COMPOSEE
 * (+=, -=, *=, /=...). "b + 5" tout seul (sans le +=) promeut
 * TOUJOURS en int (voir Exercise03) - et int ne rentre jamais
 * automatiquement dans un byte, meme si le RESULTAT, lui,
 * "rentrerait" en pratique.
 *
 *
 * ==================================================================
 * TODO 1 : incrementByteViaCompound(b, amount)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. b += amount (le cast vers byte est INSERE gratuitement par le
 *      compilateur ici).
 *   2. Renvoyer b.
 *
 *
 * ==================================================================
 * TODO 2 : scaleIntViaCompound(value, factor)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * value = 10, factor = 2.5 (un double !) : 10 * 2.5 = 25.0, PUIS le
 * cast gratuit vers int (integre a *=) tronque a 25.
 *
 * -- Le plan --
 *
 *   1. value *= factor (meme principe : le cast gratuit vers int est
 *      insere tout seul, MEME en multipliant par un double).
 *   2. Renvoyer value.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en 2 lignes.
 *
 * Exemple a verifier : incrementByteViaCompound((byte) 10, 5) == 15.
 * scaleIntViaCompound(10, 2.5) == 25 (10 * 2.5 = 25.0, tronque a 25
 * par le cast gratuit int integre au *=).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Essayez, juste pour voir (PUIS remettez en commentaire), de
 *     remplacer "value *= factor" par "value = value * factor" :
 *     cette version-la NE COMPILE PAS (int = double, sans cast) -
 *     la preuve concrete que *= cache bien un cast que sa version
 *     "developpee" n'a jamais.
 */
public class Exercise05_CompoundAssignmentImplicitCast {

    public static byte incrementByteViaCompound(byte b, int amount) {
        throw new UnsupportedOperationException("TODO 1 : implementer incrementByteViaCompound()");
    }

    public static int scaleIntViaCompound(int value, double factor) {
        throw new UnsupportedOperationException("TODO 2 : implementer scaleIntViaCompound()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("incrementByteViaCompound(10, 5) == 15 (cast gratuit vers byte)",
                incrementByteViaCompound((byte) 10, 5) == 15);

        ExerciseChecker.check("scaleIntViaCompound(10, 2.5) == 25 (cast gratuit vers int, tronque)",
                scaleIntViaCompound(10, 2.5) == 25);

        ExerciseChecker.summary();
    }
}
