package ch7_beyondclasses.projects.p06_fleet;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 * Une liaison : "lieuA lieuB MODE km" (a double sens). Modes : ROAD, SEA, AIR.
 */
public final class Data {

    public static final String[] PLACES = {"Ville", "Port", "Aeroport", "Village", "Phare", "Ile"};

    public static final String[] LINKS = {
            "Ville Port ROAD 30", "Ville Aeroport ROAD 20", "Ville Village ROAD 50", "Village Phare ROAD 15",
            "Port Ile SEA 45", "Port Phare SEA 25", "Phare Ile SEA 30",
            "Aeroport Ile AIR 150", "Village Ile AIR 40", "Ville Ile AIR 70"};

    /** Le trajet demande a chaque vehicule. */
    public static final String FROM = "Ville";
    public static final String TO = "Ile";

    private Data() {
    }
}
