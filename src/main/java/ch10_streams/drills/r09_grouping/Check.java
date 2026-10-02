package ch10_streams.drills.r09_grouping;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 9 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : HashMap 4 [Classique, Fantasy, SF]",
            "D02 : {Classique=2, Fantasy=2, SF=4}",
            "D03 : {Classique=[Zola], Fantasy=[Tolkien], SF=[Asimov, Gibson, Herbert, Simmons]}",
            "D04 : {false=5, true=3} [false, true] 0",
            "D05 : {Classique=0, Fantasy=1, SF=2}",
            "D06 : [Hobbit, Le, Silmarillion]",
            "D07 : {Classique=Germinal, Fantasy=Le Silmarillion, SF=Hyperion}",
            "D08 : {Classique=1104, Fantasy=675, SF=1420} Fondation",
            "D09 : {Classique={1800=2}, Fantasy={1900=2}, SF={1900=4}}",
            "D10 : 399 pages en moyenne sur 8",
            "D11 : {Asimov=1, Gibson=1, Herbert=1, Simmons=1, Tolkien=2, Zola=2} 3",
            "D12 : Le Hobbit/Le Silmarillion/Germinal/L'Assommoir");
            // EXPECTED-END

    static final List<String> API = List.of(
            "groupingBy(", "TreeMap::new", "counting()", "mapping(", "toCollection(",
            "partitioningBy(", "filtering(", "flatMapping(", "collectingAndThen(", "maxBy(",
            "reducing(", "teeing(", "joining(", "toSet()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
