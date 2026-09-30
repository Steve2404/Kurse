package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

/**
 * EXERCICE 2 - La syntaxe des lambdas, ecrite par toi et comparee a 23 verdicts reels de javac (niveau : difficile)
 * ===============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une lambda a deux moitiees separees par "->" :
 *   - a gauche, les parametres : sans parentheses SEULEMENT s'il y en a
 *     un seul et sans type ; sinon entre parentheses. Dans les
 *     parentheses, TOUS sans type, ou TOUS avec un type, ou TOUS avec var
 *     (jamais de melange) ; "final" est permis devant un type ou var.
 *   - a droite, le corps : une expression SANS return ni point-virgule,
 *     ou un bloc { ... } avec des instructions completes (return ...;).
 *
 * -- Verdicts reels de javac 17 --
 *
 *   s -> s.isEmpty()                 -> compile
 *   (String s) -> s.isEmpty()        -> compile ; (var s) -> ...  -> compile ; (final String s) -> ... -> compile
 *   String s -> s.isEmpty()          -> error: ';' expected
 *   var s -> s.isEmpty()             -> error: ';' expected
 *   a, b -> a + b                    -> error: ';' expected
 *   (String a, b) -> a + b           -> error: invalid lambda parameter declaration
 *   (var a, String b) -> a + b       -> error: invalid lambda parameter declaration
 *   (var a, b) -> a + b              -> error: invalid lambda parameter declaration
 *   (var a, var b) -> a + b          -> compile
 *   s -> { return s.length() }       -> error: ';' expected
 *   s -> return s.length();          -> error: illegal start of expression
 *   -> "x"                           -> error: illegal start of expression ; () -> "x" -> compile
 *   Function : s -> { s.length(); }  -> error: incompatible types: bad return type in lambda expression
 *   Runnable : () -> { return 1; }   -> error: incompatible types: bad return type in lambda expression
 *   Supplier : () -> { }             -> error: incompatible types: bad return type in lambda expression
 *   Predicate<String> p = (a, b) -> true -> error: incompatible types: incompatible parameter types in lambda expression
 *   Object o = () -> {}              -> error: incompatible types: Object is not a functional interface
 *   var f = (String s) -> s.length() -> error: 'var' is not allowed here
 *   Object o = (Runnable) () -> {}   -> compile (le cast donne la cible)
 *
 *
 * ==================================================================
 * TODO 1 : syntaxError(lambda)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On recoit le TEXTE d'une lambda et on rend le message de javac (ou
 * "OK"), en ne regardant que la syntaxe.
 *
 * -- Le plan --
 *
 *   1. left = ce qui precede "->" (strip) ; body = ce qui suit (strip).
 *   2. left vide -> "illegal start of expression".
 *   3. left sans parentheses : s'il contient un espace ou une virgule -> "';' expected".
 *   4. left entre parentheses (et non vide) : pour chaque parametre (split sur ","), enlever un "final "
 *      eventuel, puis classer : "var" s'il commence par "var ", "typed" s'il a 2 mots, "untyped" sinon.
 *      Deux classes differentes -> "invalid lambda parameter declaration".
 *   5. body commence par "return" -> "illegal start of expression".
 *   6. body est un bloc {...} qui contient "return" mais pas de ";" -> "';' expected".
 *   7. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : kindOf(param) pour l'etape 4.
 *
 *
 * ==================================================================
 * TODO 2 : returnError(targetReturnsValue, bodyIsBlock, blockReturnsValue)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un bloc doit s'accorder avec la methode de l'interface : si elle rend
 * une valeur, le bloc doit "return valeur;" ; si elle est void, le bloc
 * ne doit PAS rendre de valeur. (Un corps SANS bloc est accepte dans
 * ces exercices.)
 *
 * -- Le plan --
 *
 *   1. !bodyIsBlock -> "OK".
 *   2. targetReturnsValue != blockReturnsValue -> "incompatible types: bad return type in lambda expression".
 *   3. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : targetError(target, lambdaParams, expectedParams)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. target "var" -> "'var' is not allowed here".
 *   2. target "Object" -> "incompatible types: Object is not a functional interface".
 *   3. lambdaParams != expectedParams -> "incompatible types: incompatible parameter types in lambda expression".
 *   4. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - lambda.indexOf("->") ; left.startsWith("(") ; left.substring(1, left.length() - 1).
 */
public class Exercise02_LambdaSyntaxRules {

    public static String syntaxError(String lambda) {
        throw new UnsupportedOperationException("TODO 1 : implementer syntaxError()");
    }

    public static String returnError(boolean targetReturnsValue, boolean bodyIsBlock, boolean blockReturnsValue) {
        throw new UnsupportedOperationException("TODO 2 : implementer returnError()");
    }

    public static String targetError(String target, int lambdaParams, int expectedParams) {
        throw new UnsupportedOperationException("TODO 3 : implementer targetError()");
    }

    public static void main(String[] args) {
        String[][] javac = {
                {"s -> s.isEmpty()", "OK"},
                {"(String s) -> s.isEmpty()", "OK"},
                {"(var s) -> s.isEmpty()", "OK"},
                {"(final String s) -> s.isEmpty()", "OK"},
                {"(final var s) -> s.isEmpty()", "OK"},
                {"String s -> s.isEmpty()", "';' expected"},
                {"var s -> s.isEmpty()", "';' expected"},
                {"a, b -> a + b", "';' expected"},
                {"(a, b) -> a + b", "OK"},
                {"(String a, b) -> a + b", "invalid lambda parameter declaration"},
                {"(var a, String b) -> a + b", "invalid lambda parameter declaration"},
                {"(var a, b) -> a + b", "invalid lambda parameter declaration"},
                {"(var a, var b) -> a + b", "OK"},
                {"s -> { return s.length() }", "';' expected"},
                {"s -> return s.length();", "illegal start of expression"},
                {"s -> { return s.length(); }", "OK"},
                {"-> \"x\"", "illegal start of expression"},
                {"() -> \"x\"", "OK"}};
        int agree = 0;
        for (String[] v : javac) {
            String mine = syntaxError(v[0]);
            if (mine.equals(v[1])) {
                agree++;
            } else {
                System.out.println("   desaccord : " + v[0] + " -> attendu " + v[1] + ", obtenu " + mine);
            }
        }
        ExerciseChecker.check("syntaxError() == javac sur 18 cas (" + agree + " d'accord)", agree == 18);

        String bad = "incompatible types: bad return type in lambda expression";
        ExerciseChecker.check("returnError() == javac sur 2 cas (+ 2 corrects)",
                returnError(true, true, false).equals(bad) && returnError(false, true, true).equals(bad)
                        && returnError(true, true, true).equals("OK") && returnError(false, false, false).equals("OK"));

        ExerciseChecker.check("targetError() == javac sur 3 cas (+ 1 correct)",
                targetError("var", 1, 1).equals("'var' is not allowed here")
                        && targetError("Object", 0, 0).equals("incompatible types: Object is not a functional interface")
                        && targetError("Predicate", 2, 1).equals("incompatible types: incompatible parameter types in lambda expression")
                        && targetError("Predicate", 1, 1).equals("OK"));

        ExerciseChecker.summary();
    }
}
