package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 8 - &, | et ^ marchent AUSSI directement sur des boolean (pas seulement en binaire) (niveau : moyen)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise07_ShortCircuitVsNonShortCircuit.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * & et | (Exercise07) fonctionnent directement sur des boolean, sans
 * jamais passer par du binaire - mais il existe un 3e operateur de
 * cette famille, ^ (XOR, "ou exclusif"), qui, LUI, N'A PAS
 * d'equivalent court-circuit du tout (il n'existe pas de "^^") :
 * a ^ b rend true si a ET b sont DIFFERENTS l'un de l'autre (l'un
 * vrai, l'autre faux - peu importe lequel), et false s'ils sont
 * PAREILS (tous les 2 vrais, ou tous les 2 faux).
 *
 *
 * ==================================================================
 * TODO 1 : exactlyOneTrue(a, b)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * exactlyOneTrue(true, false) : les 2 sont DIFFERENTS -> true.
 * exactlyOneTrue(true, true) : les 2 sont PAREILS -> false.
 *
 * -- Le plan --
 *
 *   1. Renvoyer a ^ b.
 *
 *
 * ==================================================================
 * TODO 2 : bothSameValue(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'exact INVERSE du TODO 1 : "les 2 sont-ils PAREILS ?" - il suffit
 * d'inverser (!) le resultat de a ^ b.
 *
 * -- Le plan --
 *
 *   1. Renvoyer !(a ^ b).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : exactlyOneTrue(true, false) == true.
 * exactlyOneTrue(true, true) == false. bothSameValue(true, true) ==
 * true. bothSameValue(false, true) == false.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Les parentheses autour de "a ^ b" dans "!(a ^ b)" sont
 *     OBLIGATOIRES : sans elles, Java essaierait d'appliquer ! a "a"
 *     tout seul AVANT le ^, ce qui donnerait un resultat
 *     completement different.
 */
public class Exercise08_BooleanBitwiseOperators {

    public static boolean exactlyOneTrue(boolean a, boolean b) {
        throw new UnsupportedOperationException("TODO 1 : implementer exactlyOneTrue()");
    }

    public static boolean bothSameValue(boolean a, boolean b) {
        throw new UnsupportedOperationException("TODO 2 : implementer bothSameValue()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("exactlyOneTrue(true, false) == true (differents)", exactlyOneTrue(true, false));
        ExerciseChecker.check("exactlyOneTrue(true, true) == false (pareils)", !exactlyOneTrue(true, true));

        ExerciseChecker.check("bothSameValue(true, true) == true", bothSameValue(true, true));
        ExerciseChecker.check("bothSameValue(false, true) == false", !bothSameValue(false, true));

        ExerciseChecker.summary();
    }
}
