package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise12_AnonymousClasses.
 */
public class Solution12_AnonymousClasses {

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
        return new Handler() {
            @Override
            public String handle(String input) {
                return input.toUpperCase();
            }
        };
    }

    public static Handler buildLoggingHandler(Handler delegate) {
        return new Handler() {
            @Override
            public String handle(String input) {
                return "[LOG] " + delegate.handle(input);
            }
        };
    }

    public static Greeter buildFormalGreeter() {
        return new Greeter() {
            @Override
            String greet(String name) {
                return "Bonjour, " + name;
            }
        };
    }
}
