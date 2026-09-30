package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

/**
 * EXERCICE 8 - Les champs final ("blank finals") : la regle d'affectation unique, comparee a 18 verdicts de javac (niveau : difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un champ final doit recevoir sa valeur EXACTEMENT UNE FOIS, sur
 * CHAQUE chemin de construction : soit a sa declaration, soit dans un
 * bloc d'instance, soit dans CHAQUE constructeur. Jamais zero fois,
 * jamais deux fois. Et javac raisonne sur le CODE, pas sur les
 * intentions : un if sans else peut sauter l'affectation.
 *
 * -- Verdicts reels de javac 17 (champ : final int x) --
 *
 *   final int x = 5;                                  -> compile
 *   final int x;  (aucun constructeur ecrit)          -> error: variable x not initialized in the default constructor
 *   final int x;  F() { }                             -> error: variable x might not have been initialized
 *   final int x;  F() { x = 1; }  F(int v) { x = v; }  -> compile (chaque constructeur affecte)
 *   final int x;  F() { x = 1; }  F(int v) { }         -> error: variable x might not have been initialized
 *   final int x = 5;  F() { x = 10; }                  -> error: cannot assign a value to final variable x
 *   final int x;  F(boolean b) { if (b) { x = 1; } }   -> error: variable x might not have been initialized
 *   final int x;  F(boolean b) { if (b) { x = 1; } else { x = 2; } } -> compile
 *   final int x;  { x = 7; }                           -> compile (bloc d'instance)
 *   final int x;  { x = 7; }  F() { x = 8; }            -> error: variable x might already have been assigned
 *   final int x;  F() { x = 1; x = 2; }                 -> error: variable x might already have been assigned
 *   final int x;  F() { this(3); }  F(int v) { x = v; } -> compile (la delegation compte)
 *   final int x;  F() { this(3); x = 4; }  F(int v) { x = v; } -> error: variable x might already have been assigned
 *   final int x;  F() { System.out.println(x); x = 1; } -> error: variable x might not have been initialized
 *   static final int X;  (rien)                         -> error: variable X not initialized in the default constructor
 *   static final int X;  static { X = 1; }              -> compile
 *   static final int X;  F() { X = 1; }                 -> error: cannot assign a value to final variable X
 *   void m(final int p) { p = 2; }                      -> error: final parameter p may not be assigned
 *
 *
 * ==================================================================
 * TODO 1 : check(hasInitializer, assignedInBlock, constructors...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque constructeur ecrit est decrit par un mot :
 *
 *   "none"            : ne touche pas x
 *   "assign"          : x = ... une fois
 *   "assign twice"    : x = ... deux fois
 *   "if"              : x = ... seulement dans un if sans else
 *   "if-else"         : x = ... dans les deux branches
 *   "delegate"        : this(...) vers un autre constructeur, rien d'autre
 *   "delegate+assign" : this(...) puis x = ...
 *   "read-then-assign": lit x, puis x = ...
 *
 * Aucun mot = aucun constructeur ecrit (constructeur par defaut). On rend
 * le message exact de javac, ou "OK".
 *
 * -- Le plan --
 *
 *   1. before = (hasInitializer ? 1 : 0) + (assignedInBlock ? 1 : 0).
 *   2. Aucun constructeur : before == 0 -> NOT_INIT_DEFAULT ; sinon "OK".
 *   3. Pour chaque constructeur (le premier probleme gagne) :
 *        "delegate"        -> rien a verifier ici ;
 *        "delegate+assign" -> ALREADY ;
 *        si before == 1 et le constructeur affecte x (assign, assign twice, if, if-else, read-then-assign) :
 *            hasInitializer -> CANNOT_ASSIGN, sinon ALREADY ;
 *        si before == 0 : "none" ou "if" ou "read-then-assign" -> MIGHT_NOT ; "assign twice" -> ALREADY.
 *   4. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : assigns(kind) (le constructeur ecrit-il x lui-meme ?).
 *
 *
 * ==================================================================
 * TODO 2 : checkStatic(hasInitializer, assignedInStaticBlock, assignedInConstructor)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un static final appartient a la classe : il doit etre affecte UNE fois,
 * a la declaration ou dans un bloc static. Un constructeur (qui tourne a
 * chaque new) n'a jamais le droit d'y toucher.
 *
 * -- Le plan --
 *
 *   1. assignedInConstructor -> STATIC_CANNOT_ASSIGN.
 *   2. Ni initialiseur ni bloc static -> STATIC_NOT_INIT.
 *   3. Sinon "OK".
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
 *   - Utilise les constantes deja declarees (NOT_INIT_DEFAULT, MIGHT_NOT...) pour ne pas faire de faute de frappe.
 */
public class Exercise08_FinalFieldsRules {

    public static final String NOT_INIT_DEFAULT = "variable x not initialized in the default constructor";
    public static final String MIGHT_NOT = "variable x might not have been initialized";
    public static final String ALREADY = "variable x might already have been assigned";
    public static final String CANNOT_ASSIGN = "cannot assign a value to final variable x";
    public static final String STATIC_NOT_INIT = "variable X not initialized in the default constructor";
    public static final String STATIC_CANNOT_ASSIGN = "cannot assign a value to final variable X";

    public static String check(boolean hasInitializer, boolean assignedInBlock, String... constructors) {
        throw new UnsupportedOperationException("TODO 1 : implementer check()");
    }

    public static String checkStatic(boolean hasInitializer, boolean assignedInStaticBlock, boolean assignedInConstructor) {
        throw new UnsupportedOperationException("TODO 2 : implementer checkStatic()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : {initialiseur, bloc, constructeurs, verdict}.
        Object[][] javac = {
                {true, false, new String[] {}, "OK"},
                {false, false, new String[] {}, NOT_INIT_DEFAULT},
                {false, false, new String[] {"none"}, MIGHT_NOT},
                {false, false, new String[] {"assign", "assign"}, "OK"},
                {false, false, new String[] {"assign", "none"}, MIGHT_NOT},
                {true, false, new String[] {"assign"}, CANNOT_ASSIGN},
                {false, false, new String[] {"if"}, MIGHT_NOT},
                {false, false, new String[] {"if-else"}, "OK"},
                {false, true, new String[] {}, "OK"},
                {false, true, new String[] {"assign"}, ALREADY},
                {false, false, new String[] {"assign twice"}, ALREADY},
                {false, false, new String[] {"delegate", "assign"}, "OK"},
                {false, false, new String[] {"delegate+assign", "assign"}, ALREADY},
                {false, false, new String[] {"read-then-assign"}, MIGHT_NOT}};
        int agree = 0;
        for (Object[] v : javac) {
            String mine = check((Boolean) v[0], (Boolean) v[1], (String[]) v[2]);
            if (mine.equals(v[3])) {
                agree++;
            } else {
                System.out.println("   desaccord : " + String.join(", ", (String[]) v[2]) + " -> attendu " + v[3] + ", obtenu " + mine);
            }
        }
        ExerciseChecker.check("check() == javac sur 14 cas (" + agree + " d'accord)", agree == 14);

        ExerciseChecker.check("checkStatic : 4 cas reels",
                checkStatic(true, false, false).equals("OK") && checkStatic(false, false, false).equals(STATIC_NOT_INIT)
                        && checkStatic(false, true, false).equals("OK") && checkStatic(false, false, true).equals(STATIC_CANNOT_ASSIGN));

        ExerciseChecker.summary();
    }
}
