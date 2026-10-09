package ch19_final.projects.p07_review.solution;

/**
 * Les paliers. Une enum plutot que des String : "GOLD" == "GOLD" peut etre faux (deux objets String
 * differents), une faute de frappe ("GLOD") compile, et le compilateur ne connait pas la liste des valeurs.
 */
public enum Tier {
    BRONZE, SILVER, GOLD;

    /** GOLD des 1000 points (1000 compris), SILVER des 300 : le cahier des charges dit "a partir de". */
    public static Tier of(int points) {
        if (points >= 1000) {
            return GOLD;
        }
        return points >= 300 ? SILVER : BRONZE;
    }
}
