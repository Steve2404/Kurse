package ch8_lambdas.drills.solutions;

import ch8_lambdas.drills.Words;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.drills.exercises.Drill04_PrimitivesAndScope.
 */
public class SolutionDrill04_PrimitivesAndScope {

    public static IntPredicate isEven() {
        // int -> boolean, sans Integer.
        return n -> n % 2 == 0;
    }

    public static IntUnaryOperator square() {
        // int -> int : IntUnaryOperator, sa methode est applyAsInt.
        return n -> n * n;
    }

    public static IntBinaryOperator max() {
        // Math.max(int, int) correspond exactement a (int, int) -> int.
        return Math::max;
    }

    public static ToIntFunction<String> length() {
        // ToIntFunction : un objet en entree, un int en sortie, sans boxing.
        return String::length;
    }

    public static int sumOfSquares() {
        // On reutilise square() : applyAsInt, pas apply, pour une interface primitive.
        int total = 0;
        for (int n : Words.NUMBERS) {
            total += square().applyAsInt(n);
        }
        return total;
    }

    public static int countEven() {
        // IntPredicate se teste avec test, comme Predicate, mais sur un int.
        int count = 0;
        for (int n : Words.NUMBERS) {
            if (isEven().test(n)) {
                count++;
            }
        }
        return count;
    }

    public static int largest() {
        // Un IntBinaryOperator sert d'accumulateur, en partant du premier element.
        int best = Words.NUMBERS[0];
        for (int n : Words.NUMBERS) {
            best = max().applyAsInt(best, n);
        }
        return best;
    }

    public static IntSupplier ticker() {
        // La variable box ne change pas ; son contenu, si.
        int[] box = {0};
        return () -> ++box[0];
    }

    public static DoubleSupplier average() {
        // Le calcul est fait a l'appel de getAsDouble().
        return () -> {
            int sum = 0;
            for (int n : Words.NUMBERS) {
                sum += n;
            }
            return (double) sum / Words.NUMBERS.length;
        };
    }

    public static List<IntSupplier> laterMessages() {
        // copy est une nouvelle variable par tour : capturable ; i ne le serait pas.
        List<IntSupplier> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int copy = i;
            result.add(() -> copy);
        }
        return result;
    }
}
