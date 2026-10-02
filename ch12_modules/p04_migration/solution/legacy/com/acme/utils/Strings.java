package com.acme.utils;

/**
 * SOLUTION - une 2e vieille bibliotheque ; son jar annonce son nom de module dans le manifeste (Automatic-Module-Name).
 */
public final class Strings {

    private Strings() {
    }

    public static String shorten(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 1) + "~";
    }
}
