package ch2_operators.drills.exercises;

import ch2_operators.ExerciseChecker;

/**
 * DRILL 04 - Ternaire et precedence : ?:, son type, l'ordre de calcul, les parentheses, l'affectation
 * ===================================================================================================
 *
 * Mode d'emploi : voir Drill01_ArithmeticAndPromotion.
 *
 * Pour les TODO 8 a 10, ecris l'expression EXACTE demandee (sans
 * calculer toi-meme) : le test verifie que Java donne le resultat que
 * tu as prevu dans ta tete.
 *
 *
 * -- Les TODO (regle visee entre crochets) --
 *
 * TODO 1  : max(a, b)               [ternaire]
 * TODO 2  : abs(x)                  [ternaire] -5 -> 5.
 * TODO 3  : sign(x)                 [ternaires enchaines] -1, 0 ou 1.
 * TODO 4  : mixedTernary(flag)      [type d'un ternaire int/double] flag ? 1 : 2.0 -> rendu en Object : un Double 1.0.
 * TODO 5  : charTernary(flag)       [constante int qui tient dans un char] flag ? 'a' : 98 avec flag faux -> 'b'.
 * TODO 6  : averageOfTwo(a, b)      [parentheses + double] 3 et 4 -> 3.5.
 * TODO 7  : isInvalid(a, b, c)      [&& avant ||] a || b && c.
 * TODO 8  : noParentheses()         [ecrire : 2 + 3 * 4 - 6 / 2] -> 11.
 * TODO 9  : leftToRight()           [ecrire : 10 - 2 - 3] -> 5.
 * TODO 10 : withParentheses()       [ecrire : (10 - 2) * (3 + 1)] -> 32.
 * TODO 11 : chainedAssignment()     [a = b = c = 7 : l'affectation se lit de droite a gauche] a + b + c -> 21.
 * TODO 12 : compoundWithProduct()   [int x = 5; x += x * 2;] -> 15.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Precedence (haut -> bas) : x++ x-- | ++x --x + - ! ~ (unaires) | * / % | + - | << >> >>>
 *     | < > <= >= instanceof | == != | & | ^ | | | && | || | ?: | = += -= ...
 *   Meme niveau : de gauche a droite ; SAUF l'affectation et ?: : de droite a gauche
 *   Type d'un ternaire : decide a la compilation avec LES DEUX branches (int et double -> double)
 * ---------------------------------------------------------------------
 */
public class Drill04_TernaryAndPrecedence {

    public static int max(int a, int b) {
        throw new UnsupportedOperationException("TODO 1 : implementer max()");
    }

    public static int abs(int x) {
        throw new UnsupportedOperationException("TODO 2 : implementer abs()");
    }

    public static int sign(int x) {
        throw new UnsupportedOperationException("TODO 3 : implementer sign()");
    }

    public static Object mixedTernary(boolean flag) {
        throw new UnsupportedOperationException("TODO 4 : implementer mixedTernary()");
    }

    public static char charTernary(boolean flag) {
        throw new UnsupportedOperationException("TODO 5 : implementer charTernary()");
    }

    public static double averageOfTwo(int a, int b) {
        throw new UnsupportedOperationException("TODO 6 : implementer averageOfTwo()");
    }

    public static boolean isInvalid(boolean a, boolean b, boolean c) {
        throw new UnsupportedOperationException("TODO 7 : implementer isInvalid()");
    }

    public static int noParentheses() {
        throw new UnsupportedOperationException("TODO 8 : implementer noParentheses()");
    }

    public static int leftToRight() {
        throw new UnsupportedOperationException("TODO 9 : implementer leftToRight()");
    }

    public static int withParentheses() {
        throw new UnsupportedOperationException("TODO 10 : implementer withParentheses()");
    }

    public static int chainedAssignment() {
        throw new UnsupportedOperationException("TODO 11 : implementer chainedAssignment()");
    }

    public static int compoundWithProduct() {
        throw new UnsupportedOperationException("TODO 12 : implementer compoundWithProduct()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  max(3, 7) == 7", max(3, 7) == 7);
        ExerciseChecker.check("2  abs(-5) == 5, abs(4) == 4", abs(-5) == 5 && abs(4) == 4);
        ExerciseChecker.check("3  sign : -8 -> -1, 0 -> 0, 3 -> 1", sign(-8) == -1 && sign(0) == 0 && sign(3) == 1);
        ExerciseChecker.check("4  mixedTernary(true) est un Double 1.0", mixedTernary(true) instanceof Double && mixedTernary(true).equals(1.0));
        ExerciseChecker.check("5  charTernary(false) == 'b'", charTernary(false) == 'b');
        ExerciseChecker.check("6  averageOfTwo(3, 4) == 3.5", averageOfTwo(3, 4) == 3.5);
        ExerciseChecker.check("7  isInvalid(true, false, false) == true (&& d'abord)", isInvalid(true, false, false));
        ExerciseChecker.check("8  noParentheses() == 11", noParentheses() == 11);
        ExerciseChecker.check("9  leftToRight() == 5", leftToRight() == 5);
        ExerciseChecker.check("10 withParentheses() == 32", withParentheses() == 32);
        ExerciseChecker.check("11 chainedAssignment() == 21", chainedAssignment() == 21);
        ExerciseChecker.check("12 compoundWithProduct() == 15", compoundWithProduct() == 15);

        ExerciseChecker.summary();
    }
}
