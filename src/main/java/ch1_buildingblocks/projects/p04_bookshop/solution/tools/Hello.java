package ch1_buildingblocks.projects.p04_bookshop.solution.tools;

/**
 * Un programme d'UN SEUL fichier : il se lance directement avec "java Hello.java Lea", sans javac.
 * Il ne peut utiliser que le JDK (pas les autres classes du projet).
 */
public class Hello {

    public static void main(String[] args) {
        System.out.println("Bonjour " + args[0] + " ! (lance sans javac, depuis un seul fichier source)");
    }
}
