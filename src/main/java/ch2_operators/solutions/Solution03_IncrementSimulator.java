package ch2_operators.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise03_IncrementSimulator.
 */
public class Solution03_IncrementSimulator {

    public static int postIncrement(int[] x) {
        // APRES : on montre l'ancienne valeur, mais la boite a DEJA change quand on rend.
        int old = x[0];
        x[0] = x[0] + 1;
        return old;
    }

    public static int preIncrement(int[] x) {
        // AVANT : on change d'abord, puis on montre la nouvelle valeur.
        x[0] = x[0] + 1;
        return x[0];
    }

    public static int postDecrement(int[] x) {
        // Meme logique que postIncrement, avec -1.
        int old = x[0];
        x[0] = x[0] - 1;
        return old;
    }

    public static int preDecrement(int[] x) {
        // Meme logique que preIncrement, avec -1.
        x[0] = x[0] - 1;
        return x[0];
    }

    public static int[] simulatePostPlusPre(int start) {
        // Operandes evalues de gauche a droite : le ++ de gauche a DEJA agi quand on lit la droite.
        int[] x = {start};
        int left = postIncrement(x);
        int right = preIncrement(x);
        return new int[]{left + right, x[0]};
    }

    public static int[] simulatePreTimesPostMinusX(int start) {
        // La precedence dit QUOI multiplier ; l'ordre d'evaluation reste gauche -> droite.
        int[] x = {start};
        int a = preIncrement(x);
        int b = postDecrement(x);
        int c = x[0];
        return new int[]{a * b - c, x[0]};
    }

    public static int simulateCompoundSelf(int start) {
        // x += e == x = x + e : le x de gauche est lu AVANT e, puis l'affectation ecrase le ++.
        int[] x = {start};
        int leftValue = x[0];
        int e = postIncrement(x);
        x[0] = leftValue + e;
        return x[0];
    }

    public static int simulateSelfAssign(int start) {
        // x++ rend l'ancienne valeur ; l'affectation remet x a cette ancienne valeur.
        int[] x = {start};
        int e = postIncrement(x);
        x[0] = e;
        return x[0];
    }
}
