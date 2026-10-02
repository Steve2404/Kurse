package ch6_classdesign.drills.r02_constructors.solution;

/**
 * SOLUTION du drill de rappel 2 - les constructeurs.
 */
public class Recall02 {

    static final StringBuilder LOG = new StringBuilder();

    static String flush() {
        String s = LOG.toString().strip();
        LOG.setLength(0);
        return s;
    }

    public static void main(String[] args) {
        Point a = new Point();
        System.out.println("D01 : " + flush() + " | " + a.x + "," + a.y);
        Point3D b = new Point3D();
        System.out.println("D02 : " + flush() + " | " + b.x + "," + b.y + "," + b.z);
        Point3D c = new Point3D(1, 2, 3);
        System.out.println("D03 : " + flush() + " | " + c.x + "," + c.y + "," + c.z);
        Weird w = new Weird();
        w.Weird();
        System.out.println("D04 : " + flush() + " " + (new Empty() != null));
        Ticket t1 = new Ticket();
        Ticket t2 = new Ticket(42);
        Ticket t3 = new Ticket();
        System.out.println("D05 : " + t1.number + " " + t2.number + " " + t3.number);
        Config cfg = Config.defaults();
        System.out.println("D06 : " + cfg.mode + " " + Config.created);
    }
}

class Point {
    int x;
    int y;

    Point() {
        this(0, 0);                       // this(...) : PREMIERE instruction
        Recall02.LOG.append(" Point()");
    }

    Point(int x, int y) {
        this.x = x;                       // this.x : le champ ; x : le parametre qui le masque
        this.y = y;
        Recall02.LOG.append(" Point(int,int)");
    }
}

class Point3D extends Point {
    int z;

    Point3D() {
        // Pas de this(...) ni de super(...) : javac insere super(); ici.
        Recall02.LOG.append(" Point3D()");
    }

    Point3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
        Recall02.LOG.append(" Point3D(int,int,int)");
    }
}

// Aucun constructeur ecrit : javac fournit le constructeur par defaut Empty() { super(); }.
class Empty {
}

class Weird {
    Weird() {
        Recall02.LOG.append(" constructeur");
    }

    // Un type de retour (void) : c'est une METHODE qui porte le nom de la classe, pas un constructeur.
    void Weird() {
        Recall02.LOG.append(" methode");
    }
}

class Ticket {
    private static int next = 100;
    final int number;                     // final : affecte une seule fois sur CHAQUE chemin de construction

    Ticket() {
        this(next++);
    }

    Ticket(int number) {
        this.number = number;
    }
}

class Config {
    static int created;
    final String mode;

    // Constructeur prive : seule la classe elle-meme peut faire new Config(...).
    private Config(String mode) {
        this.mode = mode;
        created++;
    }

    static Config defaults() {
        return new Config("standard");
    }
}
