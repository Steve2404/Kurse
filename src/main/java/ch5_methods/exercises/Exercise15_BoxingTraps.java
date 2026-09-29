package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 15 - Les pieges de l'autoboxing : ==, cache, remove(int), Long.equals, null et le ternaire (niveau : difficile)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   Integer a = 127, b = 127 ; a == b          -> true   (cache de -128 a 127)
 *   Integer a = 128, b = 128 ; a == b          -> false  (deux objets differents) ; a.equals(b) -> true
 *   Long.valueOf(1).equals(1)                  -> false  (1 devient un Integer, pas un Long !)
 *   Long.valueOf(1).equals(1L)                 -> true
 *   liste [10, 20, 1] : remove(1)              -> [10, 1]   (supprime l'INDEX 1)
 *   liste [10, 20, 1] : remove(Integer.valueOf(1)) -> [10, 20] (supprime la VALEUR 1)
 *   Integer n = null ; int r = flag ? n : 0;   -> NullPointerException (le ternaire est de type int)
 *   Integer n = null ; Integer r = flag ? n : 0; -> NullPointerException aussi !
 *   Integer n = null ; n + 1                   -> NullPointerException
 *   Integer.parseInt("4 2")                    -> NumberFormatException
 *
 *   Compilation (javac 17) :
 *   Long z = 5;              -> error: incompatible types: int cannot be converted to Long
 *   Integer a = 5; Long b = a; -> error: incompatible types: Integer cannot be converted to Long
 *   long l = Integer.valueOf(5);   -> compile (deballer PUIS elargir)
 *   int i = Long.valueOf(5);       -> error: incompatible types: Long cannot be converted to int
 *
 *
 * ==================================================================
 * TODO 1 : sameValue(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * == sur deux Integer compare les ADRESSES : vrai pour les petits
 * nombres (cache), faux pour 1000. On veut comparer les VALEURS, sans
 * exploser si l'un est null.
 *
 * -- Essayons a la main --
 *
 *   (1000, 1000) -> true ; (null, null) -> true ; (null, 5) -> false
 *
 * -- Le plan --
 *
 *   1. Si a == null, rendre b == null ; sinon a.equals(b).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (java.util.Objects.equals(a, b) fait la meme chose.)
 *
 *
 * ==================================================================
 * TODO 2 : removeValue(list, value)    et    TODO 3 : removeAt(list, index)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * List<Integer> a DEUX remove : remove(int index) et remove(Object o).
 * Avec un int, Java choisit remove(int) (pas besoin de boxing). Pour
 * supprimer une VALEUR, il faut lui donner un Integer.
 *
 * -- Le plan --
 *
 *   1. removeValue : list.remove(Integer.valueOf(value)).
 *   2. removeAt    : list.remove(index).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : sumIgnoringNulls(values)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [3, null, 4, null] -> 7   (total += v sur un null lancerait NullPointerException)
 *
 * -- Le plan --
 *
 *   1. for-each ; si v != null, total += v.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : orDefault(value, fallback)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut value, ou fallback si value est null. Piege : un ternaire
 * "value != null ? value : fallback" est de type int, et deballe value...
 * seulement dans la branche choisie : c'est sur ici. Ce qui explose,
 * c'est "flag ? value : 0" quand value est null ET choisi.
 *
 * -- Le plan --
 *
 *   1. Rendre value == null ? fallback : value.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : countEqual(values, target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On compte les Long egaux a target (un long). v.equals(1) serait
 * toujours faux (Integer contre Long). Comparer v == target deballe v
 * en long : c'est juste (et v ne doit pas etre null).
 *
 * -- Essayons a la main --
 *
 *   [1L, 2L, 1L, null], target 1 -> 2
 *
 * -- Le plan --
 *
 *   1. Pour chaque v : si v != null && v == target, compter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : parseOrNull(text)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "42" -> 42 ; "4 2" -> null ; null -> null
 *
 * -- Le plan --
 *
 *   1. try { return Integer.valueOf(text); } catch (NumberFormatException e) { return null; }
 *      (Integer.valueOf(null) lance aussi NumberFormatException.)
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
 *   - Integer.valueOf(int) rend un Integer ; Integer.parseInt(String) rend un int.
 */
public class Exercise15_BoxingTraps {

    public static boolean sameValue(Integer a, Integer b) {
        throw new UnsupportedOperationException("TODO 1 : implementer sameValue()");
    }

    public static void removeValue(List<Integer> list, int value) {
        throw new UnsupportedOperationException("TODO 2 : implementer removeValue()");
    }

    public static void removeAt(List<Integer> list, int index) {
        throw new UnsupportedOperationException("TODO 3 : implementer removeAt()");
    }

    public static int sumIgnoringNulls(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 4 : implementer sumIgnoringNulls()");
    }

    public static int orDefault(Integer value, int fallback) {
        throw new UnsupportedOperationException("TODO 5 : implementer orDefault()");
    }

    public static int countEqual(List<Long> values, long target) {
        throw new UnsupportedOperationException("TODO 6 : implementer countEqual()");
    }

    public static Integer parseOrNull(String text) {
        throw new UnsupportedOperationException("TODO 7 : implementer parseOrNull()");
    }

    public static void main(String[] args) {
        Integer big1 = 1000;
        Integer big2 = 1000;
        ExerciseChecker.check("(rappel) 1000 == 1000 entre Integer est FAUX", big1 != big2);
        ExerciseChecker.check("sameValue : 1000/1000, null/null oui ; null/5 non",
                sameValue(big1, big2) && sameValue(null, null) && !sameValue(null, 5) && !sameValue(5, null));

        List<Integer> byValue = new ArrayList<>(List.of(10, 20, 1));
        removeValue(byValue, 1);
        List<Integer> byIndex = new ArrayList<>(List.of(10, 20, 1));
        removeAt(byIndex, 1);
        ExerciseChecker.check("removeValue -> [10, 20] ; removeAt -> [10, 1]",
                byValue.equals(List.of(10, 20)) && byIndex.equals(List.of(10, 1)));

        ExerciseChecker.check("sumIgnoringNulls([3, null, 4, null]) == 7", sumIgnoringNulls(Arrays.asList(3, null, 4, null)) == 7);
        ExerciseChecker.check("orDefault : 8 et -1", orDefault(8, -1) == 8 && orDefault(null, -1) == -1);
        ExerciseChecker.check("(rappel) Long.valueOf(1).equals(1) est FAUX", !Long.valueOf(1).equals(1));
        ExerciseChecker.check("countEqual([1, 2, 1, null], 1) == 2", countEqual(Arrays.asList(1L, 2L, 1L, null), 1) == 2);
        ExerciseChecker.check("parseOrNull : 42, null, null",
                Integer.valueOf(42).equals(parseOrNull("42")) && parseOrNull("4 2") == null && parseOrNull(null) == null);

        ExerciseChecker.summary();
    }
}
