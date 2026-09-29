package ch5_methods.drills.solutions;

import ch5_methods.drills.Team;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.drills.exercises.Drill01_DeclarationsAndVarargs.
 */
public class SolutionDrill01_DeclarationsAndVarargs {

    public static int total(int... values) {
        // Un varargs se parcourt comme un tableau ; vide si rien n'est passe.
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }

    public static int count(Object... items) {
        // length du tableau cree par Java pour les arguments.
        return items.length;
    }

    public static int best(int first, int... others) {
        // first garantit au moins une valeur.
        int max = first;
        for (int v : others) {
            max = Math.max(max, v);
        }
        return max;
    }

    public static String greet(String greeting, String... names) {
        // Le varargs est toujours le DERNIER parametre.
        return greeting + " " + String.join(", ", names);
    }

    public static double average(int... values) {
        // Pas de division par zero pour un appel sans argument.
        return values.length == 0 ? 0.0 : (double) total(values) / values.length;
    }

    public static String last(String... names) {
        // Tableau vide : pas d'index names.length - 1 valide.
        return names.length == 0 ? null : names[names.length - 1];
    }

    public static boolean contains(String target, String... names) {
        // equals compare le contenu des String.
        for (String name : names) {
            if (name.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static String joinAll(String separator, String... parts) {
        // String.join accepte lui-meme un varargs : on lui fait suivre le tableau.
        return String.join(separator, parts);
    }

    public static int[] minMax(int... values) {
        // Une methode ne rend qu'une valeur : un tableau en porte deux.
        int min = values[0];
        int max = values[0];
        for (int v : values) {
            min = Math.min(min, v);
            max = Math.max(max, v);
        }
        return new int[] {min, max};
    }

    public static int countAsArray() {
        // Un String[] est un Object[] : il devient le tableau des arguments.
        return count((Object[]) Team.NAMES);
    }

    public static int countAsOneObject() {
        // Le cast en Object en fait UN seul argument.
        return count((Object) Team.NAMES);
    }

    static public String label(String prefix, int number) {
        // "static public" : l'ordre des modificateurs est libre, le type de retour reste avant le nom.
        return prefix + "-" + number;
    }
}
