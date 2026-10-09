package ch19_final.projects.p08_atelier.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * Le seau a jetons : chaque requete prend un jeton ; le seau se remplit de perSecond jetons par seconde,
 * sans depasser capacity. On peut donc faire une rafale de capacity requetes, puis perSecond par seconde.
 * Les jetons sont comptes en MILLIEMES dans un long : un double accumulerait des erreurs d'arrondi
 * (0.1 + 0.2 != 0.3), et un jeton pourrait manquer d'un milliardieme.
 */
public final class TokenBucket {

    private static final long ONE = 1000;

    private final long capacity;
    private final long perSecond;
    private final Clock clock;
    private long milliTokens;
    private Instant last;

    public TokenBucket(int capacity, int perSecond, Clock clock) {
        if (capacity < 1 || perSecond < 1) {
            throw new IllegalArgumentException("capacite et debit doivent etre positifs");
        }
        this.capacity = capacity * ONE;
        this.perSecond = perSecond;
        this.clock = clock;
        this.milliTokens = this.capacity;
        this.last = clock.instant();
    }

    /** Prend un jeton s'il y en a un. */
    public synchronized boolean tryAcquire() {
        refill();
        if (milliTokens < ONE) {
            return false;
        }
        milliTokens -= ONE;
        return true;
    }

    /** Combien attendre avant le prochain jeton (zero s'il y en a deja un) : pour l'en-tete Retry-After. */
    public synchronized Duration timeUntilNext() {
        refill();
        long missing = Math.max(0, ONE - milliTokens);
        // missing milliemes, a perSecond jetons par seconde : missing / perSecond millisecondes, arrondi au-dessus.
        return Duration.ofMillis((missing + perSecond - 1) / perSecond);
    }

    public synchronized boolean isFull() {
        refill();
        return milliTokens == capacity;
    }

    // Une milliseconde ajoute perSecond milliemes de jeton.
    private void refill() {
        Instant now = clock.instant();
        long elapsedMillis = Duration.between(last, now).toMillis();
        if (elapsedMillis > 0) {
            milliTokens = Math.min(capacity, milliTokens + elapsedMillis * perSecond);
            last = last.plusMillis(elapsedMillis);
        }
    }
}
