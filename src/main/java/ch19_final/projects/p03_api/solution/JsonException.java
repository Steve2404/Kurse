package ch19_final.projects.p03_api.solution;

/** Un document mal forme : le message dit OU (ligne, colonne, a partir de 1) et POURQUOI. */
public class JsonException extends RuntimeException {

    private final int line;
    private final int column;

    public JsonException(String reason, int line, int column) {
        super("ligne " + line + ", colonne " + column + " : " + reason);
        this.line = line;
        this.column = column;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }
}
