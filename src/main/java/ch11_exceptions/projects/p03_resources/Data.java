package ch11_exceptions.projects.p03_resources;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    /**
     * Les travaux : "ressource1 ressource2 issue". Un nom qui commence par '!' rate son OUVERTURE,
     * un nom qui commence par '~' rate sa FERMETURE ; l'issue "fail" fait echouer le travail.
     */
    public static final String[] JOBS = {"db cache ok", "db cache fail", "db ~cache fail", "~db cache ok", "db !cache ok", "~db ~cache fail"};

    /** Les reponses successives d'un service instable, essai par essai ("ok:valeur" = succes). */
    public static final String[] FLAKY = {"timeout", "timeout", "ok:42"};
    public static final String[] DOWN = {"timeout", "refus", "panne"};
    public static final int MAX_ATTEMPTS = 3;

    /** Ce que repondrait le service distant a chaque appel, pour le disjoncteur. */
    public static final String CALLS = "ok ko ko ko ok ok ok ko ok ok ko ko ko ok ok ko ok";
    public static final int THRESHOLD = 3;
    public static final int PAUSE = 2;

    private Data() {
    }
}
