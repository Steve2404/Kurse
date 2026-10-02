package ch6_classdesign.projects.p02_tracer;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON TracerApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "01 [static] TracerApp bloc",
            "02 main : KIND = EV (aucune classe chargee)",
            "03 [static] Vehicle.count = 0",
            "04 [static] Vehicle bloc",
            "05 main : Car.count = 0 (Vehicle charge, pas Car)",
            "06 main : new ElectricCar(\"Zoe\", 300)",
            "07 [static] Car bloc",
            "08 [static] ElectricCar.built = 0",
            "09 [static] ElectricCar bloc",
            "10 [objet] Vehicle.wheels = 4",
            "11 [objet] Vehicle bloc",
            "12 [ctor] Vehicle(String) id=1",
            "13 [ctor] Vehicle voit label() = electrique Zoe batterie 0",
            "14 [objet] Car bloc",
            "15 [ctor] Car(String,int) seats=4",
            "16 [objet] ElectricCar.battery = 50",
            "17 [ctor] ElectricCar(String,int) battery=300",
            "18 main : apres construction label() = electrique Zoe batterie 300",
            "19 main : new ElectricCar()",
            "20 [objet] Vehicle.wheels = 4",
            "21 [objet] Vehicle bloc",
            "22 [ctor] Vehicle(String) id=2",
            "23 [ctor] Vehicle voit label() = electrique Anonyme batterie 0",
            "24 [objet] Car bloc",
            "25 [ctor] Car(String,int) seats=4",
            "26 [objet] ElectricCar.battery = 50",
            "27 [ctor] ElectricCar(String,int) battery=100",
            "28 [ctor] ElectricCar()",
            "29 main : new Car(\"Clio\")",
            "30 [objet] Vehicle.wheels = 4",
            "31 [objet] Vehicle bloc",
            "32 [ctor] Vehicle(String) id=3",
            "33 [ctor] Vehicle voit label() = voiture Clio 0 places",
            "34 [objet] Car bloc",
            "35 [ctor] Car(String,int) seats=5",
            "36 [ctor] Car(String)",
            "37 main : ids 1 2 3, electriques 2, roues 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "3xstatic {", "re:(?m)^\\s+\\{\\s*$##bloc { } d objet", "extends Vehicle", "extends Car",
            "this(", "super(", "static final String KIND", "private final int id",
            "protected String label()", "@Override", "abstract class Vehicle",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "TracerApp", args, EXPECTED, API);
    }
}
