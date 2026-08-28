package ch8_lambdas.solutions;

import java.util.function.Function;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise02_MethodReferences.
 */
public class Solution02_MethodReferences {

    static final class Username {
        private final String value;

        Username(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

    static String collapseSpaces(String s) {
        return s.replaceAll("\\s+", " ");
    }

    public static Function<String, Username> buildNormalizationPipeline() {
        Function<String, String> trimStep = String::trim;
        Function<String, String> collapseStep = Solution02_MethodReferences::collapseSpaces;
        Function<String, String> lowerStep = String::toLowerCase;

        String prefix = "user:";
        Function<String, String> prefixStep = prefix::concat;

        Function<String, Username> wrapStep = Username::new;

        return trimStep.andThen(collapseStep).andThen(lowerStep).andThen(prefixStep).andThen(wrapStep);
    }
}
