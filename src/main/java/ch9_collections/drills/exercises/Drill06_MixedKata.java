package ch9_collections.drills.exercises;

import ch9_collections.ExerciseChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DRILL 06 - Kata melange : tout le chapitre 9 sans indice de forme
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_ListAndSet. Ici, PAS de crochet : a toi
 * de choisir la collection et la methode. Fais ce drill seulement quand
 * les drills 01 a 05 passent.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : mostExpensive()           le nom du fruit le plus cher de Pantry.prices() -> cerise.
 * TODO 2  : cheaperThan(max)          les noms dont le prix est < max, tries -> cheaperThan(4) = [banane, kiwi, pomme].
 * TODO 3  : duplicates()              les fruits presents plus d'une fois dans FRUITS, tries -> [kiwi, pomme].
 * TODO 4  : firstUnique()             le premier fruit de FRUITS qui n'apparait qu'une fois -> banane.
 * TODO 5  : topTwo()                  les 2 plus grands de NUMBERS, du plus grand au plus petit -> [9, 8].
 * TODO 6  : basketTotal(basket)       somme des prix du panier ; un fruit inconnu coute 0.
 * TODO 7  : replay(actions)           chaque action est un mot a empiler, ou "undo" qui annule le dernier ; rendre ce qui reste, du plus ancien au plus recent.
 * TODO 8  : priceBands()              {bas=[prix <= 2], haut=[les autres]}, dans l'ordre de prices(), cles triees.
 * TODO 9  : rankByPrice(fruit)        la place (a partir de 1) du fruit quand on trie par prix croissant -> kiwi = 3.
 * TODO 10 : mergeBaskets(a, b)        additionner les quantites de deux paniers, cles triees.
 */
public class Drill06_MixedKata {

    public static String mostExpensive() {
        throw new UnsupportedOperationException("TODO 1 : implementer mostExpensive()");
    }

    public static Set<String> cheaperThan(int max) {
        throw new UnsupportedOperationException("TODO 2 : implementer cheaperThan()");
    }

    public static Set<String> duplicates() {
        throw new UnsupportedOperationException("TODO 3 : implementer duplicates()");
    }

    public static String firstUnique() {
        throw new UnsupportedOperationException("TODO 4 : implementer firstUnique()");
    }

    public static List<Integer> topTwo() {
        throw new UnsupportedOperationException("TODO 5 : implementer topTwo()");
    }

    public static int basketTotal(List<String> basket) {
        throw new UnsupportedOperationException("TODO 6 : implementer basketTotal()");
    }

    public static List<String> replay(List<String> actions) {
        throw new UnsupportedOperationException("TODO 7 : implementer replay()");
    }

    public static Map<String, List<String>> priceBands() {
        throw new UnsupportedOperationException("TODO 8 : implementer priceBands()");
    }

    public static int rankByPrice(String fruit) {
        throw new UnsupportedOperationException("TODO 9 : implementer rankByPrice()");
    }

    public static Map<String, Integer> mergeBaskets(Map<String, Integer> a, Map<String, Integer> b) {
        throw new UnsupportedOperationException("TODO 10 : implementer mergeBaskets()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  mostExpensive == cerise", mostExpensive().equals("cerise"));
        ExerciseChecker.check("2  cheaperThan(4) == [banane, kiwi, pomme]", cheaperThan(4).toString().equals("[banane, kiwi, pomme]"));
        ExerciseChecker.check("3  duplicates == [kiwi, pomme]", duplicates().toString().equals("[kiwi, pomme]"));
        ExerciseChecker.check("4  firstUnique == banane", firstUnique().equals("banane"));
        ExerciseChecker.check("5  topTwo == [9, 8]", topTwo().equals(List.of(9, 8)));
        ExerciseChecker.check("6  basketTotal([kiwi, kiwi, mangue, cerise]) == 12", basketTotal(List.of("kiwi", "kiwi", "mangue", "cerise")) == 12);
        ExerciseChecker.check("7  replay([a, b, undo, c, undo, undo, d]) == [d] ; replay([undo, x, y]) == [x, y]",
                replay(List.of("a", "b", "undo", "c", "undo", "undo", "d")).equals(List.of("d"))
                        && replay(List.of("undo", "x", "y")).equals(List.of("x", "y")));
        ExerciseChecker.check("8  priceBands == {bas=[pomme, banane], haut=[kiwi, cerise, abricot]}",
                priceBands().toString().equals("{bas=[pomme, banane], haut=[kiwi, cerise, abricot]}"));
        ExerciseChecker.check("9  rankByPrice(kiwi) == 3 ; (banane) == 1", rankByPrice("kiwi") == 3 && rankByPrice("banane") == 1);
        ExerciseChecker.check("10 mergeBaskets == {cerise=1, kiwi=5, pomme=1}",
                mergeBaskets(Map.of("kiwi", 2, "pomme", 1), Map.of("kiwi", 3, "cerise", 1)).toString().equals("{cerise=1, kiwi=5, pomme=1}"));

        ExerciseChecker.summary();
    }
}
