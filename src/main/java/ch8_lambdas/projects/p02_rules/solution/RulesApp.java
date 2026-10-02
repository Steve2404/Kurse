package ch8_lambdas.projects.p02_rules.solution;

import ch8_lambdas.projects.p02_rules.Data;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * SOLUTION du projet 2 - le moteur de regles.
 */
public class RulesApp {

    public static void main(String[] args) {
        String[] names = new String[Data.RULES.length];
        Rule[] compiled = new Rule[Data.RULES.length];
        for (int i = 0; i < Data.RULES.length; i++) {
            String[] p = Data.RULES[i].split(" = ");
            names[i] = p[0];
            // Reference de methode sur un objet precis : le Predicate compile est adapte en Rule.
            compiled[i] = RuleParser.compile(p[1])::test;
        }
        StringBuilder header = new StringBuilder(String.format("%-15s", "mot de passe"));
        for (String n : names) {
            header.append(String.format("%-8s", n));
        }
        System.out.println(header.toString().stripTrailing());
        for (String c : Data.CANDIDATES) {
            StringBuilder line = new StringBuilder(String.format("%-15s", c));
            for (Rule rule : compiled) {
                line.append(String.format("%-8s", rule.test(c) ? "oui" : "-"));
            }
            System.out.println(line.toString().stripTrailing());
        }

        // BiPredicate : deux parametres. On le "fixe" sur l'utilisateur pour obtenir un Predicate.
        BiPredicate<String, String> containsUser = (pwd, user) -> pwd.toLowerCase().contains(user.toLowerCase());
        String user = Data.USER;
        Predicate<String> notUser = pwd -> !containsUser.test(pwd, user);
        Predicate<String> strongForUser = RuleParser.compile("len>=8 & upper & lower & digit & ! space").and(notUser);
        StringBuilder ok = new StringBuilder("forts et sans \"" + user + "\" :");
        for (String c : Data.CANDIDATES) {
            if (strongForUser.test(c)) {
                ok.append(' ').append(c);
            }
        }
        System.out.println(ok);

        Predicate<String> empty = String::isEmpty;
        Predicate<String> notEmpty = Predicate.not(empty);
        Predicate<String> isSecret = Predicate.isEqual("secret");
        System.out.println("utilitaires : " + notEmpty.test("") + " " + notEmpty.test("x") + " " + isSecret.test("secret") + " " + isSecret.negate().test("secret")
                + " " + containsUser.negate().test("ALICE!", "alice"));

        // Les correctifs, essayes dans l'ordre jusqu'a ce que la regle STRONG passe (5 tours au plus).
        Fix[] fixes = {
                new Fix("sans espace", s -> s.contains(" "), s -> s.replace(" ", "")),
                new Fix("+chiffre", s -> !RuleParser.has(s, Character::isDigit), s -> s + "7"),
                new Fix("+majuscule", s -> !RuleParser.has(s, Character::isUpperCase), s -> Character.toUpperCase(s.charAt(0)) + s.substring(1)),
                new Fix("+minuscule", s -> !RuleParser.has(s, Character::isLowerCase), s -> s + "x"),
                new Fix("allonge", s -> s.length() < 8, s -> s + "#".repeat(8 - s.length()))};
        Predicate<String> strong = RuleParser.compile("len>=8 & upper & lower & digit & ! space");
        for (String c : new String[] {"motdepasse", "court1", "Mot De Passe 9", "ab", "OK"}) {
            String fixed = c;
            StringBuilder applied = new StringBuilder();
            for (int round = 0; round < 5 && !strong.test(fixed); round++) {
                for (Fix f : fixes) {
                    if (f.problem().test(fixed)) {
                        fixed = f.repair().apply(fixed);
                        applied.append(applied.length() == 0 ? "" : ", ").append(f.name());
                        break;               // un seul correctif par tour, puis on reteste
                    }
                }
            }
            System.out.println("corrige \"" + c + "\" -> \"" + fixed + "\" (" + applied + ") " + strong.test(fixed));
        }
    }
}
