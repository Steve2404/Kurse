package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 8 - Les regles du switch expression : exhaustivite, yield, throw, et les 2 syntaxes (niveau : difficile)
 * ===============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Verdicts REELS de javac 17 --
 *
 *   switch expression sur un int SANS default
 *     -> error: the switch expression does not cover all possible input values
 *   enum dont une constante manque, sans default -> meme erreur
 *   enum dont TOUTES les constantes sont listees -> COMPILE sans default
 *   oubli du ; final apres "return switch (...) { ... }"  -> error: ';' expected
 *   melange de "case 1 ->" et "case 2:" dans le meme switch
 *     -> error: different case kinds used in the switch
 *   bloc { } apres une fleche qui ne finit ni par yield ni par throw
 *     -> error: switch rule completes without providing a value
 *   switch expression ECRIT AVEC ":" et yield dans chaque case -> COMPILE
 *   switch STATEMENT avec fleches et sans default -> COMPILE (rien n'est rendu)
 *
 * Un switch EXPRESSION rend une valeur : il doit en rendre une pour
 * TOUTE entree possible (sinon, quelle valeur rendre ?). D'ou
 * l'exhaustivite obligatoire, que le switch statement n'exige pas.
 *
 *
 * ==================================================================
 * TODO 1 : sizeLabel(size)   [enum complet, SANS default]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un enum est une liste fermee : si on a une case pour chacune de ses
 * valeurs, le compilateur SAIT que tout est couvert.
 *
 * -- Essayons a la main --
 *
 *   S -> "petit" ; M -> "moyen" ; L -> "grand"
 *
 * -- Le plan --
 *
 *   1. return switch (size) { case S -> ... ; case M -> ... ; case L -> ... ; };
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : dayKind(day)   [int : default obligatoire, plusieurs valeurs par case]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   1 a 5 -> "semaine" ; 6, 7 -> "week-end" ; 0 ou 8 -> "invalide"
 *
 * -- Le plan --
 *
 *   1. case 1, 2, 3, 4, 5 -> ... ; case 6, 7 -> ... ; default -> ...
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : shippingCost(size, express)   [bloc + yield]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand une case a besoin de plusieurs lignes, on ouvre un bloc { } ;
 * yield y joue le role du return : "voici la valeur de ce case".
 *
 * -- Essayons a la main --
 *
 *   S -> 3 ; M -> 5 ; L -> 10, et le double (20) si express
 *
 * -- Le plan --
 *
 *   1. case S -> 3 ; case M -> 5 ;
 *      case L -> { int base = 10; yield express ? base * 2 : base; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : colonStyle(x)   [switch expression ecrit avec ":" et yield]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La forme avec ":" existe aussi pour un switch expression : chaque
 * case rend sa valeur avec yield. (Attention, avec ":" les case
 * coulent vers le bas s'il n'y a pas de yield.)
 *
 * -- Essayons a la main --
 *
 *   1 -> 10 ; 2 -> 20 ; autre -> 0
 *
 * -- Le plan --
 *
 *   1. return switch (x) { case 1: yield 10; case 2: yield 20; default: yield 0; };
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : daysInMonth(month)   [throw a la place d'une valeur]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour une entree impossible, un case peut lancer une exception au lieu
 * de rendre une valeur : l'exhaustivite est respectee.
 *
 * -- Essayons a la main --
 *
 *   2 -> 28 ; 4, 6, 9, 11 -> 30 ; les autres de 1 a 12 -> 31 ;
 *   13 -> IllegalArgumentException("mois invalide : 13")
 *
 * -- Le plan --
 *
 *   1. case 2 -> 28 ; case 4, 6, 9, 11 -> 30 ; case 1, 3, 5, 7, 8, 10, 12 -> 31 ;
 *      default -> throw new IllegalArgumentException(...).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 */
public class Exercise08_SwitchExpressionRules {

    public enum Size { S, M, L }

    public static String sizeLabel(Size size) {
        throw new UnsupportedOperationException("TODO 1 : implementer sizeLabel()");
    }

    public static String dayKind(int day) {
        throw new UnsupportedOperationException("TODO 2 : implementer dayKind()");
    }

    public static int shippingCost(Size size, boolean express) {
        throw new UnsupportedOperationException("TODO 3 : implementer shippingCost()");
    }

    public static int colonStyle(int x) {
        throw new UnsupportedOperationException("TODO 4 : implementer colonStyle()");
    }

    public static int daysInMonth(int month) {
        throw new UnsupportedOperationException("TODO 5 : implementer daysInMonth()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 sizeLabel : S petit, M moyen, L grand",
                sizeLabel(Size.S).equals("petit") && sizeLabel(Size.M).equals("moyen") && sizeLabel(Size.L).equals("grand"));
        ExerciseChecker.check("2 dayKind : 3 semaine, 7 week-end, 8 invalide",
                dayKind(3).equals("semaine") && dayKind(7).equals("week-end") && dayKind(8).equals("invalide"));
        ExerciseChecker.check("3 shippingCost : S 3, M 5, L 10, L express 20",
                shippingCost(Size.S, true) == 3 && shippingCost(Size.M, false) == 5
                        && shippingCost(Size.L, false) == 10 && shippingCost(Size.L, true) == 20);
        ExerciseChecker.check("4 colonStyle : 1 -> 10, 2 -> 20, 5 -> 0",
                colonStyle(1) == 10 && colonStyle(2) == 20 && colonStyle(5) == 0);
        String message = null;
        try {
            daysInMonth(13);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("5 daysInMonth : 2 -> 28, 4 -> 30, 12 -> 31, 13 -> exception \"mois invalide : 13\"",
                daysInMonth(2) == 28 && daysInMonth(4) == 30 && daysInMonth(12) == 31 && "mois invalide : 13".equals(message));

        ExerciseChecker.summary();
    }
}
