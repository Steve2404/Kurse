package ch8_lambdas.projects.p02_rules;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON RulesApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "mot de passe   STRONG  LENIENT ADMIN   NOJAVA",
            "motdepasse     -       -       -       oui",
            "MotDePasse1    oui     oui     -       oui",
            "court1         -       oui     -       oui",
            "Mot De Passe 9 -       oui     -       oui",
            "adminX         -       oui     oui     oui",
            "Secret2026!!   oui     oui     oui     oui",
            "javaRocks7A    oui     oui     -       -",
            "Alice2026Pw    oui     oui     -       oui",
            "forts et sans \"alice\" : MotDePasse1 Secret2026!! javaRocks7A",
            "utilitaires : false true true false false",
            "corrige \"motdepasse\" -> \"Motdepasse7\" (+chiffre, +majuscule) true",
            "corrige \"court1\" -> \"Court1##\" (+majuscule, allonge) true",
            "corrige \"Mot De Passe 9\" -> \"MotDePasse9\" (sans espace) true",
            "corrige \"ab\" -> \"Ab7#####\" (+chiffre, +majuscule, allonge) true",
            "corrige \"OK\" -> \"OK7x####\" (+chiffre, +minuscule, allonge) true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.RULES", "Data.CANDIDATES", "Predicate<String>", ".or(",
            ".and(", ".negate()", "Predicate.not(", "Predicate.isEqual(",
            "BiPredicate<String, String>", "interface Rule extends Predicate<String>", "::test", "Character::isDigit",
            "UnaryOperator<String> repair", "@FunctionalInterface",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "RulesApp", args, EXPECTED, API);
    }
}
