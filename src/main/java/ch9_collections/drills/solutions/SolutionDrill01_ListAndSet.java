package ch9_collections.drills.solutions;

import ch9_collections.drills.Pantry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch9_collections.drills.exercises.Drill01_ListAndSet.
 */
public class SolutionDrill01_ListAndSet {

    public static List<String> firstTwo() {
        // subList(0, 2) : 2 est exclu ; c'est une vue, sans copie.
        return Pantry.FRUITS.subList(0, 2);
    }

    public static List<String> withMango() {
        // add(index, e) decale la suite vers la droite ; il faut une copie car FRUITS est immuable.
        List<String> copy = new ArrayList<>(Pantry.FRUITS);
        copy.add(1, "mangue");
        return copy;
    }

    public static List<Integer> removeTwice() {
        // remove(1) choisit remove(int index) (pas de boxing) ; pour retirer la VALEUR 1, il faut un Integer.
        List<Integer> nums = new ArrayList<>(Pantry.NUMBERS);
        nums.remove(1);
        nums.remove(Integer.valueOf(1));
        return nums;
    }

    public static int lastKiwi() {
        // lastIndexOf cherche depuis la fin : le 2e kiwi est a l'index 3.
        return Pantry.FRUITS.lastIndexOf("kiwi");
    }

    public static List<String> withoutShort() {
        // removeIf supprime pendant le parcours sans ConcurrentModificationException.
        List<String> copy = new ArrayList<>(Pantry.FRUITS);
        copy.removeIf(s -> s.length() < 5);
        return copy;
    }

    public static List<String> shouting() {
        // replaceAll prend un UnaryOperator : meme type en entree et en sortie.
        List<String> copy = new ArrayList<>(Pantry.FRUITS);
        copy.replaceAll(String::toUpperCase);
        return copy;
    }

    public static Set<String> distinctInOrder() {
        // LinkedHashSet : pas de doublon ET l'ordre d'ajout conserve.
        return new LinkedHashSet<>(Pantry.FRUITS);
    }

    public static Set<String> distinctSorted() {
        // TreeSet trie selon l'ordre naturel des String.
        return new TreeSet<>(Pantry.FRUITS);
    }

    public static String addTwice() {
        // add rend false quand l'element est deja la : pas d'exception, pas de doublon.
        Set<String> set = new HashSet<>();
        boolean first = set.add("kiwi");
        boolean second = set.add("kiwi");
        return first + " " + second;
    }

    public static Set<String> common() {
        // retainAll = intersection, sur une copie triee.
        Set<String> set = new TreeSet<>(Pantry.FRUITS);
        set.retainAll(Set.of("pomme", "mangue", "kiwi"));
        return set;
    }

    public static Set<String> beforeKiwi() {
        // headSet(x) exclut x ; headSet(x, true) l'inclurait.
        return new TreeSet<>(Pantry.FRUITS).headSet("kiwi");
    }

    public static String addToListOf() {
        // List.of est immuable : toute modification lance UnsupportedOperationException.
        try {
            List.of("a").add("b");
            return "aucune";
        } catch (UnsupportedOperationException e) {
            return e.getClass().getSimpleName();
        }
    }
}
