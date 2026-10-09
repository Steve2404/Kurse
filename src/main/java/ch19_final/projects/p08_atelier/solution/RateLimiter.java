package ch19_final.projects.p08_atelier.solution;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Un seau par client : un client trop bavard est freine, sans gener les autres. Sur une API, un refus
 * devient 429 Too Many Requests, avec l'en-tete Retry-After.
 */
public final class RateLimiter {

    private final int capacity;
    private final int perSecond;
    private final Clock clock;
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int capacity, int perSecond, Clock clock) {
        this.capacity = capacity;
        this.perSecond = perSecond;
        this.clock = clock;
    }

    /** Duration.ZERO si la requete passe ; sinon, combien le client doit attendre. */
    public Duration acquire(String client) {
        TokenBucket bucket = buckets.computeIfAbsent(client, c -> new TokenBucket(capacity, perSecond, clock));
        return bucket.tryAcquire() ? Duration.ZERO : bucket.timeUntilNext();
    }

    /**
     * Sans menage, la map garde un seau par client JAMAIS vu depuis des mois : une fuite de memoire.
     * Un seau plein est exactement un seau neuf : on peut l'oublier sans rien changer. Rend le nombre oublie.
     */
    public int evictFull() {
        int before = buckets.size();
        buckets.values().removeIf(TokenBucket::isFull);
        return before - buckets.size();
    }

    public int clients() {
        return buckets.size();
    }
}
