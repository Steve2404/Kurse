package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * EXERCICE 17 - reduce() et collect() en profondeur : les 3 signatures de reduce, collect a 3 arguments, element neutre (niveau : difficile)
 * =========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Les 3 signatures de reduce (a connaitre PAR COEUR pour l'examen) --
 *
 *   1. Optional<T> reduce(BinaryOperator<T> accumulator)
 *        pas de valeur de depart -> resultat dans une BOITE (stream vide ?)
 *   2. T reduce(T identity, BinaryOperator<T> accumulator)
 *        valeur de depart -> resultat direct (identity si stream vide)
 *   3. <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator,
 *                   BinaryOperator<U> combiner)
 *        quand le RESULTAT n'a pas le meme type que les ELEMENTS
 *
 * -- Et les 2 de collect --
 *
 *   1. collect(Collector) : Collectors.toList(), joining()... (Exercise16)
 *   2. collect(Supplier<R> supplier, BiConsumer<R, T> accumulator,
 *              BiConsumer<R, R> combiner)
 *        "reduction MUTABLE" : on remplit UN conteneur (StringBuilder,
 *        Map...) au lieu de fabriquer un nouvel objet a chaque etape.
 *
 * -- L'element neutre (identity) --
 *
 * La valeur de depart de reduce doit etre NEUTRE pour l'operation :
 * combinee avec n'importe quel x, elle rend x. 0 pour +, 1 pour *, ""
 * pour la concatenation simple. Si elle ne l'est pas, le resultat est
 * faux (voir TODO 6).
 *
 *
 * ==================================================================
 * TODO 1 : longestWord(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un tournoi : les mots s'affrontent 2 par 2, le plus long gagne et
 * affronte le suivant. A egalite, le tenant du titre (celui qui etait
 * deja la) garde sa place. Pas de valeur de depart possible (quel
 * serait le "mot neutre" ?) -> signature 1, resultat en boite.
 *
 * -- Essayons a la main --
 *
 *   [kiwi, banane, poire, ananas]
 *   kiwi vs banane -> banane ; banane vs poire -> banane ;
 *   banane vs ananas -> egalite (6) -> banane reste
 *   -> Optional[banane]
 *   [] -> Optional.empty
 *
 * -- Le plan --
 *
 *   1. Reduire les mots 2 par 2 : garder le 2e seulement s'il est
 *      STRICTEMENT plus long que le 1er.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : totalLength(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les elements sont des String, mais le resultat est un Integer : les
 * signatures 1 et 2 sont impossibles (elles exigent le MEME type
 * partout). Signature 3 :
 *   - identity (0) : le total de depart ;
 *   - accumulator (total, mot) -> total + longueur du mot ;
 *   - combiner (total1, total2) -> total1 + total2 : sert a fusionner
 *     des resultats partiels quand le stream est PARALLELE (coupe en
 *     morceaux). En sequentiel il n'est pas appele, mais il est
 *     OBLIGATOIRE dans la signature.
 *
 * -- Essayons a la main --
 *
 *   [ab, cde, ""] -> 0 + 2 = 2 ; 2 + 3 = 5 ; 5 + 0 = 5 -> 5
 *
 * -- Le plan --
 *
 *   1. Reduire en partant de 0, en ajoutant la longueur de chaque mot.
 *   2. Fournir un combiner qui additionne deux totaux.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (Dans la vraie vie, mapToInt(String::length).sum() est plus
 * simple ; ici le but est de maitriser la signature 3.)
 *
 *
 * ==================================================================
 * TODO 3 : concatWithCollect(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * reduce("", String::concat) fabrique une NOUVELLE String a chaque
 * etape (les String sont immuables) : du gaspillage. collect a 3
 * arguments remplit UN SEUL StringBuilder :
 *   - supplier    : comment fabriquer le conteneur vide ;
 *   - accumulator : comment y ajouter UN element ;
 *   - combiner    : comment verser un conteneur dans un autre.
 *
 * -- Essayons a la main --
 *
 *   [a, b, c] -> sb = "" -> "a" -> "ab" -> "abc" -> "abc"
 *
 * -- Le plan --
 *
 *   1. Collecter dans un StringBuilder (fabriquer, ajouter, fusionner).
 *   2. Le transformer en String.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : 3 references de methode suffisent.
 *
 *
 * ==================================================================
 * TODO 4 : countOccurrences(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un tableau de pointage : pour chaque mot, on ajoute 1 a sa case (ou
 * on cree la case a 1 si elle n'existe pas encore : c'est exactement
 * Map.merge(cle, 1, Integer::sum)). Le combiner doit FUSIONNER deux
 * tableaux de pointage : attention, putAll ECRASERAIT les scores au
 * lieu de les additionner - il faut merger chaque case de l'un dans
 * l'autre.
 *
 * -- Essayons a la main --
 *
 *   [a, b, a, c, a] -> {a=3, b=1, c=1}
 *
 * -- Le plan --
 *
 *   1. Conteneur : une HashMap vide.
 *   2. Ajouter un mot : incrementer sa case.
 *   3. Fusionner deux maps : pour chaque case de la 2e, l'ajouter a la
 *      1re (avec addition si la case existe).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Eventuellement pour le combiner, si la lambda te semble trop longue.
 *
 *
 * ==================================================================
 * TODO 5 : maxWithReduce(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le max, c'est aussi un tournoi : Integer::max est un
 * BinaryOperator<Integer> tout fait. Pas de valeur de depart ->
 * boite.
 *
 * -- Essayons a la main --
 *
 *   [3, 9, 2] -> Optional[9] ; [] -> Optional.empty
 *
 * -- Le plan --
 *
 *   1. Reduire avec "le plus grand des deux".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : joinWithReduce(words, separator)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut "x-y-z". Tentation : reduce("", (a, b) -> a + "-" + b).
 * Mais "" n'est PAS neutre pour l'operation "a + separateur + b" :
 * "" + "-" + "x" donne "-x", pas "x" ! Resultat : "-x-y-z". L'identity
 * n'est neutre que si op(identity, x) == x. Ici il n'existe pas de
 * valeur neutre -> signature 1 (sans identity), puis on ouvre la
 * boite avec "" pour le cas vide.
 *
 * -- Essayons a la main --
 *
 *   [x, y, z], "-" -> x ; x-y ; x-y-z -> "x-y-z"
 *   [solo], "-"    -> "solo" (un seul element : l'accumulateur n'est
 *                     jamais appele)
 *   [], "-"        -> boite vide -> ""
 *
 * -- Le plan --
 *
 *   1. Reduire sans valeur de depart : a + separator + b.
 *   2. Rendre "" si la boite est vide.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (Dans la vraie vie : Collectors.joining(separator).)
 *
 *
 * Exemple a verifier :
 *
 *   longestWord([kiwi, banane, poire, ananas]) == Optional[banane] ; longestWord([]) vide
 *   totalLength([ab, cde, ""]) == 5 ; totalLength([]) == 0
 *   concatWithCollect([a, b, c]) == "abc"
 *   countOccurrences([a, b, a, c, a]) == {a=3, b=1, c=1}
 *   maxWithReduce([3, 9, 2]) == Optional[9] ; maxWithReduce([]) vide
 *   joinWithReduce([x, y, z], "-") == "x-y-z" ; ([solo], "-") == "solo" ; ([], "-") == ""
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - words.stream().reduce((a, b) -> b.length() > a.length() ? b : a)
 *   - words.stream().reduce(0, (total, w) -> total + w.length(), Integer::sum)
 *   - words.stream().collect(StringBuilder::new, StringBuilder::append,
 *     StringBuilder::append).toString()
 *   - collect(HashMap::new, (m, w) -> m.merge(w, 1, Integer::sum),
 *     (m1, m2) -> m2.forEach((k, v) -> m1.merge(k, v, Integer::sum)))
 *   - values.stream().reduce(Integer::max)
 *   - reduce((a, b) -> a + separator + b).orElse("")
 */
public class Exercise17_ReduceAdvanced {

    public static Optional<String> longestWord(List<String> words) {
        throw new UnsupportedOperationException("TODO 1 : implementer longestWord()");
    }

    public static int totalLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer totalLength()");
    }

    public static String concatWithCollect(List<String> words) {
        throw new UnsupportedOperationException("TODO 3 : implementer concatWithCollect()");
    }

    public static Map<String, Integer> countOccurrences(List<String> words) {
        throw new UnsupportedOperationException("TODO 4 : implementer countOccurrences()");
    }

    public static Optional<Integer> maxWithReduce(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 5 : implementer maxWithReduce()");
    }

    public static String joinWithReduce(List<String> words, String separator) {
        throw new UnsupportedOperationException("TODO 6 : implementer joinWithReduce()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("longestWord([kiwi, banane, poire, ananas]) == banane (le tenant garde sa place)",
                longestWord(List.of("kiwi", "banane", "poire", "ananas")).equals(Optional.of("banane")));
        ExerciseChecker.check("longestWord([]) est vide", longestWord(List.of()).isEmpty());

        ExerciseChecker.check("totalLength([ab, cde, \"\"]) == 5", totalLength(List.of("ab", "cde", "")) == 5);
        ExerciseChecker.check("totalLength([]) == 0", totalLength(List.of()) == 0);

        ExerciseChecker.check("concatWithCollect([a, b, c]) == abc",
                concatWithCollect(List.of("a", "b", "c")).equals("abc"));

        Map<String, Integer> expected = new HashMap<>();
        expected.put("a", 3);
        expected.put("b", 1);
        expected.put("c", 1);
        ExerciseChecker.check("countOccurrences([a, b, a, c, a]) == {a=3, b=1, c=1}",
                countOccurrences(List.of("a", "b", "a", "c", "a")).equals(expected));

        ExerciseChecker.check("maxWithReduce([3, 9, 2]) == 9", maxWithReduce(List.of(3, 9, 2)).equals(Optional.of(9)));
        ExerciseChecker.check("maxWithReduce([]) est vide", maxWithReduce(List.of()).isEmpty());

        ExerciseChecker.check("joinWithReduce([x, y, z], -) == x-y-z",
                joinWithReduce(List.of("x", "y", "z"), "-").equals("x-y-z"));
        ExerciseChecker.check("joinWithReduce([solo], -) == solo", joinWithReduce(List.of("solo"), "-").equals("solo"));
        ExerciseChecker.check("joinWithReduce([], -) == \"\"", joinWithReduce(List.of(), "-").equals(""));

        ExerciseChecker.summary();
    }
}
