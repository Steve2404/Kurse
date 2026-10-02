package ch7_beyondclasses.projects.p05_tree;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON TreeApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "infixe : 5 10 20 30 35 40 45 50 60 65 70 80 (12 valeurs, hauteur 5, 3e plus petite 20)",
            "        80",
            "    70",
            "            65",
            "        60",
            "50",
            "            45",
            "        40",
            "            35",
            "    30",
            "        20",
            "            10",
            "                5",
            "par niveaux : 50 | 30 70 | 20 40 60 80 | 10 35 45 65 | 5",
            "entre 33 et 66 : 6 ; plancher(42) 40, plafond(42) 45, plancher(4) -2147483648 ; ancetre(35, 45) 40, ancetre(5, 65) 50",
            "somme 510, profondeur max 4",
            "suppressions : 30=true 50=true 99=false -> 60 | 35 70 | 20 40 65 80 | 10 45 | 5",
            "equilibre : hauteur 5 -> 4 ; 40 | 10 65 | 5 20 45 70 | 35 60 80");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.VALUES", "Data.DELETE", "public interface Visitor", "private static class Node",
            "public static class Builder", "public class Cursor", "SortedTree.this.root", "class RangeCounter",
            "tree.new Cursor()", "new SortedTree.Builder()", "2xnew SortedTree.Visitor()",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "TreeApp", args, EXPECTED, API);
    }
}
