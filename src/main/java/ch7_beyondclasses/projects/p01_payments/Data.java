package ch7_beyondclasses.projects.p01_payments;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier).
 * "CARD numero titulaire" | "IBAN iban titulaire" | "VOUCHER code solde_en_centimes".
 */
public final class Data {

    public static final String[] METHODS = {
            "CARD 4539578763621486 Alice",
            "CARD 4539578763621487 Bob",
            "IBAN FR1420041010050500013M02606 Chloe",
            "IBAN GB82WEST12345698765432 Dan",
            "IBAN DE89370400440532013001 Eve",
            "VOUCHER ABCD-1234-X 5000",
            "CARD 4539578763621486 Fanny"};

    /** Le montant (en centimes) a payer avec chaque moyen, dans le meme ordre. */
    public static final long[] AMOUNTS = {45_000, 1_000, 120_000, 80_000, 5_000, 2_500, 600_000};

    private Data() {
    }
}
