package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 28 - Algorithmique sur le texte : anagrammes, palindromes, compression, parentheses, Cesar (niveau : avance)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Les 4 schemas d'algorithme de cet exercice --
 *
 *   TRIER POUR COMPARER  : deux mots sont anagrammes si leurs lettres TRIEES sont egales.
 *   DEUX POINTEURS       : un index au debut, un a la fin, qui avancent l'un vers l'autre.
 *   COMPTER AVEC UN int[26] : counts[c - 'a']++ remplace une Map pour les 26 lettres.
 *   PILE AVEC UN TABLEAU : char[] stack + int top ; empiler = stack[top++] = c ; depiler = stack[--top].
 *
 *
 * ==================================================================
 * TODO 1 : isAnagram(a, b)    [trier pour comparer]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "Chien" et "niche" utilisent les memes lettres. Si on range les
 * lettres de chaque mot par ordre alphabetique, on obtient la meme
 * suite. On ignore les majuscules et les espaces.
 *
 * -- Essayons a la main --
 *
 *   "Chien" / "niche" -> c,e,h,i,n = c,e,h,i,n -> true
 *   "abc" / "abd"     -> false
 *
 * -- Le plan --
 *
 *   1. Petite boite sortedLetters(s) : s.replace(" ", "").toLowerCase().toCharArray(), Arrays.sort, rendre le char[].
 *   2. Rendre Arrays.equals(sortedLetters(a), sortedLetters(b)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : sortedLetters, appelee deux fois.
 *
 *
 * ==================================================================
 * TODO 2 : isPalindromeSentence(s)    [deux pointeurs]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ne regarde que les lettres et les chiffres, sans les majuscules.
 * Le doigt de gauche saute ce qui n'est pas une lettre, celui de droite
 * aussi, puis on compare.
 *
 * -- Essayons a la main --
 *
 *   "Engage le jeu que je le gagne" -> true     "Java" -> false     "" -> true
 *
 * -- Le plan --
 *
 *   1. i = 0 ; j = longueur - 1.
 *   2. Tant que i < j : si charAt(i) n'est pas Character.isLetterOrDigit, i++ ; sinon pareil pour j ;
 *      sinon comparer Character.toLowerCase des deux : different -> false ; sinon i++ et j--.
 *   3. Rendre true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : rle(s)    et    TODO 4 : unRle(encoded)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Compression "run-length" : au lieu d'ecrire "aaa", on ecrit "a3".
 * Pour decoder, on lit une lettre, puis TOUS les chiffres qui suivent
 * (le nombre peut avoir plusieurs chiffres : "x12" = 12 fois x).
 *
 * -- Essayons a la main --
 *
 *   rle("aaabccdddd") -> "a3b1c2d4"      rle("") -> ""
 *   unRle("a3b1c2d4") -> "aaabccdddd"    unRle("x12") -> "xxxxxxxxxxxx"
 *
 * -- Le plan --
 *
 *   rle   : i = 0 ; tant que i < longueur : j = i ; avancer j tant que meme lettre ;
 *           append(lettre).append(j - i) ; i = j.
 *   unRle : i = 0 ; tant que i < longueur : c = charAt(i++) ; n = 0 ;
 *           tant que charAt(i) est un chiffre : n = n * 10 + (charAt(i++) - '0') ;
 *           append(String.valueOf(c).repeat(n)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : isBalanced(s)    [pile avec un tableau]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque parenthese ouvrante attend SA fermante, dans l'ordre inverse.
 * On empile les ouvrantes ; a chaque fermante, le sommet de la pile
 * doit etre la bonne ouvrante. A la fin, la pile doit etre vide.
 *
 * -- Essayons a la main --
 *
 *   "{[()()]}" -> true     "([)]" -> false (on attend ')' mais on lit ']')
 *   "(("       -> false (pile pas vide)     "a(b)c" -> true (les lettres sont ignorees)
 *
 * -- Le plan --
 *
 *   1. char[] stack = new char[s.length()] ; int top = 0.
 *   2. Pour chaque c : ouvrante ( [ { -> stack[top++] = c.
 *      Fermante : si top == 0 ou stack[--top] n'est pas l'ouvrante attendue -> false.
 *   3. Rendre top == 0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : opening(c) rend l'ouvrante attendue pour une fermante (switch).
 *
 *
 * ==================================================================
 * TODO 6 : caesar(text, shift)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On decale chaque lettre minuscule de shift places dans l'alphabet, en
 * revenant a 'a' apres 'z'. Les autres caracteres ne bougent pas. Un
 * decalage negatif decode. Piege : -1 % 26 vaut -1 en Java ; Math.floorMod
 * donne 25.
 *
 * -- Essayons a la main --
 *
 *   ("abc xyz", 3) -> "def abc"      ("def abc", -3) -> "abc xyz"
 *
 * -- Le plan --
 *
 *   1. Pour chaque c de 'a' a 'z' : (char) ('a' + Math.floorMod(c - 'a' + shift, 26)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : mostFrequentLetter(s)    [compter avec un int[26]]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "banana" -> 'a' (3)     "Mississippi" -> i et s ont 4 : on garde le premier dans l'alphabet -> 'i'
 *
 * -- Le plan --
 *
 *   1. counts = new int[26] ; pour chaque lettre (en minuscule) : counts[c - 'a']++.
 *   2. Chercher l'index du maximum (> strict : garde le premier en cas d'egalite).
 *   3. Rendre (char) ('a' + index).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - import java.util.Arrays; Arrays.sort(char[]) et Arrays.equals(char[], char[]).
 *   - Un char est un nombre : c - 'a' donne sa position (0 a 25), '7' - '0' vaut 7.
 */
public class Exercise28_StringAlgorithms {

    public static boolean isAnagram(String a, String b) {
        throw new UnsupportedOperationException("TODO 1 : implementer isAnagram()");
    }

    public static boolean isPalindromeSentence(String s) {
        throw new UnsupportedOperationException("TODO 2 : implementer isPalindromeSentence()");
    }

    public static String rle(String s) {
        throw new UnsupportedOperationException("TODO 3 : implementer rle()");
    }

    public static String unRle(String encoded) {
        throw new UnsupportedOperationException("TODO 4 : implementer unRle()");
    }

    public static boolean isBalanced(String s) {
        throw new UnsupportedOperationException("TODO 5 : implementer isBalanced()");
    }

    public static String caesar(String text, int shift) {
        throw new UnsupportedOperationException("TODO 6 : implementer caesar()");
    }

    public static char mostFrequentLetter(String s) {
        throw new UnsupportedOperationException("TODO 7 : implementer mostFrequentLetter()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("isAnagram : Chien/niche, listen/silent oui, abc/abd non",
                isAnagram("Chien", "niche") && isAnagram("listen", "silent") && !isAnagram("abc", "abd") && !isAnagram("ab", "abb"));
        ExerciseChecker.check("isPalindromeSentence : oui, non, vide",
                isPalindromeSentence("Engage le jeu que je le gagne") && !isPalindromeSentence("Java") && isPalindromeSentence(""));
        ExerciseChecker.check("rle : a3b1c2d4 et vide", rle("aaabccdddd").equals("a3b1c2d4") && rle("").isEmpty());
        ExerciseChecker.check("unRle : aaabccdddd et 12 x", unRle("a3b1c2d4").equals("aaabccdddd") && unRle("x12").equals("x".repeat(12)));
        ExerciseChecker.check("unRle(rle(s)) == s", unRle(rle("zzzzzzzzzzzzzzyyx")).equals("zzzzzzzzzzzzzzyyx"));
        ExerciseChecker.check("isBalanced : 5 cas",
                isBalanced("{[()()]}") && !isBalanced("([)]") && !isBalanced("((") && isBalanced("") && isBalanced("a(b)c") && !isBalanced(")("));
        ExerciseChecker.check("caesar : aller et retour",
                caesar("abc xyz", 3).equals("def abc") && caesar("def abc", -3).equals("abc xyz"));
        ExerciseChecker.check("mostFrequentLetter : a et i", mostFrequentLetter("banana") == 'a' && mostFrequentLetter("Mississippi") == 'i');

        ExerciseChecker.summary();
    }
}
