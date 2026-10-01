package ch10_streams.drills.r11_spliterator.solution;

import ch10_streams.drills.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * SOLUTION du drill de rappel 11 - Spliterator.
 */
public class Recall11 {

    // Un Spliterator maison : rend les mots deux par deux ("stream+lambda", ...).
    static class Pairs implements Spliterator<String> {
        private final List<String> words;
        private int index;

        Pairs(List<String> words) {
            this.words = words;
        }

        @Override
        public boolean tryAdvance(Consumer<? super String> action) {
            if (index >= words.size()) {
                return false;
            }
            String second = index + 1 < words.size() ? "+" + words.get(index + 1) : "";
            action.accept(words.get(index) + second);
            index += 2;
            return true;
        }

        // null = "je ne sais pas me couper" : le flux reste correct, simplement pas parallelise.
        @Override
        public Spliterator<String> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return (words.size() - index + 1) / 2;
        }

        @Override
        public int characteristics() {
            return ORDERED | SIZED | NONNULL;
        }
    }

    public static void main(String[] args) {
        List<String> words = Data.WORDS;
        Spliterator<String> s = words.spliterator();
        System.out.println("D01 : " + s.estimateSize() + " " + s.getExactSizeIfKnown() + " "
                + s.hasCharacteristics(Spliterator.ORDERED) + " " + s.hasCharacteristics(Spliterator.SIZED));

        // trySplit d'une liste : rend la PREMIERE moitie, garde la seconde.
        Spliterator<String> prefix = s.trySplit();
        System.out.println("D02 : " + prefix.estimateSize() + " " + s.estimateSize());

        StringBuilder first = new StringBuilder();
        prefix.tryAdvance(first::append);
        List<String> rest = new ArrayList<>();
        prefix.forEachRemaining(rest::add);
        System.out.println("D03 : " + first + " " + rest + " " + prefix.tryAdvance(w -> { }));

        Pairs pairs = new Pairs(words);
        System.out.println("D04 : " + pairs.estimateSize() + " " + StreamSupport.stream(pairs, false).toList());

        System.out.println("D05 : " + StreamSupport.stream(new Pairs(words), true).count() + " " + (new Pairs(words).trySplit() == null));

        // Le spliterator d'un stream infini ne connait pas sa taille.
        Spliterator<Integer> infinite = java.util.stream.Stream.iterate(1, x -> x + 1).spliterator();
        System.out.println("D06 : " + infinite.getExactSizeIfKnown() + " " + infinite.hasCharacteristics(Spliterator.SIZED));
    }
}
