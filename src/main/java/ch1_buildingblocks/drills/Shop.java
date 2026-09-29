package ch1_buildingblocks.drills;

/**
 * Les donnees du "projet caisse" partagees par TOUS les drills du chapitre 1.
 * ===========================================================================
 *
 * Toujours les memes donnees : ton cerveau se concentre sur la syntaxe
 * et les methodes, pas sur la decouverte des donnees. Lis ce fichier
 * une fois et garde-le ouvert a cote pendant les drills.
 *
 * ARGS : la ligne de commande d'une caisse (comme dans Exercise17)
 *
 *   "--client=Lea"  option avec valeur
 *   "--vip"         interrupteur
 *   "pomme:3:50"    article nom:quantite:prixEnCentimes
 *   "pain:1:120"
 *   "lait:2:99"
 *
 * RAW_QUANTITIES : des quantites tapees au clavier, bonnes ou mauvaises
 *
 *   "3"  " 12 "  "+7"  "-2"  "abc"  "2147483648"  ""
 *
 * PRODUCT_CODE : "A7-X9"      BINARY_FLAGS : "1010"      HEX_COLOR : "FF"
 */
public final class Shop {

    public static final String[] ARGS = {"--client=Lea", "--vip", "pomme:3:50", "pain:1:120", "lait:2:99"};

    public static final String[] RAW_QUANTITIES = {"3", " 12 ", "+7", "-2", "abc", "2147483648", ""};

    public static final String PRODUCT_CODE = "A7-X9";

    public static final String BINARY_FLAGS = "1010";

    public static final String HEX_COLOR = "FF";

    private Shop() {
    }
}
