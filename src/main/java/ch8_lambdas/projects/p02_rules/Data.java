package ch8_lambdas.projects.p02_rules;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 * Langage des regles :
 * atomes : len>=N, len<=N, digit, upper, lower, space, starts:XYZ, ends:XYZ, contains:XYZ
 * operateurs : ! (non), &amp; (et), | (ou), parentheses. Priorite : ! puis &amp; puis |. Mots separes par des espaces.
 */
public final class Data {

    public static final String[] RULES = {
            "STRONG = len>=8 & upper & lower & digit & ! space",
            "LENIENT = len>=6 & ( digit | upper )",
            "ADMIN = starts:adm & len>=6 | ends:!!",
            "NOJAVA = ! contains:java & ! contains:Java"};

    public static final String USER = "alice";

    public static final String[] CANDIDATES = {"motdepasse", "MotDePasse1", "court1", "Mot De Passe 9", "adminX", "Secret2026!!", "javaRocks7A", "Alice2026Pw"};

    private Data() {
    }
}
