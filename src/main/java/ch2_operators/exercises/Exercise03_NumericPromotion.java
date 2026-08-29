package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 3 - Promotion numerique : le resultat "grandit" TOUT SEUL pour ne rien perdre (niveau : moyen/difficile)
 * ==========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Melanger 2 types numeriques DIFFERENTS dans une operation
 * (a + b) oblige Java a choisir UN SEUL type pour le resultat - la
 * regle est TOUJOURS "grandir vers le plus grand, jamais vers le
 * plus petit" (pour ne jamais perdre de precision) :
 *   - byte, short et char sont TOUJOURS promus en int AU MINIMUM,
 *     meme quand on additionne 2 byte entre eux - le resultat d'une
 *     addition n'est JAMAIS un byte, meme si les 2 entrees en
 *     etaient.
 *   - au-dela, le resultat "grandit" vers le PLUS LARGE des 2 types
 *     impliques : int + long -> long. int + double -> double.
 *     N'IMPORTE QUEL entier + double -> double (double "gagne"
 *     toujours face a un entier, aussi grand soit-il).
 *
 *
 * ==================================================================
 * TODO 1 : sumBytes(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer a + b - remarquez que le TYPE DE RETOUR de la
 *      methode est deja int, PAS byte : a + b promeut AUTOMATIQUEMENT
 *      en int, meme si a et b sont TOUS LES DEUX des byte.
 *
 *
 * ==================================================================
 * TODO 2 : addIntAndDouble(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer a + b - le TYPE DE RETOUR est double : a (un int) est
 *      promu en double pour "matcher" b, le plus large des 2.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne - c'est justement la PROMOTION
 * AUTOMATIQUE qui est la lecon ici, pas un calcul complique.
 *
 * Exemple a verifier : sumBytes((byte) 100, (byte) 50) == 150 (deja
 * PLUS GRAND que la limite d'un byte, 127 - la preuve que le
 * resultat n'est VRAIMENT plus un byte). addIntAndDouble(5, 2.5) ==
 * 7.5.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "public static int sumBytes(byte a, byte b)" : le type de
 *     retour de la METHODE (int) doit deja correspondre au type
 *     REELLEMENT promu de a + b - impossible d'ecrire "byte" en
 *     retour ici sans un cast explicite.
 */
public class Exercise03_NumericPromotion {

    public static int sumBytes(byte a, byte b) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumBytes()");
    }

    public static double addIntAndDouble(int a, double b) {
        throw new UnsupportedOperationException("TODO 2 : implementer addIntAndDouble()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("sumBytes(100, 50) == 150 (deja plus grand qu'un byte, preuve de la promotion en int)",
                sumBytes((byte) 100, (byte) 50) == 150);

        ExerciseChecker.check("addIntAndDouble(5, 2.5) == 7.5 (int promu en double)",
                addIntAndDouble(5, 2.5) == 7.5);

        ExerciseChecker.summary();
    }
}
