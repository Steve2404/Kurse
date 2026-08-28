package ch6_classdesign.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise05_InitializationOrder.
 */
public class Solution05_InitializationOrder {

    public static List<String> buildExpectedOrder() {
        return List.of(
                "Parent.staticVar",
                "Parent.staticBlock",
                "Child.staticVar",
                "Child.staticBlock",
                "Parent.instanceVar",
                "Parent.instanceBlock",
                "Parent.constructor",
                "Child.instanceVar",
                "Child.instanceBlock",
                "Child.constructor");
    }
}
