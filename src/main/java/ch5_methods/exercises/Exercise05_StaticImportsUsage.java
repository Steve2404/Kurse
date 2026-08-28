package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import static java.lang.Math.PI;
import static java.lang.Math.max;

/**
 * EXERCICE 5 - Les imports static A L'OEUVRE : utiliser un membre static SANS jamais ecrire le nom de sa classe (niveau : moyen)
 * =====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * methods.exercises.Exercise01_MethodDeclarationQuiz.java (pour la
 * syntaxe des imports static, deja verifiee au quiz).
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "import static java.lang.Math.PI;" et "import static
 * java.lang.Math.max;", tout en haut de ce fichier (deja ecrits, ce
 * n'est pas le TODO), permettent d'utiliser PI et max(...)
 * DIRECTEMENT dans tout le fichier, comme s'ils avaient TOUJOURS
 * existe ici - exactement comme on n'ecrit jamais "java.lang.String"
 * en entier grace a l'import automatique de java.lang. C'est
 * purement du CONFORT DE LECTURE : Math.PI et PI designent
 * EXACTEMENT la MEME chose, seul le texte a taper change.
 *
 *
 * ==================================================================
 * TODO 1 : computeCircumference(radius)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Le perimetre d'un cercle vaut 2 * PI * rayon. Avec radius = 10,
 * ca fait environ 62.83.
 *
 * -- Le plan --
 *
 *   1. Renvoyer 2 * PI * radius - PI directement, SANS "Math." devant.
 *
 *
 * ==================================================================
 * TODO 2 : largerOf(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer max(a, b) - encore une fois, SANS "Math." devant.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : computeCircumference(10) est proche de 62.83.
 * largerOf(3, 7) == 7.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Sans les 2 lignes "import static" tout en haut du fichier, PI
 *     et max(...) ne seraient PAS reconnus tels quels - il faudrait
 *     alors ecrire Math.PI et Math.max(...) partout.
 */
public class Exercise05_StaticImportsUsage {

    public static double computeCircumference(double radius) {
        throw new UnsupportedOperationException("TODO 1 : implementer computeCircumference()");
    }

    public static int largerOf(int a, int b) {
        throw new UnsupportedOperationException("TODO 2 : implementer largerOf()");
    }

    public static void main(String[] args) {
        double circumference = computeCircumference(10);
        ExerciseChecker.check("computeCircumference(10) est proche de 62.83 -> " + circumference,
                Math.abs(circumference - 62.83) < 0.01);

        ExerciseChecker.check("largerOf(3, 7) == 7 (via max() importe sans prefixe)", largerOf(3, 7) == 7);

        ExerciseChecker.summary();
    }
}
