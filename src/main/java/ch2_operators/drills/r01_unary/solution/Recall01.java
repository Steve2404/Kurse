package ch2_operators.drills.r01_unary.solution;

/**
 * SOLUTION du drill de rappel 1 - operateurs unaires et incrementations.
 */
public class Recall01 {

    public static void main(String[] args) {
        int a = 5;
        System.out.println("D01 : " + a++ + " " + a + " " + ++a + " " + a);
        int b = 10;
        System.out.println("D02 : " + b-- + " " + --b + " " + b);
        int c = 3;
        int d = c++ * 2 + c;     // 3 * 2 + 4
        System.out.println("D03 : " + d + " " + c);
        int e = 1;
        e = e++;                 // la valeur rendue (1) ecrase l'increment
        System.out.println("D04 : " + e);
        int f = 4;
        System.out.println("D05 : " + -f + " " + -(-f) + " " + ~f + " " + ~-1);
        boolean g = true;
        System.out.println("D06 : " + !g + " " + !!g + " " + !(f > 3));
        char h = 'a';
        h++;
        System.out.println("D07 : " + h + " " + (int) h + " " + ++h);
        long i = 2;
        i++;
        double j = 1.5;
        j--;
        System.out.println("D08 : " + i + " " + j);
    }
}
