package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 28. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise28_StringAlgorithms.
 */
public class Solution28_StringAlgorithms {

    public static boolean isAnagram(String a, String b) {
        // Trier pour comparer : deux anagrammes ont la meme suite de lettres triees.
        return Arrays.equals(sortedLetters(a), sortedLetters(b));
    }

    private static char[] sortedLetters(String s) {
        // Petite boite : normaliser (espaces, casse) puis trier le tableau de char.
        char[] letters = s.replace(" ", "").toLowerCase().toCharArray();
        Arrays.sort(letters);
        return letters;
    }

    public static boolean isPalindromeSentence(String s) {
        // Deux pointeurs : on saute ce qui n'est pas lettre/chiffre, sans creer de nouvelle chaine.
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                i++;
            } else if (!Character.isLetterOrDigit(s.charAt(j))) {
                j--;
            } else {
                if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
                    return false;
                }
                i++;
                j--;
            }
        }
        return true;
    }

    public static String rle(String s) {
        // j avance sur la serie de lettres identiques ; j - i en donne la longueur.
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            sb.append(s.charAt(i)).append(j - i);
            i = j;
        }
        return sb.toString();
    }

    public static String unRle(String encoded) {
        // n = n * 10 + chiffre : lit un nombre de plusieurs chiffres de gauche a droite.
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < encoded.length()) {
            char c = encoded.charAt(i++);
            int n = 0;
            while (i < encoded.length() && Character.isDigit(encoded.charAt(i))) {
                n = n * 10 + (encoded.charAt(i++) - '0');
            }
            sb.append(String.valueOf(c).repeat(n));
        }
        return sb.toString();
    }

    public static boolean isBalanced(String s) {
        // Une pile dans un tableau : top est le nombre d'elements empiles.
        char[] stack = new char[s.length()];
        int top = 0;
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack[top++] = c;
            } else if (c == ')' || c == ']' || c == '}') {
                if (top == 0 || stack[--top] != opening(c)) {
                    return false;
                }
            }
        }
        return top == 0;
    }

    private static char opening(char closing) {
        // Petite boite : l'ouvrante attendue pour chaque fermante.
        return switch (closing) {
            case ')' -> '(';
            case ']' -> '[';
            default -> '{';
        };
    }

    public static String caesar(String text, int shift) {
        // floorMod rend toujours un reste positif : -1 devient 25 (le % de Java rendrait -1).
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + Math.floorMod(c - 'a' + shift, 26)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static char mostFrequentLetter(String s) {
        // Un int[26] indexe par c - 'a' ; > strict garde la premiere lettre en cas d'egalite.
        int[] counts = new int[26];
        for (char c : s.toLowerCase().toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                counts[c - 'a']++;
            }
        }
        int best = 0;
        for (int i = 1; i < 26; i++) {
            if (counts[i] > counts[best]) {
                best = i;
            }
        }
        return (char) ('a' + best);
    }
}
