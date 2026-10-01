package ch10_streams.projects.p08_telemetry;

import java.util.List;

/**
 * Les donnees du projet 8 (DONNEES, ne pas modifier).
 */
public final class Data {

    /**
     * serveur;region;instant (secondes epoch UTC);cpu en %;memoire en Mo;octets sortis pendant la minute.
     * Un echantillon par serveur et par minute ; cache1 a eu une panne de sonde (minutes manquantes).
     */
    public static final List<String> SAMPLES = List.of(
            "web1;EU;1790000000;35.5;4100;1200000000",
            "web2;EU;1790000000;30.0;3900;900000000",
            "db1;US;1790000000;70.0;7900;400000000",
            "cache1;ASIA;1790000000;12.0;2000;50000000",
            "web1;EU;1790000060;42.0;4300;1500000000",
            "web2;EU;1790000060;33.5;3950;950000000",
            "db1;US;1790000060;75.5;8000;420000000",
            "cache1;ASIA;1790000060;15.5;2050;60000000",
            "web1;EU;1790000120;91.5;7200;3900000000",
            "web2;EU;1790000120;38.0;4000;1000000000",
            "db1;US;1790000120;82.0;8100;450000000",
            "web1;EU;1790000180;95.0;7600;3100000000",
            "web2;EU;1790000180;41.0;4050;1100000000",
            "db1;US;1790000180;88.0;8100;470000000",
            "web1;EU;1790000240;60.0;5000;2000000000",
            "web2;EU;1790000240;39.5;4000;1050000000",
            "db1;US;1790000240;93.5;8150;500000000",
            "cache1;ASIA;1790000240;18.0;2100;70000000",
            "web1;EU;1790000300;40.5;4200;1100000000",
            "web2;EU;1790000300;36.0;3900;980000000",
            "db1;US;1790000300;79.0;8000;430000000",
            "cache1;ASIA;1790000300;11.0;2000;40000000");

    /** Premier et dernier instant de la fenetre observee, pas d'une minute. */
    public static final long WINDOW_START = 1790000000L;
    public static final long WINDOW_END = 1790000300L;
    public static final long STEP_SECONDS = 60;

    /** Regles d'alerte : nom de la mesure;seuil (alerte si la valeur est STRICTEMENT superieure). */
    public static final List<String> RULES = List.of("CPU;90", "MEMOIRE;8000", "OCTETS;3000000000");

    /** Region en maintenance : ses alertes sont masquees (mais comptees a part). */
    public static final String MAINTENANCE_REGION = "US";

    /** Points de sante retires par alerte, et plancher du score. */
    public static final int PENALTY_PER_ALERT = 15;
    public static final int MIN_HEALTH = 0;

    /** Une region demandee mais sans aucun serveur. */
    public static final String EMPTY_REGION = "AFRICA";

    private Data() {
    }
}
