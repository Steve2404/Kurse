package ch8_lambdas.projects.p02_rules.solution;

import java.util.function.Predicate;

/**
 * SOLUTION - compile une regle texte en Predicate&lt;String&gt; : chaque operateur devient and / or / negate.
 * or  := and ( "|" and )*
 * and := not ( "&amp;" not )*
 * not := "!" not | atom | "(" or ")"
 */
public class RuleParser {

    private final String[] tokens;
    private int pos;

    public RuleParser(String text) {
        tokens = text.strip().split(" +");
    }

    public static Predicate<String> compile(String text) {
        return new RuleParser(text).or();
    }

    private Predicate<String> or() {
        Predicate<String> p = and();
        while (pos < tokens.length && tokens[pos].equals("|")) {
            pos++;
            p = p.or(and());           // Predicate.or : vrai si l'un des deux est vrai
        }
        return p;
    }

    private Predicate<String> and() {
        Predicate<String> p = not();
        while (pos < tokens.length && tokens[pos].equals("&")) {
            pos++;
            p = p.and(not());          // court-circuit : le second n'est evalue que si le premier est vrai
        }
        return p;
    }

    private Predicate<String> not() {
        String t = tokens[pos++];
        if (t.equals("!")) {
            return not().negate();
        }
        if (t.equals("(")) {
            Predicate<String> inside = or();
            pos++;                     // ")"
            return inside;
        }
        return atom(t);
    }

    // Chaque atome devient une lambda ; les valeurs extraites (n, arg) sont capturees (effectively final).
    static Predicate<String> atom(String t) {
        if (t.startsWith("len>=")) {
            int n = Integer.parseInt(t.substring(5));
            return s -> s.length() >= n;
        }
        if (t.startsWith("len<=")) {
            int n = Integer.parseInt(t.substring(5));
            return s -> s.length() <= n;
        }
        int colon = t.indexOf(':');
        String arg = colon < 0 ? "" : t.substring(colon + 1);
        return switch (colon < 0 ? t : t.substring(0, colon)) {
            case "digit" -> s -> has(s, Character::isDigit);
            case "upper" -> s -> has(s, Character::isUpperCase);
            case "lower" -> s -> has(s, Character::isLowerCase);
            case "space" -> s -> s.contains(" ");
            case "starts" -> s -> s.startsWith(arg);
            case "ends" -> s -> s.endsWith(arg);
            case "contains" -> s -> s.contains(arg);
            default -> s -> false;
        };
    }

    // Une interface fonctionnelle ecrite a la main, non generique : un test sur un caractere.
    @FunctionalInterface
    interface CharTest {
        boolean test(char c);
    }

    static boolean has(String s, CharTest test) {
        for (char c : s.toCharArray()) {
            if (test.test(c)) {
                return true;
            }
        }
        return false;
    }
}
