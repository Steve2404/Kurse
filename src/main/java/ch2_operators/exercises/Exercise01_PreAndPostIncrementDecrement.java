package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 1 - ++/-- AVANT vs APRES la variable : le piege prefere de l'examen (niveau : difficile)
 * ===========================================================================================================
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
 * x++ (APRES) et ++x (AVANT) font TOUS LES DEUX la meme chose au
 * final (x augmente de 1) - mais ils ne RENDENT PAS la meme VALEUR
 * dans l'expression ou ils apparaissent :
 *   - x++ (post-incrementation) : RENVOIE la valeur ACTUELLE de x
 *     (AVANT d'augmenter), PUIS augmente x - "prends d'abord ta
 *     photo, HABILLE-toi ensuite".
 *   - ++x (pre-incrementation) : augmente x D'ABORD, PUIS RENVOIE la
 *     NOUVELLE valeur - "HABILLE-toi d'abord, prends ta photo
 *     ensuite".
 * Meme logique, inversee, pour -- (decrementation).
 *
 *
 * ==================================================================
 * TODO 1 : prePostSum(start)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec start = 5 : start++ RENVOIE 5 (start devient 6 juste apres).
 * ++start (sur ce start DEJA a 6) fait passer start a 7, ET renvoie
 * 7. Somme : 5 + 7 = 12.
 *
 * -- Le plan --
 *
 *   1. Renvoyer start++ + ++start (exactement cette expression,
 *      telle quelle).
 *
 *
 * ==================================================================
 * TODO 2 : postPostDiff(start)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec start = 10 : le 1er start-- RENVOIE 10 (start devient 9). Le
 * 2eme start-- (sur ce start DEJA a 9) RENVOIE 9 (start devient 8).
 * Difference : 10 - 9 = 1.
 *
 * -- Le plan --
 *
 *   1. Renvoyer start-- - start-- (exactement cette expression).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne - c'est justement L'EXPRESSION
 * elle-meme qui est le coeur de la lecon ici.
 *
 * Exemple a verifier : prePostSum(5) == 12. postPostDiff(10) == 1.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Java evalue une expression de GAUCHE A DROITE : le 1er start++
 *     (ou start--) "prend sa photo" AVANT que le 2eme morceau de
 *     l'expression ne soit meme regarde.
 */
public class Exercise01_PreAndPostIncrementDecrement {

    public static int prePostSum(int start) {
        throw new UnsupportedOperationException("TODO 1 : implementer prePostSum()");
    }

    public static int postPostDiff(int start) {
        throw new UnsupportedOperationException("TODO 2 : implementer postPostDiff()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("prePostSum(5) == 12 (start++ rend 5, ++start rend 7)", prePostSum(5) == 12);
        ExerciseChecker.check("postPostDiff(10) == 1 (10 - 9)", postPostDiff(10) == 1);

        ExerciseChecker.summary();
    }
}
