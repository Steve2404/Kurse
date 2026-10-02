package ch6_classdesign.projects.p01_shapes.solution;

/**
 * SOLUTION - la racine abstraite : on ne peut pas faire new Shape(), mais chaque forme EST une Shape.
 */
public abstract class Shape {

    private static int created;
    private final String name;   // final : affecte une seule fois, dans le constructeur

    // protected : seules les sous-classes (et le paquet) appellent ce constructeur, via super(name).
    protected Shape(String name) {
        this.name = name;
        created++;
    }

    // Methodes abstraites : pas de corps ; toute classe concrete DOIT les redefinir.
    public abstract double area();

    public abstract double perimeter();

    public String getName() {
        return name;
    }

    // Methode modele (template method) : final, donc le format est le meme pour toutes les formes.
    public final String describe() {
        return name + " aire=" + r2(area()) + " perimetre=" + r2(perimeter()) + extra();
    }

    // Point d'extension : les sous-classes le redefinissent si elles veulent ajouter un detail.
    protected String extra() {
        return "";
    }

    public static int created() {
        return created;
    }

    protected static double r2(double x) {
        return Math.round(x * 100) / 100.0;
    }

    @Override
    public String toString() {
        return describe();
    }
}
