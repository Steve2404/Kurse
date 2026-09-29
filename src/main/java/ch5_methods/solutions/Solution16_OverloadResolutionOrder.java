package ch5_methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise16_OverloadResolutionOrder.
 */
public class Solution16_OverloadResolutionOrder {

    public static List<String> buildExpectedResolutions() {
        // Ordre de javac : exact, puis elargissement, puis boxing, puis varargs en dernier.
        return List.of(
                "int",     // widthPick(5) : exact match sur int
                "long",    // widthPick(5L) : exact match sur long
                "int",     // widthPick(short) : elargissement vers le plus petit type suffisant (int)
                "long",    // boxPick(5) : elargissement (int -> long) prefere a l'autoboxing (int -> Integer)
                "Integer"  // varargsPick(5) : autoboxing prefere aux varargs, le dernier recours
        );
    }
}
