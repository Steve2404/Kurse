package ch6_classdesign.projects.p04_immutable.solution;

import ch6_classdesign.projects.p04_immutable.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 4 - les objets immuables.
 */
public class ImmutableLab {

    public static void main(String[] args) {
        Money price = Money.of(1999, "EUR");
        Money total = price.plus(Money.of(501, "EUR"));
        System.out.println("money : " + price + " + 5.01 EUR = " + total + " ; prix inchange " + price + " ; 15 % = " + total.times(15) + " ; EUR + USD = "
                + price.plus(Money.of(100, "USD")));
        System.out.println("partage 3 : " + Arrays.toString(Money.of(Data.BILL, "EUR").allocate(Data.SHARES)) + " ; 50/30/20 de 0.07 : "
                + Arrays.toString(Money.of(7, "EUR").allocate(Data.WEIGHTS)));
        System.out.println("egalite : " + Money.of(500, "EUR").equals(Money.of(500, "EUR")) + " " + (Money.of(500, "EUR") == Money.of(500, "EUR")) + " "
                + (Money.of(500, "EUR").hashCode() == Money.of(500, "EUR").hashCode()) + " " + Money.of(500, "EUR").equals(Money.of(500, "USD")));

        Fraction h = Fraction.ZERO;
        for (int i = 1; i <= 10; i++) {
            h = h.plus(Fraction.of(1, i));
        }
        System.out.println("fractions : " + Fraction.of(6, -8) + " " + Fraction.of(1, 3).plus(Fraction.of(1, 6)) + " " + Fraction.of(2, 3).divide(Fraction.of(4, 9))
                + " H(10)=" + h + " " + Fraction.of(2, 4).equals(Fraction.of(1, 2)));

        long[][] raw = {{1, 2}, {3, 4}};
        Matrix m = Matrix.of(raw);
        raw[0][0] = 99;                    // modifie le tableau de l'appelant...
        long[][] out = m.toArray();
        out[1][1] = 99;                    // ... et une copie rendue
        System.out.println("copies defensives : " + m + " intacte ; transposee " + m.transpose() + " ; carre " + m.times(m) + " ; egal a lui-meme reconstruit "
                + m.equals(Matrix.of(new long[][] {{1, 2}, {3, 4}})));
        Matrix fib = Matrix.of(new long[][] {{1, 1}, {1, 0}});
        System.out.println("fibonacci par puissance : F(10)=" + fib.power(10).get(0, 1) + " F(50)=" + fib.power(50).get(0, 1) + " F(90)=" + fib.power(90).get(0, 1));
        System.out.println("determinants : " + Matrix.of(Data.M3).determinant() + " " + Matrix.of(Data.M4).determinant() + " " + Matrix.of(Data.SINGULAR).determinant()
                + " " + Matrix.of(Data.HALVES).determinant() + " ; det(A^3) = det(A)^3 : " + Matrix.of(Data.M3).power(3).determinant());
    }
}
