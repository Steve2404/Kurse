package ch10_streams.projects.p06_spliterator;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON CashJournal, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "TX 1001 lea : 3 article(s), 34.90",
            "TX 1002 hugo : 3 article(s), 24.00",
            "TX 1003 ines : vide",
            "TX 1004 tom : 4 article(s), 19.25",
            "TX 1005 lea : 1 article(s), 1.20",
            "TX 1006 hugo : 3 article(s), 34.90",
            "TX 1007 zoe : 10 article(s), 5.00",
            "TX 1008 ines : annulee (0 article)",
            "ANOMALIE TX 1005 : ligne illisible \"  + 2 x\"",
            "COMPTE : 8 transactions (journal entier) / 8 (somme des 7 morceaux)",
            "CA TOTAL : 119.25 (somme des morceaux identique : oui)",
            "MEILLEUR CLIENT : hugo (58.90)",
            "DECOUPAGE : [1001] [1002] [1003 1004] [1005] [1006] [1007] [1008]",
            "PREMIERE (tryAdvance) : 1001, RESTE (forEachRemaining) : 7, ENCORE : false",
            "LOTS : [J01, J02] [J03, J04, J05] [J06, J07] [J08, J09, J10]",
            "CARACTERISTIQUES ArrayList : ORDERED SIZED SUBSIZED (taille exacte 10)",
            "CARACTERISTIQUES HashSet : DISTINCT SIZED (taille exacte 10)",
            "CARACTERISTIQUES TreeSet : DISTINCT SORTED ORDERED SIZED (taille exacte 10)",
            "CARACTERISTIQUES Stream.iterate : ORDERED IMMUTABLE (taille exacte -1)",
            "CARACTERISTIQUES sorted() : ORDERED SIZED SUBSIZED (taille exacte 10)",
            "CARACTERISTIQUES journal : ORDERED NONNULL IMMUTABLE (taille exacte -1)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements Spliterator<", "tryAdvance(", "trySplit()", "estimateSize()", "characteristics()",
            "forEachRemaining(", "hasCharacteristics(", "getExactSizeIfKnown()", ".spliterator()",
            "StreamSupport.stream(", "Spliterator.ORDERED", "Spliterator.SIZED", "Spliterator.SUBSIZED",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "CashJournal", args, EXPECTED, API);
    }
}
