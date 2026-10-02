package ch8_lambdas.projects.p05_calculator;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 * Les jetons sont separes par des espaces. Operateurs : + - * / ^ (^ associatif a droite).
 * Fonctions : sqrt abs neg sq (un argument) ; max min hyp (deux arguments separes par une virgule).
 * Variables : x, y, z (valeurs ci-dessous).
 */
public final class Data {

    public static final String[] EXPRESSIONS = {
            "3 + 4 * 2", "( 1 + 2 ) * ( 3 + 4 )", "2 ^ 3 ^ 2", "100 / 10 / 5", "sqrt ( 16 ) + abs ( -3 )",
            "max ( 3 , 7 ) * 2", "hyp ( x , y ) + z", "sq ( x - y ) / neg ( z )", "min ( 2 ^ 10 , 1000 ) - 1"};

    public static final String[] NAMES = {"x", "y", "z"};
    public static final double[] VALUES = {3, 4, 2};

    private Data() {
    }
}
