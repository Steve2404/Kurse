package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

/**
 * EXERCICE 5 - IntStream : range(), rangeClosed() et summaryStatistics() (niveau : moyen)
 * ====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * A cote de Stream<T> (qui manipule des OBJETS), le JDK offre 3
 * streams "primitifs" : IntStream, LongStream, DoubleStream. Ils
 * manipulent directement des nombres BRUTS (comme Exercise06 du
 * chapitre lambdas evitait l'autoboxing), et offrent des outils
 * mathematiques tout prets : average(), max(), sum(),
 * summaryStatistics()...
 *
 *
 * ==================================================================
 * TODO 1 : buildRange(start, endExclusive)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * range(1, 10) fabrique le stream des nombres de 1 A 9 INCLUS - la
 * borne de FIN (10) n'est PAS comprise dans le resultat (comme une
 * boucle for classique : for (int i = 1; i < 10; i++)).
 *
 * -- Le plan --
 *
 *   1. Renvoyer IntStream.range(start, endExclusive).
 *
 *
 * ==================================================================
 * TODO 2 : buildRangeClosed(start, endInclusive)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * rangeClosed(1, 10) fabrique le stream des nombres de 1 A 10 INCLUS
 * CETTE FOIS (la borne de fin FAIT partie du resultat).
 *
 * -- Le plan --
 *
 *   1. Renvoyer IntStream.rangeClosed(start, endInclusive).
 *
 *
 * ==================================================================
 * TODO 3 : summarize(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Plutot que d'appeler separement min(), max(), sum(), average() et
 * count() (ce qui obligerait a reconstruire le stream 5 fois, puisque
 * chaque stream ne se consomme qu'UNE SEULE FOIS), summaryStatistics()
 * calcule les 5 EN UN SEUL PASSAGE, et les range dans un objet
 * IntSummaryStatistics qui les garde toutes disponibles ensuite.
 *
 * -- Le plan --
 *
 *   1. Renvoyer java.util.Arrays.stream(values).summaryStatistics().
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, ce sont des methodes toutes
 * faites du JDK.
 *
 * Exemple a verifier : summarize([3, 7, 2, 9, 4]) -> min=2, max=9,
 * sum=25, count=5, average=5.0.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - IntStream.range(1, 10).sum() == 45 (1+2+...+9).
 *   - IntStream.rangeClosed(1, 10).sum() == 55 (1+2+...+10).
 *   - stats.getMin(), stats.getMax(), stats.getSum(), stats.getCount(),
 *     stats.getAverage() : tous disponibles sur l'objet renvoye par
 *     summaryStatistics(), sans jamais retoucher au stream d'origine
 *     (qui, lui, est deja "consomme" une fois summaryStatistics()
 *     appele).
 */
public class Exercise05_PrimitiveStreamsRangeStats {

    public static IntStream buildRange(int start, int endExclusive) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildRange()");
    }

    public static IntStream buildRangeClosed(int start, int endInclusive) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildRangeClosed()");
    }

    public static IntSummaryStatistics summarize(int[] values) {
        throw new UnsupportedOperationException("TODO 3 : implementer summarize()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("range(1,10).sum() == 45 (1..9)", buildRange(1, 10).sum() == 45);
        ExerciseChecker.check("range(1,10).count() == 9", buildRange(1, 10).count() == 9);

        ExerciseChecker.check("rangeClosed(1,10).sum() == 55 (1..10)", buildRangeClosed(1, 10).sum() == 55);
        ExerciseChecker.check("rangeClosed(1,10).count() == 10", buildRangeClosed(1, 10).count() == 10);

        IntSummaryStatistics stats = summarize(new int[]{3, 7, 2, 9, 4});
        ExerciseChecker.check("summarize : min == 2", stats.getMin() == 2);
        ExerciseChecker.check("summarize : max == 9", stats.getMax() == 9);
        ExerciseChecker.check("summarize : sum == 25", stats.getSum() == 25);
        ExerciseChecker.check("summarize : count == 5", stats.getCount() == 5);
        ExerciseChecker.check("summarize : average == 5.0", stats.getAverage() == 5.0);

        ExerciseChecker.summary();
    }
}
