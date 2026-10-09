package ch19_final.projects.p04_jobs.solution;

import java.util.concurrent.atomic.LongAdder;

/**
 * Des compteurs que plusieurs fils augmentent en meme temps. LongAdder plutot que AtomicLong : sous forte
 * concurrence, chaque fil ajoute dans sa propre case, et l'on additionne seulement a la lecture.
 */
public final class Metrics {

    public record Snapshot(long sent, long retries, long failed, long rejected, long timedOut) {
    }

    private final LongAdder sent = new LongAdder();
    private final LongAdder retries = new LongAdder();
    private final LongAdder failed = new LongAdder();
    private final LongAdder rejected = new LongAdder();
    private final LongAdder timedOut = new LongAdder();

    void sent() {
        sent.increment();
    }

    void retry() {
        retries.increment();
    }

    void failed() {
        failed.increment();
    }

    void rejected() {
        rejected.increment();
    }

    void timedOut() {
        timedOut.increment();
    }

    public Snapshot snapshot() {
        return new Snapshot(sent.sum(), retries.sum(), failed.sum(), rejected.sum(), timedOut.sum());
    }
}
