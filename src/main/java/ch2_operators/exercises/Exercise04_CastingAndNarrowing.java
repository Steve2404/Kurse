package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 4 - Retrecir un type : le cast est OBLIGATOIRE, et Java ne "arrondit" JAMAIS tout seul (niveau : difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise03_NumericPromotion.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Contrairement a la promotion (Exercise03, qui se fait TOUTE SEULE,
 * automatiquement, sans rien demander), RETRECIR un type (double ->
 * int, int -> byte...) risque de PERDRE de l'information - Java
 * refuse donc de le faire sans qu'on le demande EXPLICITEMENT, via
 * un cast "(NouveauType)", pour bien montrer qu'on est CONSCIENT du
 * risque.
 *
 * 2 comportements a bien distinguer :
 *   - double/float -> int/long : Java TRONQUE (jette purement la
 *     partie decimale), il n'ARRONDIT JAMAIS tout seul (9.9 devient
 *     9, PAS 10 - meme -9.9 devient -9, PAS -10 : on "coupe" toujours
 *     VERS ZERO).
 *   - int -> byte/short (ou toute reduction entre types entiers) :
 *     si la valeur ne "rentre" pas dans le nouveau type, elle
 *     BOUCLE (wraparound) plutot que de planter - (byte) 200 devient
 *     -56 (200 - 256).
 *
 *
 * ==================================================================
 * TODO 1 : narrowToByte(value)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer (byte) value.
 *
 *
 * ==================================================================
 * TODO 2 : truncateToInt(value)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer (int) value.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : narrowToByte(200) == -56 (boucle, PAS une
 * erreur). truncateToInt(9.9) == 9 (tronque, PAS arrondi a 10).
 * truncateToInt(-9.9) == -9 (tronque VERS ZERO, PAS vers -10).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "(byte) value" ou value est un int : le cast est ECRIT
 *     EXPLICITEMENT ici, contrairement a la promotion de l'Exercise03
 *     qui, elle, ne s'ecrit JAMAIS.
 */
public class Exercise04_CastingAndNarrowing {

    public static byte narrowToByte(int value) {
        throw new UnsupportedOperationException("TODO 1 : implementer narrowToByte()");
    }

    public static int truncateToInt(double value) {
        throw new UnsupportedOperationException("TODO 2 : implementer truncateToInt()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("narrowToByte(200) == -56 (boucle, wraparound)", narrowToByte(200) == -56);
        ExerciseChecker.check("truncateToInt(9.9) == 9 (tronque, PAS arrondi)", truncateToInt(9.9) == 9);
        ExerciseChecker.check("truncateToInt(-9.9) == -9 (tronque VERS ZERO)", truncateToInt(-9.9) == -9);

        ExerciseChecker.summary();
    }
}
