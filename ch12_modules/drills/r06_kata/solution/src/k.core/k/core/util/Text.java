package k.core.util;

/** SOLUTION - exporte seulement a k.app. */
public final class Text {
    private Text() {
    }

    public static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}
