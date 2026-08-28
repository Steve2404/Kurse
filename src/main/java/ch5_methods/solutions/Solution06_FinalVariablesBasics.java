package ch5_methods.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise06_FinalVariablesBasics.
 */
public class Solution06_FinalVariablesBasics {

    static class Config {
        static final int MAX_RETRIES = 3;

        final String name;

        Config(String name) {
            this.name = name;
        }

        String describe() {
            final int localDoubled = MAX_RETRIES * 2;
            return name + " : max " + MAX_RETRIES + " tentatives, double = " + localDoubled;
        }
    }
}
