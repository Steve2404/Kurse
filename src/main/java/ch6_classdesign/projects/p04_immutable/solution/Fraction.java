package ch6_classdesign.projects.p04_immutable.solution;

/**
 * SOLUTION - une fraction IMMUABLE, toujours normalisee (irreductible, denominateur positif).
 */
public final class Fraction {

    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);

    private final long num;
    private final long den;

    // Le constructeur normalise : apres lui, l'objet est dans un etat valide pour toujours.
    private Fraction(long num, long den) {
        long g = gcd(Math.abs(num), Math.abs(den));
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.num = num / g;
        this.den = den / g;
    }

    public static Fraction of(long num, long den) {
        return new Fraction(num, den);
    }

    public static Fraction of(long n) {
        return new Fraction(n, 1);
    }

    private static long gcd(long a, long b) {
        return b == 0 ? Math.max(a, 1) : gcd(b, a % b);
    }

    public Fraction plus(Fraction o) {
        return new Fraction(num * o.den + o.num * den, den * o.den);
    }

    public Fraction minus(Fraction o) {
        return new Fraction(num * o.den - o.num * den, den * o.den);
    }

    public Fraction times(Fraction o) {
        return new Fraction(num * o.num, den * o.den);
    }

    public Fraction divide(Fraction o) {
        return new Fraction(num * o.den, den * o.num);
    }

    public boolean isZero() {
        return num == 0;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Fraction f && f.num == num && f.den == den;   // possible car la forme est unique
    }

    @Override
    public int hashCode() {
        return 31 * Long.hashCode(num) + Long.hashCode(den);
    }

    @Override
    public String toString() {
        return den == 1 ? String.valueOf(num) : num + "/" + den;
    }
}
