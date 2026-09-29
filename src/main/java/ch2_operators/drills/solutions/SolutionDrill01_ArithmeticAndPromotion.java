package ch2_operators.drills.solutions;

import ch2_operators.drills.Grades;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.drills.exercises.Drill01_ArithmeticAndPromotion.
 */
public class SolutionDrill01_ArithmeticAndPromotion {

    public static int intDivision(int a, int b) {
        // Division entiere : coupe vers zero ; par 0, ArithmeticException (seulement pour les entiers).
        return a / b;
    }

    public static double realDivision(int a, int b) {
        // Le cast porte sur b AVANT la division : la division se fait en double.
        return a / (double) b;
    }

    public static int remainder(int a, int b) {
        // Le reste prend le signe du dividende (a).
        return a % b;
    }

    public static boolean isEven(int n) {
        // == 0 marche aussi pour les negatifs (-3 % 2 == -1, pas 1).
        return n % 2 == 0;
    }

    public static int sumOfBytes(byte a, byte b) {
        // byte + byte est promu en int : 200 tient sans debordement.
        return a + b;
    }

    public static int codePlusOne(char c) {
        // char + int -> int : on obtient le CODE suivant, pas une lettre.
        return c + 1;
    }

    public static char nextChar(char c) {
        // Pour revenir a une lettre, il faut caster le resultat int en char.
        return (char) (c + 1);
    }

    public static long bigProduct(int a, int b) {
        // Le cast s'applique a a : la multiplication se fait en long.
        return (long) a * b;
    }

    public static int overflow() {
        // Aucune erreur : le calcul fait le tour et donne Integer.MIN_VALUE.
        return Integer.MAX_VALUE + 1;
    }

    public static double classAverage() {
        // Somme en int, puis division en double grace au cast du diviseur.
        int sum = 0;
        for (int score : Grades.SCORES) {
            sum += score;
        }
        return sum / (double) Grades.SCORES.length;
    }

    public static double infinity() {
        // En virgule flottante, diviser par 0 ne plante pas : on obtient Infinity.
        return 1.0 / 0;
    }

    public static boolean isNotANumber(double x) {
        // NaN est la seule valeur differente d'elle-meme (Double.isNaN fait la meme chose).
        return x != x;
    }

    public static int postPlusPre(int x) {
        // Gauche a droite : 5 (x devient 6), puis 7 : 12.
        return x++ + ++x;
    }
}
