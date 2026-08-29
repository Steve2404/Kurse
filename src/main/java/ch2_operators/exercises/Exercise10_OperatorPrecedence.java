package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 10 - La precedence des operateurs : QUI calcule en premier quand plusieurs sont melanges ? (niveau : difficile)
 * ======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise09_TernaryAndPromotion.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand plusieurs operateurs se melangent dans la MEME expression,
 * SANS aucune parenthese, Java suit un ORDRE DE PRIORITE FIXE, un peu
 * comme les maths a l'ecole ("priorite aux multiplications/divisions
 * avant les additions/soustractions") - mais BEAUCOUP plus large :
 * en GROS, du plus prioritaire au moins prioritaire : unaire
 * (++, --, !, ~) > multiplicatif (*, /, %) > additif (+, -) >
 * relationnel (<, >, <=, >=, instanceof) > egalite (==, !=) > ET
 * logique (&&) > OU logique (||) > ternaire (?:) > affectation (=,
 * +=...). A priorite EGALE, Java lit de GAUCHE A DROITE (sauf pour
 * l'affectation, qui, elle, se lit de DROITE A GAUCHE).
 *
 *
 * ==================================================================
 * TODO 1 : multiplyBeforeAdd()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * 2 + 3 * 4 : la MULTIPLICATION (3 * 4 = 12) passe AVANT l'addition,
 * peu importe l'ordre d'ECRITURE - resultat : 2 + 12 = 14 (PAS (2+3)*4
 * = 20).
 *
 * -- Le plan --
 *
 *   1. Renvoyer 2 + 3 * 4.
 *
 *
 * ==================================================================
 * TODO 2 : mixedComparisonAndLogic(a, b, c)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * a + b > c && a > 0 : L'ADDITION (a + b) se fait D'ABORD (priorite
 * la plus haute des 3 familles ici), PUIS les 2 COMPARAISONS
 * (> c et > 0), PUIS ENFIN le && qui combine les 2 resultats
 * booleens.
 *
 * -- Le plan --
 *
 *   1. Renvoyer a + b > c && a > 0.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne - c'est justement l'ABSENCE de
 * parentheses qui est la lecon ici (voir Exercise11 pour le
 * contraire).
 *
 * Exemple a verifier : multiplyBeforeAdd() == 14. Avec a=3, b=5,
 * c=6 : mixedComparisonAndLogic(3, 5, 6) == true (3+5=8 > 6 VRAI, ET
 * 3 > 0 VRAI, donc && rend true).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Comparez le resultat de multiplyBeforeAdd() (14) avec celui de
 *     forcedAdditionFirst() dans Exercise11 (20) : EXACTEMENT les
 *     memes nombres, un resultat DIFFERENT, juste a cause de
 *     parentheses ajoutees.
 */
public class Exercise10_OperatorPrecedence {

    public static int multiplyBeforeAdd() {
        throw new UnsupportedOperationException("TODO 1 : implementer multiplyBeforeAdd()");
    }

    public static boolean mixedComparisonAndLogic(int a, int b, int c) {
        throw new UnsupportedOperationException("TODO 2 : implementer mixedComparisonAndLogic()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("2 + 3 * 4 == 14 (multiplication AVANT addition)", multiplyBeforeAdd() == 14);

        ExerciseChecker.check("mixedComparisonAndLogic(3, 5, 6) == true (addition, puis comparaisons, puis &&)",
                mixedComparisonAndLogic(3, 5, 6));

        ExerciseChecker.summary();
    }
}
