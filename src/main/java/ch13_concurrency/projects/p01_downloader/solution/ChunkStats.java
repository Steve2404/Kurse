package ch13_concurrency.projects.p01_downloader.solution;

import ch13_concurrency.projects.p01_downloader.Data;

/**
 * SOLUTION - le resume d'un morceau [from, to) : somme, et de quoi FUSIONNER les series d'octets identiques
 * avec le morceau voisin (premiere et derniere valeur, serie de tete, serie de queue, meilleure serie).
 */
public record ChunkStats(int from, int to, long sum, int first, int last, int prefix, int suffix, int best) {

    public static ChunkStats of(int from, int to) {
        long sum = 0;
        int best = 0;
        int run = 0;
        int prefix = 0;
        boolean inPrefix = true;
        for (int i = from; i < to; i++) {
            int v = Data.at(i);
            sum += v;
            run = i > from && v == Data.at(i - 1) ? run + 1 : 1;
            if (inPrefix && i > from && v != Data.at(i - 1)) {
                inPrefix = false;
            }
            if (inPrefix) {
                prefix = run;
            }
            best = Math.max(best, run);
        }
        return new ChunkStats(from, to, sum, Data.at(from), Data.at(to - 1), prefix, run, best);
    }

    int length() {
        return to - from;
    }

    // Fusion de deux morceaux CONTIGUS (this puis next) : une serie peut traverser la frontiere.
    public ChunkStats merge(ChunkStats next) {
        boolean joined = last == next.first;
        int newPrefix = joined && prefix == length() ? length() + next.prefix : prefix;
        int newSuffix = joined && next.suffix == next.length() ? next.length() + suffix : next.suffix;
        int across = joined ? suffix + next.prefix : 0;
        return new ChunkStats(from, next.to, sum + next.sum, first, next.last, newPrefix, newSuffix, Math.max(Math.max(best, next.best), across));
    }
}
