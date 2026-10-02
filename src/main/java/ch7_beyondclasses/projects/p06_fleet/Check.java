package ch7_beyondclasses.projects.p06_fleet;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON FleetApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "voiture Clio [ROAD@90] : inaccessible",
            "bateau Nautilus [SEA@40] : inaccessible",
            "amphibie Hippo [ROAD@60 SEA@15] : Ville > Village > Phare > Ile en 185.0 min",
            "avion Concorde [ROAD@30 AIR@600] : Ville > Ile en 7.0 min",
            "drone Bzz [AIR@80] : Ville > Ile en 52.5 min",
            "un seul objet : 60 amphibie pouet-tut true 15 Amphibian",
            "altitudes : 1000 120 ; klaxons : tut pouet-tut tut (3 roulants)",
            "casts surs : Clio=voiture Nautilus=non Hippo=voiture Concorde=non Bzz=non");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.LINKS", "Data.FROM", "enum Mode", "interface Drivable",
            "interface Sailable", "interface Flyable", "extends Car implements Sailable", "implements Flyable, Drivable",
            "((Drivable) this)", "((Sailable) this)", "((Flyable) this)", "(Amphibian) fleet[2]",
            "(Sailable) asObject", "Flyable[]", "Drivable[]", "super.horn()",
            "Mode.valueOf(",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "FleetApp", args, EXPECTED, API);
    }
}
