package ch11_exceptions.projects.p02_calculator;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les expressions a evaluer, dans l'ordre. Plusieurs sont volontairement fausses. */
    public static final String[] EXPRESSIONS = {
            "1 + 2 * 3",
            "-(2 + 3) * 4 % 7",
            "max(3, 9, 4) - abs(-12) + min(5, 2)",
            "(4 + 6) / (5 - 5)",
            "2 * (3 + 4",
            "7 $ 2",
            "9223372036854775807 + 1",
            "99999999999999999999",
            "foo(1) + 2",
            "min()",
            "1 + 2 3",
            "abs(-9223372036854775807 - 1)"};

    /** La profondeur d'imbrication du test "trop profond" : "(((...1...)))". */
    public static final int DEPTH = 100_000;

    /** Un lot evalue dans un stream. */
    public static final String[] BATCH = {"6 * 7", "(1 + 2", "100 / 4"};

    private Data() {
    }
}
