package ch10_streams.projects.p07_warehouse.solution;

import ch10_streams.projects.p07_warehouse.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.teeing;
import static java.util.stream.Collectors.toCollection;

/**
 * SOLUTION du projet 7 (capstone) - une conception possible.
 */
public class Warehouse {

    enum Tier { GOLD, STANDARD }

    // L'ordre de declaration EST l'ordre d'affichage d'une EnumMap.
    enum Status { COMPLETE, PARTIELLE, REFUSEE }

    record Product(String sku, String name, String category, long cents) {
    }

    record Customer(String id, String name, String region, Tier tier, String rawEmail) {
        Optional<String> email() {
            return Optional.ofNullable(rawEmail).map(String::strip).filter(e -> !e.isEmpty());
        }
    }

    record Line(String sku, int quantity) {
        static Line parse(String text) {
            String[] p = text.split("x");
            return new Line(p[0], Integer.parseInt(p[1]));
        }
    }

    record Order(String id, String customerId, LocalDate date, List<Line> lines) {
        static Order parse(String text) {
            String[] p = text.split(";");
            return new Order(p[0], p[1], LocalDate.parse(p[2]),
                    Arrays.stream(p[3].split(",")).map(Line::parse).toList());
        }
    }

    // Ce qu'on a reellement pu servir (ou pas) pour une ligne.
    record Allocation(Product product, int served, int missing) {
        long cents() {
            return served * product.cents();
        }
    }

    record Result(Order order, Customer customer, List<Allocation> allocations, List<String> unknownSkus) {
        long cents() {
            return allocations.stream().mapToLong(Allocation::cents).sum();
        }

        Status status() {
            boolean anyServed = allocations.stream().anyMatch(a -> a.served() > 0);
            boolean anyMissing = allocations.stream().anyMatch(a -> a.missing() > 0) || !unknownSkus.isEmpty();
            return !anyServed ? Status.REFUSEE : anyMissing ? Status.PARTIELLE : Status.COMPLETE;
        }
    }

    private final Map<String, Product> products = new LinkedHashMap<>();
    private final Map<String, Integer> stock = new TreeMap<>();
    private final Map<String, Customer> customers;
    private final List<Order> orders;
    private final List<Result> results = new ArrayList<>();

    Warehouse() {
        for (String line : Data.PRODUCTS) {
            String[] p = line.split(";");
            products.put(p[0], new Product(p[0], p[1], p[2], Long.parseLong(p[4].replace(".", ""))));
            stock.put(p[0], Integer.parseInt(p[3]));
        }
        customers = Data.CUSTOMERS.stream().map(l -> l.split(";", -1))
                .map(p -> new Customer(p[0], p[1], p[2], Tier.valueOf(p[3]), p[4]))
                .collect(Collectors.toMap(Customer::id, Function.identity()));
        orders = Data.ORDERS.stream().map(Order::parse).toList();
    }

    Optional<Customer> customer(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    static String money(long cents) {
        return String.format("%d.%02d", cents / 100, cents % 100);
    }

    // Priorite : GOLD d'abord (ordre de l'enum), puis la plus ancienne, puis l'id.
    static final Comparator<Result> PRIORITY = Comparator.comparing((Result r) -> r.customer().tier())
            .thenComparing(r -> r.order().date())
            .thenComparing(r -> r.order().id());

    // L'allocation MODIFIE le stock et depend de l'ordre de passage : une boucle, pas un map() avec effet de bord.
    Result allocate(Order o, Customer c) {
        List<Allocation> allocations = new ArrayList<>();
        List<String> unknown = new ArrayList<>();
        for (Line l : o.lines()) {
            Product p = products.get(l.sku());
            if (p == null) {
                unknown.add(l.sku());
                continue;
            }
            int served = Math.min(l.quantity(), stock.get(l.sku()));
            stock.merge(l.sku(), -served, Integer::sum);
            allocations.add(new Allocation(p, served, l.quantity() - served));
        }
        return new Result(o, c, List.copyOf(allocations), List.copyOf(unknown));
    }

    void run() {
        // 1. Refus : client inconnu (l'Optional vide est filtre, son absence est signalee).
        orders.stream().filter(o -> customer(o.customerId()).isEmpty())
                .forEach(o -> System.out.println("REFUS " + o.id() + " : client inconnu " + o.customerId()));

        // 2. Ordre de traitement : on associe chaque commande a son client PUIS on trie.
        List<Result> queue = orders.stream()
                .flatMap(o -> customer(o.customerId()).map(c -> new Result(o, c, List.of(), List.of())).stream())
                .sorted(PRIORITY)
                .toList();
        System.out.println("TRAITEMENT : " + queue.stream().map(r -> r.order().id()).collect(joining(", ")));

        for (Result pending : queue) {
            Result r = allocate(pending.order(), pending.customer());
            results.add(r);
            String missing = r.allocations().stream().filter(a -> a.missing() > 0)
                    .map(a -> a.product().sku() + "x" + a.missing()).collect(joining(", "));
            String unknown = r.unknownSkus().isEmpty() ? "" : " | inconnu " + String.join(", ", r.unknownSkus());
            System.out.println(r.order().id() + " " + r.customer().name() + " (" + r.customer().tier() + ") : "
                    + r.status() + " " + money(r.cents()) + (missing.isEmpty() ? "" : " | manque " + missing) + unknown);
        }

        // 3. Avis aux clients non servis completement : email ou courrier (orElseGet : texte construit seulement si besoin).
        results.stream().filter(r -> r.status() != Status.COMPLETE).forEach(r -> System.out.println(
                r.customer().email().map(e -> "AVIS " + e)
                        .orElseGet(() -> "AVIS par courrier a " + r.customer().name())
                        + " : " + r.order().id() + " " + r.status()));

        // 4. Bons de livraison numerotes, seulement pour ce qui part vraiment.
        List<Result> shipped = results.stream().filter(r -> r.status() != Status.REFUSEE).toList();
        System.out.println("BONS : " + IntStream.rangeClosed(1, shipped.size())
                .mapToObj(i -> String.format("BL-%03d=%s", i, shipped.get(i - 1).order().id()))
                .collect(joining(" ")));

        report(shipped);
    }

    void report(List<Result> shipped) {
        // EnumMap::new : cles dans l'ordre de l'enum ; TreeSet en aval : ids tries.
        Map<Status, TreeSet<String>> byStatus = results.stream().collect(groupingBy(Result::status,
                () -> new EnumMap<>(Status.class), mapping(r -> r.order().id(), toCollection(TreeSet::new))));
        System.out.println("STATUTS : " + byStatus);

        // flatMap : on passe des commandes a leurs allocations, puis on regroupe les manques par article.
        Map<String, Integer> backorder = results.stream().flatMap(r -> r.allocations().stream())
                .filter(a -> a.missing() > 0)
                .collect(groupingBy(a -> a.product().sku(), summingInt(Allocation::missing)));
        System.out.println("A RECOMMANDER : " + backorder.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(e -> e.getKey() + "x" + e.getValue())
                .collect(joining(", ")));

        Map<String, String> byRegion = shipped.stream().collect(groupingBy(r -> r.customer().region(), TreeMap::new,
                collectingAndThen(summingLong(Result::cents), Warehouse::money)));
        System.out.println("CA PAR REGION : " + byRegion);

        Map<Boolean, Long> gold = shipped.stream()
                .collect(partitioningBy(r -> r.customer().tier() == Tier.GOLD, summingLong(Result::cents)));
        System.out.println("CA GOLD : " + money(gold.get(true)) + ", STANDARD : " + money(gold.get(false)));

        Map<String, Integer> units = shipped.stream().flatMap(r -> r.allocations().stream())
                .collect(groupingBy(a -> a.product().category(), TreeMap::new, summingInt(Allocation::served)));
        System.out.println("UNITES PAR CATEGORIE : " + units);

        LongSummaryStatistics stats = shipped.stream().mapToLong(Result::cents).summaryStatistics();
        System.out.println("PANIERS : " + stats.getCount() + " expedies, min " + money(stats.getMin())
                + ", max " + money(stats.getMax()) + ", total " + money(stats.getSum()));

        String best = shipped.stream().collect(teeing(
                Collectors.averagingLong(Result::cents),
                Collectors.maxBy(Comparator.comparingLong(Result::cents)),
                (avg, max) -> "PANIER MOYEN : " + money(Math.round(avg)) + ", plus gros : "
                        + max.map(r -> r.order().id() + " " + money(r.cents())).orElse("-")));
        System.out.println(best);

        // Montant par client, puis les 2 meilleurs (egalite -> nom).
        Map<String, Long> perCustomer = shipped.stream()
                .collect(groupingBy(r -> r.customer().name(), summingLong(Result::cents)));
        System.out.println("TOP CLIENTS : " + perCustomer.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(2)
                .map(e -> e.getKey() + " " + money(e.getValue()))
                .collect(joining(", ")));

        System.out.println("RUPTURES : " + stock.entrySet().stream().filter(e -> e.getValue() == 0)
                .map(Map.Entry::getKey).collect(joining(", ")));
        System.out.println("STOCK RESTANT : " + stock.entrySet().stream().filter(e -> e.getValue() > 0)
                .map(e -> e.getKey() + "=" + e.getValue()).collect(joining(", ")));

        // Controle : la valeur expediee + la valeur restante = la valeur initiale du stock.
        long initial = Data.PRODUCTS.stream().map(l -> l.split(";"))
                .mapToLong(p -> Long.parseLong(p[3]) * Long.parseLong(p[4].replace(".", ""))).sum();
        long remaining = stock.entrySet().stream().mapToLong(e -> e.getValue() * products.get(e.getKey()).cents()).sum();
        long sent = shipped.stream().map(Result::cents).reduce(0L, Long::sum);
        System.out.println("CONTROLE : " + money(initial) + " = " + money(sent) + " expedies + " + money(remaining)
                + " en stock : " + (initial == sent + remaining ? "oui" : "non"));
    }

    public static void main(String[] args) {
        new Warehouse().run();
    }
}
