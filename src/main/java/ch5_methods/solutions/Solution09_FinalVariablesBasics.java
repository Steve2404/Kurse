package ch5_methods.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise09_FinalVariablesBasics.
 */
public class Solution09_FinalVariablesBasics {

    static class Config {
        static final int MAX_RETRIES = 3;

        final String name;

        Config(String name) {
            this.name = name;
        }

        String describe() {
            // final local, final d'instance (propre a l'objet) et static final (partage) : aucun ne peut etre reassigne.
            final int localDoubled = MAX_RETRIES * 2;
            return name + " : max " + MAX_RETRIES + " tentatives, double = " + localDoubled;
        }
    }
}
