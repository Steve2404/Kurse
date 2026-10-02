package ch5_methods.projects.p01_stats.solution;

import ch5_methods.projects.p01_stats.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 1 - le rapport qui appelle la boite a outils.
 */
public class StatsReport {

    public static void main(String[] args) {
        int[] t = Data.TEMPS;
        // Trois facons d'appeler un varargs : une liste, un tableau, rien du tout.
        System.out.println("somme : liste " + Stats.sum(1, 2, 3) + ", tableau " + Stats.sum(t) + ", rien " + Stats.sum());
        System.out.println("compte : " + Stats.count(t) + " " + Stats.count() + " " + Stats.count(new int[0]) + " " + Stats.count((int[]) null));
        System.out.println("moyenne : " + Stats.average(t[0], t) + " (piege : le premier compte deux fois) " + Stats.average(5) + " " + Stats.average(4, 5, 6));
        int[] rest = Arrays.copyOfRange(t, 1, t.length);
        System.out.println("moyenne juste : " + Stats.average(t[0], rest) + ", max " + Stats.max(t[0], rest) + ", max d'un seul " + Stats.max(-3));
        System.out.println("mediane : " + Stats.median(t) + " " + Stats.median(3, 1, 2) + ", original intact " + Arrays.toString(t));
        System.out.println("modes : " + Arrays.toString(Stats.modes(t)) + " " + Arrays.toString(Stats.modes(1, 1, 2, 2, 3)));
        System.out.println("k-iemes : 1er " + Stats.kth(1, t) + ", 3e " + Stats.kth(3, t) + ", 10e " + Stats.kth(10, t) + ", original intact " + (t[0] == 12));
        System.out.println("moyenne glissante (3) : " + Arrays.toString(Stats.movingAverage(3, t)));
        System.out.println("concat : " + Arrays.toString(Stats.concat(Data.SHOP_A, Data.SHOP_B, Data.SHOP_C)) + " " + Stats.concat().length);
        System.out.println("join : " + Stats.join(", ", "a", "b", "c") + " | " + Stats.join("-") + " | " + Stats.join("/", Data.LABELS));
        System.out.println("describe : " + Stats.describe(1, "deux", 3.0, 'c') + " | " + Stats.describe() + " | " + Stats.describe((Object) null)
                + " | " + Stats.describe((Object[]) null) + " | " + Stats.describe((Object) new int[] {1, 2}).startsWith("1 element"));
        Stats.histogram(Data.LABELS, Data.VOTES);
        Stats.histogram(Data.LABELS, 1, 2);
    }
}
