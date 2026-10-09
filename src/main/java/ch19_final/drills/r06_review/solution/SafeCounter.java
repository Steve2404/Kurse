package ch19_final.drills.r06_review.solution;

import java.util.concurrent.atomic.LongAdder;

/** Un compteur que plusieurs fils augmentent en meme temps : value++ perdrait des additions. */
public final class SafeCounter {

    private final LongAdder value = new LongAdder();

    public void increment() {
        value.increment();
    }

    public long value() {
        return value.sum();
    }
}
