package ch19_final.drills.r05_resilience.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/** Le seau a jetons, en milliemes de jeton dans un long ; le reste de milliseconde est garde. */
public final class Bucket {

    private final long capacity;
    private final long perSecond;
    private final Clock clock;
    private long milli;
    private Instant last;

    public Bucket(int capacity, int perSecond, Clock clock) {
        this.capacity = capacity * 1000L;
        this.perSecond = perSecond;
        this.clock = clock;
        this.milli = this.capacity;
        this.last = clock.instant();
    }

    public synchronized boolean tryAcquire() {
        refill();
        if (milli < 1000) {
            return false;
        }
        milli -= 1000;
        return true;
    }

    /** Les millisecondes avant le prochain jeton, arrondies au-dessus (0 s'il y en a un). */
    public synchronized long waitMillis() {
        refill();
        long missing = Math.max(0, 1000 - milli);
        return (missing + perSecond - 1) / perSecond;
    }

    private void refill() {
        long elapsed = Duration.between(last, clock.instant()).toMillis();
        if (elapsed > 0) {
            milli = Math.min(capacity, milli + elapsed * perSecond);
            last = last.plusMillis(elapsed);
        }
    }
}
