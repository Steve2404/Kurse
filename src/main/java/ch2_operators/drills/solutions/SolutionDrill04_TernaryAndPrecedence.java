package ch2_operators.drills.solutions;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.drills.exercises.Drill04_TernaryAndPrecedence.
 */
public class SolutionDrill04_TernaryAndPrecedence {

    public static int max(int a, int b) {
        // Un if/else qui rend une valeur.
        return a > b ? a : b;
    }

    public static int abs(int x) {
        // -x pour les negatifs (attention : -Integer.MIN_VALUE deborde et reste negatif).
        return x < 0 ? -x : x;
    }

    public static int sign(int x) {
        // Ternaires enchaines : ?: se lit de droite a gauche, chaque "sinon" est un nouveau ternaire.
        return x < 0 ? -1 : x == 0 ? 0 : 1;
    }

    public static Object mixedTernary(boolean flag) {
        // Une branche int, une double : tout le ternaire est double, meme quand 1 est choisi.
        return flag ? 1 : 2.0;
    }

    public static char charTernary(boolean flag) {
        // 98 est une constante qui tient dans un char : le ternaire est de type char -> 'b'.
        return flag ? 'a' : 98;
    }

    public static double averageOfTwo(int a, int b) {
        // Sans parentheses, a + b / 2.0 ne diviserait que b.
        return (a + b) / 2.0;
    }

    public static boolean isInvalid(boolean a, boolean b, boolean c) {
        // && passe avant || : c'est a || (b && c).
        return a || b && c;
    }

    public static int noParentheses() {
        // * et / d'abord : 2 + 12 - 3 == 11.
        return 2 + 3 * 4 - 6 / 2;
    }

    public static int leftToRight() {
        // Meme niveau : de gauche a droite, (10 - 2) - 3 == 5 (et pas 10 - (2 - 3) == 11).
        return 10 - 2 - 3;
    }

    public static int withParentheses() {
        // Les parentheses passent avant tout : 8 * 4.
        return (10 - 2) * (3 + 1);
    }

    public static int chainedAssignment() {
        // = se lit de droite a gauche : c = 7, puis b = 7, puis a = 7.
        int a;
        int b;
        int c;
        a = b = c = 7;
        return a + b + c;
    }

    public static int compoundWithProduct() {
        // x += x * 2  ==  x = x + (x * 2) : toute la partie droite est calculee d'abord.
        int x = 5;
        x += x * 2;
        return x;
    }
}
