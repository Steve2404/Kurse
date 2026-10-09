package ch19_final.drills.r01_json.solution;

/** Un texte qui n'est pas du JSON : "colonne 4 : ',' ou ']' attendu" (colonnes a partir de 1). */
public class MiniJsonException extends RuntimeException {

    public MiniJsonException(int column, String reason) {
        super("colonne " + column + " : " + reason);
    }
}
