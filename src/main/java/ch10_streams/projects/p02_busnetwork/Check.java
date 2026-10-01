package ch10_streams.projects.p02_busnetwork;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BusNetwork, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "ARRETS : Centre, Gare, Hopital, Musee, Phare, Plage, Port, Stade, Universite",
            "PAGE 2 : Musee, Phare, Plage",
            "PAGE 4 : vide",
            "LIGNES : L1 (4 arrets, 15 min), L2 (4 arrets, 20 min), L4 (3 arrets, 16 min), L3 (3 arrets, 21 min)",
            "CIRCUIT L1 + L3 : Gare -> Centre -> Musee -> Port -> Plage -> Phare",
            "PROCHAIN L1 a Musee : 08:10 (7 horaires calcules)",
            "PROCHAIN L3 a Phare : 18:36 (16 horaires calcules)",
            "PROCHAIN L3 a Phare : plus de bus (17 horaires calcules)",
            "REFUS : L2 ne dessert pas Plage",
            "REFUS : ligne inconnue L9",
            "HORAIRES L4 a Gare : 22:01 22:16 22:31 22:46",
            "DIRECT Gare -> Port : L1 depart 07:00 arrivee 07:15",
            "DIRECT Centre -> Stade : L2 depart 08:17 arrivee 08:30",
            "DIRECT Stade -> Centre : aucun",
            "CORRESPONDANCE Universite -> Port : L2 07:10 -> Centre 07:17, L1 07:24 -> Port 07:35",
            "CORRESPONDANCE Hopital -> Port : L4 08:06 -> Gare 08:16, L1 08:20 -> Port 08:35",
            "CORRESPONDANCE Stade -> Plage : aucune",
            "ACCESSIBLE L1 : non (Musee)",
            "ACCESSIBLE L2 : oui",
            "TICKETS : T001, T002, T003",
            "TICKETS : T004, T005",
            "RESEAU : toutes les lignes ont au moins 4 arrets : non",
            "RESEAU : une ligne dessert Phare : oui",
            "RESEAU : aucune ligne ne part avant 05:00 : oui",
            "REFUS : commande inconnue RETARD");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Stream.iterate(", "Stream.generate(", "Stream.concat(", "Stream.ofNullable(",
            ".flatMap(", ".distinct()", ".sorted(", ".reversed()", ".thenComparing",
            ".skip(", ".limit(", ".peek(", ".dropWhile(", ".takeWhile(",
            ".findFirst()", ".min(", ".anyMatch(", ".allMatch(", ".noneMatch(",
            "Collectors.joining(", "Optional::stream");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BusNetwork", args, EXPECTED, API);
    }
}
