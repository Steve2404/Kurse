package ch10_streams.drills.r01_optional.solution;

import ch10_streams.drills.Data;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * SOLUTION du drill de rappel 1 - Optional.
 */
public class Recall01 {

    record Book(String title, String author, String genre, int year, int pages, double price) {
        static Book parse(String line) {
            String[] p = line.split(";");
            return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]));
        }
    }

    static final List<Book> BOOKS = Data.BOOKS.stream().map(Book::parse).toList();
    static final Map<String, String> SEQUELS = Map.of("Dune", "Le Messie de Dune", "Le Hobbit", "Le Seigneur des Anneaux");

    static Optional<Book> byTitle(String title) {
        return BOOKS.stream().filter(b -> b.title().equals(title)).findFirst();
    }

    // Une recherche dans une Map rend null si absent -> ofNullable.
    static Optional<String> sequel(Book b) {
        return Optional.ofNullable(SEQUELS.get(b.title()));
    }

    public static void main(String[] args) {
        // toString : "Optional[valeur]" ou "Optional.empty".
        System.out.println("D01 : " + Optional.of("Dune") + " " + Optional.empty());

        String npe;
        try {
            Optional.of(null);
            npe = "rien";
        } catch (NullPointerException e) {
            npe = e.getClass().getSimpleName();
        }
        // of(null) lance NPE tout de suite ; ofNullable(null) rend vide.
        System.out.println("D02 : " + Optional.ofNullable(null).isEmpty() + " " + npe);

        System.out.println("D03 : " + BOOKS.stream().filter(b -> b.author().equals("Zola")).findFirst().map(Book::title).orElse("inconnu")
                + " | " + BOOKS.stream().filter(b -> b.author().equals("Proust")).findFirst().map(Book::title).orElse("inconnu"));

        // orElse EVALUE toujours son argument ; orElseGet n'appelle le Supplier que si vide.
        AtomicInteger calls = new AtomicInteger();
        Optional<String> present = Optional.of("Dune");
        present.orElse(expensive(calls));
        int withOrElse = calls.getAndSet(0);
        present.orElseGet(() -> expensive(calls));
        System.out.println("D04 : orElse appelle " + withOrElse + " fois, orElseGet " + calls.intValue() + " fois");

        // flatMap : la fonction rend deja un Optional -> pas d'Optional<Optional<...>>.
        System.out.println("D05 : " + byTitle("Dune").flatMap(Recall01::sequel).orElse("aucune suite")
                + " | " + byTitle("Germinal").flatMap(Recall01::sequel).orElse("aucune suite"));

        // or : recherche de secours paresseuse, le resultat reste un Optional.
        System.out.println("D06 : " + byTitle("Hobbit")
                .or(() -> BOOKS.stream().filter(b -> b.title().contains("Hobbit")).findFirst())
                .map(Book::title));

        StringBuilder sb = new StringBuilder("D07 :");
        byTitle("Dune").ifPresentOrElse(b -> sb.append(" trouve ").append(b.year()), () -> sb.append(" absent"));
        byTitle("Ulysse").ifPresentOrElse(b -> sb.append(" trouve ").append(b.year()), () -> sb.append(" absent"));
        System.out.println(sb);

        String e1;
        String e2;
        try {
            byTitle("Ulysse").orElseThrow();
            e1 = "rien";
        } catch (NoSuchElementException e) {
            e1 = e.getClass().getSimpleName();
        }
        try {
            byTitle("Ulysse").orElseThrow(IllegalArgumentException::new);
            e2 = "rien";
        } catch (IllegalArgumentException e) {
            e2 = e.getClass().getSimpleName();
        }
        System.out.println("D08 : " + e1 + " " + e2);

        // Optional::stream : 0 ou 1 element -> flatMap ne garde que les presents.
        System.out.println("D09 : " + BOOKS.stream().map(Recall01::sequel).flatMap(Optional::stream).sorted().toList());

        OptionalInt maxPages = BOOKS.stream().mapToInt(Book::pages).max();
        OptionalLong lastYear = BOOKS.stream().mapToLong(Book::year).max();
        OptionalDouble avgPrice = BOOKS.stream().mapToDouble(Book::price).average();
        OptionalInt none = IntStream.empty().max();
        System.out.println("D10 : " + maxPages.getAsInt() + " " + lastYear.getAsLong() + " "
                + String.format(Locale.US, "%.2f", avgPrice.getAsDouble()) + " " + none.orElse(-1));

        // map qui rend null -> Optional vide (pas d'exception).
        System.out.println("D11 : " + Optional.of("a").equals(Optional.of("a")) + " " + Optional.of(1).map(x -> null));

        System.out.println("D12 : " + byTitle("Dune").filter(b -> b.price() > 10).map(Book::title)
                + " " + byTitle("Hyperion").filter(b -> b.price() > 10).map(Book::title));

        byTitle("Fondation").ifPresent(b -> System.out.println("D13 : " + b.author() + " " + byTitle("Fondation").isPresent()));
    }

    static String expensive(AtomicInteger calls) {
        calls.incrementAndGet();
        return "defaut";
    }
}
