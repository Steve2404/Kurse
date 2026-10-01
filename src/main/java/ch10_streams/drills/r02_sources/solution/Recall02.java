package ch10_streams.drills.r02_sources.solution;

import ch10_streams.drills.Data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 2 - creer des streams, paresse, usage unique.
 */
public class Recall02 {

    public static void main(String[] args) {
        System.out.println("D01 : " + Stream.of("a", "b", "c").count() + " " + Stream.empty().count() + " " + Stream.ofNullable(null).count());

        // iterate a 2 arguments est INFINI -> limit obligatoire ; a 3 arguments il porte sa condition d'arret.
        System.out.println("D02 : " + Stream.iterate(1, x -> x * 2).limit(6).toList()
                + " " + Stream.iterate(1, x -> x < 100, x -> x * 3).toList());

        System.out.println("D03 : " + Stream.generate(() -> "ab").limit(3).collect(Collectors.joining()));

        System.out.println("D04 : " + Stream.concat(Stream.of(1, 2), Stream.of(3)).toList());

        // Arrays.stream(tableau, debut inclus, fin EXCLUE).
        System.out.println("D05 : " + Arrays.toString(Arrays.stream(Data.NUMBERS, 2, 5).toArray()));

        // chars() rend des int : il faut les remettre en char pour les afficher.
        System.out.println("D06 : " + "java".chars().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining("-")));

        // Paresse : chaque element traverse TOUT le pipeline avant le suivant ; findFirst arrete tout.
        AtomicInteger seen = new AtomicInteger();
        String first = Data.WORDS.stream().peek(w -> seen.incrementAndGet()).filter(w -> w.length() > 6).findFirst().orElse("-");
        System.out.println("D07 : " + first + " apres " + seen + " elements examines");

        // Sans operation terminale, rien ne s'execute.
        AtomicInteger calls = new AtomicInteger();
        Stream<String> neverRun = Data.WORDS.stream().peek(w -> calls.incrementAndGet()).map(String::toUpperCase);
        System.out.println("D08 : sans operation terminale : " + calls + " appel");

        Stream<String> once = Data.WORDS.stream();
        once.count();
        String reuse;
        try {
            once.count();
            reuse = "rien";
        } catch (IllegalStateException e) {
            reuse = e.getClass().getSimpleName();
        }
        System.out.println("D09 : " + reuse);

        // iterator() : operation terminale qui rend un Iterator classique.
        Iterator<String> it = Data.WORDS.stream().iterator();
        StringJoiner three = new StringJoiner(" ");
        for (int i = 0; i < 3 && it.hasNext(); i++) {
            three.add(it.next());
        }
        System.out.println("D10 : " + three);

        // Piege : Stream.of(int[]) est un Stream<int[]> d'UN element ; Arrays.stream(int[]) est un IntStream.
        System.out.println("D11 : " + Stream.of(Data.NUMBERS).count() + " " + Arrays.stream(Data.NUMBERS).count());

        System.out.println("D12 : " + Stream.iterate(1, x -> x + 1).takeWhile(x -> x * x < 50).toList());

        // L'ordre compte : limit puis filter != filter puis limit.
        System.out.println("D13 : " + Data.WORDS.stream().limit(4).filter(w -> w.length() > 4).toList()
                + " " + Data.WORDS.stream().filter(w -> w.length() > 4).limit(4).toList());
    }
}
