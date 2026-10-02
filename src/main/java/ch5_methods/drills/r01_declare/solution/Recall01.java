package ch5_methods.drills.r01_declare.solution;

import java.util.Arrays;

/**
 * SOLUTION du drill de rappel 1 - declarer des methodes.
 */
public class Recall01 {

    // L'ordre des modificateurs est libre ; le type de retour vient juste avant le nom.
    final static public String NAME = "r01";
    public static final int LIMIT = 3;
    static int $count;

    // Un nom peut commencer par _ ou $ (mais _ seul est interdit).
    static int _twice(int x) {
        return 2 * x;
    }

    // Le retour est converti comme une affectation : int -> long est implicite.
    static long widen(int x) {
        return x;
    }

    // Une CONSTANTE int qui tient dans un char est acceptee sans cast.
    static char letter() {
        return 65;
    }

    static int truncate(double d) {
        return (int) d;
    }

    // Une methode void peut sortir tot avec return; (sans valeur).
    static void log(String s) {
        if (s.isEmpty()) {
            return;
        }
        $count++;
    }

    static int[] pair(int a, int b) {
        return new int[] {a, b};
    }

    // Tous les chemins doivent finir par un return (ou une exception) : sinon "missing return statement".
    static String sign(int n) {
        if (n > 0) {
            return "+";
        } else if (n < 0) {
            return "-";
        }
        return "0";
    }

    static int value2() {
        var v = 40;    // var : seulement pour une variable LOCALE initialisee
        v += 2;
        return v;
    }

    // Une variable locale final peut etre affectee plus tard, mais une seule fois sur chaque chemin.
    static int start(int n) {
        final int s;
        if (n > 0) {
            s = n;
        } else {
            s = 0;
        }
        return s;
    }

    static int firstNegative(int[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] < 0) {
                return i;   // return sort de la methode, donc aussi de la boucle
            }
        }
        return -1;
    }

    static int area(int side) {
        return side * side;
    }

    static int area(int width, int height) {
        return width * height;
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + _twice(21) + " " + (widen(Integer.MAX_VALUE) + 1) + " " + letter() + " " + truncate(-3.9));
        log("");
        log("a");
        log("b");
        System.out.println("D02 : " + $count);
        System.out.println("D03 : " + Arrays.toString(pair(1, 2)) + " " + pair(3, 4)[1]);
        System.out.println("D04 : " + sign(5) + " " + sign(-2) + " " + sign(0));
        System.out.println("D05 : " + NAME + " " + LIMIT + " " + value2());
        System.out.println("D06 : " + start(5) + " " + start(-1));
        System.out.println("D07 : " + firstNegative(new int[] {4, 0, -2, -5}) + " " + firstNegative(new int[0]));
        System.out.println("D08 : " + area(4) + " " + area(2, 5));
    }
}
