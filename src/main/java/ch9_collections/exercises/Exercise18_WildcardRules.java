package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.Map;

/**
 * EXERCICE 18 - Wildcards : add, get et affectation, ta regle comparee a 98 verdicts de javac (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quatre sortes d'etiquettes sur une boite (kind) :
 *
 *   "exact"    List<Number>            c'est EXACTEMENT une boite a Number
 *   "extends"  List<? extends Number>  une boite a Number OU a un sous-type (Integer ? Double ? on ne sait pas)
 *   "super"    List<? super Number>    une boite a Number OU a un super-type (Object ?)
 *   "?"        List<?>                 une boite a on ne sait pas quoi
 *
 * Les types du jeu : Object > Number > Integer, Double ; Object > String ;
 * et "null", qui va dans toutes les boites. isA(a, b) (deja ecrit, plus
 * bas) repond "un a est-il un b ?".
 *
 * PECS : Producer Extends (on LIT dedans), Consumer Super (on ECRIT dedans).
 *
 * -- Verdicts reels de javac 17 (extraits ; les 98 sont dans main) --
 *
 *   List<? extends Number> l; l.add(Integer.valueOf(5));  -> error: incompatible types (CAP#1)
 *   List<? extends Number> l; l.add(null);                -> compile
 *   List<? super Integer> l;  Integer r = l.get(0);       -> error: incompatible types: CAP#1 cannot be converted to Integer
 *   List<? super Integer> l;  Object r = l.get(0);        -> compile
 *   List<?> l; l.add("x");                                -> error
 *   List<Number> l = new ArrayList<Integer>();            -> error: incompatible types
 *   List<? super Integer> l = new ArrayList<Number>();    -> compile
 *   List<?> l = new ArrayList<String>();                  -> compile
 *
 *
 * ==================================================================
 * TODO 1 : canAdd(kind, bound, arg)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Peut-on faire l.add(une valeur de type arg) ?
 *
 * -- Essayons a la main --
 *
 *   ("exact", "Number", "Integer")   -> true
 *   ("extends", "Number", "Integer") -> false (et si c'etait une boite a Double ?)
 *   ("super", "Integer", "Number")   -> false (et si c'etait une boite a Integer ?)
 *   ("?", "Object", "null")          -> true
 *
 * -- Le plan --
 *
 *   1. arg "null" -> true.
 *   2. "exact" ou "super" -> isA(arg, bound).
 *   3. "extends" ou "?" -> false.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isA, deja fournie.
 *
 *
 * ==================================================================
 * TODO 2 : readType(kind, bound)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quel est le type le plus precis GARANTI pour ce qui sort de l.get(0) ?
 * main() en deduit si "T r = l.get(0);" compile : isA(readType, T).
 *
 * -- Essayons a la main --
 *
 *   ("extends", "Number") -> Number ; ("super", "Integer") -> Object ; ("?", "Object") -> Object
 *
 * -- Le plan --
 *
 *   1. "exact" ou "extends" -> bound.
 *   2. Sinon -> "Object".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : canAssign(kind, bound, source)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Est-ce que "List<...> l = new ArrayList<source>();" compile ?
 * Piege : List<Integer> n'est PAS une List<Number>, meme si un Integer
 * est un Number (sinon on pourrait y glisser un Double).
 *
 * -- Essayons a la main --
 *
 *   ("exact", "Number", "Integer")   -> false
 *   ("extends", "Number", "Double")  -> true
 *   ("super", "Integer", "Number")   -> true
 *   ("super", "Number", "Integer")   -> false
 *
 * -- Le plan --
 *
 *   1. "exact" -> source identique a bound.
 *   2. "extends" -> isA(source, bound) ; "super" -> isA(bound, source).
 *   3. "?" -> true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isA encore.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - switch (kind) { case "exact": ... }
 *   - Attention a comparer les String avec equals.
 */
public class Exercise18_WildcardRules {

    // Hierarchie du jeu : chaque type -> son parent direct.
    static final Map<String, String> PARENT = Map.of("Integer", "Number", "Double", "Number", "Number", "Object", "String", "Object");

    // "a est-il un b ?" : on remonte les parents de a. null est un sous-type de tout.
    public static boolean isA(String a, String b) {
        if (a.equals("null")) {
            return true;
        }
        for (String t = a; t != null; t = PARENT.get(t)) {
            if (t.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canAdd(String kind, String bound, String arg) {
        throw new UnsupportedOperationException("TODO 1 : implementer canAdd()");
    }

    public static String readType(String kind, String bound) {
        throw new UnsupportedOperationException("TODO 2 : implementer readType()");
    }

    public static boolean canAssign(String kind, String bound, String source) {
        throw new UnsupportedOperationException("TODO 3 : implementer canAssign()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : "operation kind bound type verdict".
        String[] javac = {
                "add exact Number Integer OK", "add exact Number Double OK", "add exact Number Number OK", "add exact Number Object ERR",
                "add exact Number String ERR", "add exact Number null OK", "read exact Number Object OK", "read exact Number Number OK",
                "read exact Number Integer ERR", "assign exact Number Object ERR", "assign exact Number Number OK", "assign exact Number Integer ERR",
                "assign exact Number Double ERR", "assign exact Number String ERR", "add exact Integer Integer OK", "add exact Integer Double ERR",
                "add exact Integer Number ERR", "add exact Integer Object ERR", "add exact Integer String ERR", "add exact Integer null OK",
                "read exact Integer Object OK", "read exact Integer Number OK", "read exact Integer Integer OK", "assign exact Integer Object ERR",
                "assign exact Integer Number ERR", "assign exact Integer Integer OK", "assign exact Integer Double ERR", "assign exact Integer String ERR",
                "add extends Number Integer ERR", "add extends Number Double ERR", "add extends Number Number ERR", "add extends Number Object ERR",
                "add extends Number String ERR", "add extends Number null OK", "read extends Number Object OK", "read extends Number Number OK",
                "read extends Number Integer ERR", "assign extends Number Object ERR", "assign extends Number Number OK", "assign extends Number Integer OK",
                "assign extends Number Double OK", "assign extends Number String ERR", "add extends Integer Integer ERR", "add extends Integer Double ERR",
                "add extends Integer Number ERR", "add extends Integer Object ERR", "add extends Integer String ERR", "add extends Integer null OK",
                "read extends Integer Object OK", "read extends Integer Number OK", "read extends Integer Integer OK", "assign extends Integer Object ERR",
                "assign extends Integer Number ERR", "assign extends Integer Integer OK", "assign extends Integer Double ERR", "assign extends Integer String ERR",
                "add super Number Integer OK", "add super Number Double OK", "add super Number Number OK", "add super Number Object ERR",
                "add super Number String ERR", "add super Number null OK", "read super Number Object OK", "read super Number Number ERR",
                "read super Number Integer ERR", "assign super Number Object OK", "assign super Number Number OK", "assign super Number Integer ERR",
                "assign super Number Double ERR", "assign super Number String ERR", "add super Integer Integer OK", "add super Integer Double ERR",
                "add super Integer Number ERR", "add super Integer Object ERR", "add super Integer String ERR", "add super Integer null OK",
                "read super Integer Object OK", "read super Integer Number ERR", "read super Integer Integer ERR", "assign super Integer Object OK",
                "assign super Integer Number OK", "assign super Integer Integer OK", "assign super Integer Double ERR", "assign super Integer String ERR",
                "add ? Object Integer ERR", "add ? Object Double ERR", "add ? Object Number ERR", "add ? Object Object ERR",
                "add ? Object String ERR", "add ? Object null OK", "read ? Object Object OK", "read ? Object Number ERR",
                "read ? Object Integer ERR", "assign ? Object Object OK", "assign ? Object Number OK", "assign ? Object Integer OK",
                "assign ? Object Double OK", "assign ? Object String OK"
        };
        int[] agree = new int[3];
        int[] total = new int[3];
        for (String line : javac) {
            String[] p = line.split(" ");
            boolean compiles = p[4].equals("OK");
            int which;
            boolean mine;
            if (p[0].equals("add")) {
                which = 0;
                mine = canAdd(p[1], p[2], p[3]);
            } else if (p[0].equals("read")) {
                which = 1;
                mine = isA(readType(p[1], p[2]), p[3]);
            } else {
                which = 2;
                mine = canAssign(p[1], p[2], p[3]);
            }
            total[which]++;
            if (mine == compiles) {
                agree[which]++;
            }
        }
        ExerciseChecker.check("canAdd == javac sur " + total[0] + " cas (" + agree[0] + " d'accord)", agree[0] == total[0]);
        ExerciseChecker.check("readType == javac sur " + total[1] + " cas (" + agree[1] + " d'accord)", agree[1] == total[1]);
        ExerciseChecker.check("canAssign == javac sur " + total[2] + " cas (" + agree[2] + " d'accord)", agree[2] == total[2]);

        ExerciseChecker.summary();
    }
}
