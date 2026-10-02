package ch6_classdesign.projects.p03_payroll.solution;

/**
 * SOLUTION - la classe parente : ce que toute personne a en commun.
 */
public abstract class Employee {

    // Champ MASQUE par Manager : le champ lu depend du type de la REFERENCE, pas de l'objet.
    String type = "employe";

    private final int id;
    private final String name;
    protected final long base;
    private int managerId;

    protected Employee(int id, String name, long base) {
        this.id = id;
        this.name = name;
        this.base = base;
    }

    // Methode static MASQUEE (hidden) dans Manager : choisie a la compilation, d'apres le type de la reference.
    static String category() {
        return "employe";
    }

    // Redefinie (overridden) dans chaque sous-classe : choisie a l'execution, d'apres l'objet.
    public long pay() {
        return base;
    }

    public abstract String role();

    // final : aucune sous-classe ne peut changer le format du badge.
    public final String badge() {
        return "#" + id + " " + name;
    }

    // private : invisible pour les sous-classes, donc impossible a redefinir (Manager peut en declarer une autre).
    private int rate() {
        return 3;
    }

    // raise() appelle SA rate() privee, meme pour un Manager.
    public long raise() {
        return base * rate() / 100;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getManagerId() {
        return managerId;
    }

    void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    static String money(long cents) {
        long rest = cents % 100;
        return cents / 100 + "." + (rest < 10 ? "0" : "") + rest;
    }
}
