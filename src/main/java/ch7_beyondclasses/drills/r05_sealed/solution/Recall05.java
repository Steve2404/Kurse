package ch7_beyondclasses.drills.r05_sealed.solution;

/**
 * SOLUTION du drill de rappel 5 - les classes et interfaces scellees.
 */
public class Recall05 {

    static String describe(Vehicle v) {
        if (v instanceof Car) {
            return "voiture";
        } else if (v instanceof EBike) {     // le sous-type le plus precis d'abord
            return "velo electrique";
        } else if (v instanceof Bike) {
            return "velo";
        } else if (v instanceof Truck) {
            return "camion";
        }
        return "?";
    }

    static double area(Shape s) {
        if (s instanceof Circle c) {
            return Math.round(Math.PI * c.r() * c.r() * 100) / 100.0;
        } else if (s instanceof Square q) {
            return q.side() * q.side();
        }
        return 0;
    }

    public static void main(String[] args) {
        Vehicle[] all = {new Car(), new Truck(), new Bike(), new EBike(), new Lorry()};
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : all) {
            sb.append(describe(v)).append(' ');
        }
        System.out.println("D01 : " + sb.toString().strip());
        System.out.println("D02 : " + area(new Circle(1)) + " " + area(new Square(3)));
        System.out.println("D03 : " + Vehicle.class.isSealed() + " " + Vehicle.class.getPermittedSubclasses().length + " " + Truck.class.isSealed() + " "
                + Bike.class.isSealed() + " " + Animal.class.getPermittedSubclasses().length);
        System.out.println("D04 : " + java.lang.reflect.Modifier.isFinal(Circle.class.getModifiers()) + " " + java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()));
        System.out.println("D05 : " + (new Lorry() instanceof Truck) + " " + new Dog().sound() + " " + new Cat().sound());
        Fuel f = new Diesel(40);
        System.out.println("D06 : " + new Tesla().plug() + " " + Fuel.class.getPermittedSubclasses().length + " " + (f instanceof Liquid) + " "
                + Electric.class.isSealed() + " " + Liquid.class.isSealed());
    }
}

// permits liste les SEULS sous-types directs autorises (meme paquet, ou meme module).
sealed abstract class Vehicle permits Car, Truck, Bike {
}

// Chaque sous-type direct choisit : final, sealed ou non-sealed.
final class Car extends Vehicle {
}

non-sealed class Truck extends Vehicle {
}

sealed class Bike extends Vehicle permits EBike {
}

final class EBike extends Bike {
}

// Truck est non-sealed : n'importe quelle classe peut en heriter.
class Lorry extends Truck {
}

// Une interface scellee ; les records sont implicitement final.
sealed interface Shape permits Circle, Square {
}

record Circle(double r) implements Shape {
}

record Square(double side) implements Shape {
}

// Sous-types dans le MEME fichier : permits peut etre omis, javac les deduit.
sealed interface Animal {
    String sound();
}

final class Dog implements Animal {
    @Override
    public String sound() {
        return "ouaf";
    }
}

final class Cat implements Animal {
    @Override
    public String sound() {
        return "miaou";
    }
}

// Une interface scellee peut etre ETENDUE par des interfaces : chacune doit etre sealed ou non-sealed.
sealed interface Fuel permits Electric, Liquid {
}

non-sealed interface Electric extends Fuel {
    default String plug() {
        return "prise";
    }
}

sealed interface Liquid extends Fuel permits Diesel {
}

record Diesel(int litres) implements Liquid {
}

// Electric est non-sealed : n'importe quelle classe peut l'implementer.
class Tesla implements Electric {
}
