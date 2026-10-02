package ch9_collections.projects.p03_graphs;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Prerequis de cours : "A>B" = il faut A avant B. */
    public static final String[] COURSES = {"bases>poo", "bases>algo", "poo>collections", "algo>collections", "collections>streams", "poo>lambdas",
            "lambdas>streams", "streams>concurrence", "bases>io", "io>jdbc", "collections>jdbc"};

    /** Les memes prerequis, plus un cycle. */
    public static final String[] CYCLIC = {"a>b", "b>c", "c>a", "c>d"};

    /** Routes a double sens : "ville1 ville2 km". */
    public static final String[] ROADS = {"Paris Lyon 465", "Paris Lille 225", "Paris Nantes 385", "Lyon Marseille 315", "Lyon Geneve 150",
            "Nantes Bordeaux 345", "Bordeaux Toulouse 245", "Toulouse Marseille 405", "Lille Bruxelles 110", "Ajaccio Bastia 150"};

    public static final String FROM = "Lille";
    public static final String TO = "Toulouse";

    /** Mesures et taille de la fenetre glissante. */
    public static final int[] MEASURES = {4, 2, 12, 3, 8, 8, 1, 5, 9, 2};
    public static final int WINDOW = 3;

    private Data() {
    }
}
