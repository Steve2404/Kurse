package ch8_lambdas.solutions;

import java.util.function.Function;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise04_MethodReferences.
 */
public class Solution04_MethodReferences {

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
        // Methode static : elle se branche ensuite avec Solution04_MethodReferences::collapseSpaces.
        return s.replaceAll("\\s+", " ");
    }

    public static Function<String, Username> buildNormalizationPipeline() {
        // Les 4 sortes de references (static, liee, non liee, constructeur) enchainees avec andThen.
        Function<String, String> trimStep = String::trim;
        Function<String, String> collapseStep = Solution04_MethodReferences::collapseSpaces;
        Function<String, String> lowerStep = String::toLowerCase;

        String prefix = "user:";
        Function<String, String> prefixStep = prefix::concat;

        Function<String, Username> wrapStep = Username::new;

        return trimStep.andThen(collapseStep).andThen(lowerStep).andThen(prefixStep).andThen(wrapStep);
    }
}
