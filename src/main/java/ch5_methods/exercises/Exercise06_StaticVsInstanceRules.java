package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

/**
 * EXERCICE 6 - Static contre instance : la regle ecrite par toi, comparee a 16 verdicts reels de javac (niveau : difficile)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une methode static appartient a la CLASSE : elle n'a pas de "this",
 * pas d'objet a elle. Une methode d'instance appartient a UN objet. On
 * se demande : depuis une methode (static ou d'instance), puis-je
 * appeler un membre (static ou d'instance), ecrit de 4 facons ?
 *
 *   "unqualified" : im()           "this" : this.im()
 *   "object"      : new C().im()   "className" : C.im()
 *
 * -- Verdicts reels de javac 17 --
 *
 *   depuis static, membre d'instance sans objet -> error: non-static method im() cannot be referenced from a static context
 *   depuis static, this.xxx                     -> error: non-static variable this cannot be referenced from a static context
 *   depuis static, champ d'instance f           -> error: non-static variable f cannot be referenced from a static context
 *   depuis n'importe ou, C.im() (im d'instance) -> error: non-static method im() cannot be referenced from a static context
 *   depuis une instance, this.sm() (sm static)  -> compile (etrange mais permis)
 *   new C().sm() (sm static via un objet)       -> compile
 *
 * -- Et a l'execution (verifie) --
 *
 *   Tool t = null; t.version();   (version static)  -> marche ! aucune NullPointerException
 *   Tool t = null; t.name();      (name d'instance) -> NullPointerException
 *   Un appel static via une variable ne regarde que le TYPE declare, jamais l'objet.
 *   Avec javac -Xlint:all : warning: [static] static method should be qualified by type name, Tool, instead of by an expression
 *
 *
 * ==================================================================
 * TODO 1 : canCompile(caller, member, how)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("static", "instance", "unqualified") -> false (quel objet ?)
 *   ("static", "instance", "object")      -> true  (new C().im() : on a donne l'objet)
 *   ("instance", "static", "this")        -> true
 *   ("instance", "instance", "className") -> false (C.im() : quel objet ?)
 *
 * -- Le plan --
 *
 *   1. switch sur how :
 *        "object"      -> true
 *        "this"        -> caller est "instance"
 *        "className"   -> member est "static"
 *        "unqualified" -> member est "static" OU caller est "instance"
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : errorFor(caller, member, how)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand ca ne compile pas, quel message javac affiche-t-il ? Le membre
 * d'instance s'appelle im(), le membre static sm().
 *
 * -- Essayons a la main --
 *
 *   compile                              -> ""
 *   how "this" depuis static             -> "non-static variable this cannot be referenced from a static context"
 *   sinon (im() sans objet)              -> "non-static method im() cannot be referenced from a static context"
 *
 * -- Le plan --
 *
 *   1. Si canCompile -> "". Si how est "this" -> message sur this. Sinon -> message sur im().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : canCompile (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : staticThroughNull()    et    TODO 4 : instanceThroughNull()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On appelle une methode sur une variable Tool qui vaut null. Pour une
 * methode static, Java n'a besoin que du TYPE : ca marche. Pour une
 * methode d'instance, il faut l'objet : NullPointerException.
 *
 * -- Le plan --
 *
 *   1. staticThroughNull : Tool t = null ; rendre t.version().
 *   2. instanceThroughNull : try { Tool t = null ; rendre t.name() ; }
 *      catch (NullPointerException e) { rendre "NullPointerException" ; }
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
 *   - Comparer des String avec equals.
 */
public class Exercise06_StaticVsInstanceRules {

    public static class Tool {
        public static String version() {
            return "1.0";
        }

        public String name() {
            return "tournevis";
        }
    }

    public static boolean canCompile(String caller, String member, String how) {
        throw new UnsupportedOperationException("TODO 1 : implementer canCompile()");
    }

    public static String errorFor(String caller, String member, String how) {
        throw new UnsupportedOperationException("TODO 2 : implementer errorFor()");
    }

    public static String staticThroughNull() {
        throw new UnsupportedOperationException("TODO 3 : implementer staticThroughNull()");
    }

    public static String instanceThroughNull() {
        throw new UnsupportedOperationException("TODO 4 : implementer instanceThroughNull()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : {caller, member, how, message ("" = compile)}.
        String thisError = "non-static variable this cannot be referenced from a static context";
        String methodError = "non-static method im() cannot be referenced from a static context";
        String[][] javac = {
                {"static", "static", "unqualified", ""}, {"static", "static", "this", thisError},
                {"static", "static", "object", ""}, {"static", "static", "className", ""},
                {"static", "instance", "unqualified", methodError}, {"static", "instance", "this", thisError},
                {"static", "instance", "object", ""}, {"static", "instance", "className", methodError},
                {"instance", "static", "unqualified", ""}, {"instance", "static", "this", ""},
                {"instance", "static", "object", ""}, {"instance", "static", "className", ""},
                {"instance", "instance", "unqualified", ""}, {"instance", "instance", "this", ""},
                {"instance", "instance", "object", ""}, {"instance", "instance", "className", methodError}};
        int compileAgree = 0;
        int messageAgree = 0;
        for (String[] v : javac) {
            if (canCompile(v[0], v[1], v[2]) == v[3].isEmpty()) {
                compileAgree++;
            }
            if (errorFor(v[0], v[1], v[2]).equals(v[3])) {
                messageAgree++;
            }
        }
        ExerciseChecker.check("canCompile() == javac sur 16 cas (" + compileAgree + " d'accord)", compileAgree == 16);
        ExerciseChecker.check("errorFor() == le message exact de javac sur 16 cas (" + messageAgree + " d'accord)", messageAgree == 16);
        ExerciseChecker.check("staticThroughNull() == \"1.0\" (pas de NullPointerException)", "1.0".equals(staticThroughNull()));
        ExerciseChecker.check("instanceThroughNull() == \"NullPointerException\"", "NullPointerException".equals(instanceThroughNull()));

        ExerciseChecker.summary();
    }
}
