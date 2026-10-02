package ch10_streams.drills.r03_intermediate.solution;

import ch10_streams.drills.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 3 - operations intermediaires et Comparator.
 */
public class Recall03 {

    record Book(String title, String author, String genre, int year, int pages, double price) {
        static Book parse(String line) {
            String[] p = line.split(";");
            return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]));
        }
    }

    static final List<Book> BOOKS = Data.BOOKS.stream().map(Book::parse).toList();

    static List<String> titles(Stream<Book> s) {
        return s.map(Book::title).toList();
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + titles(BOOKS.stream().filter(b -> b.genre().equals("SF") && b.year() > 1960)));

        System.out.println("D02 : " + Data.WORDS.stream().distinct().sorted().toList());

        System.out.println("D03 : " + Data.WORDS.stream().distinct().sorted(Comparator.reverseOrder()).limit(3).toList());

        System.out.println("D04 : " + titles(BOOKS.stream().sorted(Comparator.comparingInt(Book::pages).reversed()).limit(2)));

        // thenComparing(cle, comparateur) : seul le 2e critere est inverse.
        System.out.println("D05 : " + BOOKS.stream()
                .sorted(Comparator.comparing(Book::author).thenComparing(Book::year, Comparator.reverseOrder()))
                .map(b -> b.author() + ":" + b.year())
                .collect(Collectors.joining(", ")));

        System.out.println("D06 : " + titles(BOOKS.stream()
                .sorted(Comparator.comparingDouble(Book::price).thenComparing(Book::title, Comparator.naturalOrder()))
                .limit(3)));

        // nullsFirst/nullsLast enveloppent un comparateur qui, seul, lancerait NullPointerException.
        List<String> withNull = Arrays.asList("b", null, "a");
        System.out.println("D07 : " + withNull.stream().sorted(Comparator.nullsFirst(Comparator.naturalOrder())).toList()
                + " " + withNull.stream().sorted(Comparator.nullsLast(Comparator.reverseOrder())).toList());

        List<String> words = BOOKS.stream().flatMap(b -> Arrays.stream(b.title().split(" "))).toList();
        System.out.println("D08 : " + words.size() + " mots, " + words.stream().distinct().count() + " distincts");

        System.out.println("D09 : " + BOOKS.stream().map(Book::title).sorted().skip(3).limit(3).toList());

        List<Integer> numbers = Arrays.stream(Data.NUMBERS).boxed().toList();
        System.out.println("D10 : " + numbers.stream().takeWhile(x -> x < 9).toList()
                + " " + numbers.stream().dropWhile(x -> x < 9).toList());

        // Java 9+ : count() sur une source de taille connue peut SAUTER le pipeline (peek non execute).
        List<String> seen = new ArrayList<>();
        long n1 = Data.WORDS.stream().peek(seen::add).count();
        int withoutFilter = seen.size();
        seen.clear();
        long n2 = Data.WORDS.stream().peek(seen::add).filter(w -> true).count();
        System.out.println("D11 : count " + n1 + " -> peek " + withoutFilter + " fois ; avec filter count " + n2 + " -> peek " + seen.size() + " fois");

        // sorted est STABLE sur un flux ordonne : a longueur egale, l'ordre d'origine est garde.
        System.out.println("D12 : " + Data.WORDS.stream().sorted(Comparator.comparingInt(String::length)).toList());

        List<List<String>> nested = List.of(List.of("a", "b"), List.of(), List.of("c"));
        System.out.println("D13 : map " + nested.stream().map(List::size).toList() + ", flatMap " + nested.stream().flatMap(List::stream).toList());
    }
}
