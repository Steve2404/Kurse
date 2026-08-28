package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 10 - La classe Math : certaines methodes marchent sur TOUT nombre, d'autres SEULEMENT sur double (niveau : difficile)
 * =========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise06_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Math.min(), Math.max() et Math.abs() sont chacune SURCHARGEES pour
 * TOUS les types numeriques primitifs (int, long, float, double) :
 * le type de retour SUIT simplement le type donne en entree.
 * Math.ceil() (arrondir vers le HAUT) et Math.floor() (arrondir vers
 * le BAS), EUX, n'existent QU'EN UNE SEULE VERSION, qui prend ET rend
 * TOUJOURS un double, meme pour arrondir un nombre "presque entier".
 *
 * PIEGE CLASSIQUE de l'examen : Math.round() existe en 2 versions
 * dont le TYPE DE RETOUR differe VRAIMENT selon le type d'entree -
 * round(float) rend un int, mais round(double) rend un long (PAS
 * un int !) - essayer de ranger round(double) directement dans un
 * int, SANS cast explicite, ne compile meme pas.
 *
 *
 * ==================================================================
 * TODO 1 : roundFloatAsInt(f)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Math.round(f) - directement rangeable dans un int
 *      (le parametre f est un float, donc round(float) est appelee,
 *      qui rend bien un int).
 *
 *
 * ==================================================================
 * TODO 2 : roundDoubleAsLong(d)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Math.round(d) - CETTE FOIS range dans un long (d
 *      est un double, donc round(double) est appelee, qui rend un
 *      long, PAS un int).
 *
 *
 * ==================================================================
 * TODO 3 : ceilingOf(value)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Math.ceil(value) - toujours un double en sortie,
 *      meme si le resultat "ressemble" a un entier.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : roundFloatAsInt(3.6f) == 4 (un int).
 * roundDoubleAsLong(3.6) == 4L (un long, PAS un int). ceilingOf(4.1)
 * == 5.0 (un double, meme si 5 "ressemble" a un entier).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "public static long roundDoubleAsLong(double d)" : le type de
 *     retour DE LA METHODE (long) doit lui-meme correspondre a ce
 *     que rend Math.round(double) - impossible d'ecrire "int" en
 *     type de retour ici sans cast explicite.
 */
public class Exercise10_MathReturnTypes {

    public static int roundFloatAsInt(float f) {
        throw new UnsupportedOperationException("TODO 1 : implementer roundFloatAsInt()");
    }

    public static long roundDoubleAsLong(double d) {
        throw new UnsupportedOperationException("TODO 2 : implementer roundDoubleAsLong()");
    }

    public static double ceilingOf(double value) {
        throw new UnsupportedOperationException("TODO 3 : implementer ceilingOf()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("Math.round(float) rend un int", roundFloatAsInt(3.6f) == 4);
        ExerciseChecker.check("Math.round(double) rend un long (PAS un int)", roundDoubleAsLong(3.6) == 4L);
        ExerciseChecker.check("Math.ceil() rend TOUJOURS un double", ceilingOf(4.1) == 5.0);

        ExerciseChecker.summary();
    }
}
