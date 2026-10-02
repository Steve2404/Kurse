package ch7_beyondclasses.projects.p02_poker;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Poker, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "couleurs : C=CLUBS(noire) D=DIAMONDS(rouge) H=HEARTS(rouge) S=SPADES(noire)",
            "rangs : 13, 10 1 -12 TEN couleur true",
            "AS KS QS JS TS -> quinte flush",
            "9C 9D 9H 9S 2D -> carre",
            "3H 3D 3S 7C 7D -> full",
            "2H 8H 5H JH KH -> couleur",
            "9D TC JS QH KD -> suite",
            "AC 2D 3H 4S 5C -> suite",
            "7S 7H 7D KC 2S -> brelan",
            "4C 4D JH JS AC -> double paire",
            "QD QS 6C 3H 2S -> paire",
            "AH JD 8C 5S 3D -> hauteur",
            "departage : roue < suite au 6 true, paire de rois kicker As > kicker 4 true, record egal true",
            "joueur 1 : 9H 3S 8H 6C 6S -> paire",
            "joueur 2 : JS 4D 5S 2H 4C -> paire",
            "joueur 3 : TD 3H QH KC AD -> hauteur",
            "joueur 4 : JD 6H 7C 2D 9S -> hauteur",
            "gagnant : joueur 1 avec paire",
            "hold'em sur KH 9H 4C 9S 2H : [AH 3H: couleur] [9D KD: full] [KC KS: full] [QH JH: couleur]",
            "gagnant hold'em : joueur 3 (KH 9H 9S KC KS -> full)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.HANDS", "Data.SEED", "Data.BOARD", "enum Suit implements Symbolic",
            "enum Rank implements Symbolic", "enum Category", "record Card(", "record Hand(",
            "abstract boolean matches(", "9xboolean matches(int[] groups", "values()", ".ordinal()",
            ".compareTo(", "valueOf(", "public Hand {", "this(cards",
            ".name()",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Poker", args, EXPECTED, API);
    }
}
