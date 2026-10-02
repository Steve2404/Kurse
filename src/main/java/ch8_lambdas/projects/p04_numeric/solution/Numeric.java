package ch8_lambdas.projects.p04_numeric.solution;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * SOLUTION - des algorithmes numeriques PARAMETRES par des fonctions primitives (pas de boxing de double/int).
 */
public class Numeric {

    // Le resultat d'une methode iterative : la valeur et le nombre d'iterations.
    public record Result(double value, int iterations) {
        @Override
        public String toString() {
            return round(value) + " en " + iterations + " iterations";
        }
    }

    public static double round(double x) {
        return Math.round(x * 1_000_000) / 1_000_000.0;
    }

    // Une fonction qui RENVOIE une fonction : la derivee numerique (difference centree).
    public static DoubleUnaryOperator derivative(DoubleUnaryOperator f) {
        double h = 1e-6;
        return x -> (f.applyAsDouble(x + h) - f.applyAsDouble(x - h)) / (2 * h);
    }

    // Dichotomie : f(a) et f(b) de signes opposes ; on garde la moitie qui change de signe.
    public static Result bisection(DoubleUnaryOperator f, double a, double b, double eps) {
        int it = 0;
        while (b - a > eps) {
            double m = (a + b) / 2;
            if (f.applyAsDouble(a) * f.applyAsDouble(m) <= 0) {
                b = m;
            } else {
                a = m;
            }
            it++;
        }
        return new Result((a + b) / 2, it);
    }

    // Newton : x <- x - f(x)/f'(x), avec la derivee numerique.
    public static Result newton(DoubleUnaryOperator f, double x, double eps) {
        DoubleUnaryOperator df = derivative(f);
        int it = 0;
        double step;
        do {
            step = f.applyAsDouble(x) / df.applyAsDouble(x);
            x -= step;
            it++;
        } while (Math.abs(step) > eps && it < 50);
        return new Result(x, it);
    }

    // Simpson : (h/3) [f(a) + 4 f(impairs) + 2 f(pairs) + f(b)], n pair.
    public static double simpson(DoubleUnaryOperator f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = f.applyAsDouble(a) + f.applyAsDouble(b);
        for (int i = 1; i < n; i++) {
            sum += (i % 2 == 1 ? 4 : 2) * f.applyAsDouble(a + i * h);
        }
        return sum * h / 3;
    }

    // Section doree : le maximum d'une fonction unimodale sur [a, b].
    public static Result goldenMax(DoubleUnaryOperator f, double a, double b, double eps) {
        double r = (Math.sqrt(5) - 1) / 2;
        int it = 0;
        while (b - a > eps) {
            double c = b - r * (b - a);
            double d = a + r * (b - a);
            if (f.applyAsDouble(c) > f.applyAsDouble(d)) {
                b = d;
            } else {
                a = c;
            }
            it++;
        }
        return new Result((a + b) / 2, it);
    }

    // Reduction generique d'un tableau d'int par un operateur binaire primitif.
    public static int reduce(int[] values, int identity, IntBinaryOperator op) {
        int acc = identity;
        for (int v : values) {
            acc = op.applyAsInt(acc, v);
        }
        return acc;
    }

    public static int count(int[] values, IntPredicate keep) {
        int n = 0;
        for (int v : values) {
            if (keep.test(v)) {
                n++;
            }
        }
        return n;
    }

    // Iterer f a partir de x jusqu'a ce que stop soit vrai ; rend le nombre de pas.
    public static int stepsUntil(int x, IntUnaryOperator f, IntPredicate stop) {
        int steps = 0;
        while (!stop.test(x)) {
            x = f.applyAsInt(x);
            steps++;
        }
        return steps;
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
