package ch13_concurrency.projects.p01_downloader.solution;

/**
 * SOLUTION - 1re facon de definir le travail d'un thread : implementer Runnable.
 * Chaque tache ecrit dans SA case du tableau : aucune donnee partagee en ecriture, donc pas de course.
 */
public class ChunkTask implements Runnable {

    private final int index;
    private final int from;
    private final int to;
    private final ChunkStats[] results;
    private final String[] workers;

    public ChunkTask(int index, int from, int to, ChunkStats[] results, String[] workers) {
        this.index = index;
        this.from = from;
        this.to = to;
        this.results = results;
        this.workers = workers;
    }

    @Override
    public void run() {
        results[index] = ChunkStats.of(from, to);
        workers[index] = Thread.currentThread().getName() + " (Runnable)";
    }
}
