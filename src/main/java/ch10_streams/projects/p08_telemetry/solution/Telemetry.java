package ch10_streams.projects.p08_telemetry.solution;

import ch10_streams.projects.p08_telemetry.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.TreeMap;
import java.util.function.BooleanSupplier;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 8 - une conception possible.
 */
public class Telemetry {

    record Sample(String server, String region, long epoch, double cpu, int memMb, long bytes) {
        static Sample parse(String line) {
            String[] p = line.split(";");
            return new Sample(p[0], p[1], Long.parseLong(p[2]), Double.parseDouble(p[3]),
                    Integer.parseInt(p[4]), Long.parseLong(p[5]));
        }
    }

    // Une regle = une mesure primitive (ToDoubleFunction, pas de boxing) + un test primitif (DoublePredicate).
    record Rule(String name, double threshold, ToDoubleFunction<Sample> measure) {
        DoublePredicate triggered() {
            return v -> v > threshold;
        }

        boolean test(Sample s) {
            return triggered().test(measure.applyAsDouble(s));
        }
    }

    static final Map<String, ToDoubleFunction<Sample>> MEASURES = Map.of(
            "CPU", Sample::cpu,
            "MEMOIRE", Sample::memMb,
            "OCTETS", Sample::bytes);

    // Les fonctions primitives du projet, nommees par leur type : c'est ce type que l'examen demande de reconnaitre.
    static final LongFunction<String> HHMM = t -> LocalTime.ofSecondOfDay(t % 86_400).toString();
    static final ToIntFunction<Sample> MEMORY = Sample::memMb;
    static final ToLongFunction<Sample> BYTES = Sample::bytes;
    static final IntBinaryOperator MAX = Math::max;
    static final LongUnaryOperator MB_TO_BYTES = mb -> mb * 1_048_576L;
    static final DoubleToIntFunction DECILE = c -> (int) (c / 10);
    static final ObjIntConsumer<StringBuilder> APPEND_DIGIT = StringBuilder::append;
    static final IntToLongFunction MINUTE_TO_EPOCH = i -> Data.WINDOW_START + i * Data.STEP_SECONDS;
    static final IntFunction<String[]> NEW_ARRAY = String[]::new;

    private final List<Sample> samples;
    private final List<Rule> rules;
    private final BooleanSupplier maintenanceOn = () -> !Data.MAINTENANCE_REGION.isBlank();

    Telemetry() {
        samples = Data.SAMPLES.stream().map(Sample::parse).toList();
        rules = Data.RULES.stream().map(l -> l.split(";"))
                .map(p -> new Rule(p[0], Double.parseDouble(p[1]), MEASURES.get(p[0])))
                .toList();
    }

    // Stream.empty() : une region inconnue donne un flux vide, jamais null.
    Stream<Sample> region(String name) {
        boolean known = samples.stream().anyMatch(s -> s.region().equals(name));
        return known ? samples.stream().filter(s -> s.region().equals(name)) : Stream.empty();
    }

    // Une decimale sans Locale (chapitre 11) : Math.round (chapitre 4), et Double.toString ecrit toujours un point.
    static String fmt(double d) {
        return String.valueOf(Math.round(d * 10) / 10.0);
    }

    void overview() {
        // distinct garde la 1re occurrence ; toArray(IntFunction<String[]>) donne un VRAI String[] (pas un Object[]).
        String[] servers = samples.stream().map(Sample::server).distinct().toArray(NEW_ARRAY);
        System.out.println("SERVEURS : " + Arrays.toString(servers));

        List<String> first = new ArrayList<>();
        samples.stream().filter(s -> s.epoch() == Data.WINDOW_START).forEach(s -> first.add(s.server() + "@" + fmt(s.cpu())));
        System.out.println("PREMIERS : " + String.join(" ", first));

        // findAny : "n'importe lequel" ; le resultat est previsible ici seulement parce qu'UN SEUL element passe le filtre.
        samples.stream().filter(s -> s.server().equals("db1") && s.cpu() > 90).findAny()
                .ifPresent(s -> System.out.println("UNIQUE db1 > 90% (findAny) : " + fmt(s.cpu()) + " a " + HHMM.apply(s.epoch())));

        System.out.println("REGIONS DISTINCTES : " + samples.stream().map(Sample::region).distinct().count());
    }

    void longs() {
        // Les octets depassent Integer.MAX_VALUE : LongStream de bout en bout ; max() rend un OptionalLong.
        long total = samples.stream().mapToLong(BYTES).sum();
        OptionalLong peak = samples.stream().mapToLong(BYTES).max();
        Sample peakSample = samples.stream().filter(s -> s.bytes() == peak.getAsLong()).findFirst().orElseThrow();
        System.out.println("OCTETS : total " + total + ", pic " + peak.getAsLong() + " (" + peakSample.server()
                + " a " + HHMM.apply(peakSample.epoch()) + ")");

        // IntStream -> LongStream AVANT la multiplication : en int, 113 650 Mo * 1 048 576 deborderait.
        long memBytes = samples.stream().mapToInt(MEMORY).asLongStream().map(MB_TO_BYTES).sum();
        int maxMem = samples.stream().mapToInt(MEMORY).reduce(Integer.MIN_VALUE, MAX);
        System.out.println("MEMOIRE : max " + maxMem + " Mo, cumul " + memBytes + " octets");

        // Deux facons d'obtenir le calendrier des minutes attendues : elles doivent coincider.
        int minutes = (int) ((Data.WINDOW_END - Data.WINDOW_START) / Data.STEP_SECONDS);
        long[] fromInts = IntStream.rangeClosed(0, minutes).mapToLong(MINUTE_TO_EPOCH).toArray();
        long[] iterated = LongStream.iterate(Data.WINDOW_START, t -> t <= Data.WINDOW_END, t -> t + Data.STEP_SECONDS).toArray();
        System.out.println("CALENDRIER : " + fromInts.length + " instants de " + HHMM.apply(fromInts[0]) + " a "
                + HHMM.apply(fromInts[fromInts.length - 1]) + " (identique a LongStream.iterate : " + Arrays.equals(fromInts, iterated) + ")");

        // Trous : les minutes attendues qu'aucun echantillon du serveur ne couvre (LongPredicate).
        Arrays.stream(samples.stream().map(Sample::server).distinct().toArray(NEW_ARRAY)).forEach(server -> {
            LongPredicate missing = t -> samples.stream().noneMatch(s -> s.server().equals(server) && s.epoch() == t);
            String gaps = LongStream.of(iterated).filter(missing).mapToObj(HHMM).collect(Collectors.joining(", "));
            if (!gaps.isEmpty()) {
                System.out.println("TROUS " + server + " : " + gaps);
            }
        });
        OptionalLong last = samples.stream().mapToLong(Sample::epoch).max();
        System.out.println("DERNIER INSTANT : " + HHMM.apply(last.getAsLong()));
    }

    void doubles() {
        DoubleSummaryStatistics cpu = samples.stream().collect(Collectors.summarizingDouble(Sample::cpu));
        System.out.println("CPU : " + cpu.getCount() + " mesures, min " + fmt(cpu.getMin()) + ", max " + fmt(cpu.getMax())
                + ", moy " + fmt(cpu.getAverage()));

        Map<String, String> byRegion = samples.stream().collect(Collectors.groupingBy(Sample::region, TreeMap::new,
                Collectors.collectingAndThen(Collectors.averagingDouble(Sample::cpu), Telemetry::fmt)));
        System.out.println("CPU MOYEN PAR REGION : " + byRegion);

        // summingDouble rend un Double ; les CPU sont des multiples de 0.5 -> sommes exactes en binaire.
        Map<String, Double> cpuMinutes = samples.stream()
                .collect(Collectors.groupingBy(Sample::server, TreeMap::new, Collectors.summingDouble(Sample::cpu)));
        System.out.println("CPU CUMULE PAR SERVEUR : " + cpuMinutes);

        // Stream.empty() + average() -> OptionalDouble vide : rien a moyenner n'est pas "0".
        OptionalDouble empty = region(Data.EMPTY_REGION).mapToDouble(Sample::cpu).average();
        System.out.println("REGION " + Data.EMPTY_REGION + " : " + region(Data.EMPTY_REGION).count()
                + " echantillon, cpu moyen " + (empty.isPresent() ? fmt(empty.getAsDouble()) : "absent"));
    }

    void alerts() {
        Map<String, Integer> alertsPerServer = new TreeMap<>();
        samples.stream().map(Sample::server).distinct().forEach(s -> alertsPerServer.put(s, 0));
        for (Rule rule : rules) {
            List<Sample> hits = samples.stream().filter(rule::test).toList();
            hits.forEach(s -> alertsPerServer.merge(s.server(), 1, Integer::sum));
            // BooleanSupplier : la condition de maintenance n'est evaluee qu'au moment ou on en a besoin.
            Map<Boolean, List<String>> split = hits.stream().collect(Collectors.partitioningBy(
                    s -> maintenanceOn.getAsBoolean() && s.region().equals(Data.MAINTENANCE_REGION),
                    Collectors.mapping(Sample::server, Collectors.toList())));
            System.out.println("ALERTE " + rule.name() + ">" + (long) rule.threshold() + " : "
                    + split.get(false).size() + " " + names(split.get(false)) + " | masquees " + split.get(true).size()
                    + " " + names(split.get(true)));
        }

        // Composition de fonctions primitives : penalite, puis score, puis plancher.
        IntUnaryOperator penalty = n -> n * Data.PENALTY_PER_ALERT;
        IntUnaryOperator health = penalty.andThen(p -> 100 - p).andThen(h -> Math.max(h, Data.MIN_HEALTH));
        System.out.println("SANTE : " + alertsPerServer.entrySet().stream()
                .map(e -> e.getKey() + "=" + health.applyAsInt(e.getValue()))
                .collect(Collectors.joining(", ")));
    }

    static String names(List<String> servers) {
        return servers.isEmpty() ? "(-)" : servers.stream().distinct().sorted().collect(Collectors.joining(", ", "(", ")"));
    }

    void profiles() {
        // DoubleStream -> IntStream (DoubleToIntFunction) puis IntStream.collect a 3 arguments (ObjIntConsumer).
        String profiles = Arrays.stream(samples.stream().map(Sample::server).distinct().toArray(NEW_ARRAY))
                .map(server -> server + "=" + samples.stream().filter(s -> s.server().equals(server))
                        .mapToDouble(Sample::cpu)
                        .mapToInt(DECILE)
                        .collect(StringBuilder::new, APPEND_DIGIT, StringBuilder::append))
                .collect(Collectors.joining(" "));
        System.out.println("PROFILS CPU (deciles) : " + profiles);

        // Une lambda ne peut pas garder d'etat modifiable : la classe anonyme, si.
        IntSupplier backoff = new IntSupplier() {
            private int next = 1;

            @Override
            public int getAsInt() {
                int current = next;
                next *= 2;
                return current;
            }
        };
        System.out.println("RELANCES SONDE cache1 : " + IntStream.generate(backoff).limit(5)
                .mapToObj(Integer::toString).collect(Collectors.joining(" ")) + " s");
    }

    // Le remede au stream a usage unique : un Supplier qui fabrique un flux NEUF a chaque appel.
    void reuse() {
        Supplier<Stream<Sample>> fresh = samples::stream;
        System.out.println("SUPPLIER : " + fresh.get().count() + " puis " + fresh.get().filter(s -> s.cpu() > 90).count());
    }

    public static void main(String[] args) {
        Telemetry t = new Telemetry();
        t.overview();
        t.longs();
        t.doubles();
        t.alerts();
        t.profiles();
        t.reuse();
    }
}
