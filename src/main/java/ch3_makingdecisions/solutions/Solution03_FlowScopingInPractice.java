package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise03_FlowScopingInPractice.
 */
public class Solution03_FlowScopingInPractice {

    public static int lengthOrMinusOne(Object o) {
        // Le return fait sortir si ce n'est pas une String : la ligne suivante ne s'execute
        // QUE si le test a reussi, donc s y existe. null instanceof donne false.
        if (!(o instanceof String s)) return -1;
        return s.length();
    }

    public static String upperOrQuestionMark(Object o) {
        // Le else d'un test NIE correspond au cas "c'est une String".
        if (!(o instanceof String s)) {
            return "?";
        } else {
            return s.toUpperCase();
        }
    }

    public static String longWordOrEmpty(Object o) {
        // && n'evalue la droite que si la gauche a reussi : s est deja la.
        return o instanceof String s && s.length() > 3 ? s : "";
    }

    public static int totalStringLength(Object[] items) {
        // continue joue le meme role que return : la suite du tour prouve que c'est une String.
        int total = 0;
        for (Object item : items) {
            if (!(item instanceof String s)) continue;
            total += s.length();
        }
        return total;
    }
}
