package ch2_operators.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise05_CompoundAssignmentImplicitCast.
 */
public class Solution05_CompoundAssignmentImplicitCast {

    public static byte incrementByteViaCompound(byte b, int amount) {
        b += amount;
        return b;
    }

    public static int scaleIntViaCompound(int value, double factor) {
        value *= factor;
        return value;
    }
}
