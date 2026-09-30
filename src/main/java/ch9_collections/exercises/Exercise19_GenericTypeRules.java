package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EXERCICE 19 - Effacement de type, new T(), static, instanceof, bornes : ta regle comparee a 39 verdicts de javac (niveau : difficile)
 * =====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les generiques n'existent que pour le COMPILATEUR. Apres la
 * compilation, il les efface (type erasure) : List<String> devient List,
 * T devient sa borne (Object si rien n'est dit, Number pour
 * <T extends Number>). Presque tous les pieges viennent de la :
 *
 *   - a l'execution, on ne sait plus ce qu'est T : pas de new T(), pas de new T[3], pas de instanceof T ;
 *   - deux methodes dont les parametres s'effacent pareil se marchent dessus ;
 *   - le T d'une classe appartient a chaque OBJET : il n'existe pas dans le monde static.
 *
 * -- Verdicts reels de javac 17 --
 *
 *   List<int> l;                                   -> error: unexpected type
 *   new T()  /  new ArrayList<?>()                 -> error: unexpected type
 *   new T[3]  /  new List<String>[3]               -> error: generic array creation
 *   new List<?>[3]  /  new List[3]                 -> compile
 *   void m(List<String>) + void m(List<Integer>)   -> error: name clash: m(List<Integer>) and m(List<String>) have the same erasure
 *   class C<T extends Number> : m(T) + m(Object)   -> compile (T s'efface en Number)
 *   class C<T> { static T x; }                     -> error: non-static type variable T cannot be referenced from a static context
 *   class C<T> { static <T> T m(T t) }             -> compile (ce T-la est a la methode)
 *   o instanceof List<String>  (o : Object)        -> error: Object cannot be safely cast to List<String>
 *   o instanceof List<?>                           -> compile
 *   <T extends Comparable<T> & Number>             -> error: interface expected here
 *   <T> static void m()                            -> error: illegal start of type (le <T> va APRES static)
 *   class C<T> extends Exception                   -> error: a generic class may not extend java.lang.Throwable
 *   catch (T e)                                    -> error: unexpected type
 *   class C<? extends Number>                      -> error: <identifier> expected
 *   class C<T super Number>                        -> error: > expected
 *   <T extends Number> T id(T) ; id("x")           -> error: method id in class C cannot be applied to given types;
 *
 *
 * ==================================================================
 * TODO 1 : typeArgs(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Rendre les arguments de type du PREMIER niveau, sans espaces autour.
 *
 * -- Essayons a la main --
 *
 *   "Map<String, List<Integer>>" -> [String, List<Integer>]
 *   "List<Map<A, B>>"            -> [Map<A, B>]
 *   "List" -> [] ; "ArrayList<>" -> [] (diamant)
 *
 * -- Le plan --
 *
 *   1. Pas de '<' -> liste vide.
 *   2. Prendre ce qui est entre le premier '<' et le DERNIER '>'.
 *   3. Couper sur les virgules de profondeur 0 (compter les '<' et '>').
 *   4. Enlever les espaces ; ignorer un morceau vide.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais typeArgs est elle-meme la boite magique des TODO 4 et 6.
 *
 *
 * ==================================================================
 * TODO 2 : erasure(type, bounds)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * bounds dit, pour chaque variable de type, sa premiere borne ("Object"
 * si aucune). Rendre le type efface.
 *
 * -- Essayons a la main --
 *
 *   "List<String>" -> List ; "T" {T=Object} -> Object ; "E" {E=Comparable<E>} -> Comparable
 *   "T[]" {T=Object} -> Object[] ; "List<String>[]" -> List[]
 *
 * -- Le plan --
 *
 *   1. Mettre de cote les "[]" de la fin.
 *   2. Enlever tout ce qui est a partir du '<'.
 *   3. Si c'est une variable de type -> effacer sa borne (elle peut avoir des <...>).
 *   4. Recoller les "[]".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : erasure elle-meme, appelee sur la borne (recursion).
 *
 *
 * ==================================================================
 * TODO 3 : nameClash(method, params1, params2, bounds)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux methodes de meme nom, aux parametres ecrits differemment. Si,
 * une fois effaces, leurs parametres sont identiques -> le message de
 * javac, qui cite la 2e methode PUIS la 1re, parametres separes par ","
 * (sans espace). Sinon "OK" (surcharge normale).
 *
 * -- Essayons a la main --
 *
 *   m [List<String>] / [List<Integer>] -> name clash: m(List<Integer>) and m(List<String>) have the same erasure
 *   m [Set<String>] / [List<String>]   -> OK
 *   m [T] / [Object] {T=Number}        -> OK
 *
 * -- Le plan --
 *
 *   1. Tailles differentes -> "OK".
 *   2. Comparer les effacements parametre par parametre ; une difference -> "OK".
 *   3. Sinon construire le message.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : erasure (TODO 2).
 *
 *
 * ==================================================================
 * TODO 4 : creationError(expr, typeVars)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * expr est ce qui suit "new" : "T()", "List<String>[3]", "ArrayList<?>()"...
 *
 *   Tableau (expr finit par ']') : element = avant le premier '[' situe
 *     APRES le dernier '>' (ArrayList<int[]>() n'est pas un tableau !). Variable de type, ou
 *     arguments autres que des "?" tout seuls -> "generic array creation".
 *   Objet : type = avant le '('. Variable de type, ou un argument de
 *     1er niveau qui commence par "?" ou qui est un primitif ->
 *     "unexpected type".
 *   Sinon "OK".
 *
 * -- Essayons a la main --
 *
 *   "List<?>[3]" -> OK ; "ArrayList<List<?>>[2]" -> generic array creation
 *   "ArrayList<List<?>>()" -> OK (le ? n'est pas au 1er niveau) ; "ArrayList<int[]>()" -> OK
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : typeArgs (TODO 1).
 *
 *
 * ==================================================================
 * TODO 5 : staticContextError(typeVar, member, ownTypeParam)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On utilise la variable de type de la CLASSE dans un membre. member
 * vaut "instance field", "instance method", "static field", "static
 * method", "static nested class" ou "interface field". ownTypeParam :
 * le membre declare-t-il son PROPRE <T> ?
 *
 * Piege : un champ d'interface est implicitement static.
 *
 * -- Le plan --
 *
 *   1. Contexte static = member commence par "static" OU vaut "interface field".
 *   2. Static et pas de <T> a lui -> "non-static type variable " + typeVar + " cannot be referenced from a static context".
 *   3. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : instanceofError(staticType, target, typeVars)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "x instanceof target", ou x est declare avec staticType. A l'execution
 * on ne peut verifier que la partie effacee : c'est permis seulement si
 * le compilateur est SUR que les arguments de type sont bons.
 *
 * -- Le plan --
 *
 *   1. target est une variable de type -> erreur.
 *   2. target sans arguments, ou seulement des "?" -> "OK".
 *   3. Memes arguments que staticType (Collection<String> -> List<String>) -> "OK".
 *   4. Sinon -> staticType + " cannot be safely cast to " + target.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : typeArgs (TODO 1).
 *
 *
 * ==================================================================
 * TODO 7 : boundsError(bounds, classes)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * <T extends A & B & C> : A peut etre une classe ou une interface, mais
 * tout ce qui suit le premier & doit etre une INTERFACE (une seule
 * classe, et en premier). classes = les noms qui sont des classes.
 *
 * -- Le plan --
 *
 *   1. Pour chaque borne a partir de la 2e : son nom (sans <...>) est une classe -> "interface expected here".
 *   2. Sinon "OK".
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
 *   - type.indexOf('<'), type.lastIndexOf('>'), un compteur depth pour les virgules.
 *   - type.endsWith("[]") dans une boucle ; String.join(",", params).
 *   - Set.of("byte", "short", "int", "long", "float", "double", "char", "boolean").
 */
public class Exercise19_GenericTypeRules {

    public static List<String> typeArgs(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer typeArgs()");
    }

    public static String erasure(String type, Map<String, String> bounds) {
        throw new UnsupportedOperationException("TODO 2 : implementer erasure()");
    }

    public static String nameClash(String method, List<String> params1, List<String> params2, Map<String, String> bounds) {
        throw new UnsupportedOperationException("TODO 3 : implementer nameClash()");
    }

    public static String creationError(String expr, Set<String> typeVars) {
        throw new UnsupportedOperationException("TODO 4 : implementer creationError()");
    }

    public static String staticContextError(String typeVar, String member, boolean ownTypeParam) {
        throw new UnsupportedOperationException("TODO 5 : implementer staticContextError()");
    }

    public static String instanceofError(String staticType, String target, Set<String> typeVars) {
        throw new UnsupportedOperationException("TODO 6 : implementer instanceofError()");
    }

    public static String boundsError(List<String> bounds, Set<String> classes) {
        throw new UnsupportedOperationException("TODO 7 : implementer boundsError()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("typeArgs : Map<String, List<Integer>>, List<Map<A, B>>, List, ArrayList<>",
                typeArgs("Map<String, List<Integer>>").equals(List.of("String", "List<Integer>"))
                        && typeArgs("List<Map<A, B>>").equals(List.of("Map<A, B>"))
                        && typeArgs("List").isEmpty() && typeArgs("ArrayList<>").isEmpty());

        Map<String, String> tObject = Map.of("T", "Object");
        ExerciseChecker.check("erasure : List<String>, Map<..>, T, E extends Comparable<E>, T[], List<String>[], String",
                erasure("List<String>", tObject).equals("List")
                        && erasure("Map<String, List<Integer>>", tObject).equals("Map")
                        && erasure("T", tObject).equals("Object")
                        && erasure("E", Map.of("E", "Comparable<E>")).equals("Comparable")
                        && erasure("T[]", tObject).equals("Object[]")
                        && erasure("List<String>[]", tObject).equals("List[]")
                        && erasure("String", tObject).equals("String"));

        // Verdicts REELS de javac 17.
        String clash = "name clash: %s and %s have the same erasure";
        Object[][] clashes = {
                {List.of("List<String>"), List.of("List<Integer>"), tObject, String.format(clash, "m(List<Integer>)", "m(List<String>)")},
                {List.of("List<String>", "int"), List.of("List<Integer>", "int"), tObject, String.format(clash, "m(List<Integer>,int)", "m(List<String>,int)")},
                {List.of("List<String>[]"), List.of("List<Integer>[]"), tObject, String.format(clash, "m(List<Integer>[])", "m(List<String>[])")},
                {List.of("Set<String>"), List.of("List<String>"), tObject, "OK"},
                {List.of("T"), List.of("Object"), tObject, String.format(clash, "m(Object)", "m(T)")},
                {List.of("T"), List.of("Object"), Map.of("T", "Number"), "OK"}};
        int agree = 0;
        for (Object[] c : clashes) {
            @SuppressWarnings("unchecked")
            String mine = nameClash("m", (List<String>) c[0], (List<String>) c[1], (Map<String, String>) c[2]);
            if (mine.equals(c[3])) {
                agree++;
            }
        }
        ExerciseChecker.check("nameClash == javac sur 6 cas (" + agree + " d'accord)", agree == 6);

        String unexpected = "unexpected type";
        String array = "generic array creation";
        String[][] creations = {
                {"T()", unexpected}, {"T[3]", array}, {"T[2][2]", array}, {"List<String>[3]", array},
                {"ArrayList<String>[2]", array}, {"List<?>[3]", "OK"}, {"List[3]", "OK"}, {"int[2]", "OK"},
                {"ArrayList<List<?>>[2]", array}, {"ArrayList<?>()", unexpected}, {"ArrayList<? extends Number>()", unexpected},
                {"ArrayList<int>()", unexpected}, {"HashMap<String, double>()", unexpected}, {"ArrayList<int[]>()", "OK"},
                {"ArrayList<>()", "OK"}, {"ArrayList<List<?>>()", "OK"}};
        agree = 0;
        for (String[] c : creations) {
            if (creationError(c[0], Set.of("T")).equals(c[1])) {
                agree++;
            }
        }
        ExerciseChecker.check("creationError == javac sur 16 cas (" + agree + " d'accord)", agree == 16);

        String nonStatic = "non-static type variable T cannot be referenced from a static context";
        Object[][] statics = {
                {"static field", false, nonStatic}, {"static method", false, nonStatic}, {"static method", true, "OK"},
                {"static nested class", false, nonStatic}, {"interface field", false, nonStatic},
                {"instance field", false, "OK"}, {"instance method", false, "OK"}};
        agree = 0;
        for (Object[] c : statics) {
            if (staticContextError("T", (String) c[0], (Boolean) c[1]).equals(c[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("staticContextError == javac sur 7 cas (" + agree + " d'accord)", agree == 7);

        String[][] instances = {
                {"Object", "List<String>", "Object cannot be safely cast to List<String>"}, {"Object", "List<?>", "OK"},
                {"Collection<String>", "List<String>", "OK"}, {"Object", "T", "Object cannot be safely cast to T"},
                {"Object", "List", "OK"}, {"Map<String,Integer>", "HashMap<String,Integer>", "OK"}};
        agree = 0;
        for (String[] c : instances) {
            if (instanceofError(c[0], c[1], Set.of("T")).equals(c[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("instanceofError == javac sur 6 cas (" + agree + " d'accord)", agree == 6);

        Set<String> classes = Set.of("Number", "Integer", "Object", "String");
        String iface = "interface expected here";
        ExerciseChecker.check("boundsError == javac sur 4 cas",
                boundsError(List.of("Number", "Comparable<T>"), classes).equals("OK")
                        && boundsError(List.of("Comparable<T>", "Number"), classes).equals(iface)
                        && boundsError(List.of("Number", "Integer"), classes).equals(iface)
                        && boundsError(List.of("Comparable<T>"), classes).equals("OK"));

        ExerciseChecker.summary();
    }
}
