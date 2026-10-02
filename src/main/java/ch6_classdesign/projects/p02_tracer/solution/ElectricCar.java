package ch6_classdesign.projects.p02_tracer.solution;

/**
 * SOLUTION - le bas de la hierarchie.
 */
public class ElectricCar extends Car {

    // Constante de compilation : lire ElectricCar.KIND ne charge PAS la classe (la valeur est recopiee par javac).
    public static final String KIND = "EV";
    static int built = Tracer.log("[static] ElectricCar.built", 0);

    static {
        Tracer.log("[static] ElectricCar bloc");
    }

    private int battery = Tracer.log("[objet] ElectricCar.battery", 50);

    public ElectricCar(String name, int battery) {
        super(name, 4);
        this.battery = battery;
        built++;
        Tracer.log("[ctor] ElectricCar(String,int) battery=" + battery);
    }

    public ElectricCar() {
        this("Anonyme", 100);
        Tracer.log("[ctor] ElectricCar()");
    }

    // Appelee pendant le constructeur de Vehicle : battery vaut encore 0 (pas 50, pas la valeur passee).
    @Override
    protected String label() {
        return "electrique " + name + " batterie " + battery;
    }
}
