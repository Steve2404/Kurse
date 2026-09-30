package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 4 - Les regles des membres d'interface, ecrites par toi et comparees a 20 verdicts reels de javac (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une interface a 5 sortes de membres, chacune avec ses modificateurs
 * AJOUTES en cachette par le compilateur :
 *
 *   champ            -> public static final (une constante : valeur obligatoire)
 *   methode abstract -> public abstract (pas de corps)
 *   methode default  -> public (corps obligatoire)
 *   methode static   -> public static (corps obligatoire ; PAS heritee par les classes qui implementent)
 *   methode private  -> private (corps obligatoire ; aide les default et, si elle est static, les static)
 *
 * -- Verdicts reels de javac 17 --
 *
 *   int X;                         -> error: = expected
 *   private int X = 1;             -> error: modifier private not allowed here
 *   protected int X = 1;           -> error: modifier protected not allowed here
 *   protected void m();            -> error: modifier protected not allowed here
 *   final void m();                -> error: modifier final not allowed here
 *   void m() {}                    -> error: interface abstract methods cannot have body
 *   default void m();              -> error: missing method body, or declare abstract
 *   private void m();              -> error: missing method body, or declare abstract
 *   static void m();               -> error: missing method body, or declare abstract
 *   static int s() { return h(); } avec h() private NON static -> error: non-static method h() cannot be referenced from a static context
 *   C.s() ou s() ou c.s() dans une classe C qui implemente I (s static de I) -> error: cannot find symbol
 *   I.s()                          -> compile (on appelle une methode static d'interface par le nom de l'INTERFACE)
 *   X = 2; (X constante de I)      -> error: cannot assign a value to final variable X
 *   deux default h() de A et B, C implements A, B sans redefinir -> error: types A and B are incompatible;
 *   ... C redefinit h() et appelle A.super.h() -> compile
 *   class C implements I { void m() {} } (I.m est public) -> error: m() in C cannot implement m() in I
 *   default boolean equals(Object o) -> error: default method equals in interface I overrides a member of java.lang.Object
 *   final interface I              -> error: illegal combination of modifiers: interface and final
 *   new I()                        -> error: I is abstract; cannot be instantiated
 *   interface I extends K (K une classe) ou class C implements K -> error: interface expected here
 *
 *
 * ==================================================================
 * TODO 1 : memberError(kind, modifiers, hasBody, hasInitializer)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * kind vaut "field", "abstract", "default", "static" ou "private".
 * modifiers : les mots-cles ECRITS en plus (par exemple ["protected"]).
 * On rend le message exact de javac, ou "OK".
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. Pour "private" et "protected" (dans cet ordre) : si ecrit et kind est "field" ou, pour
 *      "protected", n'importe quel kind -> "modifier " + mot + " not allowed here".
 *   2. "final" ecrit sur une methode -> "modifier final not allowed here".
 *   3. kind "field" : sans initialiseur -> "= expected" ; sinon "OK".
 *   4. kind "abstract" avec corps -> "interface abstract methods cannot have body".
 *   5. kind "default", "static" ou "private" sans corps -> "missing method body, or declare abstract".
 *   6. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : implicitModifiers(kind)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "field" -> "public static final" ; "abstract" -> "public abstract" ; "default" -> "public"
 *   "static" -> "public static" ; "private" -> "private"
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
 * TODO 3 : staticCallError(how)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une methode static d'INTERFACE ne s'herite pas (contrairement a une
 * methode static de classe). Depuis une classe C qui implemente I, on
 * l'appelle seulement par le nom de l'interface. how vaut "I.s()",
 * "C.s()", "s()" ou "c.s()".
 *
 * -- Le plan --
 *
 *   1. "I.s()" -> "OK" ; sinon "cannot find symbol".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : diamondError(bothHaveDefault, classRedefines)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. bothHaveDefault && !classRedefines -> "types A and B are incompatible;" ; sinon "OK".
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
 *   - modifiers.contains("final").
 */
public class Exercise04_InterfaceRules {

    public static String memberError(String kind, List<String> modifiers, boolean hasBody, boolean hasInitializer) {
        throw new UnsupportedOperationException("TODO 1 : implementer memberError()");
    }

    public static String implicitModifiers(String kind) {
        throw new UnsupportedOperationException("TODO 2 : implementer implicitModifiers()");
    }

    public static String staticCallError(String how) {
        throw new UnsupportedOperationException("TODO 3 : implementer staticCallError()");
    }

    public static String diamondError(boolean bothHaveDefault, boolean classRedefines) {
        throw new UnsupportedOperationException("TODO 4 : implementer diamondError()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : {kind, modificateurs ecrits, corps ?, initialiseur ?, verdict}.
        Object[][] javac = {
                {"field", List.of(), false, false, "= expected"},
                {"field", List.of("private"), false, true, "modifier private not allowed here"},
                {"field", List.of("protected"), false, true, "modifier protected not allowed here"},
                {"field", List.of(), false, true, "OK"},
                {"abstract", List.of("protected"), false, false, "modifier protected not allowed here"},
                {"abstract", List.of("final"), false, false, "modifier final not allowed here"},
                {"abstract", List.of(), true, false, "interface abstract methods cannot have body"},
                {"abstract", List.of(), false, false, "OK"},
                {"default", List.of(), false, false, "missing method body, or declare abstract"},
                {"default", List.of(), true, false, "OK"},
                {"private", List.of(), false, false, "missing method body, or declare abstract"},
                {"private", List.of(), true, false, "OK"},
                {"static", List.of(), false, false, "missing method body, or declare abstract"},
                {"static", List.of(), true, false, "OK"}};
        int agree = 0;
        for (Object[] v : javac) {
            @SuppressWarnings("unchecked")
            List<String> modifiers = (List<String>) v[1];
            if (memberError((String) v[0], modifiers, (Boolean) v[2], (Boolean) v[3]).equals(v[4])) {
                agree++;
            }
        }
        ExerciseChecker.check("memberError() == javac sur 14 cas (" + agree + " d'accord)", agree == 14);

        ExerciseChecker.check("implicitModifiers : 5 sortes de membres",
                implicitModifiers("field").equals("public static final") && implicitModifiers("abstract").equals("public abstract")
                        && implicitModifiers("default").equals("public") && implicitModifiers("static").equals("public static")
                        && implicitModifiers("private").equals("private"));

        ExerciseChecker.check("staticCallError == javac sur 4 cas",
                staticCallError("I.s()").equals("OK") && staticCallError("C.s()").equals("cannot find symbol")
                        && staticCallError("s()").equals("cannot find symbol") && staticCallError("c.s()").equals("cannot find symbol"));

        ExerciseChecker.check("diamondError == javac sur 2 cas (+ un cas sans conflit)",
                diamondError(true, false).equals("types A and B are incompatible;") && diamondError(true, true).equals("OK")
                        && diamondError(false, false).equals("OK"));

        ExerciseChecker.summary();
    }
}
