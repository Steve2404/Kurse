package ch10_streams.projects.p03_weather;

import java.util.List;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String STATION = "LYON-BRON-07";

    /** date;24 temperatures horaires en degres C (de 0h a 23h). Une ligne peut etre defectueuse. */
    public static final List<String> DAYS = List.of(
            "2026-07-14;18,17,16,16,17,19,22,25,27,29,30,31,31,30,29,27,25,23,21,20,19,19,18,18",
            "2026-07-15;20,19,19,18,19,21,24,27,30,32,33,34,35,35,34,32,30,28,26,24,23,22,21,21",
            "2026-07-16;21,20,20,19,20,22,25,28,31,33,35,36,37,36,35,33,31,29,27,25,24,23,22,22",
            "2026-07-17;22,21,20,20,19,19,20,23,26,28,30,31,24,21,20,19,18,18,17,17,16,16,15,15",
            "2026-07-18;15,14,14,13,14,16,19,22,24,26,28,30,31,31,30,28,26,24,22,20,19,18,17,17",
            "2026-07-19;16,15,15,14,15,17,20,23,25,27,29,30,31,30,29,27,25,23,21,20,19,18,17");

    /** Seuil (degres) au-dessus duquel la climatisation tourne. */
    public static final int AC_THRESHOLD = 24;

    /** Energie de climatisation en Wh par degre au-dessus du seuil et par heure. */
    public static final long WH_PER_DEGREE_HOUR = 350;

    /** Une journee est "caniculaire" si son maximum atteint ce seuil. */
    public static final int HEAT_WAVE_MAX = 33;

    /** Une chute strictement superieure a ce nombre de degres en une heure signale un orage. */
    public static final int STORM_DROP = 5;

    private Data() {
    }
}
