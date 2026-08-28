package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise02_PatternMatchingAndFlowScoping.
 */
public class Solution02_PatternMatchingAndFlowScoping {

    public static String describeViaEarlyReturn(Object obj) {
        if (!(obj instanceof String s)) {
            return "pas une String";
        }
        return "String de longueur " + s.length();
    }

    public static String describeIfLongString(Object obj) {
        if (obj instanceof String s && s.length() > 3) {
            return "longue String : " + s;
        }
        return "trop courte ou pas une String";
    }
}
