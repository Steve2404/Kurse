package streams.solutions;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise05_PrimitiveStreamsRangeStats.
 */
public class Solution05_PrimitiveStreamsRangeStats {

    public static IntStream buildRange(int start, int endExclusive) {
        return IntStream.range(start, endExclusive);
    }

    public static IntStream buildRangeClosed(int start, int endInclusive) {
        return IntStream.rangeClosed(start, endInclusive);
    }

    public static IntSummaryStatistics summarize(int[] values) {
        return Arrays.stream(values).summaryStatistics();
    }
}
