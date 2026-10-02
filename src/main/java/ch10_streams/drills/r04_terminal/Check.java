package ch10_streams.drills.r04_terminal;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 4 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 8 Dune Germinal",
            "D02 : Le Hobbit true",
            "D03 : true false true | vide : false true true",
            "D04 : HASTGZ",
            "D05 : Object[] 8 / String[] 8",
            "D06 : meme contenu true, Collectors.toList() apres ajout -> 9 elements",
            "D07 : 3199 1989 SF/Fantasy/Classique",
            "D08 : Optional.empty 5",
            "D09 : true false",
            "D10 : stream map");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".count()", ".min(", ".max(", ".findFirst()", ".findAny()",
            ".anyMatch(", ".allMatch(", ".noneMatch(", ".forEach(", ".toArray()",
            ".toArray(String[]::new)", ".toList()", "Collectors.toList()", ".reduce(", "Stream.generate(",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
