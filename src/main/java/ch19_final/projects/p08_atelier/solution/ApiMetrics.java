package ch19_final.projects.p08_atelier.solution;

import java.time.Duration;
import java.util.concurrent.atomic.LongAdder;

/**
 * Les mesures de l'API, partagees entre le filtre qui les remplit (MetricsFilter) et la route qui les montre
 * (GET /metrics). Ce que regarde l'equipe d'exploitation : combien de requetes, combien d'erreurs, et les percentiles.
 */
public final class ApiMetrics {

    private final LatencyRecorder latency;
    private final LongAdder requests = new LongAdder();
    private final LongAdder clientErrors = new LongAdder();
    private final LongAdder serverErrors = new LongAdder();
    private final LongAdder throttled = new LongAdder();

    public ApiMetrics(LatencyRecorder latency) {
        this.latency = latency;
    }

    void record(int status, Duration duration) {
        requests.increment();
        if (status == 429) {
            throttled.increment();
        } else if (status >= 500) {
            serverErrors.increment();
        } else if (status >= 400) {
            clientErrors.increment();
        }
        latency.record(duration);
    }

    public JsonObject toJson() {
        JsonObject.Builder json = JsonObject.builder()
                .add("requests", requests.sum())
                .add("clientErrors", clientErrors.sum())
                .add("serverErrors", serverErrors.sum())
                .add("throttled", throttled.sum());
        if (latency.count() > 0) {
            json.add("p50", latency.percentile(50)).add("p95", latency.percentile(95)).add("max", latency.percentile(100));
        }
        return json.build();
    }
}
