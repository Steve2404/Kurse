package ch6_classdesign.projects.p03_payroll.solution;

/**
 * SOLUTION - un stagiaire : la gratification est plafonnee.
 */
public class Intern extends Employee {

    public static final long CAP = 100000;

    public Intern(int id, String name, long stipend) {
        super(id, name, Math.min(stipend, CAP));   // un calcul est permis DANS l'appel a super(...)
    }

    @Override
    public String role() {
        return "stagiaire";
    }

    // Pas de redefinition de pay() : la version heritee d'Employee suffit.
}
