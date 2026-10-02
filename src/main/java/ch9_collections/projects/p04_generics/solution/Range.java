package ch9_collections.projects.p04_generics.solution;

/**
 * SOLUTION - un intervalle dont le type est BORNE : seuls les types comparables a eux-memes sont permis.
 */
public record Range<T extends Comparable<T>>(T low, T high) {

    public Range {
        if (low.compareTo(high) > 0) {
            T t = low;
            low = high;
            high = t;
        }
    }

    public boolean contains(T value) {
        return low.compareTo(value) <= 0 && value.compareTo(high) <= 0;
    }
}
