package ch6_classdesign.projects.p03_payroll.solution;

/**
 * SOLUTION - un manager : prime par personne encadree directement.
 */
public class Manager extends Employee {

    public static final long BONUS_PER_REPORT = 15000;

    String type = "manager";   // masque Employee.type (deux champs coexistent dans l'objet)
    private int reports;

    public Manager(int id, String name, long base) {
        super(id, name, base);
    }

    static String category() {  // masque (ne redefinit pas) Employee.category()
        return "encadrement";
    }

    void setReports(int reports) {
        this.reports = reports;
    }

    // super.pay() reutilise le calcul du parent au lieu de le recopier.
    @Override
    public long pay() {
        return super.pay() + BONUS_PER_REPORT * reports;
    }

    @Override
    public String role() {
        return "manager de " + reports;
    }

    // Une NOUVELLE methode privee, sans lien avec Employee.rate() : pas de @Override possible.
    private int rate() {
        return 10;
    }

    public String typeSeenFromInside() {
        return type + "/" + super.type;   // super.type : le champ masque du parent
    }
}
