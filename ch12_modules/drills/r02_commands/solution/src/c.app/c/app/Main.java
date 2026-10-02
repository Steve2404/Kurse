package c.app;

import c.lib.Calc;

/** SOLUTION - le programme modulaire du drill 2. */
public class Main {
    public static void main(String[] args) {
        System.out.println("somme " + Calc.add(2, 3) + " dans " + Main.class.getModule().getName());
    }
}
