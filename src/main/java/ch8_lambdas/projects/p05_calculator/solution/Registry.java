package ch8_lambdas.projects.p05_calculator.solution;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * SOLUTION - la table des fonctions : des tableaux de DoubleUnaryOperator et DoubleBinaryOperator
 * (types non generiques : les tableaux sont permis), remplis par des references de methode.
 */
public class Registry {

    private final String[] unaryNames = new String[8];
    private final DoubleUnaryOperator[] unary = new DoubleUnaryOperator[8];
    private int unaryCount;
    private final String[] binaryNames = new String[16];
    private final DoubleBinaryOperator[] binary = new DoubleBinaryOperator[16];
    private int binaryCount;

    public Registry() {
        unary("sqrt", Math::sqrt);                 // reference vers une methode STATIC
        unary("abs", Math::abs);
        unary("neg", x -> -x);                     // pas de methode existante : une lambda
        unary("sq", x -> x * x);
        binary("+", Double::sum);                  // static aussi : Double.sum(double, double)
        binary("-", (a, b) -> a - b);
        binary("*", (a, b) -> a * b);
        binary("/", (a, b) -> a / b);
        binary("^", Math::pow);
        binary("max", Math::max);                  // la surcharge (double, double) est choisie d'apres le type cible
        binary("min", Math::min);
        binary("hyp", Math::hypot);
    }

    public void unary(String name, DoubleUnaryOperator f) {
        unaryNames[unaryCount] = name;
        unary[unaryCount++] = f;
    }

    public void binary(String name, DoubleBinaryOperator f) {
        binaryNames[binaryCount] = name;
        binary[binaryCount++] = f;
    }

    public boolean isBinary(String name) {
        for (int i = 0; i < binaryCount; i++) {
            if (binaryNames[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    public DoubleUnaryOperator unary(String name) {
        for (int i = 0; i < unaryCount; i++) {
            if (unaryNames[i].equals(name)) {
                return unary[i];
            }
        }
        return null;
    }

    public DoubleBinaryOperator binary(String name) {
        for (int i = 0; i < binaryCount; i++) {
            if (binaryNames[i].equals(name)) {
                return binary[i];
            }
        }
        return null;
    }
}
