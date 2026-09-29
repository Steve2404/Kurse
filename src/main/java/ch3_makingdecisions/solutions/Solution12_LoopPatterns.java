package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise12_LoopPatterns.
 */
public class Solution12_LoopPatterns {

    public static boolean isPalindrome(String text) {
        // Deux compteurs du MEME type dans un seul for ; ils se rapprochent a chaque tour.
        for (int i = 0, j = text.length() - 1; i < j; i++, j--) {
            if (text.charAt(i) != text.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    public static int digitCount(int n) {
        // do/while tourne au moins une fois : 0 compte bien pour un chiffre.
        n = Math.abs(n);
        int count = 0;
        do {
            count++;
            n = n / 10;
        } while (n != 0);
        return count;
    }

    public static int collatzSteps(int n) {
        // for(;;) : pas de condition, la seule sortie est le break.
        int steps = 0;
        for (;;) {
            if (n == 1) {
                break;
            }
            n = n % 2 == 0 ? n / 2 : 3 * n + 1;
            steps++;
        }
        return steps;
    }

    public static char firstRepeatedChar(String text) {
        // La boucle interieure commence apres i : on ne compare jamais une lettre avec elle-meme.
        for (int i = 0; i < text.length(); i++) {
            for (int j = i + 1; j < text.length(); j++) {
                if (text.charAt(i) == text.charAt(j)) {
                    return text.charAt(i);
                }
            }
        }
        return '-';
    }

    public static String triangle(int rows) {
        // La borne de la boucle interieure depend du compteur de la boucle exterieure.
        String result = "";
        for (int r = 1; r <= rows; r++) {
            for (int s = 0; s < r; s++) {
                result += "*";
            }
            result += "\n";
        }
        return result;
    }
}
