package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 9 - L'operateur ternaire ?: et sa promotion DECIDEE A LA COMPILATION, pas a l'execution (niveau : difficile)
 * ===================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise03_NumericPromotion.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "condition ? siVrai : siFaux" est un RACCOURCI pour un if/else qui
 * REND directement une valeur, plutot que d'executer 2 blocs
 * separes. PIEGE CLASSIQUE de l'examen : si les 2 branches (siVrai et
 * siFaux) sont de types DIFFERENTS (un int et un double, par
 * exemple), Java decide le type FINAL du ternaire ENTIER a la
 * COMPILATION, en regardant LES 2 branches EN MEME TEMPS - MEME SI,
 * a l'execution, une SEULE des 2 est reellement choisie. Consequence
 * : la branche int, meme quand c'est ELLE qui est choisie a
 * l'execution, se retrouve QUAND MEME promue en double au final -
 * exactement comme au Exercise03, mais decide ICI a l'avance, pas au
 * moment ou la branche "gagne".
 *
 *
 * ==================================================================
 * TODO 1 : max(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer a > b ? a : b.
 *
 *
 * ==================================================================
 * TODO 2 : promoteInTernary(flag, intVal, doubleVal)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec flag = true, intVal = 5, doubleVal = 2.5 : c'est la branche
 * intVal qui est choisie a l'execution - mais le TYPE DE RETOUR de
 * la methode est deja double : intVal (5) est promu en 5.0.
 *
 * -- Le plan --
 *
 *   1. Renvoyer flag ? intVal : doubleVal.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : max(3, 7) == 7. promoteInTernary(true, 5, 2.5)
 * == 5.0 (PAS l'int 5 - meme si c'est la branche intVal qui a
 * "gagne" a l'execution, le resultat reste un double).
 * promoteInTernary(false, 5, 2.5) == 2.5.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "public static double promoteInTernary(...)" : le type de
 *     retour double, deja fixe dans la signature de la methode, est
 *     la PREUVE que Java a decide le type du ternaire des la
 *     compilation - impossible d'ecrire "int" en retour ici sans un
 *     cast explicite, meme pour le cas ou flag vaut true.
 */
public class Exercise09_TernaryAndPromotion {

    public static int max(int a, int b) {
        throw new UnsupportedOperationException("TODO 1 : implementer max()");
    }

    public static double promoteInTernary(boolean flag, int intVal, double doubleVal) {
        throw new UnsupportedOperationException("TODO 2 : implementer promoteInTernary()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("max(3, 7) == 7", max(3, 7) == 7);

        ExerciseChecker.check("promoteInTernary(true, 5, 2.5) == 5.0 (int promu en double MEME choisi)",
                promoteInTernary(true, 5, 2.5) == 5.0);
        ExerciseChecker.check("promoteInTernary(false, 5, 2.5) == 2.5", promoteInTernary(false, 5, 2.5) == 2.5);

        ExerciseChecker.summary();
    }
}
