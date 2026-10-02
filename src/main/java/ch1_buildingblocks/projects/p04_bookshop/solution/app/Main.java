package ch1_buildingblocks.projects.p04_bookshop.solution.app;

// Import avec joker : toutes les classes PUBLIQUES du paquet model (mais pas ses sous-paquets).
import ch1_buildingblocks.projects.p04_bookshop.solution.model.*;

/**
 * SOLUTION du projet 4 - une conception possible.
 * Book designe model.Book (import) ; export.Book est ecrit avec son nom pleinement qualifie.
 */
public class Main {

    public static void main(String[] args) {
        // Integer est dans java.lang : importe automatiquement, comme String et System.
        Book book = new Book(args[0], new Author(args[1], args[2]), Integer.parseInt(args[3]));
        ch1_buildingblocks.projects.p04_bookshop.solution.export.Book line =
                new ch1_buildingblocks.projects.p04_bookshop.solution.export.Book(book);
        System.out.println("MODELE : " + book.describe());
        System.out.println("EXPORT : " + line.csv());
    }
}
