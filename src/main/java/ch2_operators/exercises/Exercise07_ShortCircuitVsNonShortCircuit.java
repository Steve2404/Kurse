package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 7 - && / || (court-circuit) vs & / | (jamais court-circuit) : LE cote droit est-il evalue ? (niveau : difficile)
 * =======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise06_RelationalOperatorsAndInstanceof.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * && (ET) et || (OU) sont dits "court-circuit" : des que le cote
 * GAUCHE suffit DEJA a connaitre le resultat final (false pour un
 * &&, true pour un ||), Java n'evalue MEME PAS le cote DROIT - un peu
 * comme arreter de lire une phrase des qu'on en connait deja la fin.
 * & et | (versions SANS court-circuit, aussi utilisables sur des
 * boolean, pas seulement en binaire sur des entiers) evaluent
 * TOUJOURS les 2 cotes, meme quand le premier suffisait deja - utile
 * SEULEMENT si le cote droit a un EFFET DE BORD (comme incrementer un
 * compteur) qu'on veut absolument declencher, quoi qu'il arrive.
 *
 *
 * ==================================================================
 * TODO 1 : evaluateWithShortCircuit(flag)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec flag = false : "flag && sideEffect()" - le cote gauche (false)
 * suffit DEJA a savoir que le && sera false - sideEffect() n'est
 * JAMAIS appelee, le compteur reste a 0.
 *
 * -- Le plan --
 *
 *   1. Reinitialiser counter a 0.
 *   2. Evaluer flag && sideEffect() (le resultat du booleen
 *      lui-meme n'est pas utilise ici, seul l'effet de bord sur
 *      counter nous interesse).
 *   3. Renvoyer counter.
 *
 *
 * ==================================================================
 * TODO 2 : evaluateWithoutShortCircuit(flag)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec flag = false : "flag & sideEffect()" - MEME AVEC un cote
 * gauche deja false, & evalue QUAND MEME le cote droit -
 * sideEffect() EST appelee, counter passe a 1.
 *
 * -- Le plan --
 *
 *   1. Reinitialiser counter a 0.
 *   2. Evaluer flag & sideEffect().
 *   3. Renvoyer counter.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes - sideEffect() (deja fournie
 * plus bas) EST deja la "boite" qui produit l'effet de bord observe.
 *
 * Exemple a verifier : evaluateWithShortCircuit(false) == 0
 * (sideEffect() jamais appelee). evaluateWithoutShortCircuit(false)
 * == 1 (sideEffect() appelee QUAND MEME, malgre le cote gauche deja
 * false).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "flag && sideEffect();" (avec point-virgule final) est une
 *     instruction valide a part entiere : le resultat booleen n'a
 *     pas besoin d'etre range dans une variable pour que l'effet de
 *     bord (ou son ABSENCE) se produise.
 */
public class Exercise07_ShortCircuitVsNonShortCircuit {

    private static int counter = 0;

    private static boolean sideEffect() {
        counter++;
        return true;
    }

    public static int evaluateWithShortCircuit(boolean flag) {
        throw new UnsupportedOperationException("TODO 1 : implementer evaluateWithShortCircuit()");
    }

    public static int evaluateWithoutShortCircuit(boolean flag) {
        throw new UnsupportedOperationException("TODO 2 : implementer evaluateWithoutShortCircuit()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("&& court-circuite : sideEffect() JAMAIS appelee quand flag=false",
                evaluateWithShortCircuit(false) == 0);

        ExerciseChecker.check("& n'evite RIEN : sideEffect() appelee QUAND MEME meme si flag=false",
                evaluateWithoutShortCircuit(false) == 1);

        ExerciseChecker.summary();
    }
}
