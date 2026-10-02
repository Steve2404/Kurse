package ch5_methods.projects.p06_recursion;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON RecursionLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "bases : 20! = 2432902008176640000, somme des chiffres de 98765 = 35, pgcd(1071, 462) = 21, 37 en binaire = 100101, kayak true, kayaks false",
            "puissance : 3^20 = 3486784401 en 7 multiplications (au lieu de 19)",
            "fibonacci(25) = 75025 en 242785 appels ; memoise 75025 en 49 appels ; fibonacci(90) = 2880067194370816120",
            "tri fusion [3, 3, 9, 10, 27, 38, 43, 82] (17 comparaisons), tri rapide true (18 comparaisons), original [38, 27, 43, 3, 9, 82, 10, 3]",
            "recherche recursive : 43 -> 6, 11 -> -5",
            "hanoi 4 disques : 15 deplacements (2^4 - 1), debut : 1:A->B 2:A->C 1:B->C 3:A->B 1:C->A",
            "sous-ensembles de {1,2,3} : {} {3} {2} {2,3} {1} {1,3} {1,2} {1,2,3}",
            "combinaisons 3 parmi 5 (10) : 123 124 125 134 135 145 234 235 245 345",
            "reines : 6x6 -> 4 solutions, 8x8 -> 92 solutions ; premiere 6x6 :",
            "  .Q....",
            "  ...Q..",
            "  .....Q",
            "  Q.....",
            "  ..Q...",
            "  ....Q.",
            "iles : 6 (tailles 3,3,4,5,5,1), la plus grande 5",
            "monnaie : 4562 facons de faire 100, minimum 2 pieces, pour 63 : 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.UNSORTED", "Data.MAP", "Data.COINS", "memo",
            ">>> 1", "System.arraycopy(", ".toCharArray()", ".repeat(",
            "new long[", "!Arrays.sort(##Arrays.sort (ici on trie A LA MAIN, recursivement)",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "RecursionLab", args, EXPECTED, API);
    }
}
