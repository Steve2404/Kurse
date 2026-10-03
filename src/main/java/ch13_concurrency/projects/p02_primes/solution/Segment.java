package ch13_concurrency.projects.p02_primes.solution;

/**
 * SOLUTION - le resultat d'un segment : nombre de premiers, premier et dernier premier, plus grand ecart interne.
 */
public record Segment(int from, int to, int count, int first, int last, int maxGap) {

    // Fusion de deux segments CONTIGUS : l'ecart entre le dernier premier de l'un et le premier de l'autre compte aussi.
    public Segment merge(Segment next) {
        int across = next.first - last;
        return new Segment(from, next.to, count + next.count, first, next.last, Math.max(Math.max(maxGap, next.maxGap), across));
    }
}
