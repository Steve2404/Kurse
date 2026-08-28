package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans collections.exercises.Exercise12_MergeOverlappingIntervals.
 */
public class Solution12_MergeOverlappingIntervals {

    static final class Interval {
        final int start;
        final int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Interval)) {
                return false;
            }
            Interval other = (Interval) o;
            return this.start == other.start && this.end == other.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }

    public static List<Interval> mergeIntervals(List<Interval> intervals) {
        List<Interval> sorted = new ArrayList<>(intervals);
        sorted.sort((a, b) -> a.start - b.start);

        List<Interval> result = new ArrayList<>();
        if (sorted.isEmpty()) {
            return result;
        }

        int currentStart = sorted.get(0).start;
        int currentEnd = sorted.get(0).end;

        for (int i = 1; i < sorted.size(); i++) {
            Interval next = sorted.get(i);
            if (next.start <= currentEnd) {
                currentEnd = Math.max(currentEnd, next.end);
            } else {
                result.add(new Interval(currentStart, currentEnd));
                currentStart = next.start;
                currentEnd = next.end;
            }
        }
        result.add(new Interval(currentStart, currentEnd));

        return result;
    }
}
