package ch7_beyondclasses.projects.p04_routes.solution;

/**
 * SOLUTION - tout ce qui a une position sur le globe.
 */
public interface Located {

    double EARTH_RADIUS_KM = 6371;

    double lat();

    double lon();

    // Formule de Haversine : la distance a vol d'oiseau sur une sphere, en km.
    default double distanceTo(Located other) {
        double p1 = Math.toRadians(lat());
        double p2 = Math.toRadians(other.lat());
        double dp = p2 - p1;
        double dl = Math.toRadians(other.lon() - lon());
        double a = Math.sin(dp / 2) * Math.sin(dp / 2) + Math.cos(p1) * Math.cos(p2) * Math.sin(dl / 2) * Math.sin(dl / 2);
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(a));
    }
}
