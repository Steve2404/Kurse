package ch1_buildingblocks.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise14_ConstructorsAndInitOrder.
 */
public class Solution14_ConstructorsAndInitOrder {

    static class Widget {
        private final List<String> events = new ArrayList<>();

        {
            events.add("field-init");
            events.add("instance-block");
        }

        Widget() {
            // Un constructeur n'a pas de type de retour (pas meme void). Son corps s'execute
            // APRES les initialiseurs de champs et les blocs d'instance : il ajoute donc en dernier.
            events.add("constructor");
        }

        List<String> getEvents() {
            return events;
        }
    }
}
