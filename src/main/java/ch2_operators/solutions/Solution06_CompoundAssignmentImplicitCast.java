package ch2_operators.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise06_CompoundAssignmentImplicitCast.
 */
public class Solution06_CompoundAssignmentImplicitCast {

    public static byte incrementByteViaCompound(byte b, int amount) {
        // += cache un cast : b += amount equivaut a b = (byte) (b + amount).
        // b = b + amount ne compilerait pas (int vers byte).
        b += amount;
        return b;
    }

    public static int scaleIntViaCompound(int value, double factor) {
        // *= avec un double : calcul en double, puis cast cache vers int (partie decimale perdue).
        value *= factor;
        return value;
    }
}
