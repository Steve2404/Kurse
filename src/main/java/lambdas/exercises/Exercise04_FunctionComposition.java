package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.function.Function;

/**
 * EXERCICE 4 - Composer des Function : andThen() vs compose(), l'ordre compte (niveau : difficile)
 * ========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 *
 * ==================================================================
 * TODO 1 et 2 : addShippingFee(fee), applyTaxRate(rate)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une caisse de magasin en ligne. Deux operations peuvent
 * s'appliquer au prix d'un article : ajouter des frais de livraison
 * fixes (+5€, peu importe le prix), et appliquer une taxe en
 * pourcentage (+20%, par exemple). Le probleme : SELON L'ORDRE dans
 * lequel tu fais ces deux operations, le total final n'est PAS le
 * meme !
 *
 * -- Essayons a la main --
 *
 * Prix de depart : 100€. Frais de livraison : +5€. Taxe : +20%.
 *
 * Ordre A - livraison PUIS taxe : (100 + 5) * 1.20 = 105 * 1.20 = 126€
 * Ordre B - taxe PUIS livraison : (100 * 1.20) + 5 = 120 + 5 = 125€
 *
 * 126€ contre 125€ : UN SEUL EURO d'ecart, mais un ecart bien reel.
 * Dans l'ordre A, la taxe s'applique AUSSI sur les frais de livraison
 * (5 * 1.20 = 6€ de frais "taxes"). Dans l'ordre B, les frais de
 * livraison arrivent APRES la taxe, ils ne sont jamais taxes.
 *
 * -- Ce qu'on remarque --
 *
 * andThen() et compose() combinent 2 Function, mais pas dans le meme
 * sens : f.andThen(g) veut dire "d'abord f, PUIS g sur le resultat"
 * (comme lire une phrase francaise, de gauche a droite). f.compose(g)
 * veut dire l'INVERSE : "d'abord g, PUIS f sur le resultat" (le nom
 * "compose" vient des maths : f(g(x)), on lit de DEDANS vers DEHORS).
 * Une meme paire de fonctions, combinee dans le mauvais sens, donne
 * un resultat different dans TOUT probleme ou l'ordre compte (comme
 * ici, a cause du "+5" qui n'est pas juste une multiplication).
 *
 * -- Le plan --
 *
 *   1. addShippingFee(fee) renvoie une Function<Double,Double> qui
 *      ajoute 'fee' au prix recu.
 *   2. applyTaxRate(rate) renvoie une Function<Double,Double> qui
 *      multiplie le prix recu par (1 + rate).
 *   3. priceWithShippingThenTax(price, fee, rate) : construire la
 *      chaine "livraison PUIS taxe" avec andThen(), puis l'appliquer
 *      a price.
 *   4. priceWithTaxThenShipping(price, fee, rate) : construire la
 *      chaine "taxe PUIS livraison", MAIS cette fois en utilisant
 *      compose() au lieu de andThen() (entrainez-vous a lire compose
 *      a l'envers), puis l'appliquer a price.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * addShippingFee() et applyTaxRate() sont deja de bonnes petites
 * boites independantes (chacune se raconte seule, ET revient utilisee
 * dans les 2 methodes suivantes - Q2 clairement "oui"). Les 2
 * methodes priceWithXxx() sont courtes, une seule ligne de chainage
 * chacune : pas besoin de les redecouper davantage.
 *
 * Exemple a verifier : priceWithShippingThenTax(100, 5, 0.20) == 126.0
 * priceWithTaxThenShipping(100, 5, 0.20) == 125.0 (different !)
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - static Function<Double, Double> addShippingFee(double fee) {
 *         return price -> price + fee;
 *     }
 *   - andThen() : shipping.andThen(tax).apply(price)
 *     equivaut a tax.apply(shipping.apply(price)) - shipping D'ABORD.
 *   - compose() : tax.compose(shipping).apply(price)
 *     equivaut EXACTEMENT au meme calcul que andThen() ci-dessus :
 *     tax.apply(shipping.apply(price)) - shipping toujours D'ABORD,
 *     mais ecrit dans l'ordre inverse. C'est le piege classique de
 *     l'examen : compose() se LIT a l'envers de andThen().
 */
public class Exercise04_FunctionComposition {

    public static Function<Double, Double> addShippingFee(double fee) {
        throw new UnsupportedOperationException("TODO 1 : implementer addShippingFee()");
    }

    public static Function<Double, Double> applyTaxRate(double rate) {
        throw new UnsupportedOperationException("TODO 2 : implementer applyTaxRate()");
    }

    public static double priceWithShippingThenTax(double price, double fee, double rate) {
        throw new UnsupportedOperationException("TODO 3 : implementer priceWithShippingThenTax()");
    }

    public static double priceWithTaxThenShipping(double price, double fee, double rate) {
        throw new UnsupportedOperationException("TODO 4 : implementer priceWithTaxThenShipping()");
    }

    public static void main(String[] args) {
        double shippingThenTax = priceWithShippingThenTax(100, 5, 0.20);
        ExerciseChecker.check("(100 + 5) * 1.20 == 126.0", Math.abs(shippingThenTax - 126.0) < 0.0001);

        double taxThenShipping = priceWithTaxThenShipping(100, 5, 0.20);
        ExerciseChecker.check("(100 * 1.20) + 5 == 125.0", Math.abs(taxThenShipping - 125.0) < 0.0001);

        ExerciseChecker.check("l'ordre change bien le resultat (126.0 != 125.0)",
                Math.abs(shippingThenTax - taxThenShipping) > 0.0001);

        ExerciseChecker.summary();
    }
}
