package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise10_VariableScopeBasics.
 */
public class Solution10_VariableScopeBasics {

    static class ScopeDemo {
        static int classVar = 100;
        int instanceVar;

        ScopeDemo(int instanceVar) {
            this.instanceVar = instanceVar;
        }

        int computeWithLocal() {
            int localVar = 5;
            return instanceVar + classVar + localVar;
        }
    }

    public static void incrementSharedClassVar() {
        ScopeDemo.classVar++;
    }
}
