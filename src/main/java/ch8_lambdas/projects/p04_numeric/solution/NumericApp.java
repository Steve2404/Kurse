package ch8_lambdas.projects.p04_numeric.solution;

import ch8_lambdas.projects.p04_numeric.Data;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

/**
 * SOLUTION du projet 4 - le laboratoire numerique.
 */
public class NumericApp {

    private static long seed = Data.SEED;

    public static void main(String[] args) {
        DoubleUnaryOperator square2 = x -> x * x - 2;
        DoubleUnaryOperator cosFix = x -> Math.cos(x) - x;
        System.out.println("racine de 2 : dichotomie " + Numeric.bisection(square2, 0, 2, Data.EPSILON) + ", Newton " + Numeric.newton(square2, 1, Data.EPSILON));
        System.out.println("cos(x) = x : dichotomie " + Numeric.bisection(cosFix, 0, 1, Data.EPSILON) + ", Newton " + Numeric.newton(cosFix, 1, Data.EPSILON));
        DoubleUnaryOperator gauss = x -> Math.exp(-x * x);
        System.out.println("integrales : x^2 sur [0,3] = " + Numeric.round(Numeric.simpson(x -> x * x, 0, 3, 10)) + ", sin sur [0,pi] = "
                + Numeric.round(Numeric.simpson(Math::sin, 0, Math.PI, 100)) + ", exp(-x^2) sur [-5,5] = " + Numeric.round(Numeric.simpson(gauss, -5, 5, 200))
                + " (racine de pi " + Numeric.round(Math.sqrt(Math.PI)) + ")");
        DoubleUnaryOperator parabola = x -> -(x - 1.5) * (x - 1.5) + 4;
        System.out.println("maximum de -(x-1.5)^2+4 : x = " + Numeric.goldenMax(parabola, -10, 10, 1e-7) + " ; derivee de sin en 0 = "
                + Numeric.round(Numeric.derivative(Math::sin).applyAsDouble(0)));

        // Composition de fonctions primitives : andThen, compose, identity.
        DoubleUnaryOperator plus1 = x -> x + 1;
        DoubleUnaryOperator times3 = x -> x * 3;
        IntUnaryOperator collatz = n -> n % 2 == 0 ? n / 2 : 3 * n + 1;
        System.out.println("composition : " + plus1.andThen(times3).applyAsDouble(2) + " " + plus1.compose(times3).applyAsDouble(2) + " "
                + DoubleUnaryOperator.identity().applyAsDouble(7) + " ; collatz(27) " + Numeric.stepsUntil(27, collatz, n -> n == 1) + " pas, "
                + IntUnaryOperator.identity().andThen(collatz).applyAsInt(7));

        int[] n = Data.NUMBERS;
        IntBinaryOperator lcm = (a, b) -> a / Numeric.gcd(a, b) * b;
        System.out.println("reductions : somme " + Numeric.reduce(n, 0, Integer::sum) + ", max " + Numeric.reduce(n, Integer.MIN_VALUE, Math::max) + ", pgcd "
                + Numeric.reduce(n, 0, Numeric::gcd) + ", ppcm " + Numeric.reduce(new int[] {4, 6, 10, 15}, 1, lcm));
        IntPredicate even = x -> x % 2 == 0;
        IntPredicate big = x -> x > 50;
        IntPredicate prime = x -> {
            if (x < 2) {
                return false;
            }
            for (int d = 2; d * d <= x; d++) {
                if (x % d == 0) {
                    return false;
                }
            }
            return true;
        };
        System.out.println("filtres : pairs " + Numeric.count(n, even) + ", pairs et grands " + Numeric.count(n, even.and(big)) + ", premiers ou impairs "
                + Numeric.count(n, prime.or(even.negate())) + ", premiers <= 100 : " + Numeric.count(range(1, 100), prime));

        // Les autres formes primitives.
        ToIntFunction<String> vowels = s -> s.replaceAll("[^aeiouy]", "").length();
        IntFunction<String> bar = k -> "#".repeat(k);
        IntToDoubleFunction root = Math::sqrt;
        IntToLongFunction factorial = k -> {
            long f = 1;
            for (int i = 2; i <= k; i++) {
                f *= i;
            }
            return f;
        };
        ObjIntConsumer<StringBuilder> appendTwice = (sb, k) -> sb.append(k).append(k);
        StringBuilder sb = new StringBuilder();
        for (String w : Data.WORDS) {
            sb.append(w).append('=').append(bar.apply(vowels.applyAsInt(w))).append(' ');
        }
        appendTwice.accept(sb, 7);
        System.out.println("voyelles : " + sb + " ; racine(49) " + root.applyAsDouble(49) + ", 20! " + factorial.applyAsLong(20));

        // Monte-Carlo : un DoubleSupplier deterministe, un IntSupplier qui compte, un BooleanSupplier.
        DoubleSupplier rnd = () -> {
            seed = (seed * 25214903917L + 11) & ((1L << 48) - 1);
            return (seed >>> 22) / (double) (1L << 26);
        };
        int[] inside = {0};
        IntSupplier counter = () -> inside[0];
        for (int i = 0; i < Data.SAMPLES; i++) {
            double x = rnd.getAsDouble();
            double y = rnd.getAsDouble();
            if (x * x + y * y <= 1) {
                inside[0]++;
            }
        }
        BooleanSupplier closeToPi = () -> Math.abs(4.0 * counter.getAsInt() / Data.SAMPLES - Math.PI) < 0.05;
        DoubleBinaryOperator hypot = Math::hypot;
        System.out.println("monte-carlo : " + counter.getAsInt() + " / " + Data.SAMPLES + " -> pi ~ " + 4.0 * counter.getAsInt() / Data.SAMPLES + ", proche "
                + closeToPi.getAsBoolean() + " ; hypot(3, 4) " + hypot.applyAsDouble(3, 4));
    }

    static int[] range(int from, int to) {
        int[] r = new int[to - from + 1];
        for (int i = 0; i < r.length; i++) {
            r[i] = from + i;
        }
        return r;
    }
}
