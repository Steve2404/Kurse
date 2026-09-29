package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 11 - var : le compilateur devine le type UNE fois, puis il est fige (niveau : moyen/difficile)
 * ======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- La regle --
 *
 * "var x = 5;" veut dire : "compilateur, regarde la valeur et ecris le
 * type a ma place". C'est EXACTEMENT "int x = 5;". Le type est choisi
 * a la compilation, a partir de la valeur de depart, et ne change
 * plus jamais. var n'est PAS "n'importe quel type".
 *
 * Ce qui est INTERDIT, messages REELS de javac 17 :
 *
 *   var x = 5; x = "hello";   -> incompatible types: String cannot be converted to int
 *   var x = null;             -> cannot infer type for local variable x
 *   var x;  (puis x = 5;)     -> cannot infer type for local variable x
 *   var a = 1, b = 2;         -> 'var' is not allowed in a compound declaration
 *   var arr = {1, 2};         -> cannot infer type for local variable arr
 *   var[] arr = new int[2];   -> 'var' is not allowed as an element type of an array
 *   var sur un champ ou un parametre de methode -> 'var' is not allowed here
 *
 * Et ce qui SURPREND mais COMPILE : "var var = "var";" - var n'est pas
 * un vrai mot reserve, juste un "nom de type reserve" (voir Exercise07).
 *
 *
 * ==================================================================
 * TODO 1 : inferredTypeNames()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le compilateur lit le litteral comme un detective : 5 -> int, 5L ->
 * long, 5.0f -> float, 'x' -> char, "x" -> String. Et 1 + 2L ? Un int
 * plus un long donne un long (voir le chapitre 2).
 *
 * -- Essayons a la main --
 *
 *   var a = 5;    -> int       (mis en boite : Integer)
 *   var b = 5L;   -> long      (Long)
 *   var c = 5.0f; -> float     (Float)
 *   var d = 'x';  -> char      (Character)
 *   var e = 1 + 2L; -> long    (Long)
 *   var f = "x";  -> String
 *   -> "Integer Long Float Character Long String"
 *
 * -- Le plan --
 *
 *   1. Declarer les 6 variables avec var, exactement comme ci-dessus.
 *   2. Pour chacune, obtenir le nom simple de la classe de sa valeur
 *      (en la passant par Object pour la mettre en boite).
 *   3. Les assembler separees par un espace.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : "nom simple du type d'une valeur" revient 6 fois. Ecris une
 * petite methode privee typeName(Object value).
 *
 *
 * ==================================================================
 * TODO 2 : sumWithVar(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * var marche aussi dans les boucles : "for (var v : values)" - v est
 * un int, puisque values est un int[].
 *
 * -- Essayons a la main --
 *
 *   [3, 4, 5] -> 12 ; [] -> 0
 *
 * -- Le plan --
 *
 *   1. var total = 0 ; for (var v : values) total += v ; rendre total.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : varNamedVar()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * var n'est pas un vrai mot reserve : on peut appeler une variable
 * "var". C'est un piege classique de l'examen.
 *
 * -- Le plan --
 *
 *   1. Declarer : var var = "var";
 *   2. Rendre var + var.length()  -> "var3".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : diamondWithVar()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "var list = new ArrayList<>();" : le <> vide ne donne AUCUN indice
 * sur le contenu, alors le compilateur choisit le plus general :
 * ArrayList<Object>. On peut donc y mettre un 1 ET un "a".
 *
 * -- Essayons a la main --
 *
 *   ajouter 1 puis "a" -> [1, a]
 *
 * -- Le plan --
 *
 *   1. var list = new ArrayList<>();
 *   2. Ajouter 1, puis "a".
 *   3. Rendre la liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - private static String typeName(Object value) { return value.getClass().getSimpleName(); }
 *   - typeName(a) : passer un int a un parametre Object le met en boite (Integer)
 *   - for (var v : values) { total += v; }
 */
public class Exercise11_VarKeyword {

    public static String inferredTypeNames() {
        throw new UnsupportedOperationException("TODO 1 : implementer inferredTypeNames()");
    }

    public static int sumWithVar(int[] values) {
        throw new UnsupportedOperationException("TODO 2 : implementer sumWithVar()");
    }

    public static String varNamedVar() {
        throw new UnsupportedOperationException("TODO 3 : implementer varNamedVar()");
    }

    public static List<Object> diamondWithVar() {
        throw new UnsupportedOperationException("TODO 4 : implementer diamondWithVar()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 inferredTypeNames() == \"Integer Long Float Character Long String\"",
                inferredTypeNames().equals("Integer Long Float Character Long String"));
        ExerciseChecker.check("2 sumWithVar([3, 4, 5]) == 12, ([]) == 0",
                sumWithVar(new int[]{3, 4, 5}) == 12 && sumWithVar(new int[0]) == 0);
        ExerciseChecker.check("3 varNamedVar() == \"var3\" (var est un nom de variable permis)",
                varNamedVar().equals("var3"));
        List<Object> mixed = diamondWithVar();
        ExerciseChecker.check("4 diamondWithVar() == [1, a] (ArrayList<Object>)",
                mixed.toString().equals("[1, a]") && mixed.get(0) instanceof Integer && mixed.get(1) instanceof String);

        ExerciseChecker.summary();
    }
}
