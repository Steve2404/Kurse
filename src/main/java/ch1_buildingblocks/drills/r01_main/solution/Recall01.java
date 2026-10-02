package ch1_buildingblocks.drills.r01_main.solution;

/**
 * SOLUTION du drill de rappel 1 - main, arguments, ligne de commande.
 */
public class Recall01 {

    // final et varargs : une des signatures valides de main.
    public static void main(final String... args) {
        System.out.println("D01 : " + args[0] + " puis " + args[1]);
        // Les arguments sont des String : "42" + 8 serait "428".
        System.out.println("D02 : " + (Integer.parseInt(args[1]) + 8) + " et non " + args[1] + 8);
        System.out.println("D03 : " + Double.parseDouble(args[2]) * 2);
        System.out.println("D04 : " + Boolean.parseBoolean(args[3]) + " " + Boolean.parseBoolean(args[0]));
        // Un argument entre guillemets dans le terminal arrive comme UNE seule String, espaces compris.
        System.out.println("D05 : [" + args[4] + "]");
    }
}
