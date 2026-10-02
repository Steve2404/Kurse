package ch4_coreapis.projects.p01_textstats;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Le texte a analyser : 3 lignes, ponctuation comprise (sans accents). */
    public static final String TEXT = """
            Le radar du kayak detecte un rotor : Anna et Bob notent le niveau.
            Elle dit que le radar et le kayak sont des palindromes, comme rotor.
            Un radar, un kayak, un rotor ; Bob a tout note dans son carnet.
            """;

    /** Une ligne "sale" : espaces autour, tabulation echappee a interpreter. */
    public static final String MESSY = "   Total\\tfinal :   42 points   ";

    /** Le mot a censurer. */
    public static final String CENSORED = "radar";

    private Data() {
    }
}
