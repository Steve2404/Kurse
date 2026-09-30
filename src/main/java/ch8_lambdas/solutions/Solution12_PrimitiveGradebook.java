package ch8_lambdas.solutions;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise12_PrimitiveGradebook.
 */
public class Solution12_PrimitiveGradebook {

    public static String methodOf(String interfaceName) {
        // Rendre un primitif -> applyAsXxx / getAsXxx ; rendre boolean -> test ; ne rien rendre -> accept.
        if (interfaceName.endsWith("Predicate")) {
            return "test";
        }
        if (interfaceName.endsWith("Consumer")) {
            return "accept";
        }
        return switch (interfaceName) {
            case "IntSupplier" -> "getAsInt";
            case "BooleanSupplier" -> "getAsBoolean";
            case "IntFunction" -> "apply";
            case "DoubleUnaryOperator" -> "applyAsDouble";
            default -> "applyAsInt";
        };
    }

    public static IntPredicate passing() {
        // int -> boolean, sans boxing.
        return note -> note >= 10;
    }

    public static IntUnaryOperator curve() {
        // int -> int : Math.min plafonne a 20.
        return note -> Math.min(20, note + 2);
    }

    public static IntBinaryOperator sum() {
        // Integer::sum convient aussi : (int, int) -> int.
        return (a, b) -> a + b;
    }

    public static IntFunction<String> mention() {
        // int -> objet : IntFunction<R>, methode apply.
        return note -> {
            if (note >= 16) {
                return "TB";
            }
            if (note >= 14) {
                return "B";
            }
            if (note >= 12) {
                return "AB";
            }
            return note >= 10 ? "P" : "AJ";
        };
    }

    public static ToIntFunction<String> nameLength() {
        // objet -> int : ToIntFunction<T>, methode applyAsInt.
        return String::length;
    }

    public static IntSupplier counter() {
        // Le tableau (effectivement final) garde l'etat entre deux appels.
        int[] box = {0};
        return () -> ++box[0];
    }

    public static ObjIntConsumer<StringBuilder> appendScore() {
        // (objet, int) -> rien.
        return (sb, score) -> sb.append(score).append(';');
    }

    public static DoubleUnaryOperator toPercent() {
        // double -> double sans boxing : DoubleUnaryOperator.applyAsDouble ; une note sur 20 fois 5 = pourcentage.
        return x -> x * 5;
    }

    public static String report(int... scores) {
        // Chaque interface primitive fait une etape, sans jamais boxer.
        int admitted = 0;
        int total = 0;
        StringBuilder mentions = new StringBuilder();
        for (int note : scores) {
            int curved = curve().applyAsInt(note);
            if (passing().test(curved)) {
                admitted++;
            }
            total = sum().applyAsInt(total, curved);
            if (mentions.length() > 0) {
                mentions.append(',');
            }
            mentions.append(mention().apply(curved));
        }
        return "admis=" + admitted + " total=" + total + " mentions=" + mentions;
    }
}
