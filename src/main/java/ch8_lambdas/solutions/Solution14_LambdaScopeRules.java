package ch8_lambdas.solutions;

import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise14_LambdaScopeRules.
 */
public class Solution14_LambdaScopeRules {

    private int clickCount;

    public static String nameError(String lambdaName, List<String> localsDeclaredBefore, List<String> methodParams,
                                   String methodSignature) {
        // Une lambda partage la portee de la methode : un nom local deja declare est interdit.
        if (localsDeclaredBefore.contains(lambdaName) || methodParams.contains(lambdaName)) {
            return "variable " + lambdaName + " is already defined in method " + methodSignature;
        }
        return "OK";
    }

    public static String modifyError(String target) {
        // Seules les variables LOCALES capturees doivent rester effectivement final.
        return switch (target) {
            case "local" -> "local variables referenced from a lambda expression must be final or effectively final";
            case "final own parameter" -> "final parameter n may not be assigned";
            default -> "OK";
        };
    }

    public Supplier<Object> lambdaThis() {
        // Dans une lambda, this est l'objet englobant.
        return () -> this;
    }

    public Supplier<Object> anonymousThis() {
        // Dans une classe anonyme, this est l'objet anonyme lui-meme.
        return new Supplier<Object>() {
            @Override
            public Object get() {
                return this;
            }
        };
    }

    public int clicks() {
        // Un champ peut etre modifie dans une lambda (il n'est pas capture comme une locale).
        Runnable click = () -> clickCount++;
        click.run();
        click.run();
        click.run();
        return clickCount;
    }
}
