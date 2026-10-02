package ch10_streams.drills.r07_reduce.solution;

import ch10_streams.drills.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 7 - reduce et collect.
 */
public class Recall07 {

    public static void main(String[] args) {
        // reduce(identite, accumulateur) : jamais vide -> T directement.
        System.out.println("D01 : " + Data.WORDS.stream().map(String::length).reduce(0, Integer::sum)
                + " " + Data.WORDS.stream().reduce("", (a, b) -> a + b.charAt(0)));

        // reduce(accumulateur) : pas d'identite -> Optional (flux vide possible).
        Optional<String> longest = Data.WORDS.stream().reduce((a, b) -> b.length() > a.length() ? b : a);
        System.out.println("D02 : " + longest.orElse("-") + " " + Stream.<String>empty().reduce(String::concat));

        // reduce(identite, accumulateur, combiner) : type du resultat != type des elements.
        int letters = Data.WORDS.stream().reduce(0, (sum, w) -> sum + w.length(), Integer::sum);
        System.out.println("D03 : " + letters);

        // Identite NON neutre : un seul passage l'ajoute une fois ; reduire DEUX morceaux puis les combiner
        // l'ajoute une fois PAR morceau (c'est ce que fera un stream parallele au chapitre 13).
        int whole = Stream.of(1, 2, 3, 4).reduce(10, Integer::sum);
        int halves = Integer.sum(Stream.of(1, 2).reduce(10, Integer::sum), Stream.of(3, 4).reduce(10, Integer::sum));
        System.out.println("D04 : un passage " + whole + ", deux moities " + halves);

        // collect(supplier, accumulator, combiner) : reduction MUTABLE.
        ArrayList<String> upper = Data.WORDS.stream().collect(ArrayList::new, (l, w) -> l.add(w.toUpperCase()), ArrayList::addAll);
        TreeSet<String> sorted = Data.WORDS.stream().collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
        System.out.println("D05 : " + upper.get(0) + " " + upper.size() + " " + sorted.first() + " " + sorted.size());

        StringBuilder initials = Data.WORDS.stream().collect(StringBuilder::new, (b, w) -> b.append(w.charAt(0)), StringBuilder::append);
        System.out.println("D06 : " + initials);

        // Collector.of(supplier, accumulator, combiner, finisher) : un collecteur reutilisable.
        Collector<String, List<String>, String> shortest = Collector.of(
                ArrayList::new,
                (acc, w) -> {
                    if (acc.isEmpty() || w.length() < acc.get(0).length()) {
                        acc.clear();
                        acc.add(w);
                    }
                },
                (a, b) -> a.isEmpty() || (!b.isEmpty() && b.get(0).length() < a.get(0).length()) ? b : a,
                acc -> acc.isEmpty() ? "-" : acc.get(0));
        // Le collecteur applique A LA MAIN sur deux moities : supplier + accumulator par morceau, puis combiner et finisher.
        List<String> left = shortest.supplier().get();
        Data.WORDS.subList(0, 4).forEach(w -> shortest.accumulator().accept(left, w));
        List<String> right = shortest.supplier().get();
        Data.WORDS.subList(4, Data.WORDS.size()).forEach(w -> shortest.accumulator().accept(right, w));
        String byHalves = shortest.finisher().apply(shortest.combiner().apply(left, right));
        System.out.println("D07 : " + Data.WORDS.stream().collect(shortest) + " " + byHalves
                + " " + Stream.<String>empty().collect(shortest));

        // Une concatenation de String par reduce cree une String par etape ; collect reutilise un seul StringBuilder.
        System.out.println("D08 : " + Stream.of("a", "b", "c").reduce("", String::concat).equals(
                Stream.of("a", "b", "c").collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString()));
    }
}
