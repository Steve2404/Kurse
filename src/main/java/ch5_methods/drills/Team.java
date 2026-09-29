package ch5_methods.drills;

/**
 * Les donnees de "l'equipe" partagees par TOUS les drills du chapitre 5.
 * =====================================================================
 *
 * Toujours les memes donnees : ton cerveau se concentre sur les methodes
 * (varargs, static, passage par valeur, boxing, surcharge). Lis ce
 * fichier une fois et garde-le ouvert a cote.
 *
 *   NAMES       : {"Ada", "Linus", "Grace", "Alan"}
 *   SCORES      : {12, 7, 15, 9}          (somme 43, max 15 pour Grace)
 *   BONUS       : {3, null, 5, null}      (des Integer, certains absents ; somme des presents 8)
 *   MAX_PLAYERS : 4 (static final)
 *   CLUB        : "Java Club"
 *
 * Ne modifie JAMAIS ces tableaux dans un drill : travaille sur une copie.
 */
public final class Team {

    public static final String[] NAMES = {"Ada", "Linus", "Grace", "Alan"};

    public static final int[] SCORES = {12, 7, 15, 9};

    public static final Integer[] BONUS = {3, null, 5, null};

    public static final int MAX_PLAYERS = 4;

    public static final String CLUB = "Java Club";

    private Team() {
    }
}
