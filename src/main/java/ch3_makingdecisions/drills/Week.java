package ch3_makingdecisions.drills;

/**
 * Les donnees du "projet meteo de la semaine" partagees par TOUS les drills du chapitre 3.
 * =======================================================================================
 *
 * Toujours les memes donnees : ton cerveau se concentre sur if, switch
 * et les boucles. Lis ce fichier une fois et garde-le ouvert a cote.
 *
 *   Day      : MON, TUE, WED, THU, FRI, SAT, SUN
 *   TEMPS    : temperatures du lundi au dimanche   {12, 15, -2, 8, 21, 0, 17}   (somme 71)
 *   ITEMS    : des objets de types varies           {"lundi", 42, null, 3.5, "mardi", -7, 'x'}
 *   GRID     : une grille                           {{1, 2, 3}, {4, -5, 6}, {7, 8, 9}}
 *   COMMANDS : des commandes                        {"start", "pause", "start", "stop", "start"}
 */
public final class Week {

    public enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    public static final int[] TEMPS = {12, 15, -2, 8, 21, 0, 17};

    public static final Object[] ITEMS = {"lundi", 42, null, 3.5, "mardi", -7, 'x'};

    public static final int[][] GRID = {{1, 2, 3}, {4, -5, 6}, {7, 8, 9}};

    public static final String[] COMMANDS = {"start", "pause", "start", "stop", "start"};

    private Week() {
    }
}
