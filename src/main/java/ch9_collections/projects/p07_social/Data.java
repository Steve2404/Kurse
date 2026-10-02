package ch9_collections.projects.p07_social;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Amities a double sens : "a b". */
    public static final String[] FRIENDS = {"ana bob", "ana chloe", "bob chloe", "bob dan", "chloe eve", "dan eve", "dan fred", "eve gina",
            "fred gina", "hugo ines", "ines jade", "ana gina"};

    /** Publications : "id auteur heure likes tag1,tag2". */
    public static final String[] POSTS = {
            "1 bob 9 12 java,collections", "2 chloe 10 30 cuisine", "3 dan 8 5 java", "4 bob 14 7 sport,java", "5 eve 11 50 voyage,cuisine",
            "6 chloe 15 2 java,map", "7 dan 13 20 sport", "8 ana 12 9 collections", "9 eve 16 15 java,voyage", "10 fred 17 40 sport,voyage",
            "11 bob 18 3 map,set"};

    /** L'utilisateur dont on calcule les suggestions et le fil. */
    public static final String ME = "ana";

    /** Taille du fil et nombre de tags tendance. */
    public static final int FEED = 5;
    public static final int TRENDING = 3;

    private Data() {
    }
}
