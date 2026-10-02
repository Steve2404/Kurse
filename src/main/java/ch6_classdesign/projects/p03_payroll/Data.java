package ch6_classdesign.projects.p03_payroll;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    /**
     * Le personnel, une ligne par personne : "id type nom salaire_en_centimes id_du_manager [heures_sup]".
     * Types : M = manager, E = ingenieur (avec heures supplementaires), I = stagiaire. Le manager 0 = personne.
     */
    public static final String[] STAFF = {
            "1 M Alice 600000 0",
            "2 M Bruno 450000 1",
            "3 M Chloe 470000 1",
            "4 E David 380000 2 10",
            "5 E Emma 400000 2 0",
            "6 I Farid 90000 2",
            "7 E Gina 390000 3 5",
            "8 M Hugo 420000 3",
            "9 E Ines 360000 8 20",
            "10 I Jules 120000 8"};

    /** Les paires dont on cherche le plus proche manager commun. */
    public static final String[] PAIRS = {"David Ines", "Ines Jules", "Gina Hugo", "Emma Farid"};

    private Data() {
    }
}
