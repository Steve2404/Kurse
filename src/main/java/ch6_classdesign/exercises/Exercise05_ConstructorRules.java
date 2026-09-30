package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.List;
import java.util.Map;

/**
 * EXERCICE 5 - Les regles des constructeurs ecrites par toi, comparees aux verdicts reels de javac (niveau : difficile)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tout constructeur d'un enfant commence par construire le parent. Si
 * on n'ecrit ni this(...) ni super(...) en PREMIERE ligne, javac ajoute
 * tout seul un super() SANS argument. Et si on n'ecrit AUCUN
 * constructeur, javac ajoute un constructeur par defaut sans argument
 * (qui contient ce super()). Tu ecris ces regles ; main() compare avec
 * javac.
 *
 * -- Verdicts reels de javac 17 (parent P, enfant K) --
 *
 *   P(int x) seul, K sans constructeur           -> error: constructor P in class P cannot be applied to given types;
 *   P(int x) seul, K() { }                       -> meme erreur (le super() cache ne trouve pas P())
 *   P(int x) seul, K() { super(); }              -> meme erreur
 *   P sans constructeur ecrit, K() { super(1); } -> meme erreur (seul P() existe)
 *   P(int x) seul, K() { super(42); }            -> compile
 *   F() { System.out.println(); this(1); }       -> error: call to this must be first statement in constructor
 *   F() { System.out.println(); super(); }       -> error: call to super must be first statement in constructor
 *   F() { this(1); super(); }                    -> error: call to super must be first statement in constructor
 *   F() { this(1); }  F(int x) { this(); }       -> error: recursive constructor invocation
 *   F() -> F(int) -> F(String) -> F()            -> error: recursive constructor invocation (le cycle peut etre long)
 *   class K { K(int x) {} } puis new K()         -> error: constructor K in class K cannot be applied to given types;
 *   K() { super(value()); } avec value() d'instance -> error: cannot reference this before supertype constructor has been called
 *   (avec value() static : compile)
 *   void F() {} dans la classe F                 -> compile... mais c'est une METHODE, pas un constructeur !
 *
 *
 * ==================================================================
 * TODO 1 : check(parentConstructors, body...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On recoit les signatures des constructeurs ECRITS dans le parent P
 * ("()" ou "(int)" ; liste vide = aucun ecrit, donc P() existe par
 * defaut) et les instructions du constructeur de l'enfant. On rend le
 * message exact de javac, ou "OK".
 *
 * -- Essayons a la main --
 *
 *   (["(int)"], [])                   -> implicite super() -> "()" absent -> "constructor P in class P cannot be applied to given types;"
 *   (["(int)"], ["super(42)"])        -> "(int)" present -> "OK"
 *   ([], ["println", "this(1)"])      -> "call to this must be first statement in constructor"
 *   ([], ["this(1)", "super()"])      -> "call to super must be first statement in constructor"
 *
 * -- Le plan --
 *
 *   1. Pour chaque instruction d'index >= 1 : si elle commence par "this(" ou "super(", rendre
 *      "call to this must be..." ou "call to super must..." selon le mot.
 *   2. Si la 1re instruction commence par "this(" -> "OK" (c'est l'autre constructeur qui appellera super).
 *   3. Signature appelee : "()" si pas d'instruction super(...) en 1re ligne ou si super() est vide,
 *      "(int)" sinon.
 *   4. Constructeurs disponibles du parent : ["()"] si la liste est vide, sinon la liste.
 *   5. Signature absente -> "constructor P in class P cannot be applied to given types;" ; sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : superSignature(firstStatement).
 *
 *
 * ==================================================================
 * TODO 2 : isRecursive(delegations)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La Map dit, pour chaque constructeur, vers quel autre il delegue avec
 * this(...) (ou null s'il ne delegue pas). Si en suivant les fleches on
 * retombe sur un constructeur deja visite, c'est un cycle.
 *
 * -- Essayons a la main --
 *
 *   {"()" -> "(int)", "(int)" -> "()"}                            -> true
 *   {"()" -> "(int)", "(int)" -> "(String)", "(String)" -> "()"}  -> true
 *   {"()" -> "(int)", "(int)" -> null}                            -> false
 *   {"()" -> "()"}                                                -> true (this() dans F())
 *
 * -- Le plan --
 *
 *   1. Pour chaque depart : suivre les fleches au plus delegations.size() fois ;
 *      si on revient au depart -> true.
 *   2. Sinon false.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : canCallNoArg(writtenConstructors)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "new K()" marche si K a un constructeur sans argument : soit ecrit,
 * soit le constructeur par defaut, que javac n'ajoute QUE si aucun
 * constructeur n'est ecrit.
 *
 * -- Essayons a la main --
 *
 *   []            -> true ;  ["(int)"] -> false ;  ["(int)", "()"] -> true
 *
 * -- Le plan --
 *
 *   1. Rendre writtenConstructors.isEmpty() || writtenConstructors.contains("()").
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
 *   - statement.startsWith("super(") ; statement.equals("super()").
 *   - delegations.get(courant) rend la cible (ou null).
 */
public class Exercise05_ConstructorRules {

    public static final String NO_MATCH = "constructor P in class P cannot be applied to given types;";
    public static final String THIS_NOT_FIRST = "call to this must be first statement in constructor";
    public static final String SUPER_NOT_FIRST = "call to super must be first statement in constructor";

    public static String check(List<String> parentConstructors, String... body) {
        throw new UnsupportedOperationException("TODO 1 : implementer check()");
    }

    public static boolean isRecursive(Map<String, String> delegations) {
        throw new UnsupportedOperationException("TODO 2 : implementer isRecursive()");
    }

    public static boolean canCallNoArg(List<String> writtenConstructors) {
        throw new UnsupportedOperationException("TODO 3 : implementer canCallNoArg()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : {constructeurs ecrits du parent, instructions de l'enfant, verdict}.
        Object[][] javac = {
                {List.of("(int)"), new String[] {}, NO_MATCH},
                {List.of("(int)"), new String[] {"println"}, NO_MATCH},
                {List.of("(int)"), new String[] {"super()"}, NO_MATCH},
                {List.of(), new String[] {"super(1)"}, NO_MATCH},
                {List.of("(int)"), new String[] {"super(42)"}, "OK"},
                {List.of(), new String[] {"println", "this(1)"}, THIS_NOT_FIRST},
                {List.of(), new String[] {"println", "super()"}, SUPER_NOT_FIRST},
                {List.of(), new String[] {"this(1)", "super()"}, SUPER_NOT_FIRST},
                {List.of(), new String[] {"this(1)"}, "OK"},
                {List.of(), new String[] {}, "OK"}};
        int agree = 0;
        for (Object[] v : javac) {
            @SuppressWarnings("unchecked")
            List<String> parent = (List<String>) v[0];
            if (check(parent, (String[]) v[1]).equals(v[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("check() == javac sur 10 cas (" + agree + " d'accord)", agree == 10);

        ExerciseChecker.check("isRecursive : 4 cas",
                isRecursive(Map.of("()", "(int)", "(int)", "()"))
                        && isRecursive(Map.of("()", "(int)", "(int)", "(String)", "(String)", "()"))
                        && !isRecursive(java.util.Collections.singletonMap("()", null))
                        && isRecursive(Map.of("()", "()")));
        java.util.Map<String, String> chain = new java.util.HashMap<>();
        chain.put("()", "(int)");
        chain.put("(int)", null);
        ExerciseChecker.check("isRecursive : une chaine qui se termine n'est pas un cycle", !isRecursive(chain));

        ExerciseChecker.check("canCallNoArg : [] oui, [(int)] non, [(int), ()] oui",
                canCallNoArg(List.of()) && !canCallNoArg(List.of("(int)")) && canCallNoArg(List.of("(int)", "()")));

        ExerciseChecker.summary();
    }
}
