package ch6_classdesign.projects.p02_tracer.solution;

/**
 * SOLUTION du projet 2 - le traceur d'initialisation.
 */
public class TracerApp {

    static {
        Tracer.log("[static] TracerApp bloc");
    }

    public static void main(String[] args) {
        Tracer.log("main : KIND = " + ElectricCar.KIND + " (aucune classe chargee)");
        Tracer.log("main : Car.count = " + Car.count + " (Vehicle charge, pas Car)");
        Tracer.log("main : new ElectricCar(\"Zoe\", 300)");
        ElectricCar zoe = new ElectricCar("Zoe", 300);
        Tracer.log("main : apres construction label() = " + zoe.label());
        Tracer.log("main : new ElectricCar()");
        ElectricCar anonymous = new ElectricCar();
        Tracer.log("main : new Car(\"Clio\")");
        Car clio = new Car("Clio");
        Tracer.log("main : ids " + zoe.getId() + " " + anonymous.getId() + " " + clio.getId() + ", electriques " + ElectricCar.built
                + ", roues " + clio.wheels);
    }
}
