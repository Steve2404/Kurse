package ch1_buildingblocks.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise11_ConstructorsAndInitOrder.
 */
public class Solution11_ConstructorsAndInitOrder {

    static class Widget {
        private final List<String> events = new ArrayList<>();

        {
            events.add("field-init");
            events.add("instance-block");
        }

        Widget() {
            events.add("constructor");
        }

        List<String> getEvents() {
            return events;
        }
    }
}
