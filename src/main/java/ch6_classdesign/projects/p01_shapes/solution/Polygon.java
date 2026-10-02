package ch6_classdesign.projects.p01_shapes.solution;

/**
 * SOLUTION - un polygone donne par ses sommets, dans l'ordre.
 */
public class Polygon extends Shape {

    private final double[][] points;

    public Polygon(double[][] points) {
        super("polygone(" + points.length + ")");   // une expression est permise dans l'appel a super(...)
        this.points = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i].clone();     // copie defensive
        }
    }

    // Formule du lacet (shoelace) : la moitie de la somme des produits en croix.
    @Override
    public double area() {
        double sum = 0;
        for (int i = 0; i < points.length; i++) {
            double[] p = points[i];
            double[] q = points[(i + 1) % points.length];
            sum += p[0] * q[1] - q[0] * p[1];
        }
        return Math.abs(sum) / 2;
    }

    @Override
    public double perimeter() {
        double sum = 0;
        for (int i = 0; i < points.length; i++) {
            double[] p = points[i];
            double[] q = points[(i + 1) % points.length];
            sum += Math.hypot(q[0] - p[0], q[1] - p[1]);
        }
        return sum;
    }

    // Lancer de rayon : on compte les aretes traversees par une demi-droite horizontale vers la droite.
    public boolean contains(double x, double y) {
        boolean inside = false;
        for (int i = 0, j = points.length - 1; i < points.length; j = i++) {
            double[] p = points[i];
            double[] q = points[j];
            if ((p[1] > y) != (q[1] > y) && x < (q[0] - p[0]) * (y - p[1]) / (q[1] - p[1]) + p[0]) {
                inside = !inside;
            }
        }
        return inside;
    }

    public String vertices() {
        StringBuilder sb = new StringBuilder();
        for (double[] p : points) {
            sb.append('(').append((int) p[0]).append(',').append((int) p[1]).append(')');
        }
        return sb.toString();
    }

    // Enveloppe convexe d'Andrew : tri par x puis y, puis chaine inferieure et chaine superieure.
    public static Polygon convexHull(double[][] input) {
        double[][] pts = new double[input.length][];
        for (int i = 0; i < input.length; i++) {
            pts[i] = input[i].clone();
        }
        for (int i = 1; i < pts.length; i++) {                // tri par insertion (x, puis y)
            double[] key = pts[i];
            int j = i - 1;
            while (j >= 0 && (pts[j][0] > key[0] || pts[j][0] == key[0] && pts[j][1] > key[1])) {
                pts[j + 1] = pts[j];
                j--;
            }
            pts[j + 1] = key;
        }
        double[][] hull = new double[2 * pts.length][];
        int k = 0;
        for (double[] p : pts) {                              // chaine inferieure
            while (k >= 2 && cross(hull[k - 2], hull[k - 1], p) <= 0) {
                k--;
            }
            hull[k++] = p;
        }
        for (int i = pts.length - 2, lower = k + 1; i >= 0; i--) {   // chaine superieure
            while (k >= lower && cross(hull[k - 2], hull[k - 1], pts[i]) <= 0) {
                k--;
            }
            hull[k++] = pts[i];
        }
        double[][] result = new double[k - 1][];              // le dernier point repete le premier
        System.arraycopy(hull, 0, result, 0, k - 1);
        return new Polygon(result);
    }

    // Produit vectoriel (o->a) x (o->b) : positif si on tourne a gauche.
    private static double cross(double[] o, double[] a, double[] b) {
        return (a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0]);
    }
}
