package ch9_collections.drills.r08_generics.solution;

import java.util.ArrayList;
import java.util.Iterator;
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

    // Plusieurs bornes : la CLASSE d'abord, puis les interfaces, separees par &.
    static <T extends Number & Comparable<T>> double spread(T a, T b) {
        return a.compareTo(b) >= 0 ? a.doubleValue() - b.doubleValue() : b.doubleValue() - a.doubleValue();
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
        System.out.println("D07 : " + spread(3, 10) + " " + spread(2.5, 1.0));
        var guess = new ArrayList<>();                         // var + diamant : ArrayList<Object>
        guess.add(1);
        guess.add("x");
        System.out.println("D08 : " + guess + " " + s.echo(5) + " " + s.echo("ok").length());
        // Type BRUT (heritage d'avant Java 5) : le compilateur ne verifie plus rien, simple avertissement.
        List rawList = new ArrayList();
        rawList.add(7);
        List<String> typed = rawList;                          // "unchecked" : la liste annonce des String mais contient un Integer
        Object polluted = typed.get(0);                        // pas de cast vers String ici : pas encore d'exception
        Container rawContainer = new RawContainer();
        System.out.println("D09 : " + polluted.getClass().getSimpleName() + " " + rawContainer.content() + " " + rawContainer.describe());
        StringBuilder walk = new StringBuilder();
        for (String w : new Trio<>("a", "b", "c")) {           // for-each sur TOUT Iterable
            walk.append(w);
        }
        System.out.println("D10 : " + walk);
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
    // <T> ici est un NOUVEAU parametre qui MASQUE le T de la classe : piege d'examen.
    <T> T echo(T other) {
        return other;
    }

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

// Implementer une interface generique avec le type BRUT : T devient Object.
class RawContainer implements Container {
    @Override
    public Object content() {
        return "brut";
    }
}

// Implementer Iterable<T> rend la classe utilisable dans une boucle for-each.
class Trio<T> implements Iterable<T> {
    private final List<T> items;

    Trio(T a, T b, T c) {
        items = List.of(a, b, c);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }
}
