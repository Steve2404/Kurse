package com.acme.text;

import java.text.Normalizer;
import java.util.Locale;

/**
 * SOLUTION - une vieille bibliotheque SANS module-info : elle vit sur le classpath, ou devient un module AUTOMATIQUE.
 */
public final class Slugify {

    private Slugify() {
    }

    // "L'Ete a Paris !" -> "l-ete-a-paris" : sans accents, minuscules, un seul tiret entre les mots.
    public static String slug(String title) {
        String plain = Normalizer.normalize(title, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return plain.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
