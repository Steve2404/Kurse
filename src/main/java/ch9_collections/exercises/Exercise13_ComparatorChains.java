package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * EXERCICE 13 - Chaines de Comparator, nullsLast, le piege de reversed() et binarySearch (niveau : difficile)
 * ===========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Comparator, c'est un arbitre qui dit qui passe devant. On peut
 * coller des arbitres a la suite : le 2e ne parle qu'en cas d'egalite
 * pour le 1er (thenComparing). Et reversed() retourne TOUT ce qui est
 * a sa gauche, pas seulement le dernier maillon :
 *
 *   comparing(A).reversed().thenComparing(B)   -> A decroissant, puis B croissant
 *   comparing(A).thenComparing(B).reversed()   -> A decroissant ET B decroissant
 *
 * binarySearch cherche dans une liste TRIEE en coupant en deux a chaque
 * fois. S'il trouve : l'index. Sinon : -(point d'insertion) - 1, ou le
 * point d'insertion est l'index ou il FAUDRAIT mettre la valeur. (Le
 * "- 1" existe pour que "absent, a inserer en 0" ne donne pas 0, qui
 * voudrait dire "trouve a l'index 0".) Sur une liste NON triee, le
 * resultat n'a aucun sens.
 *
 * Les joueurs de main() :
 *
 *   Ana  Rouge 90 25 ans | Bob Bleu 75 age inconnu (null) | Cid Rouge 90 31 ans
 *   Dan  Bleu  60 19 ans | Eve Vert 75 22 ans
 *
 *
 * ==================================================================
 * TODO 1 : byScoreDescThenName()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> [Ana, Cid, Bob, Eve, Dan]  (90, 90, 75, 75, 60 ; egalites par nom croissant)
 *
 * -- Le plan --
 *
 *   1. Arbitre du score, retourne (reversed) TOUT DE SUITE.
 *   2. Puis arbitre du nom.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : byScoreThenNameBothDesc()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> [Cid, Ana, Eve, Bob, Dan]  (reversed a la FIN retourne les deux criteres)
 *
 * -- Le plan --
 *
 *   1. Score, puis nom, puis reversed() sur l'ensemble.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : byTeamThenAgeNullsLast()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> [Dan, Bob, Ana, Cid, Eve]  (Bleu : Dan 19, Bob inconnu a la fin ; Rouge : 25, 31 ; Vert)
 *
 * -- Le plan --
 *
 *   1. Equipe (ordre naturel).
 *   2. Puis age, avec un arbitre "null a la fin" qui enveloppe l'ordre naturel.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : ranking(players)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un podium "olympique" : deux ex aequo ont le meme rang, et le suivant
 * saute les places prises. Format "rang nom score".
 *
 * -- Essayons a la main --
 *
 *   -> [1 Ana 90, 1 Cid 90, 3 Bob 75, 3 Eve 75, 5 Dan 60]
 *
 * -- Le plan --
 *
 *   1. Trier une COPIE avec byScoreDescThenName().
 *   2. Pour la position i (0, 1, ...) : si le score est le meme que le precedent, garder le rang ; sinon rang = i + 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2) : l'ordre du TODO 1, deja ecrit.
 *
 *
 * ==================================================================
 * TODO 5 : binarySearch(sorted, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Refaire Collections.binarySearch a la main, avec le meme resultat.
 *
 * -- Essayons a la main --
 *
 *   [10, 20, 30, 40] : 30 -> 2 ; 5 -> -1 (inserer en 0) ; 25 -> -3 (inserer en 2) ; 99 -> -5
 *
 * -- Le plan --
 *
 *   1. low = 0, high = taille - 1.
 *   2. Tant que low <= high : milieu ; egal -> rendre milieu ; trop petit -> low = milieu + 1 ; sinon high = milieu - 1.
 *   3. Pas trouve : low est le point d'insertion -> rendre -(low) - 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : insertSorted(sorted, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ajouter key a sa place dans une liste triee (sans tout retrier), en
 * utilisant ce que rend Collections.binarySearch.
 *
 * -- Essayons a la main --
 *
 *   [10, 20, 30] + 25 -> [10, 20, 25, 30] ; + 20 -> [10, 20, 20, 30]
 *
 * -- Le plan --
 *
 *   1. r = binarySearch.
 *   2. r >= 0 -> inserer en r ; sinon inserer en -(r) - 1 (decoder le point d'insertion).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Comparator.comparingInt(Player::score).reversed().thenComparing(Player::name)
 *   - .thenComparing(Player::age, Comparator.nullsLast(Comparator.naturalOrder()))
 *   - int mid = (low + high) >>> 1;  (evite le debordement de low + high)
 */
public class Exercise13_ComparatorChains {

    public record Player(String name, String team, int score, Integer age) {
    }

    public static Comparator<Player> byScoreDescThenName() {
        throw new UnsupportedOperationException("TODO 1 : implementer byScoreDescThenName()");
    }

    public static Comparator<Player> byScoreThenNameBothDesc() {
        throw new UnsupportedOperationException("TODO 2 : implementer byScoreThenNameBothDesc()");
    }

    public static Comparator<Player> byTeamThenAgeNullsLast() {
        throw new UnsupportedOperationException("TODO 3 : implementer byTeamThenAgeNullsLast()");
    }

    public static List<String> ranking(List<Player> players) {
        throw new UnsupportedOperationException("TODO 4 : implementer ranking()");
    }

    public static int binarySearch(List<Integer> sorted, int key) {
        throw new UnsupportedOperationException("TODO 5 : implementer binarySearch()");
    }

    public static void insertSorted(List<Integer> sorted, int key) {
        throw new UnsupportedOperationException("TODO 6 : implementer insertSorted()");
    }

    public static void main(String[] args) {
        List<Player> players = List.of(
                new Player("Ana", "Rouge", 90, 25), new Player("Bob", "Bleu", 75, null),
                new Player("Cid", "Rouge", 90, 31), new Player("Dan", "Bleu", 60, 19),
                new Player("Eve", "Vert", 75, 22));

        ExerciseChecker.check("byScoreDescThenName -> [Ana, Cid, Bob, Eve, Dan]",
                names(players, byScoreDescThenName()).equals(List.of("Ana", "Cid", "Bob", "Eve", "Dan")));
        ExerciseChecker.check("byScoreThenNameBothDesc -> [Cid, Ana, Eve, Bob, Dan]",
                names(players, byScoreThenNameBothDesc()).equals(List.of("Cid", "Ana", "Eve", "Bob", "Dan")));
        ExerciseChecker.check("byTeamThenAgeNullsLast -> [Dan, Bob, Ana, Cid, Eve]",
                names(players, byTeamThenAgeNullsLast()).equals(List.of("Dan", "Bob", "Ana", "Cid", "Eve")));
        ExerciseChecker.check("ranking -> [1 Ana 90, 1 Cid 90, 3 Bob 75, 3 Eve 75, 5 Dan 60] (et players intact)",
                ranking(players).equals(List.of("1 Ana 90", "1 Cid 90", "3 Bob 75", "3 Eve 75", "5 Dan 60")));

        List<Integer> sorted = List.of(10, 20, 30, 40);
        int agree = 0;
        for (int key = 0; key <= 50; key++) {
            if (binarySearch(sorted, key) == Collections.binarySearch(sorted, key)) {
                agree++;
            }
        }
        ExerciseChecker.check("binarySearch == Collections.binarySearch pour les cles 0..50 (" + agree + "/51)", agree == 51);
        ExerciseChecker.check("binarySearch sur une liste vide -> -1 (comme le JDK)",
                binarySearch(List.of(), 7) == Collections.binarySearch(List.<Integer>of(), 7));

        List<Integer> growing = new ArrayList<>(List.of(10, 20, 30));
        insertSorted(growing, 25);
        insertSorted(growing, 20);
        insertSorted(growing, 5);
        insertSorted(growing, 99);
        ExerciseChecker.check("insertSorted 25, 20, 5, 99 -> [5, 10, 20, 20, 25, 30, 99]",
                growing.equals(List.of(5, 10, 20, 20, 25, 30, 99)));

        ExerciseChecker.summary();
    }

    static List<String> names(List<Player> players, Comparator<Player> order) {
        List<Player> copy = new ArrayList<>(players);
        copy.sort(order);
        List<String> result = new ArrayList<>();
        for (Player p : copy) {
            result.add(p.name());
        }
        return result;
    }
}
