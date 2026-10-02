package ch6_classdesign.projects.p03_payroll.solution;

/**
 * SOLUTION - un ingenieur : heures supplementaires payees.
 */
public class Engineer extends Employee {

    public static final long HOURLY = 2500;
    private final int overtime;

    public Engineer(int id, String name, long base, int overtime) {
        super(id, name, base);
        this.overtime = overtime;
    }

    @Override
    public long pay() {
        return super.pay() + overtime * HOURLY;
    }

    @Override
    public String role() {
        return "ingenieur +" + overtime + "h";
    }
}
