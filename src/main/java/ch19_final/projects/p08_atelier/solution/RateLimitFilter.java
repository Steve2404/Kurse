package ch19_final.projects.p08_atelier.solution;

import java.time.Duration;

/**
 * Le limiteur de debit (projet 5) devant l'API : un client trop presse recoit 429 Too Many Requests,
 * et l'en-tete Retry-After lui dit combien de secondes attendre (arrondi au-dessus, au moins 1).
 */
public final class RateLimitFilter implements RequestHandler {

    private final RequestHandler next;
    private final RateLimiter limiter;

    public RateLimitFilter(RequestHandler next, RateLimiter limiter) {
        this.next = next;
        this.limiter = limiter;
    }

    @Override
    public Response handle(Request request) {
        Duration wait = limiter.acquire(request.client());
        if (wait.isZero()) {
            return next.handle(request);
        }
        long seconds = Math.max(1, (wait.toMillis() + 999) / 1000);
        return Response.error(429, "trop de requetes : reessayer dans " + seconds + " s")
                .withHeader("Retry-After", String.valueOf(seconds));
    }
}
