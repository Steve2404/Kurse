package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise04_StringSearchAndCompare.
 */
public class Solution04_StringSearchAndCompare {

    public static int countOccurrences(String text, String part) {
        // indexOf(part, from) : on repart APRES le morceau trouve, donc pas de chevauchement.
        int count = 0;
        int from = 0;
        int index;
        while ((index = text.indexOf(part, from)) >= 0) {
            count++;
            from = index + part.length();
        }
        return count;
    }

    public static String lastWord(String sentence) {
        // strip d'abord (sinon le dernier "mot" serait vide) ; lastIndexOf = -1 donne substring(0).
        String clean = sentence.strip();
        return clean.substring(clean.lastIndexOf(' ') + 1);
    }

    public static int myCompareTo(String a, String b) {
        // La regle de compareTo : premiere difference de caracteres, sinon difference de longueurs.
        int n = Math.min(a.length(), b.length());
        for (int i = 0; i < n; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return a.charAt(i) - b.charAt(i);
            }
        }
        return a.length() - b.length();
    }

    public static boolean isRotation(String a, String b) {
        // a + a contient toutes les rotations ; la longueur evite les faux positifs ("ab" dans "abcabc").
        return a.length() == b.length() && (a + a).contains(b);
    }

    public static String positionsOf(String text, char c) {
        // indexOf(c, i + 1) : repartir juste apres la derniere position, sinon boucle infinie.
        StringBuilder sb = new StringBuilder();
        int i = text.indexOf(c);
        while (i >= 0) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(i);
            i = text.indexOf(c, i + 1);
        }
        return sb.toString();
    }
}
