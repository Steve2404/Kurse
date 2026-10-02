package ch7_beyondclasses.drills.r01_interfaces.solution;

/**
 * SOLUTION du drill de rappel 1 - les interfaces et leurs modificateurs implicites.
 */
public class Recall01 {

    public static void main(String[] args) {
        Square sq = new Square(3);
        System.out.println("D01 : " + Shape.UNIT + " " + Square.UNIT + " " + sq.area() + " " + sq.sides() + " " + sq.name());
        Shape[] shapes = {sq, new Circle(1)};
        StringBuilder sb = new StringBuilder();
        for (Shape s : shapes) {
            sb.append(s.name()).append('=').append(Math.round(s.area() * 100) / 100.0).append(' ');
        }
        System.out.println("D02 : " + sb.toString().strip());
        Object circle = shapes[1];
        System.out.println("D03 : " + (sq instanceof Polygon) + " " + (sq instanceof Colored) + " " + (circle instanceof Polygon) + " " + (circle instanceof Named));
        Colored c = sq;
        System.out.println("D04 : " + c.color() + " " + ((Shape) c).area() + " " + ((Named) c).name());
        System.out.println("D05 : " + new Circle(2).name() + " " + Polygon.MAX_SIDES);
    }
}

// Champs : implicitement public static final. Methodes sans corps : implicitement public abstract.
interface Shape {
    int UNIT = 1;

    double area();

    String name();
}

interface Named {
    String name();   // meme signature que Shape.name() : UNE implementation satisfait les deux
}

// Une interface en ETEND une autre (extends) ; elle n'implemente rien.
interface Polygon extends Shape {
    int MAX_SIDES = 12;

    int sides();
}

interface Colored {
    String color();
}

// Une classe abstraite peut implementer une interface sans tout fournir.
abstract class Base implements Polygon, Named {
    @Override
    public String name() {
        return "polygone a " + sides() + " cotes";
    }
}

class Square extends Base implements Colored {
    private final double side;

    Square(double side) {
        this.side = side;
    }

    @Override
    public double area() {               // public obligatoire : on ne reduit pas l'acces d'une methode d'interface
        return side * side;
    }

    @Override
    public int sides() {
        return 4;
    }

    @Override
    public String color() {
        return "rouge";
    }
}

class Circle implements Shape {
    private final double r;

    Circle(double r) {
        this.r = r;
    }

    @Override
    public double area() {
        return Math.PI * r * r;
    }

    @Override
    public String name() {
        return "cercle";
    }
}
