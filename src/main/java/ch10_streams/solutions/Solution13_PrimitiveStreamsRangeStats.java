package ch10_streams.solutions;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise13_PrimitiveStreamsRangeStats.
 */
public class Solution13_PrimitiveStreamsRangeStats {

    public static IntStream buildRange(int start, int endExclusive) {
        // range : fin EXCLUE.
        return IntStream.range(start, endExclusive);
    }

    public static IntStream buildRangeClosed(int start, int endInclusive) {
        // rangeClosed : fin INCLUSE.
        return IntStream.rangeClosed(start, endInclusive);
    }

    public static IntSummaryStatistics summarize(int[] values) {
        // Un seul passage pour count, sum, min, max et moyenne.
        return Arrays.stream(values).summaryStatistics();
    }
}
