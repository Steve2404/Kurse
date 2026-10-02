package ch7_beyondclasses.drills.r06_records.solution;

/**
 * SOLUTION du drill de rappel 6 - les records.
 */
public class Recall06 {

    public static void main(String[] args) {
        Point p = new Point(3, 4);
        System.out.println("D01 : " + p + " " + p.x() + " " + p.y() + " " + p.dist());
        Point q = new Point(3, 4);
        System.out.println("D02 : " + p.equals(q) + " " + (p == q) + " " + (p.hashCode() == q.hashCode()) + " " + p.equals(new Point(4, 3)));
        System.out.println("D03 : " + new Point(-5, 2) + " " + new Range(9, 2) + " " + new Point(7));
        System.out.println("D04 : " + Point.ORIGIN + " " + Point.of("4,5") + " " + Range.count);
        Person a = new Person("Ana", new int[] {1, 2});
        Person b = new Person("Ana", new int[] {1, 2});
        System.out.println("D05 : " + a.equals(b) + " " + a.name().equals(b.name()) + " " + java.util.Arrays.equals(a.scores(), b.scores()));
        // Un record LOCAL, declare dans une methode (implicitement static : il ne voit pas les variables locales).
        record Pair(String key, int value) {
        }
        Pair pair = new Pair("k", 1);
        System.out.println("D06 : " + pair + " " + pair.key() + " " + new Pair("k", 1).equals(pair));
    }
}

record Point(int x, int y) {

    static final Point ORIGIN = new Point(0, 0);   // champs static permis ; champs d'instance supplementaires interdits

    // Constructeur compact : on peut modifier les parametres ; les champs sont affectes APRES, automatiquement.
    Point {
        if (x < 0) {
            x = 0;
        }
    }

    // Constructeur supplementaire : la premiere ligne appelle un autre constructeur avec this(...).
    Point(int both) {
        this(both, both);
    }

    static Point of(String text) {
        String[] p = text.split(",");
        return new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
    }

    double dist() {
        return Math.sqrt(x * x + y * y);
    }
}

record Range(int lo, int hi) {

    static int count;

    Range {
        if (lo > hi) {
            int t = lo;
            lo = hi;
            hi = t;
        }
        count++;
    }
}

// Un record avec un tableau : equals compare la REFERENCE du tableau, pas son contenu.
record Person(String name, int[] scores) {
}
