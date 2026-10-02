package ch7_beyondclasses.projects.p02_poker;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 * Une carte = rang (2-9, T, J, Q, K, A) + couleur (C, D, H, S). Exemple : "TH" = dix de coeur.
 */
public final class Data {

    /** Des mains de test, une par categorie (plus la "roue" A-2-3-4-5). */
    public static final String[] HANDS = {
            "AS KS QS JS TS", "9C 9D 9H 9S 2D", "3H 3D 3S 7C 7D", "2H 8H 5H JH KH", "9D TC JS QH KD",
            "AC 2D 3H 4S 5C", "7S 7H 7D KC 2S", "4C 4D JH JS AC", "QD QS 6C 3H 2S", "AH JD 8C 5S 3D"};

    /** La graine du melange et le nombre de joueurs. */
    public static final long SEED = 2026;
    public static final int PLAYERS = 4;

    /** Texas hold'em : les 5 cartes communes et les 2 cartes de chaque joueur. */
    public static final String BOARD = "KH 9H 4C 9S 2H";
    public static final String[] HOLES = {"AH 3H", "9D KD", "KC KS", "QH JH"};

    private Data() {
    }
}
