package ch8_lambdas.projects.p06_sorting;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON SortingApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "par nom : Adam Bob Emma Hugo Ines Lea Lina Noah Theo Zoe (25 comparaisons)",
            "par age puis nom : Hugo Zoe Emma Lina Ines Lea Theo Bob Adam Noah (24)",
            "score decroissant puis age : Emma Hugo Adam Lina Lea Bob Theo Ines Noah Zoe (25)",
            "stabilite : par ville apres par nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina = ville puis nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina",
            "insertion true : 24 comparaisons contre 25 pour la fusion",
            "top 3 : Emma Adam Hugo (24 comparaisons)",
            "dichotomie par age : 29->2(Emma) 42->8(Adam) 30->-5",
            "plus proches de 30 ans : Emma Ines Lea Lina ; ordre inverse du nom : Zoe Theo Noah");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.PEOPLE", "Data.AGES", "@FunctionalInterface", "interface Order",
            "default Order reversed()", "default Order then(", "static Order by(ToIntFunction<Person>", "static Order byText(Function<Person, String>",
            "Person::name", "Person::age", "comparisons++", "int target = 30",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "SortingApp", args, EXPECTED, API);
    }
}
