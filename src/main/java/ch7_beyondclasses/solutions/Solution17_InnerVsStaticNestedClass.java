package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise17_InnerVsStaticNestedClass.
 */
public class Solution17_InnerVsStaticNestedClass {

    private final String owner;

    public Solution17_InnerVsStaticNestedClass(String owner) {
        this.owner = owner;
    }

    class Badge {
        String print() {
            // Classe interne : elle lit owner, le champ de l'objet englobant qui l'a creee.
            return "Badge de " + owner;
        }
    }

    static class Standalone {
        private final String label;

        Standalone(String label) {
            this.label = label;
        }

        String print() {
            // Classe imbriquee static : aucun objet englobant, seulement ses propres champs.
            return "Badge independant : " + label;
        }
    }
}
