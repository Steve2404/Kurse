package ch9_collections.drills.r07_comparator.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SOLUTION du drill de rappel 7 - Comparable et Comparator.
 */
public class Recall07 {

    public static void main(String[] args) {
        List<Dog> dogs = new ArrayList<>(List.of(new Dog("Rex", 5), new Dog("Ace", 9), new Dog("Max", 5), new Dog("Bob", 2)));
        Collections.sort(dogs);                                         // ordre naturel : compareTo
        System.out.println("D01 : " + dogs);
        dogs.sort(Comparator.comparingInt(Dog::age));
        System.out.println("D02 : " + dogs);
        dogs.sort(Comparator.comparingInt(Dog::age).reversed().thenComparing(Dog::name));
        System.out.println("D03 : " + dogs);
        dogs.sort(Comparator.comparing(Dog::name, Comparator.reverseOrder()));
        System.out.println("D04 : " + dogs);
        List<String> words = new ArrayList<>(List.of("b", "A", "c", "B"));
        List<String> natural = new ArrayList<>(words);
        natural.sort(Comparator.naturalOrder());
        List<String> ignoreCase = new ArrayList<>(words);
        ignoreCase.sort(String.CASE_INSENSITIVE_ORDER);
        List<String> reversed = new ArrayList<>(words);
        reversed.sort(Collections.reverseOrder());
        System.out.println("D05 : " + natural + " " + ignoreCase + " " + reversed);
        List<String> withNulls = new ArrayList<>(java.util.Arrays.asList("b", null, "a"));
        withNulls.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        List<String> lastNulls = new ArrayList<>(withNulls);
        lastNulls.sort(Comparator.nullsLast(Comparator.reverseOrder()));
        System.out.println("D06 : " + withNulls + " " + lastNulls + " " + new Dog("Rex", 1).compareTo(new Dog("Ace", 1)));
    }
}

// Comparable<Dog> : l'ordre NATUREL, ici par nom ; Comparable est generique.
record Dog(String name, int age) implements Comparable<Dog> {

    @Override
    public int compareTo(Dog other) {
        return name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + age;
    }
}
