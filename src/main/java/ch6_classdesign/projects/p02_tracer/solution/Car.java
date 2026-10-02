package ch6_classdesign.projects.p02_tracer.solution;

/**
 * SOLUTION - le niveau du milieu : deux constructeurs, l'un delegue a l'autre.
 */
public class Car extends Vehicle {

    static {
        Tracer.log("[static] Car bloc");
    }

    protected int seats;

    {
        Tracer.log("[objet] Car bloc");
    }

    public Car(String name) {
        this(name, 5);             // this(...) : pas de super(...) ici, c'est l'autre constructeur qui l'appelle
        Tracer.log("[ctor] Car(String)");
    }

    public Car(String name, int seats) {
        super(name);
        this.seats = seats;
        Tracer.log("[ctor] Car(String,int) seats=" + seats);
    }

    @Override
    protected String label() {
        return "voiture " + name + " " + seats + " places";
    }
}
