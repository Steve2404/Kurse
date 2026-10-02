package ch6_classdesign.drills.r08_object.solution;

/**
 * SOLUTION du drill de rappel 8 - redefinir les methodes d'Object : toString, equals, hashCode.
 */
public class Recall08 {

    public static void main(String[] args) {
        Plain a = new Plain();
        Plain b = new Plain();
        System.out.println("D01 : " + a.toString().contains(".Plain@") + " " + a.equals(b) + " " + a.equals(a) + " " + (a.hashCode() == a.hashCode()));
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println("D02 : " + p1 + " " + p1.equals(p2) + " " + p2.equals(p1) + " " + (p1 == p2) + " " + (p1.hashCode() == p2.hashCode()));
        System.out.println("D03 : " + p1.equals(null) + " " + p1.equals("(1, 2)") + " " + p1.equals(new Point(2, 1)));
        BadPoint q1 = new BadPoint(1, 2);
        BadPoint q2 = new BadPoint(1, 2);
        Object asObject = q2;
        System.out.println("D04 : " + q1.equals(q2) + " " + q1.equals(asObject));
        Object o = new Point(3, 4);
        System.out.println("D05 : " + o + " " + o.equals(new Point(3, 4)) + " " + o.getClass().getSimpleName());
    }
}

class Plain {
}

class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Signature EXACTE d'Object : equals(Object). Reflexive, symetrique, transitive ; false pour null.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Point p && p.x == x && p.y == y;
    }

    // Contrat : a.equals(b) => a.hashCode() == b.hashCode().
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class BadPoint {
    private final int x;
    private final int y;

    BadPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // PIEGE : parametre BadPoint et non Object -> c'est une SURCHARGE, pas une redefinition.
    // Avec @Override, javac refuserait. Un appel avec un argument de type Object prend equals(Object) d'Object.
    public boolean equals(BadPoint o) {
        return o != null && o.x == x && o.y == y;
    }
}
