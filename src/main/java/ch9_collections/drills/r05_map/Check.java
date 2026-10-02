package ch9_collections.drills.r05_map;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : null 1 {a=2, b=3} null 0 true true",
            "D02 : {a=2, c=5} 3 false",
            "D03 : {chat=1, chien=1, et=2, le=3, rat=1}",
            "D04 : {chat=100, chien=1, le=0, loup=4, rat=11}",
            "D05 : c100c1l0l4r11 45243 [chat, chien, le, loup, rat] [100, 1, 0, 4, 11]",
            "D06 : {z=1, a=2, m=3} 3 0 true null");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".put(", ".get(", ".getOrDefault(", ".containsKey(",
            ".containsValue(", ".putIfAbsent(", ".remove(\"c\", 99)", ".merge(",
            ".compute(", ".computeIfAbsent(", ".computeIfPresent(", ".replaceAll(",
            ".entrySet()", "Map.Entry<String, Integer>", ".forEach(", ".keySet()",
            ".values()", "new LinkedHashMap<>()", "nulls.put(null, 0)",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
