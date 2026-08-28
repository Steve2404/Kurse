package methods.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise03_StaticMembersBasics.
 */
public class Solution03_StaticMembersBasics {

    static class Counter {
        static int totalCreated = 0;
        int id;

        Counter() {
            totalCreated++;
            id = totalCreated;
        }

        static int doubleTotal() {
            return totalCreated * 2;
        }

        String describeWithTotal() {
            return "Counter #" + id + " sur " + totalCreated + " au total";
        }

        String describeDoubled() {
            return "Double du total : " + doubleTotal();
        }
    }
}
