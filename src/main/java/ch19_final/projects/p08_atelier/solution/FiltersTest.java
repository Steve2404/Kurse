package ch19_final.projects.p08_atelier.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les filtres seuls, devant un faux suivant (une lambda) : on voit exactement ce que chacun ajoute. */
class FiltersTest {

    private final ManualClock clock = new ManualClock(Instant.parse("2026-10-09T10:15:00Z"));
    private final List<String> reached = new ArrayList<>();

    private final RequestHandler next = request -> {
        reached.add(request.client() + " " + request.path());
        return Response.json(200, new JsonString("ok"));
    };

    private static String body(Response r) {
        return JsonWriter.compact(r.body());
    }

    @Test
    void rateLimitLetsTheBurstThroughThenSays429() {
        RateLimitFilter filter = new RateLimitFilter(next, new RateLimiter(2, 1, clock));
        Request ada = Request.of("GET", "/board").withClient("ada");
        assertEquals(200, filter.handle(ada).status());
        assertEquals(200, filter.handle(ada).status());
        Response refused = filter.handle(ada);
        assertEquals(429, refused.status());
        assertEquals(Map.of("Retry-After", "1"), refused.headers());
        assertEquals("{\"error\":\"trop de requetes : reessayer dans 1 s\"}", body(refused));
        assertEquals(List.of("ada /board", "ada /board"), reached);
    }

    @Test
    void eachClientHasItsOwnBucket() {
        RateLimitFilter filter = new RateLimitFilter(next, new RateLimiter(1, 1, clock));
        assertEquals(200, filter.handle(Request.of("GET", "/x").withClient("ada")).status());
        assertEquals(429, filter.handle(Request.of("GET", "/x").withClient("ada")).status());
        assertEquals(200, filter.handle(Request.of("GET", "/x").withClient("bob")).status());
        assertEquals(200, filter.handle(Request.of("GET", "/x")).status());
        assertEquals(429, filter.handle(Request.of("GET", "/x").withClient("  ")).status());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(200, filter.handle(Request.of("GET", "/x").withClient("ada")).status());
    }

    @Test
    void retryAfterIsRoundedUp() {
        RateLimitFilter filter = new RateLimitFilter(next, new RateLimiter(1, 3, clock));
        filter.handle(Request.of("GET", "/x"));
        clock.advance(Duration.ofMillis(300));
        Response refused = filter.handle(Request.of("GET", "/x"));
        assertEquals("1", refused.headers().get("Retry-After"));
    }

    @Test
    void anonymousIsTheDefaultClient() {
        assertEquals("anonyme", Request.of("GET", "/").client());
        assertEquals("anonyme", Request.json("POST", "/", "{}").withClient(null).client());
        assertEquals("ada", Request.of("GET", "/").withClient("ada").client());
    }

    @Test
    void metricsCountEveryKindOfAnswer() {
        ApiMetrics metrics = new ApiMetrics(new LatencyRecorder(100));
        List<Integer> statuses = new ArrayList<>(List.of(200, 201, 404, 400, 429, 500, 503));
        MetricsFilter filter = new MetricsFilter(r -> Response.error(statuses.remove(0), "x"), metrics, clock);
        for (int i = 0; i < 7; i++) {
            filter.handle(Request.of("GET", "/x"));
        }
        assertEquals("{\"requests\":7,\"clientErrors\":2,\"serverErrors\":2,\"throttled\":1,\"p50\":0,\"p95\":0,\"max\":0}",
                JsonWriter.compact(metrics.toJson()));
    }

    @Test
    void metricsMeasureTheTimeSpent() {
        ApiMetrics metrics = new ApiMetrics(new LatencyRecorder(100));
        MetricsFilter filter = new MetricsFilter(r -> {
            clock.advance(Duration.ofMillis(Long.parseLong(r.path().substring(1))));
            return Response.noContent();
        }, metrics, clock);
        for (String ms : List.of("/10", "/20", "/30", "/40", "/400")) {
            filter.handle(Request.of("GET", ms));
        }
        assertEquals("{\"requests\":5,\"clientErrors\":0,\"serverErrors\":0,\"throttled\":0,\"p50\":30,\"p95\":400,\"max\":400}",
                JsonWriter.compact(metrics.toJson()));
    }

    @Test
    void aCrashStillCountsAsA500() {
        ApiMetrics metrics = new ApiMetrics(new LatencyRecorder(100));
        MetricsFilter filter = new MetricsFilter(r -> {
            throw new IllegalStateException("bug");
        }, metrics, clock);
        assertThrows(IllegalStateException.class, () -> filter.handle(Request.of("GET", "/x")));
        assertEquals("{\"requests\":1,\"clientErrors\":0,\"serverErrors\":1,\"throttled\":0,\"p50\":0,\"p95\":0,\"max\":0}",
                JsonWriter.compact(metrics.toJson()));
    }

    @Test
    void emptyMetricsHaveNoPercentiles() {
        assertEquals("{\"requests\":0,\"clientErrors\":0,\"serverErrors\":0,\"throttled\":0}",
                JsonWriter.compact(new ApiMetrics(new LatencyRecorder(10)).toJson()));
    }
}
