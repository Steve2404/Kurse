package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise12_VariableScopeBasics.
 */
public class Solution12_VariableScopeBasics {

    static class ScopeDemo {
        static int classVar = 100;
        int instanceVar;

        ScopeDemo(int instanceVar) {
            this.instanceVar = instanceVar;
        }

        int computeWithLocal() {
            // Les 3 portees se lisent pareil : locale (ici), d'instance (par objet), de classe (partagee).
            int localVar = 5;
            return instanceVar + classVar + localVar;
        }
    }

    public static void incrementSharedClassVar() {
        // classVar est static : une seule copie, donc le changement est vu par TOUS les objets.
        ScopeDemo.classVar++;
    }
}
