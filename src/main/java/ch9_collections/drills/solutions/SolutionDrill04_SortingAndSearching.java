package ch9_collections.drills.solutions;

import ch9_collections.drills.Pantry;
import ch9_collections.drills.Pantry.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch9_collections.drills.exercises.Drill04_SortingAndSearching.
 */
public class SolutionDrill04_SortingAndSearching {

    public static List<String> natural() {
        // Collections.sort modifie la liste : il faut une copie de FRUITS (immuable).
        List<String> copy = new ArrayList<>(Pantry.FRUITS);
        Collections.sort(copy);
        return copy;
    }

    public static List<String> reverse() {
        // reverseOrder() inverse l'ordre naturel (pas l'ordre d'arrivee !).
        List<String> copy = new ArrayList<>(Pantry.FRUITS);
        copy.sort(Comparator.reverseOrder());
        return copy;
    }

    public static List<String> byPrice() {
        // comparingInt evite le boxing de la cle de tri.
        return names(Comparator.comparingInt(Item::price));
    }

    public static List<String> byPriceDesc() {
        // reversed() sur la chaine entiere (ici un seul critere).
        return names(Comparator.comparingInt(Item::price).reversed());
    }

    public static List<String> byLengthThenName() {
        // thenComparing ne parle qu'en cas d'egalite de longueur (banane / cerise).
        return names(Comparator.comparing((Item i) -> i.name().length()).thenComparing(Item::name));
    }

    public static List<String> nullsFirst() {
        // Sans nullsFirst, compareTo sur null lancerait NullPointerException.
        List<String> list = Arrays.asList("kiwi", null, "abricot");
        list.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        return list;
    }

    public static int indexOf8() {
        // binarySearch exige une liste TRIEE : on trie une copie d'abord.
        List<Integer> sorted = new ArrayList<>(Pantry.NUMBERS);
        Collections.sort(sorted);
        return Collections.binarySearch(sorted, 8);
    }

    public static int searchMissing4() {
        // 4 irait a l'index 3 : resultat -(3) - 1 = -4.
        List<Integer> sorted = new ArrayList<>(Pantry.NUMBERS);
        Collections.sort(sorted);
        return Collections.binarySearch(sorted, 4);
    }

    public static String mostExpensive() {
        // Collections.max prend un Comparator quand les elements ne sont pas Comparable.
        return Collections.max(Pantry.items(), Comparator.comparingInt(Item::price)).name();
    }

    public static String firstAlphabetical() {
        // Sans Comparator, min utilise l'ordre naturel.
        return Collections.min(Pantry.FRUITS);
    }

    private static List<String> names(Comparator<Item> order) {
        // Boite magique : trier une copie des items puis n'en garder que les noms.
        List<Item> copy = new ArrayList<>(Pantry.items());
        copy.sort(order);
        List<String> result = new ArrayList<>();
        for (Item i : copy) {
            result.add(i.name());
        }
        return result;
    }
}
