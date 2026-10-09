package ch18_design.projects.p02_shipping.solution;

/**
 * Une surcharge, ajoutee a n'importe quel transporteur : la COMPOSITION remplace l'heritage
 * (sinon il faudrait PostFragile, ExpressFragile, PostFragileHorsUE...). Une seule methode abstraite :
 * une strategie peut donc s'ecrire en lambda.
 */
@FunctionalInterface
public interface Surcharge {

    /** Le supplement en centimes, calcule sur le prix de BASE du transporteur. */
    long amount(Parcel parcel, long basePrice);
}
