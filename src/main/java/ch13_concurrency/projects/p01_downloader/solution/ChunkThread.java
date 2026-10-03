package ch13_concurrency.projects.p01_downloader.solution;

/**
 * SOLUTION - 2e facon : etendre Thread et redefinir run(). Moins souple (on ne peut plus etendre autre chose).
 */
public class ChunkThread extends Thread {

    private final int index;
    private final int from;
    private final int to;
    private final ChunkStats[] results;
    private final String[] workers;

    public ChunkThread(String name, int index, int from, int to, ChunkStats[] results, String[] workers) {
        super(name);
        this.index = index;
        this.from = from;
        this.to = to;
        this.results = results;
        this.workers = workers;
    }

    @Override
    public void run() {
        results[index] = ChunkStats.of(from, to);
        workers[index] = getName() + " (Thread)";
    }
}
