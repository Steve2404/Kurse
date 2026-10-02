package ch9_collections.drills.r08_generics.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 8 - classes, interfaces et methodes generiques.
 */
public class Recall08 {

    // Methode generique : le parametre de type est declare AVANT le type de retour.
    static <T> T first(List<T> list) {
        return list.get(0);
    }

    static <T extends Comparable<T>> T larger(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static <K, V> String entry(K key, V value) {
        return key + "=" + value;
    }

    public static void main(String[] args) {
        Box<String> s = new Box<>("java");
        Box<Integer> i = new Box<>(42);
        System.out.println("D01 : " + s.get() + " " + (i.get() + 1) + " " + s.map(String::length).get());
        System.out.println("D02 : " + first(List.of("a", "b")) + " " + Recall08.<Integer>first(List.of(7, 8)) + " " + larger("pomme", "kiwi") + " " + larger(3, 9)
                + " " + entry("age", 30));
        Container<Double> c = new NumberBox(2.5);
        System.out.println("D03 : " + c.content() + " " + c.describe());
        Holder<String, Integer> h = new Holder<>("k", 1);
        System.out.println("D04 : " + h + " " + h.swap());
        // Effacement de type : a l'execution, List<String> et List<Integer> sont la meme classe.
        List<String> ls = new ArrayList<>();
        List<Integer> li = new ArrayList<>();
        System.out.println("D05 : " + (ls.getClass() == li.getClass()) + " " + ls.getClass().getSimpleName());
        var inferred = new Box<>(List.of(1, 2));               // T deduit : List<Integer>
        System.out.println("D06 : " + inferred.get().size() + " " + new Box<>('c').get());
    }
}

class Box<T> {
    private final T value;

    Box(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }

    // Une methode generique dans une classe generique : R est propre a la methode.
    <R> Box<R> map(java.util.function.Function<? super T, ? extends R> f) {
        return new Box<>(f.apply(value));
    }
}

// Interface generique.
interface Container<T> {
    T content();

    default String describe() {
        return "contient " + content().getClass().getSimpleName();
    }
}

// Implementer une interface generique en FIXANT le type.
class NumberBox implements Container<Double> {
    private final double v;

    NumberBox(double v) {
        this.v = v;
    }

    @Override
    public Double content() {
        return v;
    }
}

// Un record generique a deux parametres.
record Holder<K, V>(K key, V value) {
    Holder<V, K> swap() {
        return new Holder<>(value, key);
    }
}
