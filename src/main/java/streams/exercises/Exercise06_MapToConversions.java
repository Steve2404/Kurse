package streams.exercises;

import streams.ExerciseChecker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * EXERCICE 6 - Passer d'un type de stream a un autre (mapToInt/mapToObj/mapToDouble) (niveau : moyen/difficile)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * map() normal transforme les elements SANS changer la "famille" du
 * stream (un Stream<T> reste un Stream<R>). Mais pour CHANGER de
 * famille (passer d'un Stream<T> d'objets a un IntStream de nombres
 * bruts, ou l'inverse), il faut un outil special dedie a CHAQUE sens :
 *
 *   - mapToInt(ToIntFunction<T>)  : Stream<T> -> IntStream
 *   - mapToLong(...) / mapToDouble(...) : pareil, pour long / double.
 *   - mapToObj(IntFunction<R>)    : IntStream -> Stream<R> (le sens
 *     inverse : on "remballe" un nombre brut dans un objet).
 *
 *
 * ==================================================================
 * TODO 1 : wordLengths(words)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. words.mapToInt(String::length) - un Stream<String> devient un
 *      IntStream (chaque mot devient sa longueur, un int brut).
 *
 *
 * ==================================================================
 * TODO 2 : intsToLabels(values)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. values.mapToObj(n -> "n=" + n) - un IntStream redevient un
 *      Stream<String> (chaque nombre brut est "remballe" dans une
 *      chaine de caracteres).
 *
 *
 * ==================================================================
 * TODO 3 : intsToPercentages(values, total)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * values = [25, 50, 25], total = 100.
 *
 * Chaque valeur devient un POURCENTAGE de total, calcule en double
 * (100.0 * valeur / total) : 25.0%, 50.0%, 25.0%.
 *
 * -- Le plan --
 *
 *   1. values.mapToDouble(v -> 100.0 * v / total) - un IntStream
 *      devient un DoubleStream.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX du bon
 * "mapToXxx" qui est le vrai coeur de l'exercice.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - String::length utilise comme ToIntFunction<String> : compatible
 *     avec mapToInt (meme forme que le SAM de ToIntFunction).
 *   - values.mapToObj(...) attend un IntFunction<R> : int -> R.
 */
public class Exercise06_MapToConversions {

    public static IntStream wordLengths(Stream<String> words) {
        throw new UnsupportedOperationException("TODO 1 : implementer wordLengths()");
    }

    public static Stream<String> intsToLabels(IntStream values) {
        throw new UnsupportedOperationException("TODO 2 : implementer intsToLabels()");
    }

    public static DoubleStream intsToPercentages(IntStream values, int total) {
        throw new UnsupportedOperationException("TODO 3 : implementer intsToPercentages()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("wordLengths(a, bb, ccc).sum() == 6",
                wordLengths(Stream.of("a", "bb", "ccc")).sum() == 6);

        List<String> labels = intsToLabels(IntStream.of(1, 2, 3)).collect(Collectors.toList());
        ExerciseChecker.check("intsToLabels == [n=1, n=2, n=3]", labels.equals(List.of("n=1", "n=2", "n=3")));

        double[] percentages = intsToPercentages(IntStream.of(25, 50, 25), 100).toArray();
        ExerciseChecker.check("intsToPercentages == [25.0, 50.0, 25.0]",
                percentages[0] == 25.0 && percentages[1] == 50.0 && percentages[2] == 25.0);

        ExerciseChecker.summary();
    }
}
