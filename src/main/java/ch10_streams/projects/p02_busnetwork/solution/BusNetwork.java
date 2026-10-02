package ch10_streams.projects.p02_busnetwork.solution;

import ch10_streams.projects.p02_busnetwork.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 2 - une conception possible.
 */
public class BusNetwork {

    // Un arret avec son decalage CUMULE depuis le premier arret (calcule une fois au chargement).
    record Stop(String name, int offset) {
    }

    // Un trajet concret : quelle ligne, a quelle heure on monte, a quelle heure on descend.
    record Trip(Line line, String from, LocalTime departure, String to, LocalTime arrival) {
    }

    record Line(String id, LocalTime first, LocalTime last, int frequency, List<Stop> stops) {

        static Line parse(String text) {
            String[] p = text.split(";");
            List<Stop> stops = new ArrayList<>();
            int cumulated = 0;
            // Somme cumulee : un etat qui avance d'element en element -> une boucle est plus honnete qu'un stream.
            for (String part : p[4].split(",")) {
                String[] s = part.split(":");
                cumulated += Integer.parseInt(s[1]);
                stops.add(new Stop(s[0], cumulated));
            }
            return new Line(p[0], LocalTime.parse(p[1]), LocalTime.parse(p[2]), Integer.parseInt(p[3]), List.copyOf(stops));
        }

        // iterate a 3 arguments (Java 9) : la condition d'arret fait partie de la source, le stream est FINI.
        Stream<LocalTime> departures() {
            return Stream.iterate(first, t -> !t.isAfter(last), t -> t.plusMinutes(frequency));
        }

        Optional<Stop> stop(String name) {
            return stops.stream().filter(s -> s.name().equals(name)).findFirst();
        }

        // "dessert from PUIS to" : l'ordre compte, un bus ne roule que dans un sens.
        boolean goesFromTo(String from, String to) {
            Optional<Stop> a = stop(from);
            Optional<Stop> b = stop(to);
            return a.isPresent() && b.isPresent() && a.orElseThrow().offset() < b.orElseThrow().offset();
        }

        // Paresseux : on genere les departs un par un et on s'arrete au premier qui convient.
        Optional<Trip> nextTrip(String from, String to, LocalTime notBefore) {
            int offFrom = stop(from).orElseThrow().offset();
            int offTo = stop(to).orElseThrow().offset();
            return departures()
                    .map(d -> new Trip(this, from, d.plusMinutes(offFrom), to, d.plusMinutes(offTo)))
                    .filter(t -> !t.departure().isBefore(notBefore))
                    .findFirst();
        }

        int duration() {
            return stops.get(stops.size() - 1).offset();
        }
    }

    private final Map<String, Line> lines = new LinkedHashMap<>();
    private int ticketCounter;

    BusNetwork(List<String> lineData) {
        lineData.stream().map(Line::parse).forEach(l -> lines.put(l.id(), l));
    }

    Optional<Line> findLine(String id) {
        return Optional.ofNullable(lines.get(id));
    }

    // Un refus est une reponse : la chaine Optional rend le resultat OU "REFUS : ...", sans if ni exception.
    // Le traitement (BiFunction, chapitre 8) n'est appele que si la ligne existe ET dessert l'arret.
    String withLineAndStop(String id, String stopName, BiFunction<Line, Stop, String> action) {
        return findLine(id)
                .map(l -> l.stop(stopName)
                        .map(s -> action.apply(l, s))
                        .orElse("REFUS : " + id + " ne dessert pas " + stopName))
                .orElse("REFUS : ligne inconnue " + id);
    }

    // flatMap : chaque ligne devient le flux de ses arrets ; distinct + sorted sur le flux aplati.
    Stream<String> allStops() {
        return lines.values().stream().flatMap(l -> l.stops().stream()).map(Stop::name).distinct().sorted();
    }

    void stops() {
        System.out.println("ARRETS : " + allStops().collect(Collectors.joining(", ")));
    }

    // skip AVANT limit : on saute les pages precedentes puis on en garde une.
    void page(int number, int size) {
        String content = allStops().skip((long) (number - 1) * size).limit(size).collect(Collectors.joining(", "));
        System.out.println("PAGE " + number + " : " + (content.isEmpty() ? "vide" : content));
    }

    // Piege : reversed() inverse TOUT le comparateur construit jusque-la -> on l'applique au 1er critere seul.
    void linesByComplexity() {
        Comparator<Line> order = Comparator.comparing((Line l) -> l.stops().size()).reversed()
                .thenComparingInt(Line::duration)
                .thenComparing(Line::id);
        System.out.println("LIGNES : " + lines.values().stream().sorted(order)
                .map(l -> l.id() + " (" + l.stops().size() + " arrets, " + l.duration() + " min)")
                .collect(Collectors.joining(", ")));
    }

    // concat garde l'ordre (L1 puis L3) ; distinct garde la 1re occurrence ; ofNullable ignore une ligne inconnue.
    void circuit(String a, String b) {
        System.out.println("CIRCUIT " + a + " + " + b + " : " + Stream.concat(Stream.ofNullable(lines.get(a)), Stream.ofNullable(lines.get(b)))
                .flatMap(l -> l.stops().stream())
                .map(Stop::name)
                .distinct()
                .collect(Collectors.joining(" -> ")));
    }

    // peek note les departs REELLEMENT generes : filter + findFirst arretent la generation au 1er trouve.
    // La lambda ne peut pas modifier une variable locale (effectivement finale) : elle remplit une liste.
    String next(Line l, Stop s, LocalTime time) {
        List<LocalTime> generated = new ArrayList<>();
        Optional<LocalTime> next = l.departures()
                .peek(generated::add)
                .map(d -> d.plusMinutes(s.offset()))
                .filter(a -> !a.isBefore(time))
                .findFirst();
        return "PROCHAIN " + l.id() + " a " + s.name() + " : " + next.map(LocalTime::toString).orElse("plus de bus")
                + " (" + generated.size() + " horaires calcules)";
    }

    // dropWhile/takeWhile : la liste est TRIEE, donc "avant from" est un prefixe et "apres to" un suffixe.
    String timetable(Line l, Stop s, LocalTime from, LocalTime to) {
        return "HORAIRES " + l.id() + " a " + s.name() + " : " + l.departures()
                .map(d -> d.plusMinutes(s.offset()))
                .dropWhile(a -> a.isBefore(from))
                .takeWhile(a -> !a.isAfter(to))
                .map(LocalTime::toString)
                .collect(Collectors.joining(" "));
    }

    static final Comparator<Trip> BY_ARRIVAL = Comparator.comparing(Trip::arrival);

    // Chaque ligne candidate donne 0 ou 1 trajet : flatMap(Optional::stream) puis min.
    void direct(String from, String to, LocalTime time) {
        Optional<Trip> best = lines.values().stream()
                .filter(l -> l.goesFromTo(from, to))
                .map(l -> l.nextTrip(from, to, time))
                .flatMap(Optional::stream)
                .min(BY_ARRIVAL.thenComparing(t -> t.line().id()));
        System.out.println("DIRECT " + from + " -> " + to + " : " + best
                .map(t -> t.line().id() + " depart " + t.departure() + " arrivee " + t.arrival())
                .orElse("aucun"));
    }

    record Connection(Trip first, Trip second) {
    }

    // Recherche exhaustive : ligne A depuis l'origine, chaque arret S apres l'origine, ligne B != A de S vers la destination.
    void connection(String from, String to, LocalTime time) {
        Optional<Connection> best = lines.values().stream()
                .filter(a -> a.stop(from).isPresent())
                .flatMap(a -> a.stops().stream()
                        .filter(s -> a.goesFromTo(from, s.name()))
                        .flatMap(s -> a.nextTrip(from, s.name(), time).stream())
                        .flatMap(leg1 -> lines.values().stream()
                                .filter(b -> !b.equals(a) && b.goesFromTo(leg1.to(), to))
                                .flatMap(b -> b.nextTrip(leg1.to(), to, leg1.arrival().plusMinutes(Data.TRANSFER_MINUTES)).stream())
                                .map(leg2 -> new Connection(leg1, leg2))))
                .min(Comparator.comparing((Connection c) -> c.second().arrival())
                        .thenComparing(c -> c.first().departure(), Comparator.reverseOrder()));
        System.out.println("CORRESPONDANCE " + from + " -> " + to + " : " + best
                .map(c -> c.first().line().id() + " " + c.first().departure() + " -> " + c.first().to() + " " + c.first().arrival()
                        + ", " + c.second().line().id() + " " + c.second().departure() + " -> " + to + " " + c.second().arrival())
                .orElse("aucune"));
    }

    // noneMatch pour la reponse, puis filter pour nommer les coupables.
    String accessible(Line l) {
        boolean ok = l.stops().stream().map(Stop::name).noneMatch(Data.NOT_ACCESSIBLE::contains);
        return "ACCESSIBLE " + l.id() + " : " + (ok ? "oui" : "non (" + l.stops().stream().map(Stop::name)
                .filter(Data.NOT_ACCESSIBLE::contains).collect(Collectors.joining(", ")) + ")");
    }

    // generate : source INFINIE et sans etat propre ; le compteur vit dans l'objet, donc la numerotation continue.
    void tickets(int count) {
        System.out.println("TICKETS : " + Stream.generate(() -> String.format("T%03d", ++ticketCounter))
                .limit(count)
                .collect(Collectors.joining(", ")));
    }

    void network() {
        System.out.println("RESEAU : toutes les lignes ont au moins 4 arrets : "
                + yesNo(lines.values().stream().allMatch(l -> l.stops().size() >= 4)));
        System.out.println("RESEAU : une ligne dessert Phare : "
                + yesNo(lines.values().stream().anyMatch(l -> l.stop("Phare").isPresent())));
        System.out.println("RESEAU : aucune ligne ne part avant 05:00 : "
                + yesNo(lines.values().stream().noneMatch(l -> l.first().isBefore(LocalTime.of(5, 0)))));
    }

    static String yesNo(boolean b) {
        return b ? "oui" : "non";
    }

    void execute(String command) {
        String[] p = command.split(" ");
        switch (p[0]) {
            case "ARRETS" -> stops();
            case "PAGE" -> page(Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            case "LIGNES" -> linesByComplexity();
            case "CIRCUIT" -> circuit(p[1], p[2]);
            case "PROCHAIN" -> System.out.println(withLineAndStop(p[1], p[2], (l, s) -> next(l, s, LocalTime.parse(p[3]))));
            case "HORAIRES" -> System.out.println(withLineAndStop(p[1], p[2],
                    (l, s) -> timetable(l, s, LocalTime.parse(p[3]), LocalTime.parse(p[4]))));
            case "DIRECT" -> direct(p[1], p[2], LocalTime.parse(p[3]));
            case "CORRESPONDANCE" -> connection(p[1], p[2], LocalTime.parse(p[3]));
            case "ACCESSIBLE" -> System.out.println(findLine(p[1]).map(this::accessible).orElse("REFUS : ligne inconnue " + p[1]));
            case "TICKETS" -> tickets(Integer.parseInt(p[1]));
            case "RESEAU" -> network();
            default -> System.out.println("REFUS : commande inconnue " + p[0]);
        }
    }

    public static void main(String[] args) {
        BusNetwork network = new BusNetwork(Data.LINES);
        Data.COMMANDS.forEach(network::execute);
    }
}
