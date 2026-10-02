package ch6_classdesign.drills.r09_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Vehicle(BMX) Bike(String)",
            "D02 : [BMX a 2 roues] 2 vehicule transport deux-roues",
            "D03 : [anonyme a 2 roues] 2",
            "D04 : 26\" 28\" 26\" true",
            "D05 : Bike[BMX] true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "abstract class Vehicle", "extends Vehicle", "this(", "super(name)",
            "super.describe()", "2xstatic String category()", "2xString kind = ", "final class Wheel",
            "public boolean equals(Object", "public int hashCode()",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
