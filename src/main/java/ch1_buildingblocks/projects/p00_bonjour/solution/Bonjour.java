package ch1_buildingblocks.projects.p00_bonjour.solution;

// SOLUTION - le tout premier programme. Chaque ligne est expliquee dans TODO.md.
public class Bonjour {

    public static void main(String[] args) {
        // args[0] est le 1er mot tape apres le nom de la classe, args[1] le 2e.
        System.out.println("Bonjour, " + args[0] + " !");
        System.out.println("Tu habites a " + args[1] + ".");
        System.out.println("Bienvenue dans le cours de Java.");
    }
}
