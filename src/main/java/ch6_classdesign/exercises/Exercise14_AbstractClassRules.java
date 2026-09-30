package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 14 - Les regles des classes et methodes abstraites, ecrites par toi et comparees a javac (niveau : difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une classe abstract est un plan a moitie rempli : on ne peut pas la
 * construire telle quelle, et la PREMIERE classe concrete de la lignee
 * doit remplir TOUS les trous (toutes les methodes abstract heritees,
 * y compris celles des grands-parents et des interfaces).
 *
 * -- Verdicts reels de javac 17 --
 *
 *   new S() avec S abstract                     -> error: S is abstract; cannot be instantiated
 *   Ci extends S sans area()                    -> error: Ci is not abstract and does not override abstract method area() in S
 *   B implements Fl sans fly()                  -> error: B is not abstract and does not override abstract method fly() in Fl
 *   U extends T extends S : U n'ecrit que b()   -> error: U is not abstract and does not override abstract method a() in S
 *   abstract R extends S sans area()            -> compile (R reste abstraite)
 *   class S { abstract void m(); }              -> error: S is not abstract and does not override abstract method m() in S
 *   abstract final class S                      -> error: illegal combination of modifiers: abstract and final
 *   abstract final void m();                    -> error: illegal combination of modifiers: abstract and final
 *   abstract private void m();                  -> error: illegal combination of modifiers: abstract and private
 *   abstract static void m();                   -> error: illegal combination of modifiers: abstract and static
 *   abstract void m() {}                        -> error: abstract methods cannot have a body
 *   abstract S();  (constructeur)               -> error: modifier abstract not allowed here
 *   abstract int x;  (champ)                    -> error: modifier abstract not allowed here
 *   abstract class sans aucune methode abstract -> compile ; un constructeur dans une classe abstract -> compile
 *   public abstract void m() implemente par void m() -> error: m() in U cannot override m() in S
 *   une classe anonyme qui implemente les methodes -> compile : new S() { ... }
 *
 *
 * ==================================================================
 * TODO 1 : stillAbstract(inherited, implemented)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * inherited : les methodes abstract heritees, dans l'ordre de
 * declaration, chacune ecrite "a() in S". implemented : les noms que la
 * classe ecrit ("a()"). On rend celles qui restent a faire, dans l'ordre.
 *
 * -- Essayons a la main --
 *
 *   (["a() in S", "b() in T"], ["b()"]) -> ["a() in S"]
 *   (["area() in S"], ["area()"])       -> []
 *
 * -- Le plan --
 *
 *   1. Pour chaque element : name = la partie avant " in " ; si implemented ne contient pas name, le garder.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : classError(className, isAbstract, inherited, implemented)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Si isAbstract -> "OK".
 *   2. missing = stillAbstract(...) ; vide -> "OK".
 *   3. Sinon : className + " is not abstract and does not override abstract method "
 *      + le PREMIER manquant (par exemple "a() in S").
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : stillAbstract (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : modifierError(target, modifiers, hasBody)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * target vaut "class", "method", "constructor" ou "field". modifiers
 * est la liste des mots-cles. On rend le message javac ou "OK".
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. Pas de "abstract" -> "OK".
 *   2. target "constructor" ou "field" -> "modifier abstract not allowed here".
 *   3. Pour "final", "private", "static" (dans cet ordre) : present ->
 *      "illegal combination of modifiers: abstract and " + ce mot.
 *      (Pour une classe, seul "final" est teste.)
 *   4. target "method" et hasBody -> "abstract methods cannot have a body".
 *   5. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : instantiationError(className, isAbstract)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. isAbstract -> className + " is abstract; cannot be instantiated" ; sinon "OK".
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
 *   - "a() in S".substring(0, "a() in S".indexOf(" in ")) -> "a()".
 */
public class Exercise14_AbstractClassRules {

    public static List<String> stillAbstract(List<String> inherited, List<String> implemented) {
        throw new UnsupportedOperationException("TODO 1 : implementer stillAbstract()");
    }

    public static String classError(String className, boolean isAbstract, List<String> inherited, List<String> implemented) {
        throw new UnsupportedOperationException("TODO 2 : implementer classError()");
    }

    public static String modifierError(String target, List<String> modifiers, boolean hasBody) {
        throw new UnsupportedOperationException("TODO 3 : implementer modifierError()");
    }

    public static String instantiationError(String className, boolean isAbstract) {
        throw new UnsupportedOperationException("TODO 4 : implementer instantiationError()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("stillAbstract : [a() in S] et []",
                stillAbstract(List.of("a() in S", "b() in T"), List.of("b()")).equals(List.of("a() in S"))
                        && stillAbstract(List.of("area() in S"), List.of("area()")).isEmpty());

        ExerciseChecker.check("classError == javac sur 5 cas",
                classError("Ci", false, List.of("area() in S"), List.of()).equals("Ci is not abstract and does not override abstract method area() in S")
                        && classError("B", false, List.of("fly() in Fl"), List.of()).equals("B is not abstract and does not override abstract method fly() in Fl")
                        && classError("U", false, List.of("a() in S", "b() in T"), List.of("b()")).equals("U is not abstract and does not override abstract method a() in S")
                        && classError("R", true, List.of("area() in S"), List.of()).equals("OK")
                        && classError("Ci", false, List.of("area() in S"), List.of("area()")).equals("OK"));

        String[][] modifierVerdicts = {
                {"class", "abstract final", "false", "illegal combination of modifiers: abstract and final"},
                {"method", "abstract final", "false", "illegal combination of modifiers: abstract and final"},
                {"method", "abstract private", "false", "illegal combination of modifiers: abstract and private"},
                {"method", "abstract static", "false", "illegal combination of modifiers: abstract and static"},
                {"method", "abstract", "true", "abstract methods cannot have a body"},
                {"constructor", "abstract", "true", "modifier abstract not allowed here"},
                {"field", "abstract", "false", "modifier abstract not allowed here"},
                {"method", "public abstract", "false", "OK"},
                {"class", "abstract", "false", "OK"}};
        int agree = 0;
        for (String[] v : modifierVerdicts) {
            if (modifierError(v[0], List.of(v[1].split(" ")), Boolean.parseBoolean(v[2])).equals(v[3])) {
                agree++;
            }
        }
        ExerciseChecker.check("modifierError == javac sur 9 cas (" + agree + " d'accord)", agree == 9);

        ExerciseChecker.check("instantiationError : S abstraite, K concrete",
                instantiationError("S", true).equals("S is abstract; cannot be instantiated") && instantiationError("K", false).equals("OK"));

        ExerciseChecker.summary();
    }
}
