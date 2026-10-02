package ch1_buildingblocks.projects.p05_cinema.solution.app;

import ch1_buildingblocks.projects.p05_cinema.solution.model.Money;
import ch1_buildingblocks.projects.p05_cinema.solution.model.Screening;
import ch1_buildingblocks.projects.p05_cinema.solution.model.TicketLine;

/**
 * SOLUTION du projet 5 (capstone) - une conception possible.
 */
public class Cinema {

    static final String HEADER = """
            +==============================+
            |        CINEMA LE PHARE       |
            +==============================+""";

    static final String FOOTER = """
                 Bonne seance !
            """;

    public static void main(String[] args) {
        // var : les types sont deduits ; ils restent fixes ensuite.
        var screening = new Screening(args[0], args[1]);
        var full = new TicketLine("Plein   : ", Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        var reduced = new TicketLine("Reduit  : ", Integer.valueOf(args[4]).intValue(), Integer.valueOf(args[5]).intValue());
        // decode comprend les prefixes 0x, # et 0 (octal) : le code promo arrive en hexadecimal.
        final int promo = Integer.decode(args[6]);
        boolean loyal = Boolean.parseBoolean(args[7]);

        int subtotal = full.totalCents() + reduced.totalCents();
        int discount = subtotal * promo / 100;
        int seats = full.count() + reduced.count();

        System.out.println(HEADER);
        System.out.println("Film    : " + screening.title());
        System.out.println(full.line());
        System.out.println(reduced.line());
        System.out.println("Sous-total       : " + Money.euros(subtotal));
        System.out.println("Code promo " + args[6] + " : -" + promo + "% = -" + Money.euros(discount));
        System.out.println("A PAYER          : " + Money.euros(subtotal - discount));
        System.out.println("Places restantes : " + screening.seatsLeft(seats));
        System.out.println("Fidelite : " + loyal + ", code en binaire " + Integer.toBinaryString(promo)
                + ", en octal " + Integer.toOctalString(promo));
        System.out.print(FOOTER);
    }
}
