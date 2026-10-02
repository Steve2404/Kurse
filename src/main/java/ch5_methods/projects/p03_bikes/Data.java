package ch5_methods.projects.p03_bikes;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les stations, dans l'ordre (indices 0 a 4). */
    public static final String[] STATIONS = {"Gare", "Centre", "Parc", "Port", "Campus"};

    /** Les velos presents au depart et la capacite de chaque station (memes indices). */
    public static final int[] BIKES = {6, 1, 0, 3, 8};
    public static final int[] CAPACITY = {8, 6, 5, 6, 10};

    /** Les routes a double sens : "A-B km". */
    public static final String[] ROADS = {"Gare-Centre 2", "Centre-Parc 3", "Parc-Port 4", "Gare-Port 9", "Centre-Campus 5",
            "Campus-Port 2", "Gare-Campus 8"};

    /** Les trajets : "depart arrivee". */
    public static final String[] TRIPS = {"Parc Gare", "Centre Port", "Centre Parc", "Campus Port", "Campus Port", "Campus Port",
            "Gare Port", "Gare Port"};

    private Data() {
    }
}
