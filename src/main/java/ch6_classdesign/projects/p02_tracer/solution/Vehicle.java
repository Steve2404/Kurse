package ch6_classdesign.projects.p02_tracer.solution;

/**
 * SOLUTION - le haut de la hierarchie.
 */
public abstract class Vehicle {

    static int count = Tracer.log("[static] Vehicle.count", 0);

    static {
        Tracer.log("[static] Vehicle bloc");
    }

    protected int wheels = Tracer.log("[objet] Vehicle.wheels", 4);
    private final int id;          // final : chaque constructeur doit l'affecter exactement une fois
    protected final String name;

    {
        Tracer.log("[objet] Vehicle bloc");
    }

    protected Vehicle(String name) {
        // Ici, super() est IMPLICITE : Object() est appele avant les initialiseurs ci-dessus.
        this.name = name;
        id = ++count;
        Tracer.log("[ctor] Vehicle(String) id=" + id);
        // PIEGE : un appel a une methode redefinie depuis le constructeur du parent
        // execute la version de l'ENFANT, alors que les champs de l'enfant ne sont pas encore initialises.
        Tracer.log("[ctor] Vehicle voit label() = " + label());
    }

    protected String label() {
        return "vehicule " + name;
    }

    public int getId() {
        return id;
    }
}
