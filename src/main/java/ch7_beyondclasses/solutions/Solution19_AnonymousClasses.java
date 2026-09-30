package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 19. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise19_AnonymousClasses.
 */
public class Solution19_AnonymousClasses {

    interface Handler {
        String handle(String input);
    }

    abstract static class Greeter {
        abstract String greet(String name);

        String shout(String name) {
            return greet(name).toUpperCase();
        }
    }

    public static Handler buildUppercaseHandler() {
        // Classe anonyme : on implemente l'interface en une seule expression, sans nom.
        return new Handler() {
            @Override
            public String handle(String input) {
                return input.toUpperCase();
            }
        };
    }

    public static Handler buildLoggingHandler(Handler delegate) {
        // L'anonyme capture delegate (effectivement final) et l'enveloppe.
        return new Handler() {
            @Override
            public String handle(String input) {
                return "[LOG] " + delegate.handle(input);
            }
        };
    }

    public static Greeter buildFormalGreeter() {
        // Une anonyme peut aussi ETENDRE une classe et redefinir une de ses methodes.
        return new Greeter() {
            @Override
            String greet(String name) {
                return "Bonjour, " + name;
            }
        };
    }
}
