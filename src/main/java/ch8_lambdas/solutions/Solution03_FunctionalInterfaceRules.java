package ch8_lambdas.solutions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise03_FunctionalInterfaceRules.
 */
public class Solution03_FunctionalInterfaceRules {

    public static int abstractCount(List<String> own, List<String> inherited, List<String> filledByDefault) {
        // Un Set fusionne les signatures identiques ; les methodes publiques d'Object ne comptent pas.
        Set<String> methods = new HashSet<>(own);
        methods.addAll(inherited);
        methods.removeAll(filledByDefault);
        methods.removeAll(List.of("equals(Object)", "hashCode()", "toString()"));
        return methods.size();
    }

    public static String annotationError(String kind, int abstractCount) {
        // L'annotation n'accepte qu'une interface avec exactement une methode abstraite.
        if (!kind.equals("interface") || abstractCount != 1) {
            return "Unexpected @FunctionalInterface annotation";
        }
        return "OK";
    }

    public static String lambdaTargetError(String typeName, String kind, int abstractCount) {
        // Une lambda ne peut viser qu'une interface fonctionnelle (jamais une classe, meme abstraite).
        if (kind.equals("interface") && abstractCount == 1) {
            return "OK";
        }
        return "incompatible types: " + typeName + " is not a functional interface";
    }
}
