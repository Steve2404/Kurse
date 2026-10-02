package ch9_collections.drills.r08_generics;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : java 43 4",
            "D02 : a 7 pomme 9 age=30",
            "D03 : 2.5 contient Double",
            "D04 : Holder[key=k, value=1] Holder[key=1, value=k]",
            "D05 : true ArrayList",
            "D06 : 2 c",
            "D07 : 7.0 1.5",
            "D08 : [1, x] 5 2",
            "D09 : Integer brut contient String",
            "D10 : abc");
            // EXPECTED-END

    static final List<String> API = List.of(
            "static <T> T first(", "static <T extends Comparable<T>> T larger(", "static <K, V> String entry(", "class Box<T>",
            "<R> Box<R> map(", "interface Container<T>", "implements Container<Double>", "record Holder<K, V>",
            "Recall08.<Integer>first(", "new Box<>(", "<T extends Number & Comparable<T>>", "var guess = new ArrayList<>()",
            "<T> T echo(", "List rawList = new ArrayList()", "implements Container {", "implements Iterable<T>",
            "Iterator<T> iterator()",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
