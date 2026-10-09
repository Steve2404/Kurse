package ch18_design.projects.p01_invoice;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Money.java", "(cents * percent + 50) / 100", "cents * percent / 100"),
            new Mutant("Money.java", "\"%d,%02d\"", "\"%d,%d\""),
            new Mutant("Labor.java", "(minutes + QUARTER - 1) / QUARTER", "(minutes + QUARTER) / QUARTER"),
            new Mutant("Labor.java", "\"%s (%d h %02d)\"", "\"%s (%d h %d)\""),
            new Mutant("InvoiceCalculator.java", "parts.cents() >= LOYALTY_THRESHOLD.cents()", "parts.cents() > LOYALTY_THRESHOLD.cents()"),
            new Mutant("InvoiceCalculator.java", "boolean eligible = customer.loyal() && ", "boolean eligible = "),
            new Mutant("InvoiceCalculator.java", "minutes > LONG_JOB_MINUTES", "minutes >= LONG_JOB_MINUTES"),
            new Mutant("InvoiceCalculator.java", "Money vat = net.percent(VAT_PERCENT);", "Money vat = parts.plus(labor).plus(fees).percent(VAT_PERCENT);"),
            new Mutant("InvoiceCalculator.java", "Money loyalty = loyaltyDiscount(customer, parts);", "Money loyalty = loyaltyDiscount(customer, parts.plus(fees));"),
            new Mutant("InvoicePrinter.java", "optional(out, \"Forfaits : \", totals.fees());", "row(out, \"Forfaits\", totals.fees());"),
            new Mutant("LineParser.java", "if (quantity < 1 || unit < 0) {", "if (unit < 0) {"),
            new Mutant("LineParser.java", "requireLength(line, fields, 3);\n        long amount", "long amount"));

    static final List<String> API_CODE = List.of(
            "record Money(long cents)", "sealed interface InvoiceLine", "record Part(", "record Labor(", "record Fee(",
            "enum Category", "record Customer(", "record InvoiceTotals(", "final class LineParser",
            "final class InvoiceCalculator", "final class InvoicePrinter", "final class Garage",
            "default int billedMinutes()", "!instanceof", "!LegacyGarage",
            "max:method=12",
            "in:InvoiceCalculator.java!format(##aucune mise en page dans le calcul (format)",
            "in:InvoicePrinter.java!percent(##aucun calcul de prix dans l'impression (percent)",
            "in:InvoiceCalculator.java!split(##aucune lecture de texte dans le calcul (split)");

    static final List<String> API_TESTS = List.of(
            "Data.LegacyGarage", "@RepeatedTest", "RepetitionInfo", "new Random(", "@ParameterizedTest", "assertThrows(",
            "new InvoiceCalculator()", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 100, MUTANTS, API_CODE, API_TESTS);
    }
}
