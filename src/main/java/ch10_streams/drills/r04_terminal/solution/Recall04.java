package ch10_streams.drills.r04_terminal.solution;

import ch10_streams.drills.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 4 - operations terminales.
 */
public class Recall04 {

    record Book(String title, String author, String genre, int year, int pages, double price) {
        static Book parse(String line) {
            String[] p = line.split(";");
            return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]));
        }
    }

    static final List<Book> BOOKS = Data.BOOKS.stream().map(Book::parse).toList();

    public static void main(String[] args) {
        // min/max d'un Stream<T> exigent un Comparator et rendent un Optional<T>.
        System.out.println("D01 : " + BOOKS.stream().count() + " "
                + BOOKS.stream().min(Comparator.comparing(Book::title)).map(Book::title).orElse("-") + " "
                + BOOKS.stream().max(Comparator.comparingInt(Book::pages)).map(Book::title).orElse("-"));

        System.out.println("D02 : " + BOOKS.stream().filter(b -> b.genre().equals("Fantasy")).findFirst().map(Book::title).orElse("-")
                + " " + BOOKS.stream().filter(b -> b.genre().equals("Fantasy")).findAny().isPresent());

        // Sur un flux VIDE : anyMatch false, allMatch true, noneMatch true (verite "vide").
        System.out.println("D03 : " + BOOKS.stream().anyMatch(b -> b.pages() > 500) + " " + BOOKS.stream().allMatch(b -> b.pages() > 500)
                + " " + BOOKS.stream().noneMatch(b -> b.pages() > 1000)
                + " | vide : " + Stream.<Book>empty().anyMatch(b -> true) + " " + Stream.<Book>empty().allMatch(b -> false)
                + " " + Stream.<Book>empty().noneMatch(b -> true));

        StringBuilder authors = new StringBuilder();
        BOOKS.stream().map(Book::author).distinct().forEach(a -> authors.append(a.charAt(0)));
        System.out.println("D04 : " + authors);

        // toArray() rend Object[] ; toArray(IntFunction) rend le bon type de tableau.
        Object[] objects = BOOKS.stream().map(Book::title).toArray();
        String[] strings = BOOKS.stream().map(Book::title).toArray(String[]::new);
        System.out.println("D05 : " + objects.getClass().getSimpleName() + " " + objects.length + " / "
                + strings.getClass().getSimpleName() + " " + strings.length);

        // Les deux listes ont le meme contenu ; seule celle de Collectors.toList() accepte un ajout
        // (toList() du stream rend une liste NON modifiable : add lancerait UnsupportedOperationException).
        List<String> fixed = BOOKS.stream().map(Book::title).toList();
        List<String> mutable = BOOKS.stream().map(Book::title).collect(Collectors.toList());
        boolean same = fixed.equals(mutable);
        mutable.add("x");
        System.out.println("D06 : meme contenu " + same + ", Collectors.toList() apres ajout -> " + mutable.size() + " elements");

        System.out.println("D07 : " + BOOKS.stream().map(Book::pages).reduce(0, Integer::sum) + " "
                + BOOKS.stream().map(Book::year).reduce(Integer::max).orElse(0) + " "
                + BOOKS.stream().map(Book::genre).distinct().collect(Collectors.joining("/")));

        System.out.println("D08 : " + Stream.<String>empty().min(Comparator.naturalOrder()) + " "
                + Stream.generate(() -> 1).limit(5).count());

        // Les xxxMatch court-circuitent : ils terminent meme sur un flux infini... si la reponse arrive.
        System.out.println("D09 : " + Stream.iterate(1, x -> x + 1).anyMatch(x -> x > 1000)
                + " " + Stream.iterate(1, x -> x + 1).allMatch(x -> x < 10));

        System.out.println("D10 : " + Data.WORDS.stream().max(Comparator.naturalOrder()).orElse("-") + " "
                + Data.WORDS.stream().min(Comparator.comparingInt(String::length).thenComparing(Comparator.reverseOrder())).orElse("-"));
    }
}
