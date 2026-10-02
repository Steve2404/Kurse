package d.base.hidden;

/** SOLUTION - un paquet exporte seulement a d.friend. */
public final class Secret {
    private Secret() {
    }

    public static String code() {
        return "42";
    }
}
