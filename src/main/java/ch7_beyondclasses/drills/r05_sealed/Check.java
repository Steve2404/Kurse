package ch7_beyondclasses.drills.r05_sealed;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : voiture camion velo velo electrique camion",
            "D02 : 3.14 9.0",
            "D03 : true 3 false true 2",
            "D04 : true true",
            "D05 : true ouaf miaou",
            "D06 : prise 2 true false true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "sealed abstract class Vehicle permits", "final class Car extends Vehicle", "non-sealed class Truck", "sealed class Bike extends Vehicle permits EBike",
            "sealed interface Shape permits", "sealed interface Animal {", "isSealed()", "getPermittedSubclasses()",
            "class Lorry extends Truck", "non-sealed interface Electric extends Fuel", "sealed interface Liquid extends Fuel permits",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
