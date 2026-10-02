package ch10_streams.projects.p04_league;

import java.util.List;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** journee;equipe a domicile-equipe a l'exterieur;buts domicile-buts exterieur, dans l'ordre chronologique. */
    public static final List<String> MATCHES = List.of(
            "J1;Lions-Tigres;3-1",
            "J1;Ours-Aigles;0-0",
            "J2;Aigles-Lions;2-2",
            "J2;Tigres-Ours;2-0",
            "J3;Lions-Ours;5-0",
            "J3;Tigres-Aigles;1-2",
            "J4;Tigres-Lions;2-1",
            "J4;Aigles-Ours;3-1",
            "J5;Ours-Lions;1-1",
            "J5;Aigles-Tigres;2-1",
            "J6;Lions-Aigles;2-0",
            "J6;Ours-Tigres;0-3");

    public static final int POINTS_WIN = 3;
    public static final int POINTS_DRAW = 1;

    /** Pour verifier un combiner : on coupe la saison en deux morceaux inegaux a cet indice. */
    public static final int SPLIT_AT = 5;

    private Data() {
    }
}
