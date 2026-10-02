package ch8_lambdas.projects.p03_events;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON EventsApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "  0 | C1 arrive, file 1",
            "  0 | C1 au guichet apres 0 min, pour 7 min",
            "  5 | C2 arrive, file 1",
            "  5 | C2 au guichet apres 0 min, pour 5 min",
            "  7 | C1 repart",
            "  7 | C3 arrive, file 1",
            "  7 | C3 au guichet apres 0 min, pour 11 min",
            " 10 | C2 repart",
            " 10 | C4 arrive, file 1",
            " 10 | C4 au guichet apres 0 min, pour 9 min",
            " 12 | C5 arrive, file 1",
            " 17 | C6 arrive, file 2",
            " 18 | C3 repart",
            " 18 | C5 au guichet apres 6 min, pour 5 min",
            "...",
            "57 evenements, 3 attentes de 10 min ou plus",
            "19 clients servis, attente moyenne 4.5 min, max 12 min, file max 4, occupation 94 %, fermeture reelle 78",
            "runnable : fermeture de l'agence");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SEED", "Data.TELLERS", "Runnable", "IntSupplier",
            "Supplier<String>", "BiConsumer<Integer, String>", ".andThen(", "this::arrival",
            ".getAsInt()", ".accept(", ".run()", "record Event(",
            "class EventQueue",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "EventsApp", args, EXPECTED, API);
    }
}
