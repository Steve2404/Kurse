package ch1_buildingblocks.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise11_VarKeyword.
 */
public class Solution11_VarKeyword {

    public static String inferredTypeNames() {
        // Le type est deduit du litteral a la compilation : 5L -> long, 5.0f -> float,
        // 'x' -> char, et int + long -> long (promotion numerique).
        var a = 5;
        var b = 5L;
        var c = 5.0f;
        var d = 'x';
        var e = 1 + 2L;
        var f = "x";
        return typeName(a) + " " + typeName(b) + " " + typeName(c) + " "
                + typeName(d) + " " + typeName(e) + " " + typeName(f);
    }

    private static String typeName(Object value) {
        // Passer un primitif a un parametre Object le met en boite : int -> Integer, etc.
        return value.getClass().getSimpleName();
    }

    public static int sumWithVar(int[] values) {
        // v est un int (type des elements du tableau) ; total est un int (valeur 0).
        var total = 0;
        for (var v : values) {
            total += v;
        }
        return total;
    }

    public static String varNamedVar() {
        // var est un "nom de type reserve", pas un mot-cle : il reste utilisable comme nom.
        var var = "var";
        return var + var.length();
    }

    public static List<Object> diamondWithVar() {
        // Un <> vide ne donne aucun indice : var choisit ArrayList<Object>, qui accepte tout.
        var list = new ArrayList<>();
        list.add(1);
        list.add("a");
        return list;
    }
}
