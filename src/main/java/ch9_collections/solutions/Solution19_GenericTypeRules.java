package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Corrige de l'exercice 19.
 */
public class Solution19_GenericTypeRules {

    private static final Set<String> PRIMITIVES = Set.of("byte", "short", "int", "long", "float", "double", "char", "boolean");

    public static List<String> typeArgs(String type) {
        // On ne coupe que sur les virgules de profondeur 0 : celle de "Map<A, B>" a l'interieur d'un argument ne compte pas.
        List<String> result = new ArrayList<>();
        int open = type.indexOf('<');
        if (open < 0) {
            return result;
        }
        String inside = type.substring(open + 1, type.lastIndexOf('>'));
        int depth = 0;
        int start = 0;
        for (int i = 0; i < inside.length(); i++) {
            char c = inside.charAt(i);
            if (c == '<') {
                depth++;
            } else if (c == '>') {
                depth--;
            } else if (c == ',' && depth == 0) {
                addPiece(result, inside.substring(start, i));
                start = i + 1;
            }
        }
        addPiece(result, inside.substring(start));
        return result;
    }

    public static String erasure(String type, Map<String, String> bounds) {
        // Une variable de type s'efface en sa 1re borne, elle-meme effacee (Comparable<E> -> Comparable) ; les [] survivent.
        String dims = "";
        while (type.endsWith("[]")) {
            dims += "[]";
            type = type.substring(0, type.length() - 2);
        }
        String base = baseName(type);
        if (bounds.containsKey(base)) {
            base = erasure(bounds.get(base), bounds);
        }
        return base + dims;
    }

    public static String nameClash(String method, List<String> params1, List<String> params2, Map<String, String> bounds) {
        // Meme effacement = meme signature dans le .class : impossible d'avoir les deux, meme si le source les distingue.
        if (params1.size() != params2.size()) {
            return "OK";
        }
        for (int i = 0; i < params1.size(); i++) {
            if (!erasure(params1.get(i), bounds).equals(erasure(params2.get(i), bounds))) {
                return "OK";
            }
        }
        return "name clash: " + method + "(" + String.join(",", params2) + ") and "
                + method + "(" + String.join(",", params1) + ") have the same erasure";
    }

    public static String creationError(String expr, Set<String> typeVars) {
        // Un tableau doit connaitre son type exact a l'execution : seuls les "?" tout seuls (reifiables) sont permis.
        // (un '[' a l'interieur des <...>, comme dans ArrayList<int[]>, ne fait pas un tableau.)
        if (expr.endsWith("]")) {
            int bracket = expr.indexOf('[', Math.max(0, expr.lastIndexOf('>')));
            String element = expr.substring(0, bracket);
            if (typeVars.contains(baseName(element))) {
                return "generic array creation";
            }
            for (String arg : typeArgs(element)) {
                if (!arg.equals("?")) {
                    return "generic array creation";
                }
            }
            return "OK";
        }
        // new a besoin d'un type concret : ni T (efface), ni wildcard, ni primitif au 1er niveau.
        String type = expr.substring(0, expr.indexOf('('));
        if (typeVars.contains(baseName(type))) {
            return "unexpected type";
        }
        for (String arg : typeArgs(type)) {
            if (arg.startsWith("?") || PRIMITIVES.contains(arg)) {
                return "unexpected type";
            }
        }
        return "OK";
    }

    public static String staticContextError(String typeVar, String member, boolean ownTypeParam) {
        // Le T de la classe vit dans chaque objet ; un champ d'interface est implicitement static, donc piege aussi.
        boolean staticContext = member.startsWith("static") || member.equals("interface field");
        if (staticContext && !ownTypeParam) {
            return "non-static type variable " + typeVar + " cannot be referenced from a static context";
        }
        return "OK";
    }

    public static String instanceofError(String staticType, String target, Set<String> typeVars) {
        // A l'execution seul le type efface se verifie : permis si les arguments sont "?" ou deja garantis par le type declare.
        if (!typeVars.contains(target)) {
            List<String> targetArgs = typeArgs(target);
            boolean onlyWildcards = true;
            for (String arg : targetArgs) {
                if (!arg.equals("?")) {
                    onlyWildcards = false;
                }
            }
            if (onlyWildcards || targetArgs.equals(typeArgs(staticType))) {
                return "OK";
            }
        }
        return staticType + " cannot be safely cast to " + target;
    }

    public static String boundsError(List<String> bounds, Set<String> classes) {
        // Une seule classe possible dans une borne multiple, et en premier : apres le 1er &, seulement des interfaces.
        for (String bound : bounds.subList(1, bounds.size())) {
            if (classes.contains(baseName(bound))) {
                return "interface expected here";
            }
        }
        return "OK";
    }

    private static String baseName(String type) {
        // Le nom avant les arguments de type : "List<String>" -> "List".
        int open = type.indexOf('<');
        return (open < 0 ? type : type.substring(0, open)).trim();
    }

    private static void addPiece(List<String> result, String piece) {
        // Le diamant "<>" donne un morceau vide : il ne compte pas comme argument.
        if (!piece.isBlank()) {
            result.add(piece.trim());
        }
    }
}
