package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 11 - Math.random() : TOUJOURS entre 0.0 (inclus) et 1.0 (exclu), jamais 1.0 pile (niveau : moyen)
 * =====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise10_MathReturnTypes.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Math.random() rend TOUJOURS un double dans l'intervalle [0.0, 1.0)
 * - 0.0 est POSSIBLE (rarissime, mais possible), 1.0 pile ne
 * sort JAMAIS. Pour obtenir un entier ALEATOIRE dans une plage
 * PRECISE [min, max] (les 2 bornes INCLUSES cette fois), la recette
 * classique est : multiplier Math.random() par la LARGEUR de la
 * plage (max - min + 1, le "+1" pour inclure max), caster en int
 * (ce qui TRONQUE, ne garde que la partie entiere), PUIS ajouter min.
 *
 * IMPORTANT pour cet exercice : comme Math.random() n'est jamais 2
 * fois la meme chose, on ne peut PAS tester une valeur EXACTE - on
 * teste plutot une PROPRIETE toujours vraie (ici : le resultat
 * reste TOUJOURS dans la bonne plage, quelle que soit la valeur
 * aleatoire tiree), en repetant le test plusieurs fois pour etre
 * raisonnablement sur.
 *
 *
 * ==================================================================
 * TODO 1 : randomInRange(min, max)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * randomInRange(5, 10) doit TOUJOURS rendre un entier ENTRE 5 et 10,
 * les 2 bornes INCLUSES (jamais 4, jamais 11).
 *
 * -- Le plan --
 *
 *   1. Renvoyer (int) (Math.random() * (max - min + 1)) + min.
 *
 *
 * ==================================================================
 * TODO 2 : minOfThree(a, b, c)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Math.min() ne prend que 2 valeurs a la fois - pour en comparer 3,
 * il suffit d'imbriquer 2 appels : trouver d'abord le plus petit de 2
 * d'entre eux, PUIS comparer ce resultat au 3e.
 *
 * -- Le plan --
 *
 *   1. Renvoyer Math.min(a, Math.min(b, c)).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : 100 appels a randomInRange(5, 10) de suite
 * rendent TOUJOURS une valeur entre 5 et 10 (jamais en dehors).
 * minOfThree(3.5, 1.2, 7.8) == 1.2.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "(int) (...)" TRONQUE la partie decimale (ne PAS confondre avec
 *     Math.round(), qui, LUI, arrondit au plus proche).
 */
public class Exercise11_MathRandomAndMinMax {

    public static int randomInRange(int min, int max) {
        throw new UnsupportedOperationException("TODO 1 : implementer randomInRange()");
    }

    public static double minOfThree(double a, double b, double c) {
        throw new UnsupportedOperationException("TODO 2 : implementer minOfThree()");
    }

    public static void main(String[] args) {
        boolean alwaysInRange = true;
        for (int i = 0; i < 100; i++) {
            int value = randomInRange(5, 10);
            if (value < 5 || value > 10) {
                alwaysInRange = false;
                break;
            }
        }
        ExerciseChecker.check("randomInRange(5, 10) reste TOUJOURS dans [5, 10] sur 100 tirages", alwaysInRange);

        ExerciseChecker.check("minOfThree(3.5, 1.2, 7.8) == 1.2", minOfThree(3.5, 1.2, 7.8) == 1.2);

        ExerciseChecker.summary();
    }
}
