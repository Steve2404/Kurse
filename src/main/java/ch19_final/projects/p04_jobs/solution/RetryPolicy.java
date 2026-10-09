package ch19_final.projects.p04_jobs.solution;

import java.time.Duration;

/**
 * Combien d'essais, et combien attendre entre deux : l'attente DOUBLE a chaque echec (recul exponentiel),
 * sans depasser maxDelay. Un service sature a besoin d'air : reessayer tout de suite l'achevrait.
 */
public record RetryPolicy(int maxAttempts, Duration firstDelay, double multiplier, Duration maxDelay) {

    public RetryPolicy {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("au moins 1 essai : " + maxAttempts);
        }
        if (multiplier < 1) {
            throw new IllegalArgumentException("multiplicateur inferieur a 1 : " + multiplier);
        }
    }

    /** L'attente apres le n-ieme echec (n a partir de 1) : firstDelay x multiplier^(n-1), plafonnee. */
    public Duration delayAfter(int failures) {
        double millis = firstDelay.toMillis() * Math.pow(multiplier, failures - 1);
        return millis >= maxDelay.toMillis() ? maxDelay : Duration.ofMillis(Math.round(millis));
    }
}
