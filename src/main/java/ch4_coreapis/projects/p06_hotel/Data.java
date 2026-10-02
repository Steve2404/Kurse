package ch4_coreapis.projects.p06_hotel;

/**
 * Les donnees du projet 6 - CAPSTONE (DONNEES, ne pas modifier).
 */
public final class Data {

    /** numero;type;prix de la nuit en euros (2 decimales) */
    public static final String[] ROOMS = {
        "101;SIMPLE;80.00",
        "102;DOUBLE;120.50",
        "201;SUITE;245.00"
    };

    /** code;client (casse et espaces aleatoires);chambre;arrivee;depart (le depart n'est pas une nuit) */
    public static final String[] BOOKINGS = {
        "B1;  lea MARTIN ;101;2026-12-18;2026-12-21",
        "B2;hugo durand;102;2026-12-20;2026-12-24",
        "B3;  INES petit;101;2026-12-21;2026-12-23",
        "B4;tom ROBERT  ;102;2026-12-23;2026-12-26",
        "B5;zoe BERNARD;201;2026-12-24;2026-12-27",
        "B6; adam leroy ;101;2026-12-22;2026-12-24"
    };

    /** Premier jour du planning, et nombre de jours affiches. */
    public static final String PLANNING_START = "2026-12-18";
    public static final int PLANNING_DAYS = 10;

    /** Majoration des nuits du vendredi et du samedi, en pourcents. */
    public static final int WEEKEND_PERCENT = 20;

    private Data() {
    }
}
