package ch1_buildingblocks.projects.p01_receipt.solution;

/**
 * SOLUTION du projet 1 - une conception possible.
 * Le fichier s'appelle Receipt.java : c'est donc la SEULE classe publique qu'il peut contenir.
 */
public class Receipt {

    // Text block : l'indentation commune (alignee sur les """ fermants) est retiree ;
    // le \ en fin de ligne colle la ligne suivante (une seule ligne a l'affichage).
    static final String HEADER = """
            +--------------------------------+
            |       LIBRAIRIE DU PORT        |
            |  12, quai des Brumes \
            - Nantes  |
            +--------------------------------+""";

    // Les """ fermants sont 4 colonnes a GAUCHE du texte : ces 4 espaces restent devant chaque ligne.
    // Les guillemets simples n'ont pas besoin d'echappement dans un text block.
    static final String FOOTER = """
                Merci de votre visite !
                  "Lire, c'est voyager."
            """;

    // Centimes -> "37.50" avec les seuls operateurs arithmetiques : euros, puis dizaines et unites de centimes.
    static String euros(int cents) {
        return cents / 100 + "." + cents / 10 % 10 + cents % 10;
    }

    // varargs : String... args est une signature de main valide, equivalente a String[] args.
    public static void main(String... args) {
        // args[0], args[1]... : les arguments arrivent TOUJOURS sous forme de String -> conversion par les enveloppes.
        Item first = new Item(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        // valueOf rend un objet Integer ; intValue() en extrait le primitif.
        Item second = new Item(args[3], Integer.valueOf(args[4]).intValue(), Integer.valueOf(args[5]).intValue());
        final int percent = Integer.parseInt(args[6]);
        boolean loyal = Boolean.parseBoolean(args[7]);

        int subtotal = first.totalCents() + second.totalCents();
        int discount = subtotal * percent / 100;

        System.out.println(HEADER);
        System.out.println(first.line());
        System.out.println(second.line());
        System.out.println("--------------------------------");
        System.out.println("Articles   : " + (first.quantity + second.quantity));
        System.out.println("Sous-total : " + euros(subtotal));
        System.out.println("Remise " + percent + "% : -" + euros(discount));
        System.out.println("TOTAL      : " + euros(subtotal - discount));
        System.out.println("Carte fidelite : " + loyal);
        System.out.print(FOOTER);
    }
}

// Une 2e classe dans le MEME fichier : autorise, a condition qu'elle ne soit pas publique.
class Item {
    String name;
    int quantity;
    int unitCents;

    Item(String name, int quantity, int unitCents) {
        // this.name designe le CHAMP ; name seul designerait le parametre.
        this.name = name;
        this.quantity = quantity;
        this.unitCents = unitCents;
    }

    int totalCents() {
        return quantity * unitCents;
    }

    String line() {
        return name + " x " + quantity + " a " + Receipt.euros(unitCents) + " = " + Receipt.euros(totalCents());
    }
}
