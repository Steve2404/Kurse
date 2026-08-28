package methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise11_OverloadResolutionOrder.
 */
public class Solution11_OverloadResolutionOrder {

    public static List<String> buildExpectedResolutions() {
        return List.of(
                "int",     // widthPick(5) : exact match sur int
                "long",    // widthPick(5L) : exact match sur long
                "int",     // widthPick(short) : elargissement vers le plus petit type suffisant (int)
                "long",    // boxPick(5) : elargissement (int -> long) prefere a l'autoboxing (int -> Integer)
                "Integer"  // varargsPick(5) : autoboxing prefere aux varargs, le dernier recours
        );
    }
}
