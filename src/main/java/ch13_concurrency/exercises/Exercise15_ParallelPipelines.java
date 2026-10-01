package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * EXERCICE 15 - Pipelines paralleles corrects : findFirst, forEachOrdered, identite de reduce, groupingByConcurrent (niveau : avance)
 * ===================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un stream parallele coupe les donnees en morceaux, les traite sur
 * plusieurs threads, puis recolle les resultats. Ca reste JUSTE si on
 * respecte 4 regles :
 *
 *   - findAny() rend N'IMPORTE quel element ; findFirst() garde l'ordre de rencontre.
 *   - forEach() en parallele affiche dans le desordre ; forEachOrdered() garde l'ordre.
 *   - reduce(identite, op) : l'identite est utilisee pour CHAQUE morceau. Elle doit etre
 *     neutre (0 pour +, 1 pour x) : reduce(1, Integer::sum) en parallele ajoute 1 PAR MORCEAU,
 *     et le resultat change selon le nombre de morceaux !
 *   - Pour regrouper en parallele, groupingByConcurrent remplit une seule ConcurrentMap
 *     au lieu de fusionner plusieurs HashMap.
 *
 * Tous les tests sont deterministes : une version juste passe a chaque
 * fois ; une version fausse peut echouer seulement de temps en temps
 * (relance plusieurs fois !).
 *
 *
 * ==================================================================
 * TODO 1 : firstSquareAbove(values, limit)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [3, 9, 12, 20, 25], limite 100 -> 144 (le PREMIER carre > 100, dans l'ordre de la liste)
 *
 * -- Le plan --
 *
 *   1. parallelStream() ; map vers le carre ; filter > limit ; findFirst() ; orElse(-1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : shoutInOrder(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Mettre chaque mot en majuscules EN PARALLELE, puis les ajouter un par
 * un a un StringBuilder, separes par "-", dans l'ordre d'origine.
 *
 * -- Essayons a la main --
 *
 *   [a, b, c] -> "A-B-C"
 *
 * -- Le plan --
 *
 *   1. parallelStream().map(String::toUpperCase)
 *   2. forEachOrdered (pas forEach !) pour ajouter au StringBuilder.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : sumAndProduct(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avec IntStream.rangeClosed(1, n).parallel() et reduce, rendre
 * "somme=S produit=P" (produit en long, pour n petit).
 *
 * -- Essayons a la main --
 *
 *   n = 10 -> "somme=55 produit=3628800"
 *
 * -- Le plan --
 *
 *   1. reduce(0, Integer::sum) : 0 est neutre pour l'addition.
 *   2. asLongStream().reduce(1, (a, b) -> a * b) : 1 est neutre pour la multiplication.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : countByLength(words)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [ab, c, de, fgh] -> {1=1, 2=2, 3=1}, et la map est une ConcurrentMap
 *
 * -- Le plan --
 *
 *   1. parallelStream().collect(Collectors.groupingByConcurrent(String::length, Collectors.counting())).
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
 *   - values.parallelStream().map(v -> v * v).filter(v -> v > limit).findFirst().orElse(-1)
 *   - sb.length() > 0 ? "-" : "" avant chaque mot.
 */
public class Exercise15_ParallelPipelines {

    public static int firstSquareAbove(List<Integer> values, int limit) {
        throw new UnsupportedOperationException("TODO 1 : implementer firstSquareAbove()");
    }

    public static String shoutInOrder(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer shoutInOrder()");
    }

    public static String sumAndProduct(int n) {
        throw new UnsupportedOperationException("TODO 3 : implementer sumAndProduct()");
    }

    public static ConcurrentMap<Integer, Long> countByLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 4 : implementer countByLength()");
    }

    public static void main(String[] args) {
        List<Integer> big = IntStream.rangeClosed(1, 50_000).boxed().collect(Collectors.toList());
        List<String> letters = IntStream.range(0, 2_000).mapToObj(i -> "w" + i).collect(Collectors.toList());
        String expectedShout = letters.stream().map(String::toUpperCase).collect(Collectors.joining("-"));

        boolean first = true;
        boolean ordered = true;
        for (int run = 0; run < 5; run++) {
            first &= firstSquareAbove(List.of(3, 9, 12, 20, 25), 100) == 144 && firstSquareAbove(big, 1_000_000) == 1001 * 1001;
            ordered &= shoutInOrder(letters).equals(expectedShout);
        }
        ExerciseChecker.check("firstSquareAbove : 144, et 1001^2 sur 50 000 nombres, 5 fois de suite", first);
        ExerciseChecker.check("shoutInOrder : W0-W1-...-W1999 dans l'ordre, 5 fois de suite (et [a, b, c] -> A-B-C)",
                ordered && shoutInOrder(List.of("a", "b", "c")).equals("A-B-C"));
        ExerciseChecker.check("sumAndProduct(10) == somme=55 produit=3628800 ; (20000) somme == 200010000",
                sumAndProduct(10).equals("somme=55 produit=3628800") && sumAndProduct(20_000).startsWith("somme=200010000 "));
        Map<Integer, Long> counts = countByLength(List.of("ab", "c", "de", "fgh"));
        ExerciseChecker.check("countByLength([ab, c, de, fgh]) == {1=1, 2=2, 3=1}, dans une ConcurrentMap",
                counts instanceof ConcurrentMap && counts.equals(Map.of(1, 1L, 2, 2L, 3, 1L)));

        ExerciseChecker.summary();
    }
}
