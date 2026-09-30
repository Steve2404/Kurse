package ch8_lambdas.drills;

import java.util.List;

/**
 * Les donnees partagees par TOUS les drills du chapitre 8.
 * =======================================================
 *
 * Toujours les memes donnees : ton cerveau se concentre sur les lambdas,
 * les references de methode et les interfaces fonctionnelles.
 *
 *   WORDS   : ["lambda", "stream", "java", "record", "sealed", "var"]
 *   NUMBERS : [3, 8, 1, 9, 4]        (somme 25, max 9)
 *   PREFIX  : "ocp-"
 */
public final class Words {

    public static final List<String> WORDS = List.of("lambda", "stream", "java", "record", "sealed", "var");

    public static final int[] NUMBERS = {3, 8, 1, 9, 4};

    public static final String PREFIX = "ocp-";

    private Words() {
    }
}
