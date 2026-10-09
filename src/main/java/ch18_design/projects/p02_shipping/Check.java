package ch18_design.projects.p02_shipping;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("PostRate.java", "if (grams <= 500) {", "if (grams < 500) {"),
            new Mutant("PostRate.java", "(grams - 5000 + 999) / 1000 * 120", "(grams - 5000) / 1000 * 120"),
            new Mutant("PostRate.java", "price * 2", "price + 750"),
            new Mutant("ExpressRate.java", "price + 1500", "price + 1000"),
            new Mutant("ExpressRate.java", "parcel.grams() <= 30_000", "parcel.grams() <= 31_000"),
            new Mutant("PickupRate.java", "Set.of(\"FR\", \"BE\")", "Set.of(\"FR\")"),
            new Mutant("PickupRate.java", "parcel.grams() <= 20_000", "parcel.grams() < 20_000"),
            new Mutant("Parcel.java", "(grams + 999) / 1000", "grams / 1000 + 1"),
            new Mutant("Surcharges.java", "(base * 15 + 50) / 100", "base * 15 / 100"),
            new Mutant("Surcharges.java", "\"IT\", \"LU\", \"NL\"", "\"IT\", \"NL\""),
            new Mutant("ShippingCalculator.java", "Comparator.comparingLong(Quote::priceCents).thenComparing(Quote::carrier)", "Comparator.comparing(Quote::carrier)"),
            new Mutant("ShippingCalculator.java", "putIfAbsent(rate.code(), rate) != null", "putIfAbsent(rate.code(), rate) != null && false"),
            new Mutant("ShippingCalculator.java", "if (!rate.accepts(parcel)) {", "if (false) {"),
            new Mutant("ShippingCalculator.java", "return base + surcharges.stream().mapToLong(s -> s.amount(parcel, base)).sum();",
                    "long total = base;\n        for (Surcharge s : surcharges) {\n            total += s.amount(parcel, total);\n        }\n        return total;"),
            new Mutant("FreightRate.java", "parcel.grams() > 30_000", "parcel.grams() >= 30_000"),
            new Mutant("FreightRate.java", "(parcel.grams() - 30_000 + 999) / 1000 * 90L", "(parcel.grams() - 30_000) / 1000 * 90L"));

    static final List<String> API_CODE = List.of(
            "record Parcel(", "interface ShippingRate", "@FunctionalInterface", "interface Surcharge", "record Quote(",
            "final class PostRate", "final class ExpressRate", "final class PickupRate", "final class FreightRate",
            "final class Surcharges", "final class ShippingCalculator", "final class Shop", "Optional<Quote> cheapest(",
            "List<Quote> quotes(", "Comparator.", "->",
            "!switch", "!instanceof", "!catch (", "!LegacyShipping",
            "max:method=10",
            "in:ShippingCalculator.java!PostRate##le calculateur ne nomme aucun transporteur (PostRate)",
            "in:ShippingCalculator.java!PickupRate##le calculateur ne nomme aucun transporteur (PickupRate)",
            "in:ShippingCalculator.java!FreightRate##le calculateur ne nomme aucun transporteur (FreightRate)",
            "in:ShippingCalculator.java!Surcharges.##le calculateur ne nomme aucune surcharge (Surcharges.)",
            "in:Shop.java=new FreightRate()");

    static final List<String> API_TESTS = List.of(
            "Data.LegacyShipping", "@RepeatedTest", "new SplittableRandom(", "@ParameterizedTest", "assertThrows(",
            "new ShippingRate()", "new ShippingCalculator(", "re:Surcharge\\s+\\w+\\s*=\\s*\\(##une Surcharge ecrite en lambda",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 100, MUTANTS, API_CODE, API_TESTS);
    }
}
