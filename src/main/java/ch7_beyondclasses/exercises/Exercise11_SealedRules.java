package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 11 - Les regles des types sealed, ecrites par toi et comparees a 19 verdicts reels de javac (niveau : difficile)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un type sealed dresse la liste FERMEE de ses enfants. Chaque enfant
 * doit dire ce que devient la lignee apres lui :
 *   final      : personne apres moi ;
 *   sealed     : une liste fermee continue (et j'ai au moins un enfant) ;
 *   non-sealed : n'importe qui peut m'etendre, comme avant.
 * Un record et un enum sont deja implicitement final. Une interface ne
 * peut pas etre final : elle choisit sealed ou non-sealed.
 *
 * -- Verdicts reels de javac 17 --
 *
 *   sealed class Shape {} + final class Circle extends Shape {} (meme fichier, sans permits) -> compile
 *   class Circle extends Shape {} (Shape sealed)         -> error: sealed, non-sealed or final modifiers expected
 *   non-sealed class Circle extends Shape {} + class Grand extends Circle {} -> compile
 *   sealed class Circle extends Shape {} (sans enfant)    -> error: sealed class must have subclasses
 *   Square extends Shape mais pas dans permits           -> error: class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)
 *   permits Circle, mais Circle n'etend pas Shape        -> error: invalid permits clause
 *   record R() implements S (S sealed interface)         -> compile ; enum E implements S -> compile
 *   record R() extends S                                 -> error: '{' expected (un record n'etend jamais de classe)
 *   sealed final class S                                 -> error: illegal combination of modifiers: final and sealed
 *   non-sealed class S (sans parent sealed)              -> error: non-sealed modifier not allowed here
 *   interface T extends S (S sealed)                     -> error: sealed or non-sealed modifiers expected
 *   non-sealed interface T extends S                     -> compile ; final interface T -> error: illegal combination of modifiers: interface and final
 *   Shape sealed sans permits, Circle dans UN AUTRE fichier -> error: sealed class must have subclasses
 *   Shape sealed permits Circle, Circle dans un autre fichier -> compile
 *   sealed class Shape {} sans aucun enfant              -> error: sealed class must have subclasses
 *   new S() { } (classe anonyme)                         -> error: anonymous classes must not extend sealed classes
 *   final class L extends S {} dans une methode          -> error: local classes must not extend sealed classes
 *
 *
 * ==================================================================
 * TODO 1 : childError(childKind, modifier)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * childKind : "class", "interface", "record", "enum", "anonymous" ou
 * "local". modifier : "final", "sealed", "non-sealed" ou "" (aucun).
 * On suppose l'enfant bien autorise par son parent sealed.
 *
 * -- Le plan --
 *
 *   1. "anonymous" -> "anonymous classes must not extend sealed classes" ; "local" -> "local classes must not extend sealed classes".
 *   2. "record" ou "enum" -> "OK".
 *   3. "interface" : "" -> "sealed or non-sealed modifiers expected" ; "final" -> "illegal combination of modifiers: interface and final".
 *   4. "class" : "" -> "sealed, non-sealed or final modifiers expected".
 *   5. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : parentError(hasPermits, childrenInSameFile, childListed, childExtends, hasChildren)
 * ==================================================================
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. !hasChildren -> "sealed class must have subclasses".
 *   2. !hasPermits && !childrenInSameFile -> "sealed class must have subclasses".
 *   3. hasPermits && !childListed -> "class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)".
 *   4. hasPermits && !childExtends -> "invalid permits clause".
 *   5. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : modifiersError(modifiers, parentIsSealed)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. "sealed" et "final" ensemble -> "illegal combination of modifiers: final and sealed".
 *   2. "non-sealed" sans parent sealed -> "non-sealed modifier not allowed here".
 *   3. "OK".
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
 *   - Un switch expression sur childKind rend le code tres court.
 */
public class Exercise11_SealedRules {

    public static String childError(String childKind, String modifier) {
        throw new UnsupportedOperationException("TODO 1 : implementer childError()");
    }

    public static String parentError(boolean hasPermits, boolean childrenInSameFile, boolean childListed,
                                     boolean childExtends, boolean hasChildren) {
        throw new UnsupportedOperationException("TODO 2 : implementer parentError()");
    }

    public static String modifiersError(List<String> modifiers, boolean parentIsSealed) {
        throw new UnsupportedOperationException("TODO 3 : implementer modifiersError()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : {sorte d'enfant, modificateur, verdict}.
        String[][] children = {
                {"class", "final", "OK"},
                {"class", "", "sealed, non-sealed or final modifiers expected"},
                {"class", "non-sealed", "OK"},
                {"record", "", "OK"},
                {"enum", "", "OK"},
                {"interface", "", "sealed or non-sealed modifiers expected"},
                {"interface", "non-sealed", "OK"},
                {"interface", "final", "illegal combination of modifiers: interface and final"},
                {"anonymous", "", "anonymous classes must not extend sealed classes"},
                {"local", "final", "local classes must not extend sealed classes"}};
        int agree = 0;
        for (String[] v : children) {
            if (childError(v[0], v[1]).equals(v[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("childError() == javac sur 10 cas (" + agree + " d'accord)", agree == 10);

        String notListed = "class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)";
        ExerciseChecker.check("parentError() == javac sur 6 cas",
                parentError(false, true, true, true, true).equals("OK")
                        && parentError(false, false, true, true, true).equals("sealed class must have subclasses")
                        && parentError(true, false, true, true, true).equals("OK")
                        && parentError(true, true, false, true, true).equals(notListed)
                        && parentError(true, true, true, false, true).equals("invalid permits clause")
                        && parentError(false, true, false, false, false).equals("sealed class must have subclasses"));

        ExerciseChecker.check("modifiersError() == javac sur 3 cas",
                modifiersError(List.of("sealed", "final"), false).equals("illegal combination of modifiers: final and sealed")
                        && modifiersError(List.of("non-sealed"), false).equals("non-sealed modifier not allowed here")
                        && modifiersError(List.of("non-sealed"), true).equals("OK"));

        ExerciseChecker.summary();
    }
}
