package ch18_design.projects.p01_invoice.solution;

/** De la main-d'oeuvre : facturee au quart d'heure commence, 18,00 le quart d'heure (72,00 de l'heure). */
public record Labor(String name, int minutes) implements InvoiceLine {

    // Les nombres magiques du legacy (15, 1800) ont maintenant un nom.
    static final int QUARTER = 15;
    static final Money PRICE_PER_QUARTER = Money.ofCents(1800);

    public Labor {
        if (minutes < 1) {
            throw new IllegalArgumentException("minutes invalides : " + minutes);
        }
    }

    // Arrondi au quart d'heure SUPERIEUR : 10 minutes se facturent 15.
    private int quarters() {
        return (minutes + QUARTER - 1) / QUARTER;
    }

    @Override
    public int billedMinutes() {
        return quarters() * QUARTER;
    }

    @Override
    public String label() {
        return String.format("%s (%d h %02d)", name, billedMinutes() / 60, billedMinutes() % 60);
    }

    @Override
    public Money price() {
        return PRICE_PER_QUARTER.times(quarters());
    }

    @Override
    public Category category() {
        return Category.LABOR;
    }
}
