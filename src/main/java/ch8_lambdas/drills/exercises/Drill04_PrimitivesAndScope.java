package ch8_lambdas.drills.exercises;

import ch8_lambdas.ExerciseChecker;
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
 * DRILL 04 - Interfaces primitives et variables capturees
 * ======================================================
 *
 * Mode d'emploi : voir Drill01_LambdaSyntax.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : isEven()            [IntPredicate] n % 2 == 0.
 * TODO 2  : square()            [IntUnaryOperator] n * n.
 * TODO 3  : max()               [IntBinaryOperator] Math::max.
 * TODO 4  : length()            [ToIntFunction<String>] String::length.
 * TODO 5  : sumOfSquares()      [IntUnaryOperator dans une boucle] carres de Words.NUMBERS -> 171.
 * TODO 6  : countEven()         [IntPredicate dans une boucle] pairs de Words.NUMBERS -> 2.
 * TODO 7  : largest()           [IntBinaryOperator comme accumulateur] max de Words.NUMBERS -> 9.
 * TODO 8  : ticker()            [IntSupplier + tableau d'une case] 1, 2, 3...
 * TODO 9  : average()           [DoubleSupplier] la moyenne de Words.NUMBERS -> 5.0.
 * TODO 10 : laterMessages()     [copie par tour de boucle] une IntSupplier par index 0..2 qui rend l'index.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   IntPredicate test(int)   IntUnaryOperator applyAsInt(int)   IntBinaryOperator applyAsInt(int, int)
 *   ToIntFunction<T> applyAsInt(T)   IntFunction<R> apply(int)   IntSupplier getAsInt()
 *   DoubleSupplier getAsDouble()   BooleanSupplier getAsBoolean()   ObjIntConsumer<T> accept(T, int)
 *   Variable LOCALE capturee : final ou effectivement final ; un CHAMP : libre
 *   Pour muter : tableau d'une case (int[] box = {0}) ; dans une boucle : int copy = i ;
 *   Un parametre de lambda ne peut pas reprendre le nom d'une locale deja declaree
 * ---------------------------------------------------------------------
 */
public class Drill04_PrimitivesAndScope {

    public static IntPredicate isEven() {
        throw new UnsupportedOperationException("TODO 1 : implementer isEven()");
    }

    public static IntUnaryOperator square() {
        throw new UnsupportedOperationException("TODO 2 : implementer square()");
    }

    public static IntBinaryOperator max() {
        throw new UnsupportedOperationException("TODO 3 : implementer max()");
    }

    public static ToIntFunction<String> length() {
        throw new UnsupportedOperationException("TODO 4 : implementer length()");
    }

    public static int sumOfSquares() {
        throw new UnsupportedOperationException("TODO 5 : implementer sumOfSquares()");
    }

    public static int countEven() {
        throw new UnsupportedOperationException("TODO 6 : implementer countEven()");
    }

    public static int largest() {
        throw new UnsupportedOperationException("TODO 7 : implementer largest()");
    }

    public static IntSupplier ticker() {
        throw new UnsupportedOperationException("TODO 8 : implementer ticker()");
    }

    public static DoubleSupplier average() {
        throw new UnsupportedOperationException("TODO 9 : implementer average()");
    }

    public static List<IntSupplier> laterMessages() {
        throw new UnsupportedOperationException("TODO 10 : implementer laterMessages()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  isEven", isEven().test(8) && !isEven().test(3));
        ExerciseChecker.check("2  square(7) == 49", square().applyAsInt(7) == 49);
        ExerciseChecker.check("3  max(3, 9) == 9", max().applyAsInt(3, 9) == 9);
        ExerciseChecker.check("4  length(\"java\") == 4", length().applyAsInt("java") == 4);
        ExerciseChecker.check("5  sumOfSquares() == 171", sumOfSquares() == 171);
        ExerciseChecker.check("6  countEven() == 2", countEven() == 2);
        ExerciseChecker.check("7  largest() == 9", largest() == 9);
        IntSupplier t = ticker();
        ExerciseChecker.check("8  ticker : 1, 2, 3", t.getAsInt() == 1 && t.getAsInt() == 2 && t.getAsInt() == 3);
        ExerciseChecker.check("9  average() == 5.0", average().getAsDouble() == 5.0);
        List<IntSupplier> later = laterMessages();
        ExerciseChecker.check("10 laterMessages : 0, 1, 2",
                later.size() == 3 && later.get(0).getAsInt() == 0 && later.get(2).getAsInt() == 2);

        ExerciseChecker.summary();
    }
}
