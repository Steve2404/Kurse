package ch19_final.projects.p08_atelier.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/** Le filtre le plus EXTERIEUR : il mesure chaque requete, y compris celles que le limiteur refuse. */
public final class MetricsFilter implements RequestHandler {

    private final RequestHandler next;
    private final ApiMetrics metrics;
    private final Clock clock;

    public MetricsFilter(RequestHandler next, ApiMetrics metrics, Clock clock) {
        this.next = next;
        this.metrics = metrics;
        this.clock = clock;
    }

    @Override
    public Response handle(Request request) {
        Instant start = clock.instant();
        int status = 500;
        try {
            Response response = next.handle(request);
            status = response.status();
            return response;
        } finally {
            // finally : meme si next lance une exception (un bug qui traverse tout), la requete est comptee, en 500.
            metrics.record(status, Duration.between(start, clock.instant()));
        }
    }
}
