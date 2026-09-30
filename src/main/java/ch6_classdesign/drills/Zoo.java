package ch6_classdesign.drills;

/**
 * Les donnees du "zoo" partagees par les drills du chapitre 6.
 * ===========================================================
 *
 * Toujours les memes animaux : ton cerveau se concentre sur la
 * conception des classes (constructeurs, redefinition, abstract, final,
 * immuabilite). Lis ce fichier une fois et garde-le ouvert a cote.
 *
 *   NAMES : {"Rex", "Tweety", "Nemo", "Kaa"}
 *   KINDS : {"chien", "oiseau", "poisson", "serpent"}
 *   LEGS  : {4, 2, 0, 0}          (somme 6)
 *   FOOD  : {500, 30, 10, 200}    (grammes par jour, somme 740)
 */
public final class Zoo {

    public static final String[] NAMES = {"Rex", "Tweety", "Nemo", "Kaa"};

    public static final String[] KINDS = {"chien", "oiseau", "poisson", "serpent"};

    public static final int[] LEGS = {4, 2, 0, 0};

    public static final int[] FOOD = {500, 30, 10, 200};

    private Zoo() {
    }
}
