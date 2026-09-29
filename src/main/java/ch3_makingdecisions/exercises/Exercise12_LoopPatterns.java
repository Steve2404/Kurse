package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 12 - Les formes de boucles qu'on rencontre vraiment : 2 compteurs, do/while, for(;;), boucles imbriquees (niveau : difficile)
 * =====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Rappels verifies avec javac 17 --
 *
 *   for (int i = 0, j = 10; i < j; i++, j--)   -> COMPILE (2 variables du MEME type)
 *   for (int i = 0, long j = 10; ...)          -> error: <identifier> expected (2 types : interdit)
 *   for (;;) { }                               -> COMPILE : boucle infinie, sortie par break/return
 *   while (true) { } sans return apres         -> COMPILE (la fin de la methode est inatteignable)
 *   while (false) { x = 2; }                   -> error: unreachable statement
 *   do { i++; } while (i < 5); avec i = 10 au depart -> le corps s'execute UNE fois (i vaut 11)
 *
 *
 * ==================================================================
 * TODO 1 : isPalindrome(text)   [2 compteurs dans un for]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux doigts : un au debut, un a la fin. On compare, puis ils
 * avancent l'un vers l'autre. S'ils se croisent sans difference, c'est
 * un palindrome. Les 2 compteurs vivent dans le MEME for.
 *
 * -- Essayons a la main --
 *
 *   "kayak" -> true ; "java" -> false ; "" -> true ; "a" -> true
 *
 * -- Le plan --
 *
 *   1. for (int i = 0, j = longueur - 1; i < j; i++, j--) : si caracteres differents -> false.
 *   2. Fin de boucle -> true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : digitCount(n)   [do/while]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On enleve un chiffre (n / 10) jusqu'a ce qu'il n'en reste plus. Mais
 * 0 a UN chiffre : avec un while, la boucle ne tournerait pas et on
 * dirait 0. Le do/while tourne au moins une fois : il gere 0
 * naturellement. Les negatifs : on travaille sur la valeur absolue.
 *
 * -- Essayons a la main --
 *
 *   12345 -> 5 ; 0 -> 1 ; -907 -> 3
 *
 * -- Le plan --
 *
 *   1. n = valeur absolue ; count = 0.
 *   2. do { count++; n = n / 10; } while (n != 0);
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : collatzSteps(n)   [for(;;) + break]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ne sait pas combien de tours il faudra : une boucle "pour
 * toujours" avec une sortie a l'interieur. Pair -> n / 2, impair ->
 * 3n + 1, on compte les etapes jusqu'a 1.
 *
 * -- Essayons a la main --
 *
 *   6 -> 3, 10, 5, 16, 8, 4, 2, 1 -> 8 etapes ; 1 -> 0
 *
 * -- Le plan --
 *
 *   1. steps = 0 ; for (;;) { si n == 1 : break ; n = suivant ; steps++ ; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : firstRepeatedChar(text)   [boucles imbriquees]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour chaque lettre (boucle exterieure), on regarde si elle revient
 * plus loin (boucle interieure). La premiere qui revient gagne. Aucune :
 * '-'.
 *
 * -- Essayons a la main --
 *
 *   "abcbad" -> 'a' (revient en position 4) ; "abc" -> '-'
 *
 * -- Le plan --
 *
 *   1. for i : for j de i + 1 a la fin : si egaux -> rendre ce caractere.
 *   2. Rendre '-'.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : triangle(rows)   [la boucle interieure depend de l'exterieure]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   3 -> "*\n**\n***\n" ; 0 -> ""
 *
 * -- Le plan --
 *
 *   1. Pour chaque ligne r de 1 a rows : r etoiles, puis "\n".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - text.charAt(i) != text.charAt(j) ; Math.abs(n)
 *   - n % 2 == 0 ? n / 2 : 3 * n + 1
 */
public class Exercise12_LoopPatterns {

    public static boolean isPalindrome(String text) {
        throw new UnsupportedOperationException("TODO 1 : implementer isPalindrome()");
    }

    public static int digitCount(int n) {
        throw new UnsupportedOperationException("TODO 2 : implementer digitCount()");
    }

    public static int collatzSteps(int n) {
        throw new UnsupportedOperationException("TODO 3 : implementer collatzSteps()");
    }

    public static char firstRepeatedChar(String text) {
        throw new UnsupportedOperationException("TODO 4 : implementer firstRepeatedChar()");
    }

    public static String triangle(int rows) {
        throw new UnsupportedOperationException("TODO 5 : implementer triangle()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 isPalindrome : kayak, \"\" et a oui ; java non",
                isPalindrome("kayak") && isPalindrome("") && isPalindrome("a") && !isPalindrome("java"));
        ExerciseChecker.check("2 digitCount : 12345 -> 5, 0 -> 1, -907 -> 3",
                digitCount(12345) == 5 && digitCount(0) == 1 && digitCount(-907) == 3);
        ExerciseChecker.check("3 collatzSteps(6) == 8, (1) == 0", collatzSteps(6) == 8 && collatzSteps(1) == 0);
        ExerciseChecker.check("4 firstRepeatedChar : abcbad -> a, abc -> -",
                firstRepeatedChar("abcbad") == 'a' && firstRepeatedChar("abc") == '-');
        ExerciseChecker.check("5 triangle(3) et triangle(0)", triangle(3).equals("*\n**\n***\n") && triangle(0).isEmpty());

        ExerciseChecker.summary();
    }
}
