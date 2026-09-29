package ch5_methods.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise10_EffectivelyFinalInPractice.
 */
public class Solution10_EffectivelyFinalInPractice {

    public static boolean isEffectivelyFinal(boolean declaredWithValue, String... statements) {
        // Avec valeur : aucune ecriture ; sans valeur : exactement UNE ecriture simple.
        int simple = 0;
        int compound = 0;
        for (String statement : statements) {
            switch (writeKind(statement)) {
                case "simple" -> simple++;
                case "compound" -> compound++;
                default -> {
                }
            }
        }
        if (declaredWithValue) {
            return simple == 0 && compound == 0;
        }
        return simple == 1 && compound == 0;
    }

    private static String writeKind(String statement) {
        // Petite boite : reconnait les ecritures dans x ; tout le reste est une lecture.
        String s = statement.strip();
        if (s.startsWith("x =") && !s.startsWith("x ==")) {
            return "simple";
        }
        if (s.startsWith("x++") || s.startsWith("x--") || s.startsWith("++x") || s.startsWith("--x")
                || s.startsWith("x +=") || s.startsWith("x -=") || s.startsWith("x *=") || s.startsWith("x /=")) {
            return "compound";
        }
        return "none";
    }

    public static IntUnaryOperator multiplier(int factor) {
        // Un parametre jamais reassigne est effectivement final : la lambda peut le capturer.
        return x -> x * factor;
    }

    public static List<IntSupplier> suppliers(int n) {
        // copy est une NOUVELLE variable a chaque tour, jamais modifiee (i, lui, change).
        List<IntSupplier> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int copy = i;
            result.add(() -> copy);
        }
        return result;
    }

    public static int countWithLambda(int times) {
        // La reference box ne change pas ; seul son contenu change : c'est permis.
        int[] box = {0};
        Runnable r = () -> box[0]++;
        for (int i = 0; i < times; i++) {
            r.run();
        }
        return box[0];
    }

    public static Function<Integer, String> labeler(String prefix) {
        // prefix n'est jamais reassigne dans la methode.
        return value -> prefix + value;
    }
}
