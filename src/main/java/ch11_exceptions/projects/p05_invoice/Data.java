package ch11_exceptions.projects.p05_invoice;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier). Montants en centimes, taux de TVA en pour mille.
 */
public final class Data {

    /** "quantite|libelle|prix unitaire HT en centimes|TVA en pour mille". */
    public static final String[] LINES = {"2|clavier|4999|200", "1|ecran|18990|200", "4|cafe|899|55", "1|formation|45000|0"};

    /** Les locales d'affichage (balises IETF). */
    public static final String[] LOCALES = {"en-US", "fr-FR", "de-DE", "de-CH", "ja-JP"};

    /** Des motifs DecimalFormat et les valeurs a leur appliquer. */
    public static final String[] PATTERNS = {"#,##0.00", "0000.##", "#.#", "#,##0.00;(#,##0.00)", "'#'000", "0.0%"};
    public static final double[] SAMPLES = {1234.5, -7.25, 0.08};

    /** Des tailles a afficher en format compact. */
    public static final long[] BIG = {999, 1_234, 1_250_000, 7_800_000_000L};

    /** Des saisies a analyser : "locale|genre|texte", genre = nombre ou monnaie. */
    public static final String[] INPUTS = {"en-US|nombre|1,234.56", "de-DE|nombre|1.234,56", "fr-FR|nombre|1 234,56", "en-US|nombre|12abc",
            "en-US|nombre|abc", "en-US|monnaie|$12.50", "en-US|monnaie|12.50"};

    /** Les poids des payeurs qui se partagent la facture. */
    public static final int[] WEIGHTS = {2, 3, 4};

    /** Le pret : capital en centimes, taux annuel en pour mille, nombre de mois. */
    public static final long LOAN = 1_000_000;
    public static final int LOAN_RATE = 36;
    public static final int MONTHS = 12;

    private Data() {
    }
}
