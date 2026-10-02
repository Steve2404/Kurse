package ch1_buildingblocks.projects.p05_cinema.solution.model;

/**
 * Une ligne de tarif : un libelle, un nombre de places et un prix unitaire en centimes.
 */
public class TicketLine {
    String label;
    int count;
    int unitCents;

    public TicketLine(String label, int count, int unitCents) {
        this.label = label;
        this.count = count;
        this.unitCents = unitCents;
    }

    public int count() {
        return count;
    }

    public int totalCents() {
        return count * unitCents;
    }

    public String line() {
        return label + count + " x " + Money.euros(unitCents) + " = " + Money.euros(totalCents());
    }
}
