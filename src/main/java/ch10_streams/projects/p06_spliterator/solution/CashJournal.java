package ch10_streams.projects.p06_spliterator.solution;

import ch10_streams.projects.p06_spliterator.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * SOLUTION du projet 6 - une conception possible.
 */
public class CashJournal {

    // Les montants en CENTIMES (long) : une somme de double n'est pas associative -> le total par morceaux pourrait differer.
    record Item(int quantity, String article, long unitCents) {
        long cents() {
            return quantity * unitCents;
        }
    }

    record Transaction(String id, String customer, List<Item> items, List<String> unreadable) {
        int units() {
            return items.stream().mapToInt(Item::quantity).sum();
        }

        long cents() {
            return items.stream().mapToLong(Item::cents).sum();
        }
    }

    /**
     * Decoupe un journal en transactions. Il travaille sur une PLAGE [from, to) de la liste :
     * trySplit coupe la plage en deux, mais seulement sur une ligne "TX", jamais au milieu d'une transaction.
     */
    static class TransactionSpliterator implements Spliterator<Transaction> {
        private final List<String> lines;
        private int from;
        private final int to;

        TransactionSpliterator(List<String> lines, int from, int to) {
            this.lines = lines;
            this.from = from;
            this.to = to;
        }

        static boolean isHeader(String line) {
            return line.startsWith("TX ");
        }

        // Contrat : traiter AU PLUS un element ; rendre false quand il n'y en a plus.
        @Override
        public boolean tryAdvance(Consumer<? super Transaction> action) {
            while (from < to && !isHeader(lines.get(from))) {
                from++; // commentaires et lignes orphelines avant le 1er en-tete
            }
            if (from >= to) {
                return false;
            }
            String[] header = lines.get(from++).split(" ");
            List<Item> items = new ArrayList<>();
            List<String> unreadable = new ArrayList<>();
            while (from < to && !isHeader(lines.get(from))) {
                String line = lines.get(from++);
                if (line.startsWith("  ")) {
                    parseItem(line, items, unreadable);
                }
            }
            action.accept(new Transaction(header[1], header[2], List.copyOf(items), List.copyOf(unreadable)));
            return true;
        }

        private static void parseItem(String line, List<Item> items, List<String> unreadable) {
            String[] p = line.trim().split(" ");
            // Forme attendue : [+|-] qte x article a prix  (l'article peut contenir un tiret, pas d'espace)
            if (p.length != 6 || !p[2].equals("x") || !p[4].equals("a")) {
                unreadable.add(line);
                return;
            }
            int sign = p[0].equals("-") ? -1 : 1;
            items.add(new Item(sign * Integer.parseInt(p[1]), p[3], Long.parseLong(p[5].replace(".", ""))));
        }

        // Prefixe rendu, suffixe garde : l'ordre de rencontre reste correct (exige par ORDERED).
        @Override
        public Spliterator<Transaction> trySplit() {
            if (to - from < Data.MIN_SPLIT_LINES) {
                return null;
            }
            int mid = (from + to) >>> 1;
            while (mid < to && !isHeader(lines.get(mid))) {
                mid++; // on recule la coupe jusqu'au prochain en-tete
            }
            if (mid >= to || mid <= from) {
                return null;
            }
            Spliterator<Transaction> prefix = new TransactionSpliterator(lines, from, mid);
            from = mid;
            return prefix;
        }

        // Majorant : on ne sait pas combien de transactions restent, seulement combien de lignes -> pas SIZED.
        @Override
        public long estimateSize() {
            return to - from;
        }

        @Override
        public int characteristics() {
            return ORDERED | NONNULL | IMMUTABLE;
        }

        String range() {
            List<String> ids = new ArrayList<>();
            new TransactionSpliterator(lines, from, to).forEachRemaining(t -> ids.add(t.id()));
            return "[" + String.join(" ", ids) + "]";
        }
    }

    // StreamSupport.stream(spliterator, false) : un Stream ordinaire (sequentiel) au-dessus de NOTRE source.
    static Stream<Transaction> transactions(List<String> log) {
        return StreamSupport.stream(new TransactionSpliterator(log, 0, log.size()), false);
    }

    static String money(long cents) {
        return String.format("%d.%02d", cents / 100, Math.abs(cents % 100));
    }

    // Decoupe recursive : on coupe tant que trySplit accepte ; les morceaux finaux sont gardes de gauche a droite.
    static void leaves(TransactionSpliterator s, List<TransactionSpliterator> out) {
        Spliterator<Transaction> prefix = s.trySplit();
        if (prefix == null) {
            out.add(s);
            return;
        }
        leaves((TransactionSpliterator) prefix, out);
        leaves(s, out);
    }

    static <T> void batches(Spliterator<T> s, int max, List<List<T>> out) {
        if (s.estimateSize() > max) {
            Spliterator<T> prefix = s.trySplit();
            if (prefix != null) {
                batches(prefix, max, out);
                batches(s, max, out);
                return;
            }
        }
        List<T> batch = new ArrayList<>();
        s.forEachRemaining(batch::add);
        out.add(batch);
    }

    static final Map<Integer, String> FLAGS = new TreeMap<>(Map.of(
            Spliterator.ORDERED, "ORDERED", Spliterator.DISTINCT, "DISTINCT", Spliterator.SORTED, "SORTED",
            Spliterator.SIZED, "SIZED", Spliterator.NONNULL, "NONNULL", Spliterator.IMMUTABLE, "IMMUTABLE",
            Spliterator.CONCURRENT, "CONCURRENT", Spliterator.SUBSIZED, "SUBSIZED"));

    static String describe(String name, Spliterator<?> s) {
        String flags = FLAGS.entrySet().stream().filter(e -> s.hasCharacteristics(e.getKey()))
                .map(Map.Entry::getValue).collect(Collectors.joining(" "));
        return "CARACTERISTIQUES " + name + " : " + flags + " (taille exacte " + s.getExactSizeIfKnown() + ")";
    }

    public static void main(String[] args) {
        List<Transaction> all = transactions(Data.LOG).toList();
        for (Transaction t : all) {
            String detail = t.items().isEmpty() ? "vide"
                    : t.units() == 0 ? "annulee (0 article)"
                    : t.units() + " article(s), " + money(t.cents());
            System.out.println("TX " + t.id() + " " + t.customer() + " : " + detail);
        }
        all.forEach(t -> t.unreadable().forEach(l -> System.out.println("ANOMALIE TX " + t.id() + " : ligne illisible \"" + l + "\"")));

        // Chaque morceau de la decoupe devient son propre stream ; ensemble, ils doivent redonner tout le journal.
        List<TransactionSpliterator> leaves = new ArrayList<>();
        leaves(new TransactionSpliterator(Data.LOG, 0, Data.LOG.size()), leaves);
        String ranges = leaves.stream().map(TransactionSpliterator::range).collect(Collectors.joining(" "));
        List<List<Transaction>> pieces = leaves.stream().map(l -> StreamSupport.stream(l, false).toList()).toList();
        long total = all.stream().mapToLong(Transaction::cents).sum();
        long piecesTotal = pieces.stream().flatMap(List::stream).mapToLong(Transaction::cents).sum();
        System.out.println("COMPTE : " + all.size() + " transactions (journal entier) / "
                + pieces.stream().mapToInt(List::size).sum() + " (somme des " + pieces.size() + " morceaux)");
        System.out.println("CA TOTAL : " + money(total) + " (somme des morceaux identique : " + (total == piecesTotal ? "oui" : "non") + ")");

        Map<String, Long> perCustomer = transactions(Data.LOG)
                .collect(Collectors.groupingBy(Transaction::customer, TreeMap::new, Collectors.summingLong(Transaction::cents)));
        perCustomer.entrySet().stream().max(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.println("MEILLEUR CLIENT : " + e.getKey() + " (" + money(e.getValue()) + ")"));

        System.out.println("DECOUPAGE : " + ranges);

        // tryAdvance consomme UN element ; forEachRemaining consomme le reste, sur le MEME spliterator.
        Spliterator<Transaction> s = new TransactionSpliterator(Data.LOG, 0, Data.LOG.size());
        StringBuilder first = new StringBuilder();
        s.tryAdvance(t -> first.append(t.id()));
        List<Transaction> rest = new ArrayList<>();
        s.forEachRemaining(rest::add);
        System.out.println("PREMIERE (tryAdvance) : " + first + ", RESTE (forEachRemaining) : " + rest.size()
                + ", ENCORE : " + s.tryAdvance(t -> { }));

        List<List<String>> lots = new ArrayList<>();
        batches(Data.JOBS.spliterator(), Data.MAX_BATCH, lots);
        System.out.println("LOTS : " + lots.stream().map(List::toString).collect(Collectors.joining(" ")));

        System.out.println(describe("ArrayList", new ArrayList<>(Data.JOBS).spliterator()));
        System.out.println(describe("HashSet", new HashSet<>(Data.JOBS).spliterator()));
        System.out.println(describe("TreeSet", new TreeSet<>(Data.JOBS).spliterator()));
        System.out.println(describe("Stream.iterate", Stream.iterate(1, i -> i + 1).spliterator()));
        System.out.println(describe("sorted()", Data.JOBS.stream().sorted(Comparator.reverseOrder()).spliterator()));
        System.out.println(describe("journal", new TransactionSpliterator(Data.LOG, 0, Data.LOG.size())));
    }
}
