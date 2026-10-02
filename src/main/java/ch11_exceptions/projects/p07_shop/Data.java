package ch11_exceptions.projects.p07_shop;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les reglages de la boutique : "cle=valeur". */
    public static final String[] SETTINGS = {"shop.name=Javashop", "shipping.free=5000", "promo.item=stylo"};

    /** Le catalogue : "produit=prix en centimes". */
    public static final String[] CATALOG = {"livre=1250", "stylo=199", "sac=3490", "lampe=2599"};

    /** Les clients : "balise de locale|prenom|produit:quantite,produit:quantite". */
    public static final String[] CUSTOMERS = {"fr-FR|Marie|livre:2,stylo:3", "fr-CA|Louis|sac:1,lampe:1", "de-DE|Jonas|lampe:2,livre:1", "it-IT|Giulia|stylo:1",
            "en|Sam|livre:1,parapluie:1", "en-US|Ann|livre:x"};

    /** Les locales dont on mesure la traduction. */
    public static final String[] COVERAGE = {"fr-CA", "fr", "de", "en"};

    private Data() {
    }
}
