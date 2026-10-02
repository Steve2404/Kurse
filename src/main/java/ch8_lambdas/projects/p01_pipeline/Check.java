package ch8_lambdas.projects.p01_pipeline;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON PipelineApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "trim | squeeze | title : [Le Java Est Un Langage] [Aaabbbccccd] [Bonjour Le Monde]",
            "rle : [3 1l1e2 1J1a1v1a3 1e1s1t3 1u1n1 1l1a1n1g1a1g1e3 ] [3a3b4c1d] [1B1o1n1j1o1u1r1 1l1e1 1M1o1n1d1e]",
            "rle | unrle : [   le  Java   est   un langage   ] [aaabbbccccd] [Bonjour le Monde]",
            "lower | caesar 3 | upper : [   OH  MDYD   HVW   XQ ODQJDJH   ] [DDDEEEFFFFG] [ERQMRXU OH PRQGH]",
            "caesar 3 | caesar 23 : [   le  Java   est   un langage   ] [aaabbbccccd] [Bonjour le Monde]",
            "novowels | reverse | replace l 1 : [   ggn1 n   ts   vJ  1   ] [dccccbbb] [dnM 1 rjnB]",
            "trim | squeeze | repeat 2 : [le Java est un langage / le Java est un langage] [aaabbbccccd / aaabbbccccd] [Bonjour le Monde / Bonjour le Monde]",
            "andThen ok!ok!, compose okok!, identite meme",
            "types : ****** <ababab> kotlin kiwi",
            "compteur : X apres 2 appels",
            "memo : [B A] [D C] [B A] [D C] [B A] [E] -> 6 appels, 3 depuis le cache");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.TEXTS", "Data.PIPELINES", "@FunctionalInterface", "interface Step extends UnaryOperator<String>",
            "default Step then(", "String::toLowerCase", "(String s) ->", "(var s) ->",
            "Step::title", "yield s -> {", ".andThen(", ".compose(",
            "Function.<String>identity()", "BiFunction<String, Integer, String>", "BinaryOperator.minBy(", "int[] applied",
            "class Memo",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "PipelineApp", args, EXPECTED, API);
    }
}
