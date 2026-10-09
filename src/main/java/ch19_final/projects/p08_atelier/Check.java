package ch19_final.projects.p08_atelier;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 8, le projet final (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Board.java", "if (Math.abs(target - from) != 1) {", "if (target - from > 1) {"),
            new Mutant("Board.java", "counts.getOrDefault(IN_PROGRESS, 0) >= wipLimit", "counts.getOrDefault(IN_PROGRESS, 0) > wipLimit"),
            new Mutant("Board.java", "if (to.equals(IN_PROGRESS) && counts.getOrDefault(IN_PROGRESS, 0) >= wipLimit) {", "if (counts.getOrDefault(to, 0) >= wipLimit) {"),
            new Mutant("Board.java", "if (!task.column().equals(COLUMNS.get(0))) {", "if (false) {"),
            new Mutant("Board.java", "if (from == target) {", "if (false) {"),
            new Mutant("BoardService.java", "public synchronized Task move(", "public Task move("),
            new Mutant("BoardService.java", "if (!current.column().equals(task.column())) {", "if (false) {"),
            new Mutant("BoardService.java", "counts.getOrDefault(column, 0)", "counts.get(column)"),
            new Mutant("BoardService.java", "    public List<String> history(long id) {\n        get(id);\n", "    public List<String> history(long id) {\n"),
            new Mutant("RateLimitFilter.java", "Math.max(1, (wait.toMillis() + 999) / 1000)", "wait.toMillis() / 1000"),
            new Mutant("RateLimitFilter.java", "limiter.acquire(request.client())", "limiter.acquire(\"tous\")"),
            new Mutant("MetricsFilter.java", "int status = 500;", "int status = 200;"),
            new Mutant("MetricsFilter.java", "metrics.record(status, Duration.between(start, clock.instant()));", "metrics.record(status, Duration.ZERO);"),
            new Mutant("ApiMetrics.java", "if (status == 429) {", "if (status == 428) {"),
            new Mutant("AtelierApp.java", "return new MetricsFilter(new RateLimitFilter(router, limiter), metrics, clock);", "return new RateLimitFilter(new MetricsFilter(router, metrics, clock), limiter);"),
            new Mutant("AtelierApp.java", "        if (!tasks.countByColumn().isEmpty()) {", "        if (false) {"),
            new Mutant("Request.java", "client == null || client.isBlank() ? ANONYMOUS : client", "client == null ? ANONYMOUS : client"),
            new Mutant("WebServer.java", "exchange.getRequestHeaders().getFirst(\"X-Client\")", "null"),
            new Mutant("AtelierApi.java", "        requireJson(request);\n        TaskJson.Move move", "        TaskJson.Move move"));

    static final List<String> API_CODE = List.of(
            "final class JsonParser", "final class JdbcTaskRepository implements TaskRepository", "final class Migrations",
            "final class Router implements RequestHandler", "final class WebServer", "final class RateLimiter",
            "final class LatencyRecorder", "interface RequestHandler", "interface TaskRepository", "final class Board",
            "final class BoardService", "synchronized", "final class ApiMetrics", "final class MetricsFilter implements RequestHandler",
            "final class RateLimitFilter implements RequestHandler", "\"Retry-After\"", "\"X-Client\"", "final class AtelierApi",
            "final class AtelierApp", "record Config(", "Migrations.apply(", "Data.SEED", "!printStackTrace",
            "max:method=18",
            "in:Board.java!java.sql##le metier ne connait pas la base",
            "in:Board.java!Request##le metier ne connait pas HTTP",
            "in:BoardService.java!Jdbc##le service ne connait que le port TaskRepository",
            "in:BoardService.java!Response##le service ne connait pas HTTP",
            "in:AtelierApi.java!Jdbc##les routes ne connaissent pas la base",
            "in:AtelierApi.java!Response.error(##les handlers lancent, le Router traduit",
            "in:AtelierApp.java=new MetricsFilter(new RateLimitFilter(##l'ordre des filtres : MetricsFilter(RateLimitFilter(Router))");

    static final List<String> API_TESTS = List.of(
            "JdbcDataSource", "ManualClock", "WebServer.start(0", "AtelierApp.create(", "HttpClient", "\"X-Client\"",
            "CountDownLatch", "@ParameterizedTest", "@AfterEach", "!System.out", "!Thread.sleep", "!8080");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 30, MUTANTS, API_CODE, API_TESTS);
    }
}
