package t.util;

/** SOLUTION - du code SANS module-info, qu'on mettra dans des jars. */
public final class Tools {
    private Tools() {
    }

    public static String shout(String s) {
        return s.toUpperCase() + "!";
    }
}
