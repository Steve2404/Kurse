package ch5_methods.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise05_StaticMembersBasics.
 */
public class Solution05_StaticMembersBasics {

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
            // Une methode d'instance lit son champ id ET le champ static partage totalCreated.
            return "Counter #" + id + " sur " + totalCreated + " au total";
        }

        String describeDoubled() {
            // Instance -> static : toujours permis (l'inverse demanderait un objet).
            return "Double du total : " + doubleTotal();
        }
    }
}
