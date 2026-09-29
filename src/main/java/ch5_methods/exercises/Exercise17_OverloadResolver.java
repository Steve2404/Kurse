package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

/**
 * EXERCICE 17 - Ecris l'algorithme de choix de surcharge de javac, compare a 26 vrais appels (niveau : avance)
 * ===========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand plusieurs methodes portent le meme nom, javac cherche en
 * TROIS tours, et s'arrete au premier tour qui trouve quelque chose :
 *
 *   Tour 1 : SANS boxing ni varargs. Un primitif peut s'ELARGIR
 *            (byte < short < int < long < float < double) ; un Integer
 *            peut aller vers Integer, Number ou Object.
 *   Tour 2 : AVEC boxing/unboxing, toujours sans varargs. Un int
 *            devient Integer (puis Number ou Object) ; un Integer devient
 *            int (puis s'elargit).
 *   Tour 3 : AVEC varargs.
 *
 * Dans un tour, s'il y a plusieurs candidats, on prend le PLUS PRECIS :
 * le plus petit primitif, ou Integer avant Number avant Object.
 *
 * -- Vrais resultats (Java 17) utilises dans main() --
 *
 *   (long, Integer)  avec un int      -> long     (elargir passe AVANT boxer)
 *   (Integer, int...) avec un short   -> int...   (un short ne se boxe PAS en Integer : Short !)
 *   (Long, Object)   avec un int      -> Object   (int se boxe en Integer, pas en Long)
 *   (long, Object)   avec un Integer  -> Object   (tour 1 : Integer est un Object ; deballer attendrait le tour 2)
 *   (Number, Long)   avec un long     -> Long     (Long plus precis que Number)
 *   (short, float)   avec un int      -> float    (int ne retrecit jamais en short)
 *
 *   Ambiguites reelles (javac 17) : m(int...) et m(Object...) avec m(1) -> error: reference to m is ambiguous ;
 *   m(long, int) et m(int, long) avec m(1, 1) -> meme erreur ; m(String) et m(Integer) avec m(null) -> meme erreur.
 *
 *
 * ==================================================================
 * TODO 1 : widens(from, to)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("short", "int") -> true ; ("int", "int") -> true ; ("int", "short") -> false ; ("long", "float") -> true
 *
 * -- Le plan --
 *
 *   1. Les deux sont dans PRIMITIVES et index(to) >= index(from).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : index(type) dans PRIMITIVES (-1 si ce n'est pas un primitif).
 *
 *
 * ==================================================================
 * TODO 2 : boxOf(primitive)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "short" -> "Short" ; "int" -> "Integer" ; "long" -> "Long"
 *
 * -- Le plan --
 *
 *   1. Un switch expression.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : acceptsReference(argRef, candidate)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("Integer", "Integer") -> true ; ("Integer", "Number") -> true ; ("Short", "Object") -> true
 *   ("Short", "Integer")   -> false ; ("Integer", "Long") -> false
 *
 * -- Le plan --
 *
 *   1. candidate est argRef, "Number" ou "Object".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : resolve(arg, candidates...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * arg est "short", "int", "long" ou "Integer". Les candidats sont des
 * types de parametre ("int", "Long", "Object", "int..."...). On rend le
 * candidat choisi, ou "none".
 *
 * -- Le plan --
 *
 *   1. Tour 1 : si arg est primitif, les candidats primitifs ou widens(arg, c) ; sinon (Integer)
 *      les candidats references ou acceptsReference(arg, c). Garder le plus precis.
 *   2. Tour 2 : si arg est primitif, les references ou acceptsReference(boxOf(arg), c) ;
 *      sinon les primitifs ou widens("int", c). Garder le plus precis.
 *   3. Tour 3 : les varargs "X..." ou X accepte arg (primitif : widens depuis arg, ou depuis "int" pour
 *      un Integer ; reference : acceptsReference de la boite).
 *   4. Sinon "none".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : moreSpecific(a, b) (plus petit index pour les primitifs ; rang Integer/Long/Short 0,
 * Number 1, Object 2 pour les references), et les boites des TODO 1 a 3.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - candidate.endsWith("...") et candidate.substring(0, candidate.length() - 3).
 */
public class Exercise17_OverloadResolver {

    public static final String[] PRIMITIVES = {"byte", "short", "int", "long", "float", "double"};

    public static boolean widens(String from, String to) {
        throw new UnsupportedOperationException("TODO 1 : implementer widens()");
    }

    public static String boxOf(String primitive) {
        throw new UnsupportedOperationException("TODO 2 : implementer boxOf()");
    }

    public static boolean acceptsReference(String argRef, String candidate) {
        throw new UnsupportedOperationException("TODO 3 : implementer acceptsReference()");
    }

    public static String resolve(String arg, String... candidates) {
        throw new UnsupportedOperationException("TODO 4 : implementer resolve()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("widens : 4 cas", widens("short", "int") && widens("int", "int") && !widens("int", "short") && widens("long", "float"));
        ExerciseChecker.check("boxOf : Short, Integer, Long", boxOf("short").equals("Short") && boxOf("int").equals("Integer") && boxOf("long").equals("Long"));
        ExerciseChecker.check("acceptsReference : 5 cas",
                acceptsReference("Integer", "Integer") && acceptsReference("Integer", "Number") && acceptsReference("Short", "Object")
                        && !acceptsReference("Short", "Integer") && !acceptsReference("Integer", "Long"));

        short s = 5;
        long l = 5L;
        Integer boxed = 5;
        // {arg, choix REEL de Java, candidats...}
        String[][] real = {
                {"short", a(s), "int", "long"}, {"int", a(5), "int", "long"}, {"long", a(l), "int", "long"},
                {"int", b(5), "long", "Integer"}, {"Integer", b(boxed), "long", "Integer"}, {"short", b(s), "long", "Integer"},
                {"int", c(5), "Integer", "int..."}, {"short", c(s), "Integer", "int..."},
                {"int", d(5), "Object", "long"}, {"Integer", d(boxed), "Object", "long"},
                {"int", e(5), "Number", "double"}, {"Integer", e(boxed), "Number", "double"},
                {"int", f(5), "Object", "Object..."},
                {"int", g(5), "short", "float"}, {"short", g(s), "short", "float"},
                {"int", h(5), "Long", "Object"}, {"long", h(l), "Long", "Object"},
                {"int", i(5), "int...", "Object"},
                {"Integer", k(boxed), "double", "Integer"}, {"int", k(5), "double", "Integer"},
                {"Integer", m(boxed), "long", "Object"}, {"int", m(5), "long", "Object"},
                {"int", n(5), "Integer", "long..."}, {"short", n(s), "Integer", "long..."}, {"long", n(l), "Integer", "long..."},
                {"int", p(5), "Number", "Long"}};
        int agree = 0;
        for (String[] call : real) {
            String[] candidates = new String[call.length - 2];
            System.arraycopy(call, 2, candidates, 0, candidates.length);
            String mine = resolve(call[0], candidates);
            if (mine.equals(call[1])) {
                agree++;
            } else {
                System.out.println("   desaccord : arg " + call[0] + " parmi " + String.join(", ", candidates)
                        + " -> Java choisit " + call[1] + ", toi " + mine);
            }
        }
        ExerciseChecker.check("resolve() == le choix REEL de Java sur " + real.length + " appels (" + agree + " d'accord)", agree == real.length);
        ExerciseChecker.check("resolve(\"long\", \"int\", \"Integer\") == \"none\" (rien n'accepte un long)", resolve("long", "int", "Integer").equals("none"));

        ExerciseChecker.summary();
    }

    // Deja ecrit : de vraies surcharges. Chacune rend le type de SON parametre.
    static String a(int x) { return "int"; }
    static String a(long x) { return "long"; }
    static String b(long x) { return "long"; }
    static String b(Integer x) { return "Integer"; }
    static String c(Integer x) { return "Integer"; }
    static String c(int... x) { return "int..."; }
    static String d(Object x) { return "Object"; }
    static String d(long x) { return "long"; }
    static String e(Number x) { return "Number"; }
    static String e(double x) { return "double"; }
    static String f(Object x) { return "Object"; }
    static String f(Object... x) { return "Object..."; }
    static String g(short x) { return "short"; }
    static String g(float x) { return "float"; }
    static String h(Long x) { return "Long"; }
    static String h(Object x) { return "Object"; }
    static String i(int... x) { return "int..."; }
    static String i(Object x) { return "Object"; }
    static String k(double x) { return "double"; }
    static String k(Integer x) { return "Integer"; }
    static String m(long x) { return "long"; }
    static String m(Object x) { return "Object"; }
    static String n(Integer x) { return "Integer"; }
    static String n(long... x) { return "long..."; }
    static String p(Number x) { return "Number"; }
    static String p(Long x) { return "Long"; }
}
