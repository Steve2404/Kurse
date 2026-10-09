package ch18_design.projects.p09_cheese;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 9, le capstone (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("DecayRule.java", "int loss = sellIn < 0 ? baseLoss * 2 : baseLoss;", "int loss = sellIn <= 0 ? baseLoss * 2 : baseLoss;"),
            new Mutant("DecayRule.java", "Math.max(0, cheese.quality() - loss)", "Math.max(1, cheese.quality() - loss)"),
            new Mutant("AgingRule.java", "Math.min(MAX_QUALITY, quality + gain)", "Math.min(MAX_QUALITY + 1, quality + gain)"),
            new Mutant("AgingRule.java", "return Math.max(quality, Math.min(MAX_QUALITY, quality + gain));", "return Math.min(MAX_QUALITY, quality + gain);"),
            new Mutant("AgedRule.java", "cheese.sellIn() - 1 < 0 ? 2 : 1", "cheese.sellIn() < 0 ? 2 : 1"),
            new Mutant("TicketRule.java", "(sellIn < 10 ? 1 : 0)", "(sellIn <= 10 ? 1 : 0)"),
            new Mutant("TicketRule.java", "(sellIn < 5 ? 1 : 0)", "(sellIn < 6 ? 1 : 0)"),
            new Mutant("TicketRule.java", "if (sellIn < 0) {", "if (sellIn < -1) {"),
            new Mutant("RuleBook.java", "\"Brie\", new DecayRule(2)", "\"Brie\", new DecayRule(1)"),
            new Mutant("RuleBook.java", "static final AgingRule SALT = cheese -> cheese;", "static final AgingRule SALT = cheese -> cheese.next(cheese.quality());"),
            new Mutant("RuleBook.java", "name.substring(BIO.length())", "name.substring(BIO.length() - 1)"),
            new Mutant("BioRule.java", "cheese.quality() - 2 * lost", "cheese.quality() - lost - 1"),
            new Mutant("BioRule.java", "return lost > 0 ?", "return lost != 0 ?"),
            new Mutant("CheeseShop.java", "day < days", "day <= days"),
            new Mutant("Cheese.java", "if (quality < 0) {", "if (quality < -1) {"));

    static final List<String> API_CODE = List.of(
            "record Cheese(", "interface AgingRule", "final class DecayRule implements AgingRule", "final class AgedRule implements AgingRule",
            "final class TicketRule implements AgingRule", "final class BioRule implements AgingRule", "final class RuleBook",
            "final class CheeseShop", "Map<String, AgingRule>", "static AgingRule ruleFor(String", "->",
            "!switch", "!instanceof", "!Legacy", "!else if",
            "max:method=8",
            "in:CheeseShop.java!\"##la cave ne connait aucun nom de produit",
            "in:DecayRule.java!equals(##une regle ne teste pas le nom du produit",
            "in:AgedRule.java!equals(##une regle ne teste pas le nom du produit",
            "in:TicketRule.java!equals(##une regle ne teste pas le nom du produit",
            "in:BioRule.java=private final AgingRule inner##le bio enveloppe une autre regle (decorateur)");

    static final List<String> API_TESTS = List.of(
            "Data.LegacyCheeseShop", "Data.LegacyItem", "@RepeatedTest", "new SplittableRandom(", "@ParameterizedTest",
            "CheeseShop.afterDays(", "\"Bio ", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 100, MUTANTS, API_CODE, API_TESTS);
    }
}
