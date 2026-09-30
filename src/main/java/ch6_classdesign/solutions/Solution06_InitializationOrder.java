package ch6_classdesign.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise06_InitializationOrder.
 */
public class Solution06_InitializationOrder {

    public static List<String> buildExpectedOrder() {
        // static parent, static enfant (une fois), puis instance+constructeur du parent, puis ceux de l'enfant.
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
