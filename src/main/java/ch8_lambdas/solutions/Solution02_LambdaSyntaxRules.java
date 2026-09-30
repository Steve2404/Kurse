package ch8_lambdas.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise02_LambdaSyntaxRules.
 */
public class Solution02_LambdaSyntaxRules {

    public static String syntaxError(String lambda) {
        // Gauche (parametres) puis droite (corps), dans l'ordre ou javac les lit.
        int arrow = lambda.indexOf("->");
        String left = lambda.substring(0, arrow).strip();
        String body = lambda.substring(arrow + 2).strip();
        if (left.isEmpty()) {
            return "illegal start of expression";
        }
        if (!left.startsWith("(")) {
            if (left.contains(" ") || left.contains(",")) {
                return "';' expected";
            }
        } else {
            String inside = left.substring(1, left.length() - 1).strip();
            if (!inside.isEmpty()) {
                String firstKind = null;
                for (String param : inside.split(",")) {
                    String kind = kindOf(param);
                    if (firstKind == null) {
                        firstKind = kind;
                    } else if (!firstKind.equals(kind)) {
                        return "invalid lambda parameter declaration";
                    }
                }
            }
        }
        if (body.startsWith("return")) {
            return "illegal start of expression";
        }
        if (body.startsWith("{") && body.contains("return") && !body.contains(";")) {
            return "';' expected";
        }
        return "OK";
    }

    private static String kindOf(String param) {
        // final est permis devant un type ou var : il ne change pas la categorie.
        String p = param.strip();
        if (p.startsWith("final ")) {
            p = p.substring(6).strip();
        }
        if (p.startsWith("var ")) {
            return "var";
        }
        return p.split("\\s+").length == 2 ? "typed" : "untyped";
    }

    public static String returnError(boolean targetReturnsValue, boolean bodyIsBlock, boolean blockReturnsValue) {
        // Un bloc doit s'accorder avec la methode abstraite : valeur si elle en rend une, rien si void.
        if (bodyIsBlock && targetReturnsValue != blockReturnsValue) {
            return "incompatible types: bad return type in lambda expression";
        }
        return "OK";
    }

    public static String targetError(String target, int lambdaParams, int expectedParams) {
        // Une lambda n'a pas de type propre : il lui faut une cible fonctionnelle de la bonne arite.
        if (target.equals("var")) {
            return "'var' is not allowed here";
        }
        if (target.equals("Object")) {
            return "incompatible types: Object is not a functional interface";
        }
        if (lambdaParams != expectedParams) {
            return "incompatible types: incompatible parameter types in lambda expression";
        }
        return "OK";
    }
}
