package ch5_methods.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise01_MethodDeclarationRules.
 */
public class Solution01_MethodDeclarationRules {

    public static final String[] KEYWORDS = {"class", "void", "int", "static", "public", "private", "protected",
            "final", "return", "new", "if", "for", "while", "switch", "var"};

    public static String[] paramTypes(String header) {
        // Le type est le 1er mot de chaque parametre ; un varargs est un tableau.
        String[] params = params(header);
        String[] types = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].split("\\s+")[0].replace("...", "[]");
        }
        return types;
    }

    private static String[] params(String header) {
        // Petite boite : les parametres bruts entre les parentheses (tableau vide si aucun).
        String inside = header.substring(header.indexOf('(') + 1, header.indexOf(')')).strip();
        if (inside.isEmpty()) {
            return new String[0];
        }
        String[] parts = inside.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].strip();
        }
        return parts;
    }

    public static String check(String header) {
        // Les regles dans l'ordre : on rend le PREMIER probleme, comme javac rend sa premiere erreur.
        String[] words = header.substring(0, header.indexOf('(')).strip().split("\\s+");
        int nameIndex = words.length - 1;
        boolean typeSeen = false;
        for (int i = 0; i < nameIndex; i++) {
            if (!isModifier(words[i])) {
                typeSeen = true;
            } else if (typeSeen) {
                return "MODIFIER_AFTER_TYPE";
            }
        }
        if (words.length < 2 || isModifier(words[nameIndex - 1])) {
            return "RETURN_TYPE_REQUIRED";
        }
        int accessCount = 0;
        for (int i = 0; i < nameIndex; i++) {
            for (int j = i + 1; j < nameIndex; j++) {
                if (isModifier(words[i]) && words[i].equals(words[j])) {
                    return "REPEATED_MODIFIER";
                }
            }
            if (isAccess(words[i])) {
                accessCount++;
            }
        }
        if (accessCount > 1) {
            return "ILLEGAL_COMBINATION";
        }
        String[] params = params(header);
        for (int i = 0; i < params.length - 1; i++) {
            if (params[i].contains("...")) {
                return "VARARGS_NOT_LAST";
            }
        }
        for (int i = 0; i < params.length; i++) {
            for (int j = i + 1; j < params.length; j++) {
                if (params[i].split("\\s+")[1].equals(params[j].split("\\s+")[1])) {
                    return "DUPLICATE_PARAMETER";
                }
            }
        }
        return "OK";
    }

    private static boolean isModifier(String word) {
        // Les modificateurs connus de cet exercice.
        return isAccess(word) || word.equals("static") || word.equals("final");
    }

    private static boolean isAccess(String word) {
        // Un seul modificateur d'acces par methode.
        return word.equals("public") || word.equals("protected") || word.equals("private");
    }

    public static boolean sameSignature(String a, String b) {
        // Signature = nom + types des parametres ; ni le retour ni les noms des parametres.
        return nameOf(a).equals(nameOf(b)) && Arrays.equals(paramTypes(a), paramTypes(b));
    }

    private static String nameOf(String header) {
        // Le nom est le dernier mot avant la parenthese.
        String[] words = header.substring(0, header.indexOf('(')).strip().split("\\s+");
        return words[words.length - 1];
    }

    public static boolean isValidName(String name) {
        // Les regles d'identifiant ; "_" seul est reserve depuis Java 9.
        if (name.isEmpty() || name.equals("_") || Arrays.asList(KEYWORDS).contains(name)) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
