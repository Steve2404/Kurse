package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 21 - Les regles des classes imbriquees (membre, static, locale, anonyme), comparees a 24 verdicts de javac (niveau : difficile)
 * ======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * 4 sortes de classes dans une classe O :
 *   classe interne (membre non static) : liee a UN objet O (acces a O.this et aux champs d'instance) ;
 *   classe imbriquee static            : independante de tout objet O ;
 *   classe locale (dans une methode)    : visible seulement dans la methode ;
 *   classe anonyme (new I() { ... })    : sans nom, creee en une seule expression.
 *
 * -- Verdicts reels de javac 17 --
 *
 *   private class C {} / static class C {} / protected class C {} (top-level) -> error: modifier private/static/protected not allowed here
 *   class O { private class I {} }        -> compile (un membre accepte tous les modificateurs)
 *   public/private/static class L {} dans une methode -> error: illegal start of expression
 *   final/abstract class L {} dans une methode        -> compile ; interface, record et enum locaux -> compile
 *   class I { static int x = 1; static int m() {...} } dans une classe interne -> compile (autorise depuis Java 16)
 *   static class N { int g() { return f; } } (f champ d'instance de O) -> error: non-static variable f cannot be referenced from a static context
 *   static class N { ... O.this.f ... }   -> error: non-static variable this cannot be referenced from a static context
 *   class I { ... f ... } et O.this.f     -> compile
 *   new I() depuis une methode static de O -> error: non-static variable this cannot be referenced from a static context
 *   new O().new I()                        -> compile ; new N() (N static) -> compile
 *   classe locale qui lit une variable modifiee -> error: local variables referenced from an inner class must be final or effectively final
 *   new A, B() { }                         -> error: '(' or '[' expected (une anonyme n'a qu'UN super-type)
 *   new A(1) { } (A interface)             -> error: anonymous class implements interface; cannot have arguments
 *   new A() { A() { } }                    -> error: invalid method declaration; return type required (pas de constructeur)
 *   static class N lit o.secret (private de O) -> compile (les imbriquees partagent les private)
 *
 *
 * ==================================================================
 * TODO 1 : declarationError(where, modifier)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * where : "top-level", "member" ou "local". modifier : "public",
 * "protected", "private", "static", "final", "abstract" ou "".
 *
 * -- Le plan --
 *
 *   1. "member" -> "OK".
 *   2. "top-level" : private, protected ou static -> "modifier " + modifier + " not allowed here".
 *   3. "local" : public, protected, private ou static -> "illegal start of expression".
 *   4. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : accessError(fromStaticNested, target)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Depuis une classe interne (fromStaticNested faux) -> "OK".
 *   2. Depuis une static : target "instance field" -> "non-static variable f cannot be referenced from a static context" ;
 *      target "O.this" -> "non-static variable this cannot be referenced from a static context" ;
 *      target "static field" ou "private via object" -> "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : creationError(innerClass, fromStaticContext, withOuterObject)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Classe interne, depuis un contexte static, SANS objet O (new I()) ->
 *      "non-static variable this cannot be referenced from a static context".
 *   2. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : anonymousError(superTypes, isInterface, hasArguments, declaresConstructor)
 * ==================================================================
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. superTypes > 1 -> "'(' or '[' expected".
 *   2. isInterface && hasArguments -> "anonymous class implements interface; cannot have arguments".
 *   3. declaresConstructor -> "invalid method declaration; return type required".
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
 *   - List.of("public", "protected", "private", "static").contains(modifier).
 */
public class Exercise21_NestedClassRules {

    public static String declarationError(String where, String modifier) {
        throw new UnsupportedOperationException("TODO 1 : implementer declarationError()");
    }

    public static String accessError(boolean fromStaticNested, String target) {
        throw new UnsupportedOperationException("TODO 2 : implementer accessError()");
    }

    public static String creationError(boolean innerClass, boolean fromStaticContext, boolean withOuterObject) {
        throw new UnsupportedOperationException("TODO 3 : implementer creationError()");
    }

    public static String anonymousError(int superTypes, boolean isInterface, boolean hasArguments, boolean declaresConstructor) {
        throw new UnsupportedOperationException("TODO 4 : implementer anonymousError()");
    }

    public static void main(String[] args) {
        String[][] declarations = {
                {"top-level", "private", "modifier private not allowed here"},
                {"top-level", "static", "modifier static not allowed here"},
                {"top-level", "protected", "modifier protected not allowed here"},
                {"top-level", "final", "OK"},
                {"member", "private", "OK"},
                {"member", "static", "OK"},
                {"local", "public", "illegal start of expression"},
                {"local", "private", "illegal start of expression"},
                {"local", "static", "illegal start of expression"},
                {"local", "final", "OK"},
                {"local", "abstract", "OK"}};
        int agree = 0;
        for (String[] v : declarations) {
            if (declarationError(v[0], v[1]).equals(v[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("declarationError() == javac sur 11 cas (" + agree + " d'accord)", agree == 11);

        ExerciseChecker.check("accessError() == javac sur 6 cas",
                accessError(true, "instance field").equals("non-static variable f cannot be referenced from a static context")
                        && accessError(true, "O.this").equals("non-static variable this cannot be referenced from a static context")
                        && accessError(true, "private via object").equals("OK")
                        && accessError(true, "static field").equals("OK")
                        && accessError(false, "instance field").equals("OK")
                        && accessError(false, "O.this").equals("OK"));

        ExerciseChecker.check("creationError() == javac sur 3 cas",
                creationError(true, true, false).equals("non-static variable this cannot be referenced from a static context")
                        && creationError(true, true, true).equals("OK") && creationError(false, true, false).equals("OK"));

        ExerciseChecker.check("anonymousError() == javac sur 4 cas",
                anonymousError(2, true, false, false).equals("'(' or '[' expected")
                        && anonymousError(1, true, true, false).equals("anonymous class implements interface; cannot have arguments")
                        && anonymousError(1, true, false, true).equals("invalid method declaration; return type required")
                        && anonymousError(1, false, true, false).equals("OK"));

        ExerciseChecker.summary();
    }
}
