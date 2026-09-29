package ch4_coreapis.drills.solutions;

import ch4_coreapis.drills.Journal;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill04_MathApi.
 */
public class SolutionDrill04_MathApi {

    public static long roundUp() {
        // round(double) rend un long : un int ne suffirait pas sans cast.
        return Math.round(2.5);
    }

    public static long roundNegative() {
        // floor(-2.5 + 0.5) = floor(-2.0) = -2 : round ne s'eloigne pas de zero.
        return Math.round(-2.5);
    }

    public static int roundFloat() {
        // round(float) rend un int.
        return Math.round(7.6f);
    }

    public static double ceilNegative() {
        // ceil monte vers +infini : -1.5 -> -1.0.
        return Math.ceil(-1.5);
    }

    public static double floorNegative() {
        // floor descend vers -infini : -1.5 -> -2.0 (un cast (int) donnerait -1).
        return Math.floor(-1.5);
    }

    public static double power() {
        // pow rend toujours un double, meme avec des entiers.
        return Math.pow(2, 10);
    }

    public static double root() {
        // sqrt rend un double ; sqrt d'un negatif donne NaN (pas d'exception).
        return Math.sqrt(81);
    }

    public static double mixedMax() {
        // Le int 3 est promu en double : resultat 3.0.
        return Math.max(3, 2.5);
    }

    public static int lowestScore() {
        // Math.min n'a que 2 arguments : on le repete dans une boucle.
        int min = Journal.SCORES[0];
        for (int s : Journal.SCORES) {
            min = Math.min(min, s);
        }
        return min;
    }

    public static int absMinValue() {
        // +2147483648 n'existe pas en int : abs deborde et rend la meme valeur negative.
        return Math.abs(Integer.MIN_VALUE);
    }

    public static int rollDie() {
        // [0.0, 1.0) * 6 -> [0.0, 6.0) ; cast -> 0 a 5 ; + 1 -> 1 a 6.
        return (int) (Math.random() * 6) + 1;
    }

    public static long roundedAverage() {
        // Moyenne en double d'abord (226 / 6 en int donnerait 37), puis round.
        int sum = 0;
        for (int s : Journal.SCORES) {
            sum += s;
        }
        return Math.round((double) sum / Journal.SCORES.length);
    }

    public static int checkedSum(int a, int b) {
        // addExact detecte le debordement au lieu de rendre un nombre faux.
        try {
            return Math.addExact(a, b);
        } catch (ArithmeticException e) {
            return -1;
        }
    }
}
