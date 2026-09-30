package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise05_ValidatorToolkit.
 */
public class Solution05_ValidatorToolkit {

    public interface Validator {
        boolean test(String s);

        default Validator and(Validator other) {
            // this = le validateur courant ; on rend un NOUVEAU validateur qui combine les deux.
            return s -> this.test(s) && other.test(s);
        }

        default Validator or(Validator other) {
            return s -> this.test(s) || other.test(s);
        }

        default Validator negate() {
            return s -> !this.test(s);
        }

        default String describe(String s) {
            // Une methode default peut appeler une methode private de la meme interface.
            return s + " : " + label(test(s));
        }

        default int firstFailure(String... values) {
            // test() est la methode abstraite : chaque implementation fournit la sienne.
            for (int i = 0; i < values.length; i++) {
                if (!test(values[i])) {
                    return i;
                }
            }
            return -1;
        }

        private String label(boolean ok) {
            return ok ? "valide" : "invalide";
        }

        static Validator minLength(int n) {
            // Fabrique static : s'appelle Validator.minLength(...), jamais heritee.
            return s -> s.length() >= n;
        }

        static Validator containsDigit() {
            return s -> {
                for (char c : s.toCharArray()) {
                    if (Character.isDigit(c)) {
                        return true;
                    }
                }
                return false;
            };
        }

        static Validator allOf(Validator... validators) {
            // Element neutre "tout accepter", puis and(...) en chaine.
            Validator result = s -> true;
            for (Validator v : validators) {
                result = result.and(v);
            }
            return result;
        }
    }
}
